package com.example.hayoung.mirrore.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hayoung.mirrore.Alarm.ui.AlarmActivity;
import com.example.hayoung.mirrore.Memo.MemoActivity;
import com.example.hayoung.mirrore.R;
import com.example.hayoung.mirrore.Recommend.RecommendActivity;
import com.example.hayoung.mirrore.Subway.SubwayActivity;
import com.example.hayoung.mirrore.Weather.WeatherActivity;
import com.zcw.togglebutton.ToggleButton;

import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

import static android.app.Activity.RESULT_OK;


public class Home_SettingFragment extends Fragment implements View.OnClickListener {

    private CardView weather_loc_cardview, memo_cardview, alarm_cardview, subway_loc_cardview, news_cardview, recommend_cardview;
    //    private Button voice_btn;
    private com.zcw.togglebutton.ToggleButton weather_loc_togglebtn, memo_togglebtn, alarm_togglebtn, subway_loc_togglebtn, news_togglebtn, recommend_togglebtn;
    private UserInfo userInfo;

    public Home_SettingFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_setting, container, false);
        weather_loc_cardview = (CardView) view.findViewById(R.id.weather_loc_cardview);
        memo_cardview = (CardView) view.findViewById(R.id.memo_cardview);
        alarm_cardview = (CardView) view.findViewById(R.id.alarm_cardview);
        subway_loc_cardview = (CardView) view.findViewById(R.id.subway_loc_cardview);
        news_cardview = (CardView) view.findViewById(R.id.news_cardview);
//        voice_btn = (Button) view.findViewById(R.id.voice_btn);
        recommend_cardview = (CardView) view.findViewById(R.id.recommend_cardview);

        weather_loc_cardview.setOnClickListener(this);
        memo_cardview.setOnClickListener(this);
        alarm_cardview.setOnClickListener(this);
        subway_loc_cardview.setOnClickListener(this);
        news_cardview.setOnClickListener(this);
//        voice_btn.setOnClickListener(this);
        recommend_cardview.setOnClickListener(this);

        userInfo = new UserInfo(getActivity());

        weather_loc_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.weather_loc_togglebtn);
        memo_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.memo_home_togglebtn);
        alarm_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.alarm_home_togglebtn);
        subway_loc_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.subway_loc_togglebtn);
        news_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.news_home_togglebtn);
        recommend_togglebtn = (com.zcw.togglebutton.ToggleButton) view.findViewById(R.id.recommend_home_togglebtn);

        System.out.println("alarm_info" + userInfo.getKeyCalendar());
        //weather 날씨
        if (userInfo.getKeyWeather() == 0) {
            weather_loc_togglebtn.setToggleOff();
            userInfo.setKeyWeather(0);
        } else {
            weather_loc_togglebtn.setToggleOn();
            userInfo.setKeyWeather(1);
        }
        //memo 메모
        if (userInfo.getKeyMemo() == 0) {
            memo_togglebtn.setToggleOff();
            userInfo.setKeyMemo(0);
        } else {
            memo_togglebtn.setToggleOn();
            userInfo.setKeyMemo(1);
        }
        //alarm 알람
        if (userInfo.getKeyCalendar() == 0) {
            alarm_togglebtn.setToggleOff();
            userInfo.setKeyCalendar(0);
        } else {
            alarm_togglebtn.setToggleOn();
            userInfo.setKeyCalendar(1);
        }
        //subway 지하철
        if (userInfo.getKeySubway() == 0) {
            subway_loc_togglebtn.setToggleOff();
            userInfo.setKeySubway(0);
        } else {
            subway_loc_togglebtn.setToggleOn();
            userInfo.setKeySubway(1);
        }
        //news 뉴스
        if (userInfo.getKeyNews() == 0) {
            news_togglebtn.setToggleOff();
            userInfo.setKeyNews(0);
        } else {
            news_togglebtn.setToggleOn();
            userInfo.setKeyNews(1);
        }

        weather_loc_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(getContext());
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("weather", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyWeather(1);
                        weather_loc_togglebtn.setToggleOn();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("weather", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyWeather(0);
                        weather_loc_togglebtn.setToggleOff();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        memo_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(getContext());
                CustomCallingWeb customCallingWeb = new CustomCallingWeb(getContext());
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("memo", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyMemo(1);
                        memo_togglebtn.setToggleOn();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("memo", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyMemo(0);
                        memo_togglebtn.setToggleOff();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alarm_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(getContext());
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
        subway_loc_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(getContext());
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("subway", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeySubway(1);
                        subway_loc_togglebtn.setToggleOn();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("subway", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeySubway(0);
                        subway_loc_togglebtn.setToggleOff();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        news_togglebtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(getContext());
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("news", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyNews(1);
                        news_togglebtn.setToggleOn();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("news", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyNews(0);
                        news_togglebtn.setToggleOff();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case RESULT_OK:
                if (userInfo.getKeyWeather() == 0) {
                    weather_loc_togglebtn.setToggleOff();
                    userInfo.setKeyWeather(0);
                } else {
                    weather_loc_togglebtn.setToggleOn();
                    userInfo.setKeyWeather(1);
                }
                if (userInfo.getKeySubway() == 0) {
                    subway_loc_togglebtn.setToggleOff();
                    userInfo.setKeySubway(0);
                } else {
                    subway_loc_togglebtn.setToggleOn();
                    userInfo.setKeySubway(1);
                }
                if (userInfo.getKeyMemo() == 0) {
                    memo_togglebtn.setToggleOff();
                    userInfo.setKeyMemo(0);
                } else {
                    memo_togglebtn.setToggleOn();
                    userInfo.setKeyMemo(1);
                }
                if (userInfo.getKeyCalendar() == 0) {
                    alarm_togglebtn.setToggleOff();
                    userInfo.setKeyCalendar(0);
                } else {
                    alarm_togglebtn.setToggleOn();
                    userInfo.setKeyCalendar(1);
                }
                break;

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.weather_loc_cardview:
                intent = new Intent(getActivity(), WeatherActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.memo_cardview:
                intent = new Intent(getActivity(), MemoActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.alarm_cardview:
                intent = new Intent(getActivity(), AlarmActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.subway_loc_cardview:
                intent = new Intent(getActivity(), SubwayActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.recommend_cardview:
//                intent = new Intent(getActivity(), RecommendActivity.class);
//                startActivityForResult(intent, 0);
                break;
            case R.id.news_cardview:
                break;
            case R.id.weather_loc_togglebtn:
                break;

//            case R.id.voice_btn:
//                intent = new Intent(getActivity(), SpeechToTextActivity.class);
//                startActivity(intent);
//                break;

        }
    }

}
