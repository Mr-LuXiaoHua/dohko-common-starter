package com.dohko.log.configuration;

import com.dohko.log.aspect.CallLogAspect;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @description: 配置导入
 * @author: luxiaohua
 * @date: 2020-06-24 13:57
 */
public class CallLogImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{CallLogAspect.class.getName()};
    }
}
