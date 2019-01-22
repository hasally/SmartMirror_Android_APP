package com.example.hayoung.mirrore;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hayoung.mirrore.BluetoothSocket.ConnectTask;
import com.example.hayoung.mirrore.BluetoothSocket.ReplyAnswer;
import com.example.hayoung.mirrore.Login.CustomLogoutTask;
import com.example.hayoung.mirrore.Login.LoginActivity;
import com.example.hayoung.mirrore.Main.Mirror_SettingFragment;
import com.example.hayoung.mirrore.Main.MyPagerAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;
import prefs.UserSession;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    //NFC
    Tag myTag;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private static String tagNum = null;
    private TextView tagDesc;

    //사용자 정보
    private UserSession userSession;
    private UserInfo userInfo;

    public static Context mContext;
    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        activity = this;

        userSession = new UserSession(this);
        userInfo = new UserInfo(this);

        //NFC
        tagDesc = (TextView) findViewById(R.id.tagDesc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity( MainActivity.this, 0, intent, 0);

        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Home"));
        tabs.addTab(tabs.newTab().setText("Mirror"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(myPagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        //ToolBar 추가하기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*ImageView action_button = (ImageView) findViewById(R.id.action_button);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("블루투스 연결 확인");
                DeviceDialog();
            }
        });*/

        //activity_mypage.xml 연결
        ImageView settings_icon = (ImageView) findViewById(R.id.settings_icon);
        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });


        //(임시) activity_sign_up1.xml 연결
        ImageView onoff_icon = (ImageView) findViewById(R.id.onoff_icon);
        onoff_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CustomLogoutTask customLogoutTask = new CustomLogoutTask(getApplicationContext());
                    String success_check = customLogoutTask.execute().get();
                    if (success_check.equals("success")) {
                        userSession.setLoggedin(false);
                        userInfo.clearUserInfo();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //NFC
    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(userSession.isUserLoggedin() == true){
            setIntent(intent);
            readFromIntent(intent);
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
                myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            }
        }

    }
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String textNfc = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            textNfc = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        if(textNfc.equals(userInfo.getKeyMirrorId())){
            NfcMirrorLoginTask nfcMirrorLoginTask = new NfcMirrorLoginTask(getApplicationContext());
            nfcMirrorLoginTask.execute();
            tagDesc.setText("NFC Content: " + textNfc);
        }
    }

    public  static  final String CHARS = "123456789ABCDEF";

    public  static final String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<data.length; ++i) {
            sb.append(CHARS.charAt((data[i]>>4) & 0x0F)).append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }


}
