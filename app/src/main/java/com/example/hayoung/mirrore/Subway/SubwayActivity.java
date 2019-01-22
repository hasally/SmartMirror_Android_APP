package com.example.hayoung.mirrore.Subway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hayoung.mirrore.Main.CustomMemberTask;
import com.example.hayoung.mirrore.R;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

import static android.text.TextUtils.substring;


public class SubwayActivity extends AppCompatActivity {
    private String receiveMsg;
    private Context mContext;
    Toolbar toolbar;
    ToggleButton subway_togglebtn;
    private EditText editSearch;
    private String now_sub_code_string, first_pre_code_string, first_next_code_string;

    private String day;

    private UserInfo userInfo;
    private TextView trainview, previous, next;
    private ImageView arrow_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);

        //ToolBar 추가하기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        subway_togglebtn = (ToggleButton) findViewById(R.id.subway_togglebtn);
        userInfo = new UserInfo(this);

        arrow_back = (ImageView) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        StrictMode.enableDefaults();

        //회원가입시 입력했던 지하철역 정보
        trainview = findViewById(R.id.trainview);
        trainview.setText(userInfo.getKeySubwayLoc());
        ///////////////////////////////////////////////////////
        // 회원가입 시 선택했던 지하철역의 전역, 다음역구하기
        //전역
        try {
            int first_pre_int = Integer.parseInt(userInfo.getKeySubwayLoc());
            int first_pre_code = first_pre_int - 1;
            first_pre_code_string = Integer.toString(first_pre_code);

            if (first_pre_code_string.length() != 4) {
                first_pre_code_string = "0" + first_pre_code_string;

            }
            TextView previous = (TextView) findViewById(R.id.previous);
            previous.setText(first_pre_code_string);
            //System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+previous);
        } catch (Exception e) {

        }
        //다음역
        try {

            int first_next_int = Integer.parseInt(userInfo.getKeySubwayLoc());
            int first_next_code = first_next_int + 1;
            first_next_code_string = Integer.toString(first_next_code);

            if (first_next_code_string.length() != 4) {
                first_next_code_string = "0" + first_next_code_string;
            }
            TextView next = (TextView) findViewById(R.id.next);
            next.setText(first_next_code_string);
            //System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+next);
        } catch (Exception e) {

        }
        //////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.trainview);
        TextView pre_tv = (TextView) findViewById(R.id.previous);
        TextView next_tv = (TextView) findViewById(R.id.next);


        String file = "train_js.json";
        String result = "";
        try { //assets 폴더에 있는 json 파일을 읽어 문자열 객체 result에 저장함
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "utf-8");
            //읽어들인 JSON 포맷의 데이터를 JSON 객체를 변환
            JSONObject json = new JSONObject(result);
            //customers에 해당하는 배열을 할당
            JSONArray jArr = json.getJSONArray("DATA");
            //배열의 크기만큼 반복하면서 name과 address의 값을 추출함
            for (int i = 0; i < jArr.length(); i++) {
                //i번째 JSON 객체 할당
                json = jArr.getJSONObject(i);
                //line_num 해당하는 데이터를 추출함
                String line_num = json.getString("line_num");
                //station_nm에 해당하는 데이터를 추출함
                String station_nm = json.getString("station_nm");
                //station_cd에 해당하는 데이터를 추출함
                String station_cd = json.getString("station_cd");
//                tv.append(line_num + "\n");
//                tv.append(station_nm + "\n");
//                tv.append(station_cd + "\n");

                if (station_cd.equals(userInfo.getKeySubwayLoc())) {
                    trainview.setText(line_num + " " + station_nm);

                    if (line_num.equals("1호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line1);
                    } else if (line_num.equals("2호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line2);
                    } else if (line_num.equals("3호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line3);
                    } else if (line_num.equals("4호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line4);
                    } else if (line_num.equals("5호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line5);
                    } else if (line_num.equals("6호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line6);
                    } else if (line_num.equals("7호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line7);
                    } else if (line_num.equals("8호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line8);
                    } else if (line_num.equals("9호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line9);
                    } else if (line_num.equals("분당선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linebun);
                    } else if (line_num.equals("경춘선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linegyeongchun);
                    } else if (line_num.equals("수인선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linesu);
                    } else if (line_num.equals("경강선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linegyeonggang);
                    } else if (line_num.equals("경의중앙선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linegyeongui);
                    } else if (line_num.equals("에버라인")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.lineever);
                    } else if (line_num.equals("인천1호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linein1);
                    } else if (line_num.equals("인천2호선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linein2);
                    } else if (line_num.equals("신분당선")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linesin);
                    } else if (line_num.equals("의정부경전철")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.lineui);
                    } else if (line_num.equals("우이신설")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.lineuisin);
                    } else if (line_num.equals("공항철도")) {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.linegonghang);
                    } else {
                        ImageView img = (ImageView) findViewById(R.id.line_img);
                        img.setImageResource(R.drawable.line2);
                    }

                } else if (station_cd.equals(first_pre_code_string)) {
                    System.out.print("previous : " + pre_tv + "\n 역명 " + station_nm + "\n");
                    pre_tv.setText(station_nm);
                } else if (station_cd.equals(first_next_code_string)) {
                    System.out.print("next : " + next_tv + "\n 역명 : " + station_nm + "\n");
                    next_tv.setText(station_nm);
                }
            }
        } catch (Exception e) {
            tv.setText(e.getMessage());
            System.out.print(e.getMessage() + "\n");
        }


        // 회원가입시 선택했던 역 정보 파싱
        //url에 사용할 현재 요일 값 구하기
        day = setDayNumber();

        TextView status1 = (TextView) findViewById(R.id.pre_list); //파싱된 결과확인!
        TextView status2 = (TextView) findViewById(R.id.next_list); //파싱된 결과확인!

        //상행선 지하철 정보
        try {
            SubwayGetXmlTask subwayGetXmlTask = new SubwayGetXmlTask(this);
            String upSubwayResult = subwayGetXmlTask.execute(userInfo.getKeySubwayLoc(), "1", day).get();
            System.out.println("upSubwayResult!! : " + upSubwayResult);
            status1.setText(upSubwayResult);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //하행선 지하철 정보
        try {
            SubwayGetXmlTask subwayGetXmlTask = new SubwayGetXmlTask(this);
            String downSubwayResult = subwayGetXmlTask.execute(userInfo.getKeySubwayLoc(), "2", day).get();
            System.out.println("downSubwayResult!! : " + downSubwayResult);
            status2.setText(downSubwayResult);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////////////////////////////////////////////////
        //toggle
        if (userInfo.getKeySubway() == 0) {
            subway_togglebtn.setToggleOff();
            userInfo.setKeySubway(0);
        } else {
            subway_togglebtn.setToggleOn();
            userInfo.setKeySubway(1);
        }
        subway_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on == true) {
                    userInfo.setKeySubway(1);
                } else {
                    userInfo.setKeySubway(0);
                }
            }
        });

        editSearch = (EditText) findViewById(R.id.editSearch);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.subway_custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSubway();
            }
        });

    }


    public void searchSubway() {
        Intent intent = new Intent(getApplicationContext(), SubwaySearch.class);
        startActivityForResult(intent, 0);
    }

    //su->now_sub
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView tv_subway = (TextView) findViewById(R.id.trainview);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            String now_sub = data.getStringExtra("train_name");
            String pre_sub = data.getStringExtra("pre_train_name");
            String next_sub = data.getStringExtra("next_train_name");

            //지하철역 색 변경
            tv_subway.setText(substring(now_sub, 0, now_sub.indexOf("(")));
            //editSearch.setText(su);
            now_sub = now_sub.substring(0, now_sub.indexOf(" "));
            //Toast.makeText(this,su, Toast.LENGTH_SHORT).show();
            changeSubwayColor(now_sub);
            userInfo.setKeySubwayLoc(tv_subway.getText().toString());

            //전철역 코드만 뽑기
            String now_sub_code = data.getStringExtra("train_name");
            now_sub_code = substring(now_sub_code, now_sub_code.indexOf("(") + 1, now_sub_code.indexOf(")"));
            // Toast.makeText(this,su2, Toast.LENGTH_SHORT).show();
            now_sub_code_string = now_sub_code;
            int now_sub_code_int = Integer.parseInt(now_sub_code);

            //이전역 코드 구하기
            int pre_sub_code_int = now_sub_code_int - 1;
            String pre_sub_code_string = Integer.toString(pre_sub_code_int);
            TextView previous = (TextView) findViewById(R.id.previous);
            previous.setText(substring(pre_sub, pre_sub.indexOf("선") + 1, pre_sub.indexOf("(")));
            userInfo.setKeySubwayLoc(previous.getText().toString());


            //다음역 코드 구하기
            int next_sub_code_int = now_sub_code_int + 1;
            String next_sub_code_string = Integer.toString(next_sub_code_int);
            TextView next = (TextView) findViewById(R.id.next);
            next.setText(substring(next_sub, next_sub.indexOf("선") + 1, next_sub.indexOf("(")));
            userInfo.setKeySubwayLoc(next.getText().toString());

            System.out.println("지하철역 정보: " + now_sub + ", 전철역코드:" + now_sub_code + "//" + now_sub_code_string);

            //url에 사용할 현재 요일 값 구하기
            day = setDayNumber();

            TextView status1 = (TextView) findViewById(R.id.pre_list); //파싱된 결과확인!
            TextView status2 = (TextView) findViewById(R.id.next_list); //파싱된 결과확인!

            //상행선 지하철 정보
            try {
                SubwayGetXmlTask subwayGetXmlTask = new SubwayGetXmlTask(this);
                String upSubwayResult = subwayGetXmlTask.execute(now_sub_code_string, "1", day).get();
                System.out.println("upSubwayResult : " + upSubwayResult);
                status1.setText(upSubwayResult);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //하행선 지하철 정보
            try {
                SubwayGetXmlTask subwayGetXmlTask = new SubwayGetXmlTask(this);
                String downSubwayResult = subwayGetXmlTask.execute(now_sub_code_string, "2", day).get();
                System.out.println("downSubwayResult : " + downSubwayResult);
                status2.setText(downSubwayResult);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*status1.setText(""); //리셋해서 다시 찍을 준비
            status2.setText(""); //리셋해서 다시 찍을 준비*/

            //subway데이터 update
            try {
                CustomMemberTask customMemberTask = new CustomMemberTask(SubwayActivity.this);
                String resultStr = customMemberTask.execute("subway_loc", "", now_sub_code_string, userInfo.getKeyId().toString()).get();
                System.out.println("update 성공 여부:" + resultStr);
                if (resultStr.equals("success")) {
                    userInfo.setKeySubwayLoc(now_sub_code_string);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //호선마다 지하철 색 변경
    public void changeSubwayColor(String subway_number) {
        if (subway_number.equals("1호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line1);
        } else if (subway_number.equals("2호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line2);
        } else if (subway_number.equals("3호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line3);
        } else if (subway_number.equals("4호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line4);
        } else if (subway_number.equals("5호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line5);
        } else if (subway_number.equals("6호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line6);
        } else if (subway_number.equals("7호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line7);
        } else if (subway_number.equals("8호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line8);
        } else if (subway_number.equals("9호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line9);
        } else if (subway_number.equals("분당선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linebun);
        } else if (subway_number.equals("경춘선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linegyeongchun);
        } else if (subway_number.equals("수인선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linesu);
        } else if (subway_number.equals("경강선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linegyeonggang);
        } else if (subway_number.equals("경의중앙선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linegyeongui);
        } else if (subway_number.equals("에버라인")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.lineever);
        } else if (subway_number.equals("인천1호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linein1);
        } else if (subway_number.equals("인천2호선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linein2);
        } else if (subway_number.equals("신분당선")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linesin);
        } else if (subway_number.equals("의정부경전철")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.lineui);
        } else if (subway_number.equals("우이신설")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.lineuisin);
        } else if (subway_number.equals("공항철도")) {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.linegonghang);
        } else {
            ImageView img = (ImageView) findViewById(R.id.line_img);
            img.setImageResource(R.drawable.line2);
        }
    }

    public String setDayNumber() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        System.out.println("day_of_week1: " + day_of_week);

        // 1:평일 2:토요일 3:공휴일
        if (day_of_week == 1) {
            day = "3";
        } else if (day_of_week == 2) {
            day = "1";
        } else if (day_of_week == 3) {
            day = "1";
        } else if (day_of_week == 4) {
            day = "1";
        } else if (day_of_week == 5) {
            day = "1";
        } else if (day_of_week == 6) {
            day = "1";
        } else if (day_of_week == 7) {
            day = "2";
        } else {
            day = "1";
        }
        return day;
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

//    public void firstLineColor(String tv_line, TextView tv){
//        TextView first_trainview = (TextView) findViewById(R.id.trainview);
//
//        //tvline = substring(tv, 0, tv.getText().toString().indexOf("3호선"));
//        if (){
//            first_trainview = substring(first_trainview, 0, first_trainview.indexOf(" ",0));
//        }
//
//
//    }

    //backButton 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
