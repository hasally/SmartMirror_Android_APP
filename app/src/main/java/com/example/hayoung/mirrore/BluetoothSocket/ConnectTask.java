package com.example.hayoung.mirrore.BluetoothSocket;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import prefs.UserInfo;

public class ConnectTask extends AsyncTask<String, Void, Object> {

    public static Context mContext;
    public static AppCompatActivity activity;

    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    int check = 1;

    String connectCheck;
    //블루투스가 사용가능한지 검사
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;

    public ConnectTask(Context context, Activity activity_out, BluetoothSocket mmSocket_out, BluetoothDevice mmDevice_out) {
        mContext = context;
        activity = (AppCompatActivity) activity_out;
        mmSocket = mmSocket_out;
        mmDevice = mmDevice_out;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Object doInBackground(String... params) {
        try {
            System.out.println("ConnectTask클래스 진입");
            //7. 블루투스 장치로 연결을 시도합니다.

            try {
                mmSocket.connect();
                Log.e("", "Connected");
            } catch (IOException e) {
                Log.e("", e.getMessage());
                try {
                    Log.e("", "trying fallback...");

                    mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                    mmSocket.connect();

                    Log.e("", "Connected");
                } catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
            connectCheck = "블루투스 장치로 연결: 성공";
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

            //workerThread = new DataCheck().beginListenForData(mmInputStream);

        } catch (Throwable t) {
            Log.e("", "connect? " + t.getMessage());
            workerThread.interrupt();
            new CloseTask(mContext, activity, mmOutputStream).execute();
            return t;
        }
        return mmOutputStream;
    }

    @Override
    protected void onPostExecute(Object result) {
        //10. 블루투스 통신이 연결되었음을 화면에 출력합니다.
        connectCheck = "Bluetooth Opened";
        if (result instanceof Throwable) {
            Log.d("", "ConnectTask " + result.toString());

        }
            final Handler handler = new Handler(Looper.getMainLooper());

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == '\n') {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");

                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                System.out.println("mRecv 값: " + data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();
        }


    }

