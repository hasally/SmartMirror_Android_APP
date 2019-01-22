package prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class MirroreLoginInfo  implements Serializable {
        private static final String TAG = MirroreSession.class.getSimpleName();
        private static final String PREF_NAME = "mirroreinfo";
        private static final String KEY_MIRROR_ID = "mirror_id";

        transient SharedPreferences prefs;
        transient SharedPreferences.Editor editor;
        transient Context ctx;

        public MirroreLoginInfo(Context ctx) {
            this.ctx = ctx;
            prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
            editor = prefs.edit();
        }

        public void setKeyMirrorId(String mirror_id) {
            editor.putString(KEY_MIRROR_ID, mirror_id);
            editor.apply();
        }

        public String getKeyMirrorId() {
            return prefs.getString(KEY_MIRROR_ID, "");
        }

    public void clearMirroreInfo() {
        editor.clear();
        editor.commit();
    }


}
