package com.dohko.lock.annotation;

import com.dohko.lock.config.DistributedLockImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @desc: 启用分布式锁
 * @author luxiaohua
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DistributedLockImportSelector.class)
public @interface EnableLock {

}
