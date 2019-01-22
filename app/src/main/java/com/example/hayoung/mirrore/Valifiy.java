package com.example.hayoung.mirrore;

import android.content.Context;

import prefs.MirroreLoginInfo;
import prefs.MirroreSession;
import prefs.UserInfo;

public class Valifiy {
    MirroreLoginInfo mirroreLoginInfo;
    MirroreSession mirroreSession;
    Context mContext;
    public Valifiy(Context context){
        mContext = context;
    }
    public void invalid(String result){
        mirroreLoginInfo = new MirroreLoginInfo(mContext);
        mirroreSession = new MirroreSession(mContext);

        if(result != "validated_user"){
            mirroreLoginInfo.clearMirroreInfo();
            mirroreSession.setMirroreLoggedin(false);
        }
    }
}
