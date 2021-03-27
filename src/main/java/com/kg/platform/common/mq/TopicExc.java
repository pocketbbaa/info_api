package com.kg.platform.common.mq;

/**
 * @author hesimin 16-12-29
 */
public interface TopicExc {
    public boolean exc(String tags, String msgBody);
}
