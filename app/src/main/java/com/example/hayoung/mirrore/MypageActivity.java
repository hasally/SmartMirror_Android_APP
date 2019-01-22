package com.example.hayoung.mirrore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hayoung.mirrore.MypageUpdateActivity;

import prefs.UserInfo;

public class MypageActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private TextView my_update_btn;
    private UserInfo userInfo;
    private TextView userId, userName, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //ToolBar 추가하기

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userInfo = new UserInfo(this);

        //회원가입시 입력했던 이름, 아이디, 휴대전화 정보
        userId = findViewById(R.id.userId);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);

        userId.setText(userInfo.getKeyId());
        userName.setText(userInfo.getKeyName());
        userPhone.setText(userInfo.getKeyPhone());

        my_update_btn = (TextView) findViewById(R.id.my_update_btn);
        my_update_btn.setOnClickListener((View.OnClickListener) this);
    }


    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_update_btn:
                Intent intent = new Intent(getApplicationContext(), MypageUpdateActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }
}