package com.zk.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 自定义过滤器
 *
 * 作用：在线程开始处，给MDC的指定key赋任意值。在各个输出源配置该过滤器，根据情况看是否需要ACCEPT、DENY、NEUTRAL
 */
public class CheckMdcKeyIsExistFilter extends AbstractMatcherFilter<LoggingEvent> {
    private String mdcKey;

    @Override
    public FilterReply decide (LoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (mdcKey != null && mdcKey.trim().length() > 0) {
            String value = event.getMDCPropertyMap().get(mdcKey);
            if (value != null) {
                return onMatch;
            }
        }
        return onMismatch;
    }

    public void setMdcKey (String mdcKey) {
        this.mdcKey = mdcKey;
    }
}
