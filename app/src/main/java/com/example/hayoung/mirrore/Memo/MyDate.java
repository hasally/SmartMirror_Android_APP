package com.example.hayoung.mirrore.Memo;

import java.util.Calendar;

public class MyDate {

    Calendar calendar = Calendar.getInstance();

    private final int year = calendar.get(Calendar.YEAR);

    private final int month = calendar.get(Calendar.MONTH) + 1;

    private final int day = calendar.get(Calendar.DAY_OF_MONTH);

//    private final int hour = calendar.get(Calendar.HOUR);
    private final int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示

    private final int minute = calendar.get(Calendar.MINUTE);

    private final int second = calendar.get(Calendar.SECOND);

    private final int weekday = calendar.get(Calendar.DAY_OF_WEEK);

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getWeekday() {
        return weekday;
    }

    @Override
    public String toString() {
        //        return year + "年" + month + "月" + day + "日 " + hour + ":" + minute + ":" + second;
        return String.format("%02d년%02d월%02d일 %02d:%02d:%02d", year, month, day, hour, minute, second);

    }
}
