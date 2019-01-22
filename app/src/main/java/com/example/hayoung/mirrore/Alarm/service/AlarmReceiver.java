package com.example.hayoung.mirrore.Alarm.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.example.hayoung.mirrore.Alarm.model.Alarm;
import com.example.hayoung.mirrore.Alarm.ui.AlarmLandingPageActivity;
import com.example.hayoung.mirrore.Alarm.util.AlarmUtils;
import com.example.hayoung.mirrore.R;

import java.util.Calendar;

public final class AlarmReceiver extends BroadcastReceiver {

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";
    private static final String CHANNEL_ID = "channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Alarm alarm = intent.getBundleExtra(BUNDLE_EXTRA).getParcelable(ALARM_KEY);
        final int id = AlarmUtils.getNotificationId(alarm);

        //추가
        CharSequence channel_name = Integer.toString(R.string.channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //추가
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }


        final Intent notifIntent = new Intent(context, AlarmLandingPageActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pIntent = PendingIntent.getActivity(
                context, id, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        //추가
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        //원본
//        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        builder.setColor(ContextCompat.getColor(context, R.color.accent));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(alarm.getLabel());
        builder.setTicker(alarm.getLabel());
        builder.setVibrate(new long[] {1000,500,1000,500,1000,500});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        manager.notify(id, builder.build());

        //Reset Alarm manually
        setReminderAlarm(context, alarm);
    }

    //Convenience method for setting a notification
    public static void setReminderAlarm(Context context, Alarm alarm) {

        //Check whether the alarm is set to run on any days
        if(!AlarmUtils.isAlarmActive(alarm)) {
            //If alarm not set to run on any days, cancel any existing notifications for this alarm
            cancelReminderAlarm(context, alarm);
            return;
        }

        final Calendar nextAlarmTime = getTimeForNextAlarm(alarm);

        System.out.println("nextAlarmTime.getTimeInMillis():" + nextAlarmTime.getTimeInMillis());

        long alarmLong = nextAlarmTime.getTimeInMillis() / (1000 * 60);
        System.out.println("분으로 연산한 후의 alarmLong:" + alarmLong);

        alarmLong = alarmLong * (1000 * 60);
        System.out.println("다시 원상복구된 alarmLong:" + alarmLong);

        alarm.setTime(alarmLong);

        final Intent intent = new Intent(context, AlarmReceiver.class);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ALARM_KEY, alarm);
        intent.putExtra(BUNDLE_EXTRA, bundle);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                AlarmUtils.getNotificationId(alarm),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //잠금화면에서 깨우기
        final PowerManager powerManager = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pIntent);
            System.out.println("alarm.getTime(): " + alarm.getTime());

            wakeLock.acquire();

        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, alarm.getTime(), pIntent);
            System.out.println("alarm.getTime(): " + alarm.getTime());

            wakeLock.acquire();
        }

    }

    /**
     * Calculates the actual time of the next alarm/notification based on the user-set time the
     * alarm should sound each day, the days the alarm is set to run, and the current time.
     * @param alarm Alarm containing the daily time the alarm is set to run and days the alarm
     *              should run
     * @return A Calendar with the actual time of the next alarm.
     */
    private static Calendar getTimeForNextAlarm(Alarm alarm) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());

        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final SparseBooleanArray daysArray = alarm.getDays();

        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
            if(!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
        } while(!isAlarmSetForDay && count < 7);

        return calendar;

    }

    public static void cancelReminderAlarm(Context context, Alarm alarm) {

        final Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                AlarmUtils.getNotificationId(alarm),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);
    }

    private static int getStartIndexFromTime(Calendar c) {

        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break;
            case Calendar.TUESDAY:
                startIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                startIndex = 2;
                break;
            case Calendar.THURSDAY:
                startIndex = 3;
                break;
            case Calendar.FRIDAY:
                startIndex = 4;
                break;
            case Calendar.SATURDAY:
                startIndex = 5;
                break;
            case Calendar.SUNDAY:
                startIndex = 6;
                break;
        }

        return startIndex;

    }

}
