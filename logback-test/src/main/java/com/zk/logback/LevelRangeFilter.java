package com.zk.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 自定义过滤器
 */
public class LevelRangeFilter extends AbstractMatcherFilter<ILoggingEvent> {
    private Level levelMin;
    private Level levelMax;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        // 赋默认值
        if (levelMin == null) {
            levelMin = Level.INFO;
        }
        if (levelMax == null) {
            levelMax = Level.ERROR;
        }

        // 判断是否满足：event.getLevel().isGreaterOrEqual(this.levelMin)
        if (!event.getLevel().isGreaterOrEqual(this.levelMin)) {
            return FilterReply.DENY;
        }

        // 判断是否满足：this.levelMax.isGreaterOrEqual(event.getLevel())
        if (!this.levelMax.isGreaterOrEqual(event.getLevel())) {
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }

    public void setLevelMax(Level levelMax) {
        this.levelMax = levelMax;
    }

    public void setLevelMin(Level levelMin) {
        this.levelMin = levelMin;
    }

    public void start() {
        if (this.levelMin != null || this.levelMax != null) {
            super.start();
        }
    }
}