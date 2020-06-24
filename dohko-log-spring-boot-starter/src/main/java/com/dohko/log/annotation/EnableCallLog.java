package com.dohko.log.annotation;


import com.dohko.log.configuration.CallLogImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @desc 启用日志
 * @author luxiaohua
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CallLogImportSelector.class)
public @interface EnableCallLog {

}
