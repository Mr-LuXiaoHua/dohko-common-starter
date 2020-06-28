package com.dohko.lock;

import org.redisson.api.RLock;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 11:07
 */
public interface DistributedLockService {

    /**
     * 根据lockName获取锁
     * @param lockName
     * @return
     */
    RLock getLock(String lockName);

    /**
     * 可控制是否需要公平锁
     * @param lockName
     * @param isFair    true-公平锁，false-非公平锁
     * @return
     */
    RLock getLock(String lockName, boolean isFair);
}
