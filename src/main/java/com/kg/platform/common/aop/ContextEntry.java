package com.kg.platform.common.aop;

import com.kg.platform.common.entity.MonitorEntity;

/**
 * 
 * @author Mark
 * @version $Id: ContextEntry.java, v 0.1 2016年6月10日 下午5:28:52 pengliqing Exp $
 */
public class ContextEntry {

    private static ThreadLocal<MonitorEntity> monitor = new ThreadLocal<MonitorEntity>();

    public static MonitorEntity getMonitor() {
        MonitorEntity me = monitor.get();
        if (me == null)
            me = MonitorEntity.getInstance();
        monitor.set(me);
        return me;
    }

    public static void removeMonitor() {
        monitor.remove();
    }
}
