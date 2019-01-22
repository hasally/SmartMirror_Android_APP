package com.example.hayoung.mirrore.SignUp;

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

public class CustomMirrorIdTask extends AsyncTask<String, Void, String>{
    String sendMsg, resultStr,ip_name;
    int limit_member;
    HttpURLConnection conn;

    private UserInfo userInfo;

    private Context mContext;
    private JSONObject jobj = null;

    public CustomMirrorIdTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {
        try {
            userInfo = new UserInfo(mContext);
            //192.168.0.15
            URL url = new URL(ip_name+"mirror_id_check.do");//보낼 jsp 주소를 ""안에 작성합니다.
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            OutputStream os = conn.getOutputStream();
            jobj = new JSONObject();
            jobj.put("mirror_id", strings[0]);
            System.out.println("url_mirror_id : " + strings[0]);

            //보낼 정보. JsonObject type으로 전송.
            os.write(jobj.toString().getBytes("utf-8"));//OutputStream에 담아 전송합니다.
            os.flush();
            os.close();

            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if (conn.getResponseCode() == conn.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    reader.close();
                    System.out.println("builder.tostring : " + builder.toString());

                    JSONObject responseJSON = new JSONObject(builder.toString());
                    System.out.println("responseJSON : " + responseJSON);

                    //미러 아이디 확인 성공여부 string
                    resultStr = (String) responseJSON.get("resultStr");
                    if(resultStr.equals("success")){
                        userInfo.setKeyMirrorId(strings[0]);
                    }

                    //해당 미러 아이디에 등록된 회원 수
                    limit_member = (int) responseJSON.get("limit_member");

                    System.out.println("responseJSONInt : " + limit_member);

                    System.out.println("성공여부 문자열 : " + resultStr);
                    System.out.println("최대 회원 수 데이터 : " + limit_member);
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