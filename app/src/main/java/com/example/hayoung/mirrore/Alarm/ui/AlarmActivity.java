package com.example.hayoung.mirrore.Alarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.hayoung.mirrore.Alarm.util.AlarmUtils;
import com.example.hayoung.mirrore.Main.CustomCallingWeb;
import com.example.hayoung.mirrore.Main.CustomSettingTask;
import com.example.hayoung.mirrore.R;
import com.example.hayoung.mirrore.Weather.WeatherActivity;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

import static org.litepal.LitePalApplication.getContext;

public class AlarmActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView add_alarm;
    AppCompatActivity activity;

    private com.zcw.togglebutton.ToggleButton alarm_togglebtn;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //activity
        activity = this;

        alarm_togglebtn = (com.zcw.togglebutton.ToggleButton) findViewById(R.id.alarm_togglebtn);
        userInfo = new UserInfo(this);
        add_alarm = (ImageView) findViewById(R.id.add_alarm);

        //ToolBar 추가하기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toggle
        if (userInfo.getKeyCalendar() == 0) {
            alarm_togglebtn.setToggleOff();
            userInfo.setKeyCalendar(0);
        } else {
            alarm_togglebtn.setToggleOn();
            userInfo.setKeyCalendar(1);
        }

        alarm_togglebtn.setOnToggleChanged(new com.zcw.togglebutton.ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(AlarmActivity.this);
                CustomCallingWeb customCallingWeb = new CustomCallingWeb(AlarmActivity.this);
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("calendar", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyCalendar(1);
                        alarm_togglebtn.setToggleOn();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("calendar", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyCalendar(0);
                        alarm_togglebtn.setToggleOff();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //알람 추가하기
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("add_alarm_AlarmActivity");
                AlarmUtils.checkAlarmPermissions(activity);
                final Intent i =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                getContext(), AddEditAlarmActivity.ADD_ALARM
                        );
                startActivity(i);
            }
        });


    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        System.out.println("optionMenu : AlarmActivity");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting_main_menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //backButton 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
