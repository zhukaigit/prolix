package com.zk.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Map;

/**
 * 自定义过滤器
 * @param <E>
 */
public class LevelRangeFilter<E> extends Filter<E> {
    private boolean acceptOnMatch = false;
    private Level levelMin;
    private Level levelMax;

    public LevelRangeFilter() {
    }

    public void setLevelMax(Level levelMax) {
        this.levelMax = levelMax;
    }

    public void setLevelMin(Level levelMin) {
        this.levelMin = levelMin;
    }

    public void setAcceptOnMatch(boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }

    public FilterReply decide(E eventObject) {

        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        } else {
            LoggingEvent event = (LoggingEvent)eventObject;


            Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();


            if (this.levelMin != null && !event.getLevel().isGreaterOrEqual(this.levelMin)) {
                return FilterReply.DENY;
            } else if (this.levelMax != null && event.getLevel().toInt() > this.levelMax.toInt()) {
                return FilterReply.DENY;
            } else {
                return this.acceptOnMatch ? FilterReply.ACCEPT : FilterReply.NEUTRAL;
            }

        }
    }

    public void start() {
        if (this.levelMin != null || this.levelMax != null) {
            super.start();
        }

    }
}