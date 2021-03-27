package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * 时间枚举：最近3天、最近7天、最近30天
 */
public enum TimeEnum {

    /**
     * 最近3天
     */
    Days_3(1) {
        @Override
        public Date startDate() {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_YEAR, -2);
            Date date = new Date();
            date.setTime(now.getTimeInMillis());
            return date;
        }

        @Override
        public Date endDate() {
            return null;
        }
    },
    /**
     * 最近7天
     */
    Days_7(2) {
        @Override
        public Date startDate() {
            return null;
        }

        @Override
        public Date endDate() {
            return null;
        }
    },
    /**
     * 最近30天
     */
    Days_30(3) {
        @Override
        public Date startDate() {
            return null;
        }

        @Override
        public Date endDate() {
            return null;
        }
    };

    private int time;

    TimeEnum(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public static TimeEnum getTimeEnum(int time) {
        TimeEnum times[] = TimeEnum.values();
        Optional<TimeEnum> optional = Arrays.stream(times).filter(item -> item.time == time).findFirst();
        return optional.isPresent() ? optional.get() : TimeEnum.Days_3;
    }

    public abstract Date startDate();

    public abstract Date endDate();

    public static void main(String[] args) {
        TimeEnum time = TimeEnum.getTimeEnum(1);
        System.out.println(time.startDate());
    }
}
