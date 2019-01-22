package com.example.hayoung.mirrore.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.R;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;


public class SignUp1Activity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout mirror_id;
    private Button sign_up_btn;
    private ImageView position_zero;

    private UserInfo userInfo;

    private TextView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        TextView sign_up_text = (TextView) findViewById(R.id.sign_up_text);
        sign_up_text.setText("사용자의 스마트 미러 고유번호를 입력해주세요.");

        userInfo = new UserInfo(this);

        mirror_id = (TextInputLayout) findViewById(R.id.mirror_id);
        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);

        //Status Bar
        position_zero = (ImageView) findViewById(R.id.intro_indicator_0);
        position_zero.setBackgroundResource(R.drawable.indicator_selected);

        sign_up_btn.setOnClickListener(this);

        back_btn = (TextView) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                String mirror_id_text = mirror_id.getEditText().getText().toString().trim();
                System.out.println("mirror_id_user_input : " + mirror_id_text);

                if (!validateMirrorId(mirror_id_text)) {
                    return;
                } else {
                    try {
                        CustomMirrorIdTask mirrorIdTask = new CustomMirrorIdTask(SignUp1Activity.this);
                        String checkSuccess = mirrorIdTask.execute(mirror_id_text).get();
                        System.out.println("checkSuccess : " + checkSuccess);
                        System.out.println("mirror_id_user_info1 : " + userInfo.getKeyMirrorId());
                        if (checkSuccess != null && checkSuccess.equals("success")) {
                            Intent intent = new Intent(getApplicationContext(), SignUp2Activity.class);
                            //intent.putExtra("userInfo", userInfo);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUp1Activity.this, checkSuccess, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    //검사하는 메소드
    private boolean validateMirrorId(String mirror_id_text) {
        if (mirror_id_text.isEmpty()) {
            mirror_id.setError("고유번호를 입력해주세요.");
            return false;
        } else {
            mirror_id.setError(null);
            return true;
        }
    }
}
