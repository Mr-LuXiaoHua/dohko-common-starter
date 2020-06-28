package com.dohko.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @desc 分布式锁注解
 * @author luxiaohua
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {


    /**
     * 业务模块
     * @return
     */
    String module();

    /**
     * 分布式锁的key
     * @return
     */
    String key();


    /**
     * 尝试获取锁，最多等待时间，默认不等待。单位：秒
     * @return
     */
    int waitTime() default 0;


    /**
     * 上锁成功后最长持有时间，单位秒。默认10秒
     * @return
     */
    int leaseTime() default 10;


}
