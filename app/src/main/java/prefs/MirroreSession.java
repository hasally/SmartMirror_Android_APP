package prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class MirroreSession {
    private static final String TAG = MirroreSession.class.getSimpleName();
    private static final String PREF_NAME = "mirrorelogin";
    private static final String KEY_IS_LOGGED_IN = "isloggedin";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public MirroreSession(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setMirroreLoggedin(boolean isLoggedin) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedin);
        editor.apply();
    }

    public boolean isMirroreLoggedin() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
