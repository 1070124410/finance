package com.finance.anubis.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Component
public class RedisDistributedLock {

    private static final String LOCK_KEY_PREFIX = "distributed_lock:";
    private static final DefaultRedisScript<Boolean> RELEASE_LOCK_SCRIPT = new DefaultRedisScript<>();
    static {
        RELEASE_LOCK_SCRIPT.setResultType(Boolean.class);
        RELEASE_LOCK_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("release_lock.lua")));
    }

    private final StringRedisTemplate redisTemplate;
    private final String lockKey;

    public RedisDistributedLock(StringRedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = LOCK_KEY_PREFIX + lockKey;
    }

    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        long end = System.currentTimeMillis() + unit.toMillis(waitTime);
        while (System.currentTimeMillis() < end) {
            if (redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", leaseTime, unit)) {
                return true;
            }
            LockSupport.parkNanos(unit.toNanos(leaseTime / 10));
        }
        return false;
    }

    public void unlock() {
        redisTemplate.execute(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey), "locked");
    }

    public void tryApplyWithinLockScopeInterruptibly(long waitTime, TimeUnit timeUnit, Runnable action) throws InterruptedException {
        //注意timeUnit，这里时间写死了30
        if (tryLock(waitTime, timeUnit.toSeconds(30), timeUnit)) {
            try {
                action.run();
            } finally {
                unlock();
            }
        } else {
            throw new IllegalStateException("Failed to acquire the lock.");
        }
    }
}
