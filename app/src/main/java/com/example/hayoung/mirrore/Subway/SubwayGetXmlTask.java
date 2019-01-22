package com.example.hayoung.mirrore.Subway;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SubwayGetXmlTask extends AsyncTask<String, Void, String> {
    private String receiveMsg;
    private Context mContext;

    public SubwayGetXmlTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        boolean inrow = false, inStation_cd = false, inLefttime = false;
        boolean inSubwayname = false;

        String STATION_CD = "", LEFTTIME = "", SUBWAYNAME = "";
        int up = 1;
        try {

//            System.out.println("역코드 확인: "+su10);
//            URL url = new URL("http://openapi.seoul.go.kr:8088/515047645379616e39385a67724e65/xml/SearchArrivalInfoByIDService/1/2/1004/1/3");//검색 URL부분
            System.out.println("역코드: "+strings[0]+", 상행/하행: "+strings[1]+", 일자: "+strings[2]);
            URL url = new URL("http://openapi.seoul.go.kr:8088/515047645379616e39385a67724e65/xml/SearchArrivalInfoByIDService/1/1/" + strings[0] + "/" + strings[1] + "/" + strings[2]); //검색 URL부분
            InputStream urIls = url.openStream();

            System.out.println("url인식 성공");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            System.out.println("XmlPullParserFactory 성공");

            parser.setInput(urIls, "UTF-8");
            parser.next();

            System.out.println("parser 성공");

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                System.out.println("parserEvent while문 진입 성공");
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        System.out.println("parserEvent case문1 진입 성공");
                        if (parser.getName().equals("STATION_CD")) { //STATION_CD 만나면 내용을 받을수 있게 하자
                            inStation_cd = true;
                        }

                        if (parser.getName().equals("LEFTTIME")) { //LEFTTIME 만나면 내용을 받을수 있게 하자
                            inLefttime = true;
                        }
                        if (parser.getName().equals("SUBWAYNAME")) { //SUBWAYNAME 만나면 내용을 받을수 있게 하자
                            inSubwayname = true;
                        }
                        if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                            receiveMsg = "error";
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        System.out.println("parserEvent case문2 진입 성공");
                        if (inStation_cd) { //inStation_cd true일 때 태그의 내용을 저장.
                            STATION_CD = parser.getText();
                            System.out.println("STATION_CD: " + STATION_CD);
                            System.out.println("STATION_CD 성공");
                            inStation_cd = false;
                        }

                        if (inLefttime) { //inLefttime true일 때 태그의 내용을 저장.
                            LEFTTIME = parser.getText();
                            System.out.println("LEFTTIME: " + LEFTTIME);
                            inLefttime = false;
                        }
                        if (inSubwayname) { //inSubwayname true일 때 태그의 내용을 저장.
                            SUBWAYNAME = parser.getText();
                            System.out.println("SUBWAYNAME: " + SUBWAYNAME);
                            inSubwayname = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        System.out.println("parserEvent case문3 진입 성공");
                        if (parser.getName().equals("row")) {
                            System.out.println("행완성 성공");
                            receiveMsg = SUBWAYNAME + "행\n" + LEFTTIME + "\n\n";
                            System.out.println("receiveMsg:" + receiveMsg);
                            inrow = false;
                        }
                        break;
                }
                parserEvent = parser.next();

            }
            if (LEFTTIME.equals("99:99:99")) {
                receiveMsg = "종점입니다";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
        }
        //jsp로부터 받은 리턴 값(성공 여부)
        return receiveMsg;
    }
}
