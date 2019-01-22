package prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 10/1/2016.
 */

public class UserData implements Parcelable {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "userinfo";
    private static final String KEY_MIRROR_ID = "mirror_id";
    private static final String KEY_ID = "id";
    private static final String KEY_PW = "pw";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_WEATHER_LOC = "weather_loc";
    private static final String KEY_SUBWAY_LOC = "subway_loc";
    private static final String KEY_PROFILE = "profile";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    transient Context ctx;

    public UserData(Context ctx) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
