package com.example.hayoung.mirrore.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hayoung.mirrore.R;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;


public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout id, pw, name, phone;
    private ImageView position_zero, position_one;
    private Button sign_up_btn;

    private UserInfo userInfo;

    private TextView back_btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        TextView sign_up_text = (TextView) findViewById(R.id.sign_up_text);

        sign_up_text.setText("사용자의 기본정보를 입력해주세요.");

        //Intent intent = getIntent();
        //userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        userInfo = new UserInfo(this);

        System.out.println("mirror_id_user_info2 : " + userInfo.getKeyMirrorId());
        /*System.out.println("id_user_info : " + userInfo.getKeyMirrorId());
        System.out.println("pw_user_info : " + userInfo.getKeyId());
        System.out.println("name_user_info : " + userInfo.getKeyName());
        System.out.println("phone_user_info : " + userInfo.getKeyPhone());*/

        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);

        id = (TextInputLayout) findViewById(R.id.id);
        pw = (TextInputLayout) findViewById(R.id.pw);
        name = (TextInputLayout) findViewById(R.id.name);
        phone = (TextInputLayout) findViewById(R.id.phone);

        //Status Bar
        position_zero = (ImageView) findViewById(R.id.intro_indicator_0);
        position_one = (ImageView) findViewById(R.id.intro_indicator_1);
        position_zero.setBackgroundResource(R.drawable.indicator_selected);
        position_one.setBackgroundResource(R.drawable.indicator_selected);

        sign_up_btn.setOnClickListener(this);

        back_btn2 = (TextView) findViewById(R.id.back_btn2);

        back_btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                String id_text      = id.getEditText().getText().toString().trim();
                String pw_text      = pw.getEditText().getText().toString().trim();
                String name_text    = name.getEditText().getText().toString().trim();
                String phone_text   = phone.getEditText().getText().toString().trim();

                if (!validateId(id_text) | !validatePassword(pw_text) | !validateName(name_text) | !validatePhone(phone_text)) {
                    return;
                } else {
                    System.out.println("mirror_id_user_info_sign_up_btn_sign_up2 : " + userInfo.getKeyMirrorId());
                    try {
                        CustomIdOverlapCheckTask idOverlapCheckTask = new CustomIdOverlapCheckTask(SignUp2Activity.this);
                        String checkSuccess = idOverlapCheckTask.execute(id_text).get();
                        System.out.println("checkSuccess : " + checkSuccess);
                        if(checkSuccess != null && checkSuccess.equals("exists")){
                            id.setError("아이디가 중복입니다.");
                            return;
                        } else {
                            userInfo.setKeyId(id_text);
                            userInfo.setKeyPw(pw_text);
                            userInfo.setKeyName(name_text);
                            userInfo.setKeyPhone(phone_text);
                            Intent intent = new Intent(getApplicationContext(), SignUp3Activity.class);
                            //intent.putExtra("userInfo", userInfo);
                            startActivity(intent);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.back_btn2:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    //검사하는 메소드
    private boolean validateId(String id_text) {
        if (id_text.isEmpty()) {
            id.setError("아이디를 입력해주세요.");
            return false;
        } else if (!id_text.contains("@")) {
            id.setError("이메일 형식으로 입력해주세요.");
            return false;
        } else {
            id.setError(null);
            return true;
        }
    }

    //검사하는 메소드
    private boolean validatePassword(String pw_text) {
        if (pw_text.isEmpty()) {
            pw.setError("비밀번호를 입력해주세요.");
            return false;
        } else {
            pw.setError(null);
            return true;
        }
    }

    //검사하는 메소드
    private boolean validateName(String name_text) {
        if (name_text.isEmpty()) {
            name.setError("이름을 입력해주세요.");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    //검사하는 메소드
    private boolean validatePhone(String phone_text) {
        if (phone_text.isEmpty()) {
            phone.setError("핸드폰 번호을 입력해주세요.");
            return false;
        } else if (phone_text.contains("-")) {
            phone.setError("'-'을 제외하고 입력해주세요.");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    //카메라와 갤러리 선택할 수 있는 화면 보이게 하는 메소드
    public void selectImage() {

    }
}
