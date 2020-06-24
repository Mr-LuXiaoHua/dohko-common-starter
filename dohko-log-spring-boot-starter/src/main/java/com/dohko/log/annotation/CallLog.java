package com.dohko.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @desc 调用日志注解，用于记录方法的调用参数，返回结果，以及方法执行耗时
 * @author luxiaohua
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallLog {

}
