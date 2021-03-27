package com.kg.platform.common.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ThresholdFilter extends Filter<ILoggingEvent> {
    private Level level = Level.WARN;

    private boolean rebel;

    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (this.rebel) {
            if (event.getLevel().isGreaterOrEqual(this.level)) {
                return FilterReply.DENY;
            }
            return FilterReply.NEUTRAL;
        }
        if (event.getLevel().isGreaterOrEqual(this.level)) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }

    public void setLevel(String level) {
        this.level = Level.toLevel(level);
    }

    public void start() {
        if (this.level != null) {
            super.start();
        }
    }

    public boolean isRebel() {
        return this.rebel;
    }

    public void setRebel(boolean rebel) {
        this.rebel = rebel;
    }
}
