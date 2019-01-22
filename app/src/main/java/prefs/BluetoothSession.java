package prefs;

import android.content.Context;
import android.content.SharedPreferences;


public class BluetoothSession {
        private static final String TAG = BluetoothSession.class.getSimpleName();
        private static final String PREF_NAME = "bluetooth";
        private static final String KEY_IS_BLUETOOTH = "isconnected";
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        Context ctx;

        public BluetoothSession(Context ctx) {
            this.ctx = ctx;
            prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
            editor = prefs.edit();
        }

        public void setConnected(boolean isConnected) {
            editor.putBoolean(KEY_IS_BLUETOOTH, isConnected);
            editor.apply();
        }

        public boolean isConnected() {
            return prefs.getBoolean(KEY_IS_BLUETOOTH, false);
        }
}