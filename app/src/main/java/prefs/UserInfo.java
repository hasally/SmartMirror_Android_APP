package prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by Administrator on 10/1/2016.
 */

public class UserInfo implements Serializable {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "userinfo";
    private static final String KEY_MIRROR_ID = "mirror_id";
    //member
    private static final String KEY_ID = "id";
    private static final String KEY_PW = "pw";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_WEATHER_LOC = "weather_loc";
    private static final String KEY_SUBWAY_LOC = "subway_loc";
    private static final String KEY_PROFILE = "profile";
    //setting
    private static final String KEY_WEATHER = "weather";
    private static final String KEY_SUBWAY = "subway";
    private static final String KEY_MEMO = "memo";
    private static final String KEY_CALENDAR = "calendar";
    private static final String KEY_NEWS = "news";
    private static final String KEY_MIRROR_LIGHT = "mirror_light";
    private static final String KEY_ROOM_LIGHT = "room_light";
    private static final String KEY_FAN = "fan";
    private static final String KEY_DOOR_LOCK = "door_rock";
    private static final String KEY_NOW_CONDITION = "now_condition";
    //bluetooth
    private static final String KEY_BLUETOOTH = "bluetooth";

    transient SharedPreferences prefs;
    transient SharedPreferences.Editor editor;
    transient Context ctx;

    public UserInfo(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setKeyMirrorId(String mirror_id) {
        editor.putString(KEY_MIRROR_ID, mirror_id);
        editor.apply();
        mirror_id = "";
    }

    public String getKeyMirrorId() {
        return prefs.getString(KEY_MIRROR_ID, "");
    }

    public void setKeyId(String id) {
        editor.putString(KEY_ID, id);
        editor.apply();
        id = "";
    }

    public String getKeyId() {
        return prefs.getString(KEY_ID, "");
    }

    public void setKeyPw(String pw) {
        editor.putString(KEY_PW, pw);
        editor.apply();
        pw = "";
    }

    public String getKeyPw() {
        return prefs.getString(KEY_PW, "");
    }

    public void setKeyName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
        name = "";
    }

    public String getKeyName() {
        return prefs.getString(KEY_NAME, "");
    }

    public void setKeyPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.apply();
        phone = "";
    }

    public String getKeyPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    public void setKeyWeatherLoc(String weather_loc) {
        editor.putString(KEY_WEATHER_LOC, weather_loc);
        editor.apply();
        weather_loc = "";
    }

    public String getKeyWeatherLoc() {
        return prefs.getString(KEY_WEATHER_LOC, "");
    }

    public void setKeySubwayLoc(String subway_loc) {
        editor.putString(KEY_SUBWAY_LOC, subway_loc);
        editor.apply();
        subway_loc = "";
    }

    public String getKeySubwayLoc() {
        return prefs.getString(KEY_SUBWAY_LOC, "");
    }

    public void setKeyProfile(String profile) {
        editor.putString(KEY_PROFILE, profile);
        editor.apply();
        profile = "";
    }

    public String getKeyProfile() {
        return prefs.getString(KEY_PROFILE, "");
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }

    //setting
    public void setKeyWeather(int weather) {
        editor.putInt(KEY_WEATHER, weather);
        editor.apply();
        weather = 0;
    }

    public int getKeyWeather() {
        return prefs.getInt(KEY_WEATHER, 0);
    }

    public void setKeySubway(int subway) {
        editor.putInt(KEY_SUBWAY, subway);
        editor.apply();
        subway = 0;
    }

    public int getKeySubway() { return prefs.getInt(KEY_SUBWAY, 0); }

    public void setKeyMemo(int memo) {
        editor.putInt(KEY_MEMO, memo);
        editor.apply();
        memo = 0;
    }

    public int getKeyMemo() { return prefs.getInt(KEY_MEMO, 0); }

    public void setKeyCalendar(int calendar) {
        editor.putInt(KEY_CALENDAR, calendar);
        editor.apply();
        calendar = 0;
    }

    public int getKeyCalendar() { return prefs.getInt(KEY_CALENDAR, 0); }

    public void setKeyNews(int news) {
        editor.putInt(KEY_NEWS, news);
        editor.apply();
        news = 0;
    }

    public int getKeyNews() { return prefs.getInt(KEY_NEWS, 0); }

    public void setKeyMirrorLight(int mirror_light) {
        editor.putInt(KEY_MIRROR_LIGHT, mirror_light);
        editor.apply();
        mirror_light = 0;
    }

    public int getKeyMirrorLight() { return prefs.getInt(KEY_MIRROR_LIGHT, 0); }

    public void setKeyRoomLight(int room_light) {
        editor.putInt(KEY_ROOM_LIGHT, room_light);
        editor.apply();
        room_light = 0;
    }

    public int getKeyRoomLight() { return prefs.getInt(KEY_ROOM_LIGHT, 0); }

    public void setKeyFan(int fan) {
        editor.putInt(KEY_FAN, fan);
        editor.apply();
        fan = 0;
    }

    public int getKeyFan() { return prefs.getInt(KEY_FAN, 0); }

    public void setKeyDoorLock(int door_lock) {
        editor.putInt(KEY_DOOR_LOCK, door_lock);
        editor.apply();
        door_lock = 0;
    }

    public int getKeyDoorLock() { return prefs.getInt(KEY_DOOR_LOCK, 0); }

    public void setKeyNowCondition(int now_condition) {
        editor.putInt(KEY_NOW_CONDITION, now_condition);
        editor.apply();
        now_condition = 0;
    }

    public int getKeyNowCondition() { return prefs.getInt(KEY_NOW_CONDITION, 0); }

    public String getKeyBluetooth() { return prefs.getString(KEY_BLUETOOTH, ""); }

    public void setKeyBluetooth(String bluetooth) {
        editor.putString(KEY_BLUETOOTH, bluetooth);
        editor.apply();
        bluetooth = "";
    }

}
