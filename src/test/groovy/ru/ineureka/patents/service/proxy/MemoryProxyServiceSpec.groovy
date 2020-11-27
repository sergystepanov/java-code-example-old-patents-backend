package ru.ineureka.patents.service.proxy

import ru.ineureka.patents.config.ProxyConfig
import ru.ineureka.patents.persistence.proxy.ProxyServer
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.util.concurrent.CountDownLatch

class MemoryProxyServiceSpec extends Specification {

    @Unroll
    def "if proxy can load servers"() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(1, 1))

        when: 'we are loading servers'
        service.load(servers)

        then:
        service.size() == serverCount

        where:
        servers                                       || serverCount
        null                                          || 0
        []                                            || 0
        [server(1).with { it.accessedAt = null; it }] || 1
    }

    @Unroll
    def "if proxy loads servers with undefined access time"() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(1, 1))
        service.load([
                server(1).with { it.accessedAt = null; it },
                server(2).with { it.accessedAt = Instant.EPOCH; it },
        ])

        when: 'we are getting a free server'
        def server = service.get()

        then: 'it is present'
        server.isPresent()
    }

    @Unroll
    def "if proxy loads the servers in order of access time"() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(1, 100))
        def now = Instant.now()

        and: 'some list of servers'
        def list = [
                server(1).with { it.accessedAt = now.plusSeconds(10 * 60); it },
                server(3, false),
                server(4).with { it.accessedAt = now.minusSeconds(60); it },
                server(2).with { it.accessedAt = now.plusSeconds(60); it },
        ]

        and:
        service.load(list)

        when: 'we are getting a free server'
        def server = service.get()

        then: 'it is present'
        server.isPresent()

        and: 'it is correct one'
        server.get().id == 4

        and: 'its access time has changed'
        server.get().accessedAt > list.get(2).accessedAt
    }

    @Unroll
    def "if it is impossible to load disabled proxy servers with e: #enabled + d: #disabled"() {
        given:
        def proxyService = new MemoryProxyService(proxyConfigWith(wait as int, 100))
        def upServers = enabled > 0 ? (1..enabled).collect({ server(it, true) }) : []
        def downServers = disabled > 0 ? (1..disabled).collect({ server(it, false) }) : []

        proxyService.load(upServers + downServers as List<ProxyServer>)

        when:
        def servers = (1..requests).collect({ proxyService.get() }).findAll({ it.isPresent() })

        then: 'proxy loaded all the enabled servers'
        proxyService.size() == enabled

        and: 'the result list with servers is correct'
        servers.size() == resultSize

        where:
        enabled | disabled | requests | wait || resultSize
        0       | 0        | 100      | 10   || 0
        3       | 0        | 100      | 10   || 3
        0       | 10       | 100      | 10   || 0
        10      | 0        | 100000   | 10   || 10
        10      | 10       | 100000   | 10   || 10
    }

    def "if synchronization works properly"() {
        given: 'an initial proxy list for service'
        def dailyRequestLimit = 2
        def rateLimitSec = 1
        def service = new MemoryProxyService(proxyConfigWith(rateLimitSec, dailyRequestLimit))
        def serverCount = 10

        and:
        service.load((1..serverCount).collect { server(it) })

        and: 'data for proper thread synchronization'
        def requests = 50
        def latch = new CountDownLatch(serverCount)

        when: 'initial group of 50 requests is created'
        def servers = []
        (1..requests).each {
            new Thread({
                service.get().ifPresent({ servers.add(it) })
                latch.countDown()
            }).run()
        }
        latch.await()

        and: 'there is a wait period greater than rate limit for these servers'
        sleep(rateLimitSec * 1000 + 1)

        and: 'another bunch of requests'
        (1..requests * 2).each { service.get().ifPresent({ servers.add(it) }) }

        and: 'another wait period'
        sleep(rateLimitSec * 1000 + 1)

        and: 'even more requests but the limit for all servers is reached'
        (1..requests / 2).each { service.get().ifPresent({ servers.add(it) }) }

        then:
        servers.size() == serverCount * 2
    }

    def "if proxy day limit reset is ok"() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(0, 110))
        service.load([server(1).with { it.accessedAt = null; it }])

        and: 'some number of requests'
        def requests = 10

        when: 'we are have gotten a number free servers'
        (1..requests).each { service.get() }
        def server = service.get().get()

        and: 'done a stat reset for an abused server'
        server.reset()

        then: 'it should have its stats reset'
        server.getCount() == 0
    }

    @Unroll
    def 'if server access time can be set correctly'() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(0, 42))
        service.load([
                server(1).with { it.accessedAt = null; it },
                server(2).with { it.accessedAt = null; it },
                server(3).with { it.accessedAt = null; it }
        ])

        and: 'some number of requests'
        def requests = 10

        when: 'we are have gotten a number free servers'
        (1..requests).each { service.get() }
        def server = service.get().get()
        def oldTime = server.getAccessedAt()

        and: 'done a manual access time set'
        service.updateAccessTime(server.getId(), Instant.now().plusSeconds(100))

        and: 'done invalid server id set'
        service.updateAccessTime(-1, null)
        service.updateAccessTime(0, null)
        service.updateAccessTime(100, Instant.now())

        then: 'it should have new access time'
        server.getAccessedAt() > oldTime
    }

    @Unroll
    def 'if server access time includes config lock time: #lockSec'() {
        given: 'a proxy service'
        def service = new MemoryProxyService(proxyConfigWith(10, 42, lockSec))
        service.load([server(1).with { it.accessedAt = null; it }])

        and: 'a time mark before a request'
        def before = Instant.now()

        when: 'we are have gotten a free server'
        def server = service.get().get()

        and: 'a time mark after the request'
        def after = Instant.now()

        and: 'an original proxy access time'
        def proxyAccessTime = server.getAccessedAt().minusSeconds(lockSec)

        then: 'it should have access time with a lock -> access time in the future'
        // 1: 00:00:01
        // 2: 00:00:02 + lock
        // 3: 00:00:03
        // -> 2 - lock = [1;3]
        proxyAccessTime >= before && proxyAccessTime <= after

        where:
        lockSec || _
        200     || _
        100     || _
        0       || _
    }

    def server(int i, enabled = true) {
        new ProxyServer(i, 'test-' + i, 'TEST', '10.0.0.' + i, 0, '?', 0, enabled, Instant.EPOCH)
    }

    def proxyConfigWith(waitSec = 0, dailyRequestLimit = 100, lockSec = 0, zone = 'Europe/Moscow') {
        new ProxyConfig(lockSec, waitSec, dailyRequestLimit, zone)
    }
}
