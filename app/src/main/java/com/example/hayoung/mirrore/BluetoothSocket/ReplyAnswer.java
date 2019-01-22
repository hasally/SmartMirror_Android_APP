package com.example.hayoung.mirrore.BluetoothSocket;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

public class ReplyAnswer {
    public String sendMsg;
    TextView myLabel;
    public OutputStream mmOutputStream;
    public static Context mContext;
    UserInfo userInfo;

    public ReplyAnswer(Context context) {
        mContext = context;


    }

    public String replyAnswer(String input, ArrayList<String> txtSpeechInput, TextToSpeech tts, OutputStream outputStream) {
        mmOutputStream = outputStream;
        System.out.println("outstream:" + mmOutputStream);
        //기능 배열 변수 선언부입니다..
        // 0기능 안내
        String mirrorFunc[] = {"기능", "기넝", "기눙", "긔눙", "긴응", "안녕", "안녕하세요", "기능 알려 줘",
                "기능 마래 줘", "기능 말해 줘", "기능 말해", "기능 말 해", "기능 뭐야", " 기능 머야", "기능 마래", "기능 뭐 있냐", "기능 뭐있냐", "기능 뭐있어", "기능 뭐 있어", "기능 뭐뭐 있어", "기능 뭐 뭐 있어", "기능 뭐냐", "기능 머냐", "기능 모냐", "기능 말해주", "기능 말해 주"};

        //현재 시간 응답
        String timeNow[] = {"지금 몇시야", "지금몇시야", "지금 몇 시야", "지금 몄시야", "지금 몄 시야", "지금몄시야", "지금몄씨야", "지금 몄씨야", "지금 며씨야", "지금며씨야", "미러리몇시야", "미러리 몇시야", "미러리 멧시야", "미러리멧시야", "미러리멧씨야", "미러리 멧씨야"};

            /*내부처리되는 명령(show/hide)
            11내부조명*/
        String showInnerLight[] = {"거울조명 켜줘", "거울 조명 켜줘", "거울 조명 켜 줘", "거울 조명 켜", "거울 조명 키자", "거울조명 켜", "거울조명 키자", "거울조명 켜죠", "거울조명 켜 죠", "거울 조명 켜 죠", "거울저명 켜줘", "거울 저명 켜 줘", "거울 저명 켜 줘"};
        String hideInnerLight[] = {"거울조명 꺼줘", "거울 조명 꺼줘", "거울 조명 꺼 줘", "거울 조명 꺼", "거울 조명 끄자", "거울조명 꺼", "거울조명 끄자", "거울조명 꺼죠", "거울조명 꺼 죠", "거울 조명 꺼 죠", "거울저명 꺼줘", "거울 저명 꺼 줘", "거울 저명 꺼 줘"};

        //12메모
        String showMemo[] = {"메모 켜 줘", "메모 켜", "메모 켜줘", "메모 켜죠", "메모 켜 죠", "메모 켜쥬", "메모 켜 쥬", "매모 켜줘", "매모 켜 줘", "매모 켜", "매모 켜죠", "매모 켜 죠"};
        String hideMemo[] = {"메모 꺼 줘", "메모 꺼줘", "메모 꺼", "메모 꺼죠", "메모 꺼 죠", "메모 꺼쥬", "메모 꺼 쥬", "메 모 꺼 줘"};

        //13일정
        String showCalendar[] = {"일정 보여 줘", "일정 보여줘", "일 정 보 여 줘", "일정 보여줘", "일 정", "일쩡 보여 줘", "일쩡 보여줘", "일정 보여죠", "일정 보여쥬", "일정 뭐야", "일정 어떻게 돼", "일정 알려줘", "일정 알려 줘"};
        String hideCalendar[] = {"일정 꺼줘", "일정 꺼져", "일 정 꺼 줘", "일정꺼줘", "일정 꺼", "일정 꺼죠", "일정 그만", "일정꺼조"};

        //end of 내부처리되는 명령(show/hide)

        //주변기기제어 관련 명령(on/off)

        //21 외부조명
        String onOuterLight[] = {"너무 어두워", "불 커줘", "불커줘", "불 켜 줘", "불 커 줘", "불커 줘", "불 커줘", "조명 커줘", "조명 커 줘", "조명 켜 줘", "조명 커조", "조명 커저", "조명 커져", "조명 커종", "불 켜줘", "조명 켜줘", "조명 켜 줘", "조명 켜조", "조명 켜저", "조명 켜져", "조명 켜종", "외부조명 켜줘", "외부조명 켜 줘", "외부 조명 켜 줘", "외부 조명 켜줘", "외부 조명 켜줘", "외부 조명 켜 줘", "외부 조명 켜져", "외부 조명 켜 져", "외부 조명 켜", "외부조명 켜"};
        String offOuterLight[] = {"너무 밝아", "불 꺼줘", "불꺼줘", "불 꺼 줘", "조명 꺼줘", "조명 꺼 줘", "조명 꺼조", "조명 꺼저", "조명 꺼져", "조명 꺼종", "조명 꺼저", "조명 꺼 줘", "조명 꺼조", "조명 꺼저", "조명 꺼져", "조명 꺼종", "외부조명 꺼줘", "외부조명 꺼 줘", "외부 조명 꺼 줘", "외부 조명 꺼줘", "외부 조명 꺼줘", "외부 조명 꺼 줘", "외부 조명 꺼져", "외부조명 꺼 져", "외부 조명 꺼", "외부조명 꺼"};

        //22 선풍기
        String onFan[] = {"선풍기", " 성풍기", "성푼기", "선퐁기", "성풍긔", "선풍긔",
                "선풍기 켜줘", "선풍기 켜죠", "선풍기 켜 줘", "선풍기 켜 죠", "성풍기 켜줘", "성풍기 켜죠", "선풍기 켜", "선풍기 켜주"};
        String offFan[] = {"선풍기 꺼", "선풍기 꺼줘", "선풍기 꺼 줘", "선풍기 꺼죠", "선풍기 꺼 죠", "선풍기 꺼져", "선풍기 끄자"};

        //end of 주변기기제어 관련 명령(on/off)

        //외부입력값 관련 명령(open,on/close,off)
        //31 자동문(cctv)
        String openDoor[] = {"문 열어줘", "문 열 어 줘", "문 열어 줘", "문 열어", "문 열 어", "문열 어", "문 열려", "문 열어봐", "문 잠금해제", "문 잠금 해제", "문 열자", "문 열어요"};
        String closeDoor[] = {"문 닫아줘", "문 닫 아 줘", "문 닫아 줘", "문 닫아", "문 닫어", "문 닫아봐", "문 닫어봐", " 문 잠가", "문 잠궈", "문 잠구어", "문 잠가라", "문 잠그자", "문 닫자", "문 닫아요", "문 닫으라고"};

        //32온습도
        String onTemp[] = {"온도 알려 줘", "온도 알려줘", "온 도 알 려 줘", "온 도 알려줘", "온도", "온도 알려죠", "온도 알려 져", "언도 알려줘", "언도 알려 줘", "습도 알려 줘", "습도 알려줘", "습 도 알 려 줘", "습 도 알려줘", "습도", "습도 알려죠", "습도 알려 져"};
        String offTemp[] = {"온도 꺼줘", "온도 꺼", "온 도 꺼 줘", "온도 꺼죠", "온도꺼", "온도 그만", "온도꺼죠", "온도 꺼져", "온도꺼져", "습도 꺼 줘", "습도 꺼줘", "습 도 꺼 줘", "습도 꺼죠", "습도꺼죠", "습도끄라고", "습도 끄라고"};

        //end of 외부입력값 관련 명령(open,on/close,off)

        //질문 2회이상지속되는 명령
        //41버스
        String showBus[] = {"버스", " 뻐쓰", "뽀쓰", " 보스", "뻐쑤",
                "버스 어디야", "버스 오디야", "버스 어디 야", "버스 오디 야", "버스 언제 와",
                "버스 시간 보여 줘", "버스 시간보여줘", "버스 시간", "버스 시간 보여줘", "버스 시간 버여져", "버스 시간 버여 져", "버스 시간 봐봐", "버스 시간 봐 봐"};
        String hideBus[] = {};

        //42날씨
        String showWeather[] = {"날씨", "나씨", "널씨", "날씨 알려줘", "날씨 알려 줘", "날씨 알려죠", "날씨 알려 죠", "날씨 아려줘", "날씨 아려 줘", "날씨 알려져", "날씨 알려 져", "날씨 알러져"};
        String hideWeather[] = {};

        // 뉴스
        String news[] = {"뉴스", "뉴스 읽어줘", "뉴스 읽어 줘", "뉴스읽어줘", "뉴스 읽어조", "뉴스읽어조", "뉴스 읽어", "뉴스 말해줘", "뉴스 읽어봐", "뉴스 읽어바", "뉴스 들려줘", "뉴스 들려조"};

        // 재미
        String joke[] = {"노래", "비트박스", "노래 해조", "노래 불러조", "비트박스 해줘", "노래 해줘", "심심해", "노래 불러봐", "노래 불러 줘"};
        String joke_school[] = {"피곤해", "피곤", "힘들어", "그만할래", "도와줘", "죽을꺼같아", "죽겠어"};


        String myString;


        String var = "다시 한번만 말씀해주세요";//음성 출력 값 옵션 스프링
        int optionNum;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Arrays.asList(mirrorFunc).contains(input)) {
                    var = "거울조명, 선풍기, 문, 온도와 습도, 버스, 날씨, 일정 기능을 on/off 할 수 있습니다.";
                    optionNum = 0;
                    txtSpeechInput.add("[스마트미러] " + var + " \n");
                    System.out.println("speakWeb() 호출 전");
                    speakWeb(tts, var);
                    System.out.println("speakWeb() 호출 후");
                } else if (Arrays.asList(showInnerLight).contains(input)) {
                    var = "거울 조명을 켜드릴게요.";
                    optionNum = 111;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(hideInnerLight).contains(input)) {
                    var = "거울 조명을 꺼드릴게요.";
                    optionNum = 110;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(showMemo).contains(input)) {
                    var = "메모를 보여드릴게요";
                    optionNum = 121;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(hideMemo).contains(input)) {
                    var = "메모를 꺼드릴게요";
                    optionNum = 120;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(showCalendar).contains(input)) {
                    var = "일정을 보여드릴게요";
                    optionNum = 131;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(hideCalendar).contains(input)) {
                    var = "일정을 꺼드릴게요";
                    optionNum = 130;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(onOuterLight).contains(input)) {
                    txtSpeechInput.add("on");

                    myString = "on";
                    sendData(myString, mmOutputStream);

                    var = "외부 조명을 켜드릴게요.";
                    optionNum = 211;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (Arrays.asList(offOuterLight).contains(input)) {
                    txtSpeechInput.add("off");
                    myString = "off";
                    sendData(myString, mmOutputStream);

                    var = "외부 조명을 꺼드릴게요.";
                    optionNum = 210;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (Arrays.asList(onFan).contains(input)) {
                    var = "선풍기를 켜드릴게요.";
                    optionNum = 221;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (Arrays.asList(offFan).contains(input)) {
                    var = "선풍기를 꺼드릴게요.";
                    optionNum = 220;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (Arrays.asList(openDoor).contains(input)) {
                    var = "문을 열어드릴게요.";
                    optionNum = 311;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(closeDoor).contains(input)) {
                    var = "문을 닫아드릴게요.";
                    optionNum = 310;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(onTemp).contains(input)) {
                    var = "온도와 습도를 켜드릴게요.";
                    optionNum = 321;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(offTemp).contains(input)) {
                    var = "온도와 습도를 꺼드릴게요.";
                    optionNum = 320;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (Arrays.asList(showBus).contains(input)) {
                    var = "어떤 버스를 알려드릴까요?";
                    optionNum = 410;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(hideBus).contains(input)) {
                    var = "번 버스를 알려드릴게요.";
                    optionNum = 411;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(showWeather).contains(input)) {
                    var = "어디 날씨를 알려드릴까요?";
                    optionNum = 420;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(hideWeather).contains(input)) {
                    var = "날씨를 알려드릴게요.";
                    optionNum = 421;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(joke).contains(input)) {
                    var = "노래 잘 못하는데.. 쿵쿵따리 쿵쿵따 쿵쿵쿵 따따";
                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(news).contains(input)) {
                    NewsSpeechTask newsSpeechTask = new NewsSpeechTask();

                    var = newsSpeechTask.execute().get();
                    System.out.println("ReplyAnswer:" + var);

                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(joke_school).contains(input)) {
                    var = "졸작을 얼른 끝내시길 바래요! 교수님 사랑해요! 미러리 파이팅!";
                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(timeNow).contains((input))) {

                    //현재시간 string 생성
                    long timeNowLong = System.currentTimeMillis();
                    Date date = new Date(timeNowLong);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH시 mm분");
                    String timeNowString = simpleDateFormat.format(date);

                    //난수생성
                    String randomString = "";
                    Random random = new Random();
                    int randomNum = random.nextInt(2);
                    if (randomNum == 0) {
                        randomString = "좋은 하루 되세요 ~ ";
                    } else if (randomNum == 1) {
                        randomString = "오늘도 좋은하루 ~";
                    } else {
                        randomString = "즐거운 하루 되세요!";
                    }

                    var = "현재시각은 " + timeNowString + "입니다. " + randomString;
                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (input.equals("종료")) {
                    optionNum = -1;
                    System.out.println("종료되어야한다.");
                } else {
                    txtSpeechInput.add("스마트미러 : 다시 한번만 말씀해주세요\n");
                    optionNum = 500;
                    speakWeb(tts, "다시 한번만 말씀해주세요\n");
                }
            } else {
                if (Arrays.asList(mirrorFunc).contains(input)) {
                    var = "거울조명, 선풍기, 문, 온도와 습도, 버스, 날씨, 일정 기능을 on/off 할 수 있습니다.";
                    optionNum = 0;
                    txtSpeechInput.add("[스마트미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);

                } else if (Arrays.asList(showInnerLight).contains(input)) {
                    var = "거울 조명을 켜드릴게요.";
                    optionNum = 111;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(hideInnerLight).contains(input)) {
                    var = "거울 조명을 꺼드릴게요.";
                    optionNum = 110;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(showMemo).contains(input)) {
                    var = "메모를 보여드릴게요";
                    optionNum = 121;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(hideMemo).contains(input)) {
                    var = "메모를 꺼드릴게요";
                    optionNum = 120;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(showCalendar).contains(input)) {
                    var = "일정을 보여드릴게요";
                    optionNum = 131;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(hideCalendar).contains(input)) {
                    var = "일정을 꺼드릴게요";
                    optionNum = 130;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(onOuterLight).contains(input)) {
                    txtSpeechInput.add("on");
                    myString = "on";
                    sendData(myString, mmOutputStream);

                    var = "외부 조명을 켜드릴게요.";
                    optionNum = 211;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(offOuterLight).contains(input)) {
                    txtSpeechInput.add("off");
                    myString = "off";
                    sendData(myString, mmOutputStream);

                    var = "외부 조명을 꺼드릴게요.";
                    optionNum = 210;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(onFan).contains(input)) {
                    var = "선풍기를 켜드릴게요.";
                    optionNum = 221;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(offFan).contains(input)) {
                    var = "선풍기를 꺼드릴게요.";
                    optionNum = 220;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(openDoor).contains(input)) {
                    var = "문을 열어드릴게요.";
                    optionNum = 311;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(closeDoor).contains(input)) {
                    var = "문을 닫아드릴게요.";
                    optionNum = 310;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(onTemp).contains(input)) {
                    var = "온도와 습도를 켜드릴게요.";
                    optionNum = 321;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(offTemp).contains(input)) {
                    var = "온도와 습도를 꺼드릴게요.";
                    optionNum = 320;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(showBus).contains(input)) {
                    var = "어떤 버스를 알려드릴까요?";
                    optionNum = 410;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(hideBus).contains(input)) {
                    var = "번 버스를 알려드릴게요.";
                    optionNum = 411;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(showWeather).contains(input)) {
                    var = "어디 날씨를 알려드릴까요?";
                    optionNum = 420;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(hideWeather).contains(input)) {
                    var = "날씨를 알려드릴게요.";
                    optionNum = 421;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                //500
                else if (Arrays.asList(joke).contains(input)) {
                    var = "노래 잘 못하는데.. 쿵쿵따리 쿵쿵따 쿵쿵쿵 따따";
                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (Arrays.asList(news).contains(input)) {
                    NewsSpeechTask newsSpeechTask = new NewsSpeechTask();

                    var = newsSpeechTask.execute().get();
                    System.out.println("ReplyAnswer:" + var);

                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    speakWeb(tts, var);
                } else if (Arrays.asList(joke_school).contains(input)) {
                    var = "졸작을 얼른 끝내시길 바래요 교수님 사랑해요 미러리 파이팅!";
                    optionNum = 500;
                    txtSpeechInput.add("[스마트 미러] " + var + " \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                } else if (input.equals("종료")) {
                    optionNum = -1;
                    System.out.println("종료되어야한다.");
                } else {
                    txtSpeechInput.add("스마트미러 : 다시 한번만 말씀해주세요\n");
                    optionNum = 500;
                    tts.speak("다시 한번만 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            //optionNum 웹으로 보내기
            //txtSpeechInput.add(Integer.toString(optionNum));
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
        }
        return var;
    }

    void speakWeb(TextToSpeech tts, String var) {
        System.out.println("speakWeb 진입");
        //tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
        SpeakAsyncTask speakAsyncTask = new SpeakAsyncTask(mContext);
        try {
            String successCheck = speakAsyncTask.execute(var).get();
            System.out.println("speakWebSuccessCheck: " + successCheck);
            setSendMsg(var);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    //아두이노로 데이터 보내는 메소드
    public void sendData(String myString, OutputStream outputStream) throws IOException {
        String msg = myString;
        mmOutputStream = outputStream;
        System.out.println("sendData OutputStream :" + mmOutputStream);
        if (msg.length() == 0) {
            return;
        }
        msg += "\n";
        Log.d(msg, msg);
        System.out.println("msg 값1: " + msg.getBytes());
        System.out.println("msg 값2: " + msg);
        mmOutputStream.write(msg.getBytes());
    }
}
