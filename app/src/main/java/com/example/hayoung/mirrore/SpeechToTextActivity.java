package com.example.hayoung.mirrore;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class SpeechToTextActivity extends AppCompatActivity {

    private TextToSpeech tts;              // TTS 변수 선언

    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;

    public static Context mContext;
    public static AppCompatActivity activity;

    private android.support.v7.widget.Toolbar myToolbar;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_SETTING = 200;

    TextView myLabel, mRecv;
    static BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    int check = 1;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        mContext = this;
        activity = this;

        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        myLabel = (TextView) findViewById(R.id.label);
        mRecv = (TextView) findViewById(R.id.recv);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        // hide the action bar
        //getActionBar().hide();

        //1.블루투스 사용 가능한지 검사합니다.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            ErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            ErrorDialog("This device is disabled Bluetooth.");
            return;
        }

        //마이크 클릭시 실행된다.
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    /**
     * 음성 청취
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());  //잘 안되면
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 결과 처리
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //마이크 음성 청취 동작 후 텍스트를 화면에 출력
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


//                    if(result.get(0).equals("켜 줘")) {
//                        txtSpeechInput.setText("on");
//                        myString = txtSpeechInput.getText().toString();
//                    } else {
//                        txtSpeechInput.setText("off");
//                        myString = txtSpeechInput.getText().toString();
//                    }

                    txtSpeechInput.append("[나] "+result.get(0)+"\n");
                    replyAnswer(result.get(0), txtSpeechInput);


                }
                break;
            }
        }
    }


    public void replyAnswer(String input, TextView txtSpeechInput){

        //기능 배열 변수 선언부입니다..
           // 0기능 안내
            String mirrorFunc[] = {"기능", "기넝", "기눙", "긔눙", "긴응" ,"안녕", "안녕하세요","기능 알려 줘",
                    "기능 마래 줘", "기능 말해 줘", "기능 말해" ,"기능 말 해","기능 뭐야"," 기능 머야","기능 마래", "기능 뭐 있냐", "기능 뭐있냐", "기능 뭐있어", "기능 뭐 있어","기능 뭐뭐 있어","기능 뭐 뭐 있어","기능 뭐냐","기능 머냐","기능 모냐","기능 말해주","기능 말해 주"};

            /*내부처리되는 명령(show/hide)
            11내부조명*/
            String showInnerLight[] = {"거울조명 켜줘","거울 조명 켜줘","거울 조명 켜 줘", "거울 조명 켜","거울 조명 키자","거울조명 켜","거울조명 키자","거울조명 켜죠","거울조명 켜 죠","거울 조명 켜 죠","거울저명 켜줘","거울 저명 켜 줘","거울 저명 켜 줘" };
            String hideInnerLight[] = {"거울조명 꺼줘","거울 조명 꺼줘","거울 조명 꺼 줘", "거울 조명 꺼","거울 조명 끄자","거울조명 꺼","거울조명 끄자","거울조명 꺼죠","거울조명 꺼 죠","거울 조명 꺼 죠","거울저명 꺼줘","거울 저명 꺼 줘","거울 저명 꺼 줘" };

            //12메모
            String showMemo[] = {"메모 켜 줘","메모 켜","메모 켜줘","메모 켜죠","메모 켜 죠","메모 켜쥬","메모 켜 쥬","매모 켜줘","매모 켜 줘","매모 켜","매모 켜죠","매모 켜 죠"};
            String hideMemo[] = {"메모 꺼 줘","메모 꺼줘","메모 꺼","메모 꺼죠","메모 꺼 죠","메모 꺼쥬","메모 꺼 쥬","메 모 꺼 줘" };

            //13일정
            String showCalendar[] = {"일정 보여 줘","일정 보여줘","일 정 보 여 줘","일정 보여줘","일 정","일쩡 보여 줘","일쩡 보여줘","일정 보여죠","일정 보여쥬","일정 뭐야","일정 어떻게 돼","일정 알려줘","일정 알려 줘" };
            String hideCalendar[] = {"일정 꺼줘","일정 꺼져","일 정 꺼 줘","일정꺼줘","일정 꺼","일정 꺼죠","일정 그만","일정꺼조" };

            //end of 내부처리되는 명령(show/hide)

            //주변기기제어 관련 명령(on/off)

            //21 외부조명
            String onOuterLight[] = {"너무 어두워","불 커줘","불커줘","불 켜 줘","불 커 줘","불커 줘","불 커줘","조명 커줘","조명 커 줘","조명 켜 줘","조명 커조","조명 커저","조명 커져","조명 커종","불 켜줘","조명 켜줘","조명 켜 줘","조명 켜조","조명 켜저","조명 켜져","조명 켜종","외부조명 켜줘","외부조명 켜 줘","외부 조명 켜 줘","외부 조명 켜줘","외부 조명 켜줘","외부 조명 켜 줘","외부 조명 켜져","외부 조명 켜 져","외부 조명 켜","외부조명 켜"};
            String offOuterLight[] = {"너무 밝아","불 꺼줘","불꺼줘","불 꺼 줘","조명 꺼줘","조명 꺼 줘","조명 꺼조","조명 꺼저","조명 꺼져","조명 꺼종","조명 꺼저","조명 꺼 줘","조명 꺼조","조명 꺼저","조명 꺼져","조명 꺼종","외부조명 꺼줘","외부조명 꺼 줘","외부 조명 꺼 줘","외부 조명 꺼줘","외부 조명 꺼줘","외부 조명 꺼 줘","외부 조명 꺼져","외부조명 꺼 져","외부 조명 꺼","외부조명 꺼"};

            //22 선풍기
            String onFan[] = {"선풍기"," 성풍기", "성푼기", "선퐁기", "성풍긔", "선풍긔",
                    "선풍기 켜줘","선풍기 켜죠","선풍기 켜 줘", "선풍기 켜 죠","성풍기 켜줘","성풍기 켜죠","선풍기 켜","선풍기 켜주"};
            String offFan[] = {"선풍기 꺼","선풍기 꺼줘","선풍기 꺼 줘","선풍기 꺼죠","선풍기 꺼 죠","선풍기 꺼져","선풍기 끄자"};

            //end of 주변기기제어 관련 명령(on/off)

            //외부입력값 관련 명령(open,on/close,off)
            //31 자동문(cctv)
            String openDoor[] = {"문 열어줘","문 열 어 줘", "문 열어 줘","문 열어", "문 열 어", "문열 어","문 열려","문 열어봐","문 잠금해제","문 잠금 해제","문 열자","문 열어요"};
            String closeDoor[] = {"문 닫아줘","문 닫 아 줘","문 닫아 줘","문 닫아","문 닫어","문 닫아봐","문 닫어봐"," 문 잠가","문 잠궈","문 잠구어","문 잠가라","문 잠그자","문 닫자","문 닫아요","문 닫으라고"};

            //32온습도
            String onTemp[] = {"온도 알려 줘","온도 알려줘","온 도 알 려 줘","온 도 알려줘","온도","온도 알려죠","온도 알려 져","언도 알려줘","언도 알려 줘","습도 알려 줘","습도 알려줘","습 도 알 려 줘","습 도 알려줘","습도","습도 알려죠","습도 알려 져"};
            String offTemp[] = {"온도 꺼줘","온도 꺼","온 도 꺼 줘","온도 꺼죠","온도꺼","온도 그만","온도꺼죠","온도 꺼져","온도꺼져","습도 꺼 줘","습도 꺼줘","습 도 꺼 줘","습도 꺼죠","습도꺼죠","습도끄라고","습도 끄라고"};

            //end of 외부입력값 관련 명령(open,on/close,off)

            //질문 2회이상지속되는 명령
            //41버스
            String showBus[] = {"버스"," 뻐쓰", "뽀쓰"," 보스","뻐쑤",
                    "버스 어디야","버스 오디야","버스 어디 야","버스 오디 야","버스 언제 와",
                    "버스 시간 보여 줘","버스 시간보여줘","버스 시간","버스 시간 보여줘","버스 시간 버여져","버스 시간 버여 져","버스 시간 봐봐","버스 시간 봐 봐" };
            String hideBus[] = {};

            //42날씨
            String showWeather[] = {"날씨", "나씨","널씨", "날씨 알려줘","날씨 알려 줘","날씨 알려죠","날씨 알려 죠","날씨 아려줘","날씨 아려 줘","날씨 알려져","날씨 알려 져","날씨 알러져"};
            String hideWeather[] = {};

            // 뉴스
            String news[] = {"뉴스", "뉴스 읽어줘","뉴스 읽어 줘","뉴스읽어줘", "뉴스 읽어조","뉴스읽어조","뉴스 읽어","뉴스 말해줘", "뉴스 읽어봐", "뉴스 읽어바", "뉴스 들려줘", "뉴스 들려조"};

            // 재미
            String joke[] = {"노래", "비트박스", "노래 해조","노래 불러조","비트박스 해줘", "노래 해줘", "심심해", "노래 불러봐", "노래 불러 줘"};
            String joke_school[] = {"피곤해", "피곤", "힘들어","그만할래","도와줘", "죽을꺼같아", "죽겠어"};



        String myString;


        String var;//음성 출력 값 옵션 스프링
        int optionNum;
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(Arrays.asList(mirrorFunc).contains(input)){
                    var = "거울조명, 선풍기, 문, 온도와 습도, 버스, 날씨, 일정 기능을 on/off 할 수 있습니다.";
                    optionNum = 0;
                    txtSpeechInput.append("[스마트미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);

                }
                else if(Arrays.asList(showInnerLight).contains(input)){
                    var = "거울 조명을 켜드릴게요.";
                    optionNum = 111;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(hideInnerLight).contains(input)){
                    var = "거울 조명을 꺼드릴게요.";
                    optionNum = 110;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(showMemo).contains(input)){
                    var = "메모를 보여드릴게요";
                    optionNum = 121;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(hideMemo).contains(input)){
                    var = "메모를 꺼드릴게요";
                    optionNum = 120;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(showCalendar).contains(input)){
                    var = "일정을 보여드릴게요";
                    optionNum = 131;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null,null);
                }

                else if(Arrays.asList(hideCalendar).contains(input)){
                    var = "일정을 꺼드릴게요";
                    optionNum = 130;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(onOuterLight).contains(input)){
                    txtSpeechInput.setText("on");
                    myString = txtSpeechInput.getText().toString();
                    sendData(myString);

                    var = "외부 조명을 켜드릴게요.";
                    optionNum = 211;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(offOuterLight).contains(input)){
                    txtSpeechInput.setText("off");
                    myString = txtSpeechInput.getText().toString();
                    sendData(myString);

                    var = "외부 조명을 꺼드릴게요.";
                    optionNum = 210;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(onFan).contains(input)){
                    var = "선풍기를 켜드릴게요.";
                    optionNum = 221;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(offFan).contains(input)){
                    var = "선풍기를 꺼드릴게요.";
                    optionNum = 220;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(openDoor).contains(input)){
                    var = "문을 열어드릴게요.";
                    optionNum = 311;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(closeDoor).contains(input)){
                    var = "문을 닫아드릴게요.";
                    optionNum = 310;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(onTemp).contains(input)){
                    var = "온도와 습도를 켜드릴게요.";
                    optionNum = 321;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(offTemp).contains(input)){
                    var = "온도와 습도를 꺼드릴게요.";
                    optionNum = 320;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(showBus).contains(input)){
                    var = "어떤 버스를 알려드릴까요?";
                    optionNum = 410;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(hideBus).contains(input)){
                    var = "번 버스를 알려드릴게요.";
                    optionNum = 411;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(showWeather).contains(input)){
                    var = "어디 날씨를 알려드릴까요?";
                    optionNum = 420;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                else if(Arrays.asList(hideWeather).contains(input)){
                    var = "날씨를 알려드릴게요.";
                    optionNum = 421;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null, null);
                }
                else if(Arrays.asList(joke).contains(input)){
                    var = "노래 잘 못하는데.. 쿵쿵따리 쿵쿵따 쿵쿵쿵 따따";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null,null);
                }
                else if(Arrays.asList(news).contains(input)){
                    var = "오늘의 뉴스를 읽어드릴께요! 첫번째 기사입니다. '양심의 자유' 두텁게 보호…'병역거부' 유무죄 뒤바뀐 배경\n" +
                            "두번째 기사입니다. '포용국가' 첫걸음…\"국민 한 명도 차별 없는 나라로\"\n" +
                            "세번째 기사입니다. 유치원 무단 폐원도 가능? 아이들은?…3가지 궁금증";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null,null);
                }
                else if(Arrays.asList(joke_school).contains(input)){
                    var = "졸작을 얼른 끝내시길 바래요! 교수님 사랑해요! 미러리 파이팅!";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null,null);
                }
                else if(input.equals("종료")){
                    optionNum=-1;
                    finish();
                }
                else {
                    txtSpeechInput.append("스마트미러 : 다시 한번만 말씀해주세요\n");
                    optionNum=500;
                    tts.speak("다시 한번만 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            } else {
                if(Arrays.asList(mirrorFunc).contains(input)){
                    var = "거울조명, 선풍기, 문, 온도와 습도, 버스, 날씨, 일정 기능을 on/off 할 수 있습니다.";
                    optionNum = 0;
                    txtSpeechInput.append("[스마트미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);

                }
                else if(Arrays.asList(showInnerLight).contains(input)){
                    var = "거울 조명을 켜드릴게요.";
                    optionNum = 111;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(hideInnerLight).contains(input)){
                    var = "거울 조명을 꺼드릴게요.";
                    optionNum = 110;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(showMemo).contains(input)){
                    var = "메모를 보여드릴게요";
                    optionNum = 121;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(hideMemo).contains(input)){
                    var = "메모를 꺼드릴게요";
                    optionNum = 120;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(showCalendar).contains(input)){
                    var = "일정을 보여드릴게요";
                    optionNum = 131;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(hideCalendar).contains(input)){
                    var = "일정을 꺼드릴게요";
                    optionNum = 130;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(onOuterLight).contains(input)){
                    txtSpeechInput.setText("on");
                    myString = txtSpeechInput.getText().toString();
                    sendData(myString);

                    var = "외부 조명을 켜드릴게요.";
                    optionNum = 211;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(offOuterLight).contains(input)){
                    txtSpeechInput.setText("off");
                    myString = txtSpeechInput.getText().toString();
                    sendData(myString);

                    var = "외부 조명을 꺼드릴게요.";
                    optionNum = 210;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(onFan).contains(input)){
                    var = "선풍기를 켜드릴게요.";
                    optionNum = 221;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(offFan).contains(input)){
                    var = "선풍기를 꺼드릴게요.";
                    optionNum = 220;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(openDoor).contains(input)){
                    var = "문을 열어드릴게요.";
                    optionNum = 311;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(closeDoor).contains(input)){
                    var = "문을 닫아드릴게요.";
                    optionNum = 310;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(onTemp).contains(input)){
                    var = "온도와 습도를 켜드릴게요.";
                    optionNum = 321;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(offTemp).contains(input)){
                    var = "온도와 습도를 꺼드릴게요.";
                    optionNum = 320;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(showBus).contains(input)){
                    var = "어떤 버스를 알려드릴까요?";
                    optionNum = 410;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(hideBus).contains(input)){
                    var = "번 버스를 알려드릴게요.";
                    optionNum = 411;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(showWeather).contains(input)){
                    var = "어디 날씨를 알려드릴까요?";
                    optionNum = 420;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                else if(Arrays.asList(hideWeather).contains(input)){
                    var = "날씨를 알려드릴게요.";
                    optionNum = 421;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }

                //500
                else if(Arrays.asList(joke).contains(input)){
                    var = "노래 잘 못하는데.. 쿵쿵따리 쿵쿵따 쿵쿵쿵 따따";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }
                else if(Arrays.asList(news).contains(input)){
                    var = "오늘의 뉴스를 읽어드릴께요! 첫번째 기사입니다. '양심의 자유' 두텁게 보호…'병역거부' 유무죄 뒤바뀐 배경\n" +
                            "두번째 기사입니다. '포용국가' 첫걸음…\"국민 한 명도 차별 없는 나라로\"\n" +
                            "세번째 기사입니다. 유치원 무단 폐원도 가능? 아이들은?…3가지 궁금증";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null,null);
                }
                else if(Arrays.asList(joke_school).contains(input)){
                    var = "졸작을 얼른 끝내시길 바래요 교수님 사랑해요 미러리 파이팅!";
                    optionNum = 500;
                    txtSpeechInput.append("[스마트 미러] "+var+" \n");
                    tts.speak(var, TextToSpeech.QUEUE_FLUSH, null);
                }
                else if(input.equals("종료")){
                    optionNum=-1;
                    finish();
                }
                else {
                    txtSpeechInput.append("스마트미러 : 다시 한번만 말씀해주세요\n");
                    optionNum=500;
                    tts.speak("다시 한번만 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null);
                }
            }




            //optionNum 웹으로 보내기
            txtSpeechInput.append(Integer.toString(optionNum));
        }
        catch (Exception e) {
            toast(e.toString());
        }
    }





    //상단 설정메뉴 아이콘 클릭시
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.speech_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_button) {
            //2. 페어링 되어 있는 블루투스 장치들의 목록을 보여줍니다.
            //3. 목록에서 블루투스 장치를 선택하면 선택한 디바이스를 인자로 하여 doConnect 함수가 호출됩니다.
            DeviceDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static public Set<BluetoothDevice> getPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    //13. 백버튼이 눌러지거나, ConnectTask에서 예외발생시
    //데이터 수신을 위한 스레드를 종료시키고 CloseTask를 실행하여 입출력 스트림을 닫고,
    //소켓을 닫아 통신을 종료합니다.
    public void doClose() {
        workerThread.interrupt();
        new CloseTask().execute();
    }


    public void doConnect(BluetoothDevice device) {
        mmDevice = device;

        //Standard SerialPortService ID
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 4. 지정한 블루투스 장치에 대한 특정 UUID 서비스를 하기 위한 소켓을 생성합니다.
            // 여기선 시리얼 통신을 위한 UUID를 지정하고 있습니다.
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            // 5. 블루투스 장치 검색을 중단합니다.
            mBluetoothAdapter.cancelDiscovery();
            System.out.println("블루투스 장치 검색: 성공");

            // 6. ConnectTask를 시작합니다.
            new ConnectTask().execute();

        } catch (IOException e) {
            Log.e("", e.toString(), e);
            ErrorDialog("doConnect " + e.toString());
        }
    }

    public void btn_room_onClick(View view) {
        String myString = "";

        if (check == 1) {
            txtSpeechInput.setText("on");
            myString = txtSpeechInput.getText().toString();
            check = 0;
        } else {
            txtSpeechInput.setText("off");
            myString = txtSpeechInput.getText().toString();
            check = 1;
        }

        try {
            sendData(myString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_mirror_onClick(View view) {
    }

    public void btn_fan_onClick(View view) {
    }

    public void btn_key_onClick(View view) {
    }

    private class ConnectTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                System.out.println("ConnectTask클래스 진입");
                //7. 블루투스 장치로 연결을 시도합니다.
                mmSocket.connect();
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

                beginListenForData();


            } catch (Throwable t) {
                Log.e("", "connect? " + t.getMessage());
                doClose();
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            //10. 블루투스 통신이 연결되었음을 화면에 출력합니다.
            myLabel.setText("Bluetooth Opened");
            if (result instanceof Throwable) {
                Log.d("", "ConnectTask " + result.toString());
                ErrorDialog("ConnectTask " + result.toString());

            }
        }
    }    //ConnectTask End

    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try {
                    mmOutputStream.close();
                } catch (Throwable t) {/*ignore*/}
                try {
                    mmInputStream.close();
                } catch (Throwable t) {/*ignore*/}
                mmSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e("", result.toString(), (Throwable) result);
                ErrorDialog(result.toString());
            }
        }
    }    //CloseTask End

    //dialog 호출
    public void DeviceDialog() {
        if (activity.isFinishing()) return;

        FragmentManager fm = SpeechToTextActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(DEVICES_DIALOG, "");    //dialog 만드는 class호출
        alertDialog.show(fm, "");
    }


    public void ErrorDialog(String text) {
        if (activity.isFinishing()) return;

        FragmentManager fm = SpeechToTextActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(ERROR_DIALOG, text);
        alertDialog.show(fm, "");
    }

    //음성 -> Text 보내기 메소드
    public void beginListenForData() {
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
                                            mRecv.setText(data);
                                            System.out.println("mRecv 값: " + mRecv);
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

    //아두이노로 데이터 보내는 메소드
    void sendData(String myString) throws IOException {
        String msg = myString;
        if (msg.length() == 0) {
            return;
        }
        msg += "\n";
        Log.d(msg, msg);
        System.out.println("msg 값1: " + msg.getBytes());
        System.out.println("msg 값2: " + msg);
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
