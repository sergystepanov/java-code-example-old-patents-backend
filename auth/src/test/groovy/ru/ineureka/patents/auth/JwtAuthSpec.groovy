package ru.ineureka.patents.auth


import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import spock.lang.Specification
import spock.lang.Unroll

class JwtAuthSpec extends Specification {

    def "if key converter to base64 is ok"() {
        when:
        def key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        def base64 = JwtAuth.keyToBase64(key)

        then:
        JwtAuth.base64ToKey(base64) == key
    }

    def "if service works with various init params"() {
        given:
        new JwtAuth(randomKey(), ttl) != null

        where:
        ttl || _
        1   || _
        2   || _
    }

    @Unroll
    def "if service token validation is ok"() {
        given:
        def auth = new JwtAuth(randomKey(), 10000)

        when:
        def token = auth.getToken(1)

        and:
        auth.validate(token)

        then:
        auth.getSubject(token) == 1

        and:
        noExceptionThrown()
    }

    @Unroll
    def "if service token validation is not ok"() {
        given:
        def auth = new JwtAuth(randomKey(), ttl)

        when:
        def token = auth.getToken(1)

        and:
        sleep wait

        and:
        auth.validate token + garbage

        then:
        thrown(AuthException)

        where:
        ttl | garbage    | wait || _
        100 | '1'        | 0    || _
        100 | '.garbage' | 0    || _
        10  | ''         | 100  || _
    }

    def "if refresh token exists"() {
        when:
        def auth = new JwtAuth(randomKey(), 1)

        then:
        !auth.generateRefreshToken().isBlank()
    }

    def "if random key generator is ok"() {
        expect:
        !JwtAuth.showMeAKey().isBlank()
    }

    def randomKey() { JwtAuth.showMeAKey() }
}
