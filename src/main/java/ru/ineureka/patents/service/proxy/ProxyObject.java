package ru.ineureka.patents.service.proxy;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * An object which stores proxy data.
 * Thread safe.
 *
 * @since 3.0.0
 */
public class ProxyObject {
    private final long id;
    private final String ip;

    private volatile Instant accessedAt;
    private AtomicLong count;

    public ProxyObject(long id, String ip, Instant accessedAt, long count) {
        this.id = id;
        this.ip = ip;
        this.accessedAt = Objects.nonNull(accessedAt) ? accessedAt : Instant.EPOCH;
        this.count = new AtomicLong(count);
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    /**
     * Returns the Last Access Time for this proxy in milliseconds.
     */
    public Instant getAccessedAt() {
        return accessedAt;
    }

    public void setAccessedAt(Instant time) {
        accessedAt = time;
    }

    public long getCount() {
        return count.get();
    }

    public final synchronized void update() {
        setAccessedAt(Instant.now());
        count.incrementAndGet();
    }

    public void reset() {
        count.getAndSet(0);
    }

    /**
     * A complex check whether the proxy resolves provided conditions.
     * This synchronized atomic garbage is used for performance reasons.
     * If all the conditions are satisfied then it will update its
     * internal params (time and access counter).
     *
     * @param pessimisticLockSec Amount of seconds to lock the server ahead.
     * @param conditions         A list of conditions to check.
     * @return This proxy.
     */
    @SafeVarargs
    public final synchronized ProxyObject updateAndGetIf(int pessimisticLockSec, Predicate<ProxyObject>... conditions) {
        var isOk = true;
        for (var condition : conditions) {
            isOk = isOk && condition.test(this);
        }

        if (isOk) {
            update();
            accessedAt = accessedAt.plusSeconds(pessimisticLockSec);
            return this;
        }

        return null;
    }

    /**
     * Returns the waiting time since last access in milliseconds since epoch.
     * Everything in UTC.
     */
    public final synchronized boolean isAccessedLaterThan(long ms) {
        return !Duration.between(accessedAt, Instant.now()).minusMillis(ms).isNegative();
    }

    @Override
    public String toString() {
        return "ProxyObject{" + "id=" + id + ", ip='" + ip + '\'' + ", accessedAt=" + accessedAt + ", count=" + count + '}';
    }
}
