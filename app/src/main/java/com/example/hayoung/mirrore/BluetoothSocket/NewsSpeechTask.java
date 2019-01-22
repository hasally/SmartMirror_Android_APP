package com.example.hayoung.mirrore.BluetoothSocket;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewsSpeechTask extends AsyncTask<Object, Void, String> {
    public StringBuilder sb;
    String clientId = "sM3ppoIQEVPdEN8YUi2V";
    String clientSecret = "Um9vhmD81I";
    int display = 4;

    String dataTemp="이 시각 주요 뉴스 입니다!";

    @Override
    protected String doInBackground(Object[] objects) {
        try {

            String text = URLEncoder.encode("오늘의 뉴스", "UTF-8"); //�տ� �˻���

            String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=" + display + "&sort=date";
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            System.out.println("여기까지왔다.");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            con.connect();

            BufferedReader br;

            if (con.getResponseCode() == con.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            con.disconnect();
            String data = sb.toString();

            System.out.println("뉴스 데이터 확인 (row_data) :"+data);
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("items");
            List jsonArray = new ArrayList();
            for(int n = 0; n < array.length(); n++)
            {
                if(n!=0){
                    jsonArray.add(array.getJSONObject(n).get("title").toString() +":"+array.getJSONObject(n).get("description").toString());
                    System.out.println("jsonObject for문 :"+jsonArray.get(n-1));
                }
            }

            /*array = data.split("\"");

            String[] title = new String[display];
            String[] description = new String[display];

            int k = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("title"))
                    title[k] = array[i + 2];
                if (array[i].equals("description"))
                    description[k] = array[i + 2];

            }
            for (int i=1; i<description.length; i++){
                description[i] = description[i].substring(description[i].lastIndexOf("기자")-1);
            }

            for (int i=1; i<title.length; i++) {
                if(i==1){
                    dataTemp += "첫번째 기사입니다."+title[i]+" "+description[i]+"의 글입니다.";
                } else if(i==2) {
                    dataTemp += "두번째 기사입니다."+title[i]+" "+description[i]+"의 글입니다.";
                } else {
                    dataTemp += "세번째 기사입니다."+title[i]+" "+description[i]+"의 글입니다.";
                }
            }*/

            System.out.println("대입 후 dataTemp: "+dataTemp);
            System.out.println("----------------------------");

//            System.out.println("ù��° Ÿ��Ʋ : " + title[0]);
//            System.out.println("�ι�° Ÿ��Ʋ : " + title[1]);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            return dataTemp;
        }
    }

}
