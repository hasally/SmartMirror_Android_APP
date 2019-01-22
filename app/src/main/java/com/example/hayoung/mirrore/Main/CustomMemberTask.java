package com.example.hayoung.mirrore.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hayoung.mirrore.R;
import com.example.hayoung.mirrore.Valifiy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import prefs.MirroreLoginInfo;
import prefs.UserInfo;

public class CustomMemberTask extends AsyncTask<String, Void, String> {
    MirroreLoginInfo mirroreLoginInfo;
    UserInfo userInfo;
    String str, receiveMsg, ip_name = "";
    HttpURLConnection conn;

    private Context mContext;
    private JSONObject jobj = null;

    public CustomMemberTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {
        mirroreLoginInfo = new MirroreLoginInfo(mContext);
        userInfo = new UserInfo(mContext);
        try {
            //192.168.0.15
            URL url = new URL(ip_name+"update_member.do");//보낼 jsp 주소를 ""안에 작성합니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream out = conn.getOutputStream();

            jobj = new JSONObject();
            jobj.put("member_key", strings[0]);
            jobj.put("member_value_en", strings[1]);
            jobj.put("member_value", strings[2]);
            jobj.put("member_id", strings[3]);
            jobj.put("mirror_id",userInfo.getKeyMirrorId());

            out.write(jobj.toString().getBytes("utf-8"));//OutputStream에 담아 전송합니다.
            out.flush();
            out.close();

            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if (conn.getResponseCode() == conn.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    receiveMsg = builder.toString();
                    System.out.println("builder.tostring : " + receiveMsg);

                    Valifiy valifiy = new Valifiy(mContext);
                    valifiy.invalid(receiveMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
                // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
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