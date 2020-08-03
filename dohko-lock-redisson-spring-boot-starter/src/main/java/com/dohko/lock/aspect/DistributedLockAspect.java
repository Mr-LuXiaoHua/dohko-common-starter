package com.dohko.lock.aspect;

import com.dohko.lock.DistributedLockService;
import com.dohko.lock.annotation.DistributedLock;
import com.dohko.lock.exception.LockException;
import com.dohko.lock.util.KeyParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 分布式锁切面
 * @author: luxiaohua
 * @date: 2020-06-28 11:26
 */
@Slf4j
@Aspect
@Order
@Component
public class DistributedLockAspect {


    @Autowired
    private DistributedLockService distributedLockService;



    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock lock) {

        Object proceed = null;

        String module = lock.module();
        String key = lock.key();

        long waitTime = lock.waitTime();
        long leaseTime = lock.leaseTime();

        String lockName = KeyParser.parse(joinPoint, module, key);
        log.info("try to get distributed lock -> lockName:{}, waitTime:{}s, leaseTime:{}s", lockName, waitTime, leaseTime);

        RLock rLock = distributedLockService.getLock(lockName);

        boolean isLock = false;
        try {
            isLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (!isLock) {
            log.warn("try to get distributed lock -> lockName:{} get lock fail.", lockName);
            throw new LockException("try to get distributed lock is fail.");
        }

        if (isLock) {
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable e){
                log.error(e.getMessage(), e);
            } finally {
                if (Objects.nonNull(rLock) && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        }

        return proceed;
    }


}
