package com.zyw.shopping.redis;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {
    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey);
    /**
     * 加锁并设置锁过期时间
     * @param lockKey
     * @param timeout
     * @return
     */
    public RLock lock(String lockKey, int timeout);
    /**
     * 加锁并设置锁过期时间，并指定时间格式
     * @param lockKey
     * @param unit
     * @param timeout
     * @return
     */
    public RLock lock(String lockKey, TimeUnit unit, int timeout);
    /**
     * tryLock()，马上返回，拿到lock就返回true，不然返回false。
     * 带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false.
     * @param lockKey
     * @param unit
     * @param waitTime
     * @param leaseTime
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);
    /**
     * 解锁
     * @param lockKey
     */
    public void unlock(String lockKey);
    /**
     * 解锁
     * @param lock
     */
    public void unlock(RLock lock);

}
