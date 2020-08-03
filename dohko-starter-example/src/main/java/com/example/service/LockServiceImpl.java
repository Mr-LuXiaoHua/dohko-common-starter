package com.example.service;

import com.dohko.lock.DistributedLockService;
import com.dohko.lock.annotation.DistributedLock;
import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 15:07
 */
@Component
@Slf4j
public class LockServiceImpl implements LockService {

    @Autowired
    private DistributedLockService distributedLockService;


    @Override
    public void testLock1(OrderDTO dto) {
        log.info("进入方法了");
        RLock lock = distributedLockService.getLock(dto.getOrderNo());
        try {
            boolean isLock = lock.tryLock(0, 10L, TimeUnit.SECONDS);
            if (isLock) {
                // 获取到锁

            } else {
                // 没有获取到锁
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(lock) && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    @Override
    @DistributedLock(module = "order-", key="#{#orderNo}")
    public void testLock2(String orderNo) {
        try {
            log.info("模拟业务");
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
