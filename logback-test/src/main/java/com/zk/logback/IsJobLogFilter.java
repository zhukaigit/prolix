package com.zk.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 自定义过滤器
 * 用于检验是否是job日志
 * 思路：在job入口，在mdc中对key：IS_JOB，赋值为“IS_JOB”
 */
public class IsJobLogFilter extends AbstractMatcherFilter<LoggingEvent> {

    @Override
    public FilterReply decide (LoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        String is_job = event.getMDCPropertyMap().get("IS_JOB");
        if ("THIS_IS_JOB_LOG".equals(is_job)) {
            return onMatch;
        }
        return onMismatch;
    }
}
