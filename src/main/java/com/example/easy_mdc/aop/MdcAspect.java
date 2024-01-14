package com.example.easy_mdc.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class MdcAspect {

    public static final String logIdKey = "logId";

    private static final Logger log = LoggerFactory.getLogger(MdcAspect.class);

    private final HttpServletRequest httpServletRequest;

    @Around(
            "@annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) "
    )
    public Object injectLogId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        String logId = httpServletRequest.getHeader(MdcAspect.logIdKey);
        logId = StringUtils.isNotBlank(logId) ? (logId + "_") : "" + UUID.randomUUID();
        MDC.put(logIdKey, logId);
        log.info("inject logId = {}", logId);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            MDC.remove(logIdKey);
        }
    }


}
