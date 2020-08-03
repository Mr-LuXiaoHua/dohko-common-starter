package com.dohko.log.aspect;

import com.dohko.log.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
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


    @Pointcut("@annotation(com.dohko.log.annotation.CallLog)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        logInputParam(joinPoint);
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            long timeConsuming = System.currentTimeMillis() - start;
            log.info("调用方法: {}.{}  耗时: {} ms", point.getTarget().getClass().getSimpleName(), point.getSignature().getName(), timeConsuming);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
        return result;
    }


    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        log.info("调用方法: {}.{}, 响应: {}", point.getTarget().getClass().getSimpleName(), point.getSignature().getName(), result);
    }


    /**
     * 记录方法入参
     * @param joinPoint
     */
    private void logInputParam(JoinPoint joinPoint) {


        String className = joinPoint.getTarget().getClass().getSimpleName();
        String method = joinPoint.getSignature().getName();

        // 获取参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if (Objects.isNull(argNames) || argNames.length == 0) {
            log.info("调用方法: {}.{}, 方法参数: 无", className, method);
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

                    log.info("调用方法: {}.{}, 请求来源: {}, 请求URI: {}", className, method, ip, uri);


                } else if (arg instanceof HttpServletResponse) {
                    continue;
                } else {
                    paramsMap.put(argNames[i], arg);
                }
            }

            log.info("调用方法: {}.{}, 方法参数: {}", className, method, paramsMap);
        }



    }

}
