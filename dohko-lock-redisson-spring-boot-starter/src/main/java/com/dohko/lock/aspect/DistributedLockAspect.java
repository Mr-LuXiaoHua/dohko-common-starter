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
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 11:26
 */
@Slf4j
@Aspect
@Order
@Component
public class DistributedLockAspect {


    @Autowired
    private DistributedLockService redissonService;



    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock lock) {

        Object proceed = null;

        String module = lock.module();
        String key = lock.key();

        int waitTime = lock.waitTime();
        int leaseTime = lock.leaseTime();


        log.info("lock -> module:{}, key:{}, waitTime:{}, leaseTime:{}", module, key, waitTime, leaseTime);

        String lockName = KeyParser.parse(joinPoint, module, key);

        log.info("lockName:{}", lockName);

        RLock rLock = redissonService.getLock(lockName);

        boolean isLock = false;
        try {
            isLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isLock) {
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable e){

            } finally {
                if (Objects.nonNull(rLock) && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }

        } else {
            throw new LockException("获取锁失败");
        }

        return proceed;
    }


}
