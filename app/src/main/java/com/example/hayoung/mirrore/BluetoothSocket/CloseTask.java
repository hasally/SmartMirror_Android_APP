package com.example.hayoung.mirrore.BluetoothSocket;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

public class CloseTask extends AsyncTask<Void, Void, Object> {

    public static Context mContext;
    public static AppCompatActivity activity;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    //블루투스가 사용가능한지 검사
    static BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    String closeTask;

    public CloseTask(Context context, Activity activity_work, OutputStream mmOutputStream_work) {
        mContext = context;
        activity = (AppCompatActivity)activity_work;
        mmOutputStream = mmOutputStream_work;
    }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                try {
                    mmOutputStream.close();
                } catch (Throwable t) {/*ignore*/}
                try {
                    mmInputStream.close();
                } catch (Throwable t) {/*ignore*/}
                mmSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e("", result.toString(), (Throwable) result);
            }
        }
    }