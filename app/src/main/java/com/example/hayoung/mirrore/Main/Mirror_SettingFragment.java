package com.example.hayoung.mirrore.Main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.BluetoothSocket.ConnectTask;
import com.example.hayoung.mirrore.BluetoothSocket.ReplyAnswer;
import com.example.hayoung.mirrore.ChatMessage;
import com.example.hayoung.mirrore.ChatMessageAdapter;
import com.example.hayoung.mirrore.MainActivity;
import com.example.hayoung.mirrore.MyDialogFragment;
import com.example.hayoung.mirrore.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


public class Mirror_SettingFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private ChatMessageAdapter mAdapter;

    private Button light_btn;

    private TextToSpeech tts;              // TTS 변수 선언
    public static Context mContext;
    public static AppCompatActivity activity;
    public String outputStream;
    public ArrayList<String> listItems = null;
    MainActivity mainActivity;

    //블루투스
    int check = 1;
    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;
    static BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    public static OutputStream mmOutputStream = null;

    //mainActivity
    View view;

    private android.support.v7.widget.Toolbar myToolbar;


    private ImageView btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    TextView myLabel, mRecv;


    public Mirror_SettingFragment() {
        //Required empty public constructor
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = this.getContext();
        activity = (AppCompatActivity) getActivity();

        view = inflater.inflate(R.layout.fragment_mirror_setting, container, false);

        //블루투스 연결 확인
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            ErrorDialog("This device is not implement Bluetooth.");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            ErrorDialog("This device is disabled Bluetooth.");
        }

        /*LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main_view = layoutInflater.inflate(R.layout.activity_main, null);*/
        mainActivity = (MainActivity)getActivity();

        ImageView action_button = (ImageView) mainActivity.findViewById(R.id.action_button);
        listItems = new ArrayList<String>();
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("블루투스 연결 확인");
                DeviceDialog();
            }
        });
        btnSpeak = (ImageView) view.findViewById(R.id.btnSpeak);
        light_btn = (Button) view.findViewById(R.id.light_btn);

        myLabel = (TextView) view.findViewById(R.id.label);
        mRecv = (TextView) view.findViewById(R.id.recv);

        tts = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new ChatMessageAdapter(mContext, new ArrayList<ChatMessage>());
        mRecyclerView.setAdapter(mAdapter);

        //조명
        light_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplyAnswer replyAnswer = new ReplyAnswer(getContext());
                try {
                    if(check == 1) {
                        replyAnswer.sendData("on", mmOutputStream);
                        check = 0;
                    } else {
                        replyAnswer.sendData("off", mmOutputStream);
                        check = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //마이크 클릭시 실행된다.
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        //Inflate the layout for this fragment
        return view;
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
            Toast.makeText(this.getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 결과 처리
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //마이크 음성 청취 동작 후 텍스트를 화면에 출력
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    listItems.add("[나] "+result.get(0)+"\n");

                    String message = result.get(0);
                    if (TextUtils.isEmpty(message)) {
                        return;
                    }
                    sendMessage(message,true);

                    ReplyAnswer replyAnswer = new ReplyAnswer(getContext());
//                    replyAnswer.replyAnswer(result.get(0), listItems, tts);


                    // give adapter to ListView UI element to render

                    mimicOtherMessage(replyAnswer.replyAnswer(result.get(0), listItems, tts, mmOutputStream));

                }
                break;
            }
        }
    }

    // chat
    private void sendMessage(String message, Boolean isMine) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    //dialog 호출
    public void DeviceDialog() {
        if (activity.isFinishing()) return;

        FragmentManager fm = this.getChildFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(DEVICES_DIALOG, "");    //dialog 만드는 class호출
        alertDialog.show(fm, "");
    }


    public void ErrorDialog(String text) {
        if (activity.isFinishing()) return;

        FragmentManager fm = this.getChildFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(ERROR_DIALOG, text);
        alertDialog.show(fm, "");
    }

    static public Set<BluetoothDevice> getPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    public void doConnect(BluetoothDevice device) {
        mmDevice = device;

        //Standard SerialPortService ID 00001101-0000-1000-8000-00805F9B34FB
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        try {
            // 4. 지정한 블루투스 장치에 대한 특정 UUID 서비스를 하기 위한 소켓을 생성합니다.
            // 여기선 시리얼 통신을 위한 UUID를 지정하고 있습니다.
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            // 5. 블루투스 장치 검색을 중단합니다.
            mBluetoothAdapter.cancelDiscovery();
            System.out.println("블루투스 장치 검색: 성공");

            // 6. ConnectTask를 시작합니다.
            String connectCheck = new ConnectTask(mainActivity,getActivity(), mmSocket, mmDevice).execute().get().toString();
            System.out.println("connect 확인: " + connectCheck);
            mmOutputStream = mmSocket.getOutputStream();

        } catch (IOException e) {
            Log.e("", e.toString(), e);
            ErrorDialog("doConnect " + e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
