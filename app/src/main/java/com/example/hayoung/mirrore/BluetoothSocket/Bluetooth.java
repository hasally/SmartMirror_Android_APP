package com.example.hayoung.mirrore.BluetoothSocket;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;

public class Bluetooth extends AsyncTask<Void, Void, Object> {

    public static Context mContext;
    public static AppCompatActivity activity;

    TextView myLabel;
    //블루투스가 사용가능한지 검사
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    public Bluetooth(Context context, Activity activity) {
        mContext = context;
        activity = activity;
    }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                System.out.println("ConnectTask클래스 진입");
                //7. 블루투스 장치로 연결을 시도합니다.
                mmSocket.connect();
                System.out.println("블루투스 장치로 연결: 성공");

                //8. 소켓에 대한 입출력 스트림을 가져옵니다.
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
                System.out.println("소켓에 대한 입출력 스트림: 성공");
                System.out.println("mmOutputStream: " + mmOutputStream);
                System.out.println("mmInputStream: " + mmInputStream);


                //9. 데이터 수신을 대기하기 위한 스레드를 생성하여 입력스트림로부터의 데이터를 대기하다가
                // 들어오기 시작하면 버퍼에 저장합니다.
                // '\n' 문자가 들어오면 지금까지 버퍼에 저장한 데이터를 UI에 출력하기 위해 핸들러를 사용합니다.
                new DataCheck().beginListenForData(mmInputStream);
            } catch (Throwable t) {
                Log.e("", "connect? " + t.getMessage());
                workerThread.interrupt();
                new CloseTask(mContext, activity, mmOutputStream).execute();
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            //10. 블루투스 통신이 연결되었음을 화면에 출력합니다.
            myLabel.setText("Bluetooth Opened");
            if (result instanceof Throwable) {
                Log.d("", "ConnectTask " + result.toString());

            }
        }
    }    //ConnectTask End



