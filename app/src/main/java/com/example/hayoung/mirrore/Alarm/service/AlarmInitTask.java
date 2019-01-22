package com.example.hayoung.mirrore.Alarm.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hayoung.mirrore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import prefs.UserInfo;

public class AlarmInitTask extends AsyncTask<String, Void, String> {
    UserInfo userInfo;
    String str, receiveMsg, ip_name = "";
    HttpURLConnection conn;

    private Context mContext;
    private JSONObject jobj = null;

    public AlarmInitTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {
        userInfo = new UserInfo(mContext);
        try {

            URL url = new URL(ip_name+"initAlarm.do");//보낼 jsp 주소를 ""안에 작성합니다.
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();

            jobj = new JSONObject();
            jobj.put("mirror_id", userInfo.getKeyMirrorId());
            jobj.put("member_id", userInfo.getKeyId());
            os.write(jobj.toString().getBytes("utf-8"));//OutputStream에 담아 전송
            os.flush();

            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if (conn.getResponseCode() == conn.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    System.out.println("builder.tostring : " + builder.toString());

                    receiveMsg = builder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
                // 통신이 실패했을 때 실패한 이유 로그 확인
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
