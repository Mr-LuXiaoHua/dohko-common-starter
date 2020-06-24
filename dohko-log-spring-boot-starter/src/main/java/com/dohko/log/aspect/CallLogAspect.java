package com.dohko.log.aspect;

import com.dohko.log.util.RandomUtils;
import com.dohko.log.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: CallLog 切面
 * @author: luxiaohua
 * @date: 2020-06-24 10:19
 */
@Aspect
@Component
@Slf4j
public class CallLogAspect {

    /**
     * 日志标识
     */
    private final static String LOG_ID = "LOG-ID";

    @Pointcut("@annotation(com.dohko.log.annotation.CallLog)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MDC.put(LOG_ID, RandomUtils.randStr());
        logInParam(joinPoint);
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        } finally {
            long end = System.currentTimeMillis();
            log.info("LOG-ID: {} -> 调用方法: {}.{}  耗时: {} ms", MDC.get(LOG_ID), point.getTarget().getClass().getSimpleName(), point.getSignature().getName(), end - start);
        }
        return result;
    }


    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        log.info("LOG-ID: {} -> 调用方法: {}.{}, 方法响应: {}", MDC.get(LOG_ID), point.getTarget().getClass().getSimpleName(), point.getSignature().getName(), result);
        MDC.remove(LOG_ID);
    }


    /**
     * 记录方法入参
     * @param joinPoint
     */
    private void logInParam(JoinPoint joinPoint) {


        String className = joinPoint.getTarget().getClass().getSimpleName();
        String method = joinPoint.getSignature().getName();

        // 获取参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if (Objects.isNull(argNames) || argNames.length == 0) {
            log.info("LOG-ID:{} -> 调用方法: {}.{}, 方法参数: 无", MDC.get(LOG_ID), className, method);
        } else {

            Map<String, Object> paramsMap = new HashMap<>(16);


            Object[] args = joinPoint.getArgs();
            for (int i=0; i<argNames.length; i++) {
                Object arg = args[i];
                if (arg instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) arg;
                    Map<String, String> params = RequestUtils.getParams(request);
                    paramsMap.put(argNames[i], params);

                    // 获取请求uri
                    String uri = request.getRequestURI();

                    // 获取请求ip
                    String ip = RequestUtils.getIpAddr(request);

                    log.info("LOG-ID: {} -> 调用方法: {}.{}, 请求来源: {}, 请求URI: {}", MDC.get(LOG_ID), className, method, ip, uri);


                } else if (arg instanceof HttpServletResponse) {
                    continue;
                } else {
                    paramsMap.put(argNames[i], arg);
                }
            }

            log.info("LOG-ID: {} -> 调用方法: {}.{}, 方法参数: {}", MDC.get(LOG_ID), className, method, paramsMap);
        }



    }

}
