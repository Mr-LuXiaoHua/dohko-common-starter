package com.dohko.lock.config;

import com.dohko.lock.aspect.DistributedLockAspect;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 14:45
 */
public class DistributedLockImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{DistributedLockAspect.class.getName(), RedissonAutoConfiguration.class.getName()};
    }
}
