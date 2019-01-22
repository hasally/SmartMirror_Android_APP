package com.example.hayoung.mirrore.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.MainActivity;
import com.example.hayoung.mirrore.R;
import com.example.hayoung.mirrore.SignUp.SignUp1Activity;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;
import prefs.UserSession;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    private UserSession userSession;
    private UserInfo userInfo;

    //private EditText id, pw;
    private Button login_btn;
    private TextView open_sign_up, open_find_id_pw;

    private TextInputLayout id, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);

        id = (TextInputLayout) findViewById(R.id.id);
        pw = (TextInputLayout) findViewById(R.id.pw);

        login_btn = (Button) findViewById(R.id.login_btn);
        open_sign_up = (TextView) findViewById(R.id.open_sign_up);
        open_find_id_pw = (TextView) findViewById(R.id.open_find_id_pw);

        if (userSession.isUserLoggedin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        login_btn.setOnClickListener(this);
        open_sign_up.setOnClickListener(this);
        open_find_id_pw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.open_sign_up:
                intent = new Intent(getApplicationContext(), SignUp1Activity.class);
                startActivity(intent);
                break;
            case R.id.open_find_id_pw:
                Toast.makeText(getApplicationContext(), "아이디/비밀번호찾기 버튼 클릭됨", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_btn:
                String id_text = id.getEditText().getText().toString().trim();
                String pw_text = pw.getEditText().getText().toString().trim();

                System.out.println("id :"+id_text);
                System.out.println("pw :"+pw_text);
                if (!validateId(id_text) | !validatePassword(pw_text)) {
                    return;
                } else {
                    try {
                        CustomLoginTask loginTask = new CustomLoginTask(LoginActivity.this);
                        String checkSuccess = loginTask.execute(id_text, pw_text).get();
                        System.out.println("checkSuccess : "+checkSuccess);
                        if (checkSuccess != null &&checkSuccess.equals("success")) {
                            userSession.setLoggedin(true);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, checkSuccess, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
}
