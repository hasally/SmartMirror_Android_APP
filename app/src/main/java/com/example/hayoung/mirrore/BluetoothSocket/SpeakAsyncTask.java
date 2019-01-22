package com.example.hayoung.mirrore.BluetoothSocket;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hayoung.mirrore.R;
import com.example.hayoung.mirrore.Valifiy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import prefs.MirroreLoginInfo;
import prefs.UserInfo;

public class SpeakAsyncTask extends AsyncTask<String, Void, String> {
    MirroreLoginInfo mirroreLoginInfo;
    UserInfo userInfo;
    String sendMsg, resultStr, ip_name;
    HttpURLConnection conn;

    private Context mContext;
    private JSONObject jobj = null;

    public SpeakAsyncTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    protected String doInBackground(String... strings) {
        userInfo = new UserInfo(mContext);
        mirroreLoginInfo = new MirroreLoginInfo(mContext);
        try {
            //192.168.0.15
            URL url = new URL(ip_name+"speakWeb.do");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();

            /*Gson gson = new GsonBuilder().create();
            String json = gson.toJson(userInfo);*/
            System.out.println("음성 인식 정보 데이터-------------------------");

            jobj = new JSONObject();
            jobj.put("text", strings[0]);
            jobj.put("mirror_id", userInfo.getKeyMirrorId());
            jobj.put("member_id", userInfo.getKeyId());
            System.out.println("음성 인식 정보 확인: "+ jobj);

            //보낼 정보. JsonObject type으로 전송.
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

                    resultStr = builder.toString();

                    Valifiy valifiy = new Valifiy(mContext);
                    valifiy.invalid(resultStr);

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
            conn.disconnect();
        }
        //jsp로부터 받은 리턴 값(성공 여부)
        return resultStr;
    }
}
