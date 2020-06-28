package com.dohko.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 11:07
 */
public class DistributedLockServiceImpl implements DistributedLockService {

    private RedissonClient redissonClient;

    public DistributedLockServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 根据名称获取锁
     * @param lockName
     * @return
     */
    @Override
    public RLock getLock(String lockName) {
        return redissonClient.getLock(lockName);
    }

    @Override
    public RLock getLock(String lockName, boolean isFair) {
        if (isFair) {
            return redissonClient.getFairLock(lockName);
        } else {
            return getLock(lockName);
        }
    }
}
