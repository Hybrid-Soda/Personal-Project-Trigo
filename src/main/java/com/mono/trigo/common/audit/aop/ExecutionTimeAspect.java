package com.mono.trigo.common.audit.aop;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class ExecutionTimeAspect {

    private Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("execution(* com.mono.trigo.web..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long t = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        log.info("Execution time=" + (System.currentTimeMillis() - t) + "ms");
        return result;
    }
}