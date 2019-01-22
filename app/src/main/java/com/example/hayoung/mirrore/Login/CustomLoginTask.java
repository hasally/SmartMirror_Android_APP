package com.example.hayoung.mirrore.Login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hayoung.mirrore.R;

import org.json.JSONArray;
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

public class CustomLoginTask extends AsyncTask<String, Void, String> {
    String sendMsg, resultStr, ip_name;
    HttpURLConnection conn;

    private UserInfo userInfo;

    private Context mContext;
    private JSONObject jobj = null;

    public CustomLoginTask(Context context) {
        mContext = context;
        ip_name = mContext.getResources().getString(R.string.ip);
    }

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {

        try {
            userInfo = new UserInfo(mContext);
            URL url = new URL(ip_name+"login.do");//보낼 jsp 주소를 ""안에 작성합니다.
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            OutputStream os = conn.getOutputStream();
            jobj = new JSONObject();
            jobj.put("id", strings[0]);
            System.out.println("url_id : " + strings[0]);
            jobj.put("pw", strings[1]);
            System.out.println("url_id : " + strings[1]);

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
                    System.out.println("builder.tostring : " + builder.toString());

                    JSONObject responseJSON = new JSONObject(builder.toString());
                    System.out.println("responseJSON : " + responseJSON);

                    //로그인 성공여부 string
                    resultStr = (String) responseJSON.get("result");

                    //회원정보
                    JSONArray memberArr = (JSONArray) responseJSON.get("memberList");
                    System.out.println("responseJSONArray : " + memberArr);
                    System.out.println("responseJSONArray_length : " + memberArr.length());
                    for (int i = 0; i < memberArr.length(); i++) {

                        if (resultStr.equals("success")) {
                            //key를 뽑기 위해 jsonObject 형태로 변환
                            JSONObject member = memberArr.getJSONObject(i);
                            System.out.println("member_jsonObj_id : " + member.getString("id"));

                            //userInfo에 저장
                            //member
                            userInfo.setKeyMirrorId(member.getString("mirrorId"));
                            userInfo.setKeyId(member.getString("id"));
                            userInfo.setKeyPw(member.getString("pw"));
                            userInfo.setKeyName(member.getString("name"));
                            userInfo.setKeyPhone(member.getString("phone"));
                            userInfo.setKeyWeatherLoc(member.getString("weatherLoc"));
                            userInfo.setKeySubwayLoc(member.getString("subwayLoc"));
                            //userInfo.setKeyProfile(member.getString("profile"));

                            //setting
                            userInfo.setKeyWeather(Integer.parseInt(member.getString("weather")));
                            userInfo.setKeySubway(Integer.parseInt(member.getString("subway")));
                            userInfo.setKeyMemo(Integer.parseInt(member.getString("memo")));
                            userInfo.setKeyCalendar(Integer.parseInt(member.getString("calendar")));
                            userInfo.setKeyNews(Integer.parseInt(member.getString("news")));
                            userInfo.setKeyMirrorLight(Integer.parseInt(member.getString("mirrorLight")));
                            userInfo.setKeyRoomLight(Integer.parseInt(member.getString("roomLight")));
                            userInfo.setKeyFan(Integer.parseInt(member.getString("fan")));
                            userInfo.setKeyDoorLock(Integer.parseInt(member.getString("doorLock")));
                            userInfo.setKeyNowCondition(Integer.parseInt(member.getString("nowCondition")));


                        }
                    }

                    System.out.println("성공여부 문자열 : " + resultStr);
                    System.out.println("회원정보 데이터 : " + memberArr);
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