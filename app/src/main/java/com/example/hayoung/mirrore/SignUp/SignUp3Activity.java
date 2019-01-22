package com.example.hayoung.mirrore.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.Login.LoginActivity;
import com.example.hayoung.mirrore.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

public class SignUp3Activity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout weather_loc, subway_loc;
    private TextInputEditText subway_loc_edittext;
    private ImageView position_zero, position_one, position_two;

    private RelativeLayout search_subway_layout;
    private Button sign_up_btn;
    private UserInfo userInfo;

    private String subway_code;

    private TextView back_btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        TextView sign_up_text = (TextView) findViewById(R.id.sign_up_text);
        sign_up_text.setText("지역과 역정보를 입력하시면 \nMIRRORE:에서 해당 정보를 확인하실 수 있습니다.");

        /*Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");*/
        userInfo = new UserInfo(this);

        search_subway_layout = (RelativeLayout) findViewById(R.id.search_subway_layout);
        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        weather_loc = (TextInputLayout) findViewById(R.id.weather_loc);
        subway_loc = (TextInputLayout) findViewById(R.id.subway_loc);
        subway_loc_edittext = (TextInputEditText) findViewById(R.id.subway_loc_edittext);
        subway_loc_edittext.setCursorVisible(false);

        //지역 Spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_dropdown_item);
        MaterialBetterSpinner area_betterSpinner = (MaterialBetterSpinner) findViewById(R.id.weather_loc_spinner);
        area_betterSpinner.setAdapter(arrayAdapter);

        System.out.println("mirror_id_user_info : " + userInfo.getKeyMirrorId());
        System.out.println("id_user_info : " + userInfo.getKeyId());
        System.out.println("pw_user_info : " + userInfo.getKeyPw());
        System.out.println("name_user_info : " + userInfo.getKeyName());
        System.out.println("phone_user_info : " + userInfo.getKeyPhone());

        //Status Bar
        position_zero = (ImageView) findViewById(R.id.intro_indicator_0);
        position_one = (ImageView) findViewById(R.id.intro_indicator_1);
        position_two = (ImageView) findViewById(R.id.intro_indicator_2);
        position_zero.setBackgroundResource(R.drawable.indicator_selected);
        position_one.setBackgroundResource(R.drawable.indicator_selected);
        position_two.setBackgroundResource(R.drawable.indicator_selected);

        search_subway_layout.setOnClickListener(this);
        subway_loc_edittext.setOnClickListener(this);
        sign_up_btn.setOnClickListener(this);

        back_btn3 = (TextView) findViewById(R.id.back_btn3);

        back_btn3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_subway_layout:
                Intent intent_subway = new Intent(this, SearchSubwayActivity.class);
                startActivityForResult(intent_subway, 10);
                break;
            case R.id.sign_up_btn:
                String weather_loc_text = weather_loc.getEditText().getText().toString().trim();
                String subway_loc_text = subway_loc.getEditText().getText().toString().trim();
                userInfo.setKeyWeatherLoc(weather_loc_text);
                userInfo.setKeySubwayLoc(subway_loc_text);

                if (!validateArea(weather_loc_text) | !validateSubway(subway_loc_text)) {
                    return;
                } else {
                    try {
                        CustomMemberInsertTask customMemberInsertTask = new CustomMemberInsertTask(SignUp3Activity.this);
                        String resultStr = customMemberInsertTask.execute().get();
                        if (resultStr.equals("success")){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), resultStr, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.back_btn3:
                onBackPressed();
                break;
//            case R.id.subway_loc_edittext:
//                String weather_loc_text = weather_loc.getEditText().getText().toString().trim();
//                String subway_loc_text = subway_loc.getEditText().getText().toString().trim();
//                userInfo.setKeyWeatherLoc(weather_loc_text);
//                userInfo.setKeySubwayLoc(subway_loc_text);
//
//                if (!validateArea(weather_loc_text) | !validateSubway(subway_loc_text)) {
//                    return;
//                } else {
//                    try {
//                        CustomMemberInsertTask customMemberInsertTask = new CustomMemberInsertTask(SignUp3Activity.this);
//                        String resultStr = customMemberInsertTask.execute().get();
//                        if (resultStr.equals("success")){
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(getApplicationContext(), resultStr, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
            default:
                break;
        }
    }

    //검사하는 메소드
    private boolean validateArea(String weather_loc_text) {
        if (weather_loc_text.isEmpty()) {
            weather_loc.setError("지역을 선택해주세요.");
            return false;
        } else {
            weather_loc.setError(null);
            return true;
        }
    }

    //검사하는 메소드
    private boolean validateSubway(String subway_loc_text) {
        if (subway_loc_text.isEmpty()) {
            subway_loc.setError("지하철 이름을 선택해주세요.");
            return false;
        } else {
            subway_loc.setError(null);
            return true;
        }
    }

    //지하철 이름 검색하는 메소드_intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10&&resultCode==RESULT_OK){
            String train_name = data.getStringExtra("train_name");
            subway_loc.getEditText().setText(train_name);
            subway_code = data.getStringExtra("train_code");
        }
    }
}
