package com.example.hayoung.mirrore.SignUp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hayoung.mirrore.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomIdOverlapCheckTask extends AsyncTask<String, Void, String> {
    String str, receiveMsg, ip_name = "";
    HttpURLConnection conn;

    private Context mContext;

    public CustomIdOverlapCheckTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {

        try {
            //192.168.0.15
            URL url = new URL(ip_name+"id_overlap_check.do");//보낼 jsp 주소를 ""안에 작성합니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            String requestStr = "id=" + strings[0];

            OutputStream out = conn.getOutputStream();

            out.write(requestStr.getBytes());
            out.write("&".getBytes());
            out.flush();
            out.close();

            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStream is = conn.getInputStream();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuffer response = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                    }

                    receiveMsg = response.toString();
                    System.out.println("receiveMsg : " + receiveMsg);
                    System.out.println("http연결 성공_CustomIdOverlapCheckTask");
                    reader.close();
                }
                is.close();

            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
                // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        //jsp로부터 받은 리턴 값(성공 여부)
        return receiveMsg;
    }
}