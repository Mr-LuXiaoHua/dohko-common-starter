package com.dohko.lock.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @description: key 解析
 * @author: luxiaohua
 * @date: 2020-06-28 13:54
 */
public class KeyParser {


    /**
     *
     * @param joinPoint
     * @param module 业务模块
     * @param key   key
     * @return 返回分布式锁的key
     */
    public static String parse(ProceedingJoinPoint joinPoint, String module, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] params = discoverer.getParameterNames(method);

        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();

        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        return module + parser.parseExpression(key, new TemplateParserContext()).getValue(context, String.class);
    }

}
