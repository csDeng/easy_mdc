package com.example.easy_mdc.utils;

import com.example.easy_mdc.aop.MdcAspect;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MDCTaskDecorator implements TaskDecorator {
    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        // 此时获取的是父线程的上下文数据
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (!ObjectUtils.isEmpty(contextMap)) {
                    String logId = contextMap.getOrDefault(MdcAspect.logIdKey, UUID.randomUUID().toString());
                    logId += "_" + Thread.currentThread().getName();
                    contextMap.put(MdcAspect.logIdKey, logId);
                    // 内部为子线程的领域范围，所以将父线程的上下文保存到子线程上下文中，而且每次submit/execute调用都会更新为最新的上                     // 下文对象
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // 清除子线程的，避免内存溢出，就和ThreadLocal.remove()一个原因
                MDC.clear();
            }
        };
    }
}
