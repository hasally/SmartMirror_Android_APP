package com.example.hayoung.mirrore.Weather;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.Main.CustomCallingWeb;
import com.example.hayoung.mirrore.Main.CustomMemberTask;
import com.example.hayoung.mirrore.Main.CustomSettingTask;
import com.example.hayoung.mirrore.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

public class WeatherActivity extends AppCompatActivity {
    private TextInputLayout weather_loc;
    Toolbar toolbar;
    com.zcw.togglebutton.ToggleButton weather_togglebtn;
    ListView area_listview;
    TextView cityField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    //    detailsField,
    ProgressBar loader;
    Typeface weatherFont;
    UserInfo userInfo;
    String city = "Seoul, KR";
    String weather_loc_text_en = "";
    /* Please Put your API KEY here */
    String OPEN_WEATHER_MAP_API = "cbfdb21fa1793c10b14b6b6d00fbef03";
    /* Please Put your API KEY here */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        weather_loc = (TextInputLayout) findViewById(R.id.weather_loc);
        // area_listview = (ListView) findViewById(R.id.area_listview);
        loader = (ProgressBar) findViewById(R.id.loader);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
//        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weather_togglebtn = (com.zcw.togglebutton.ToggleButton) findViewById(R.id.weather_togglebtn);

        userInfo = new UserInfo(this);

        //ToolBar 추가하기

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toggle
        if (userInfo.getKeyWeather() == 0) {
            weather_togglebtn.setToggleOff();
            userInfo.setKeyWeather(0);
        } else {
            weather_togglebtn.setToggleOn();
            userInfo.setKeyWeather(1);
        }

        weather_togglebtn.setOnToggleChanged(new com.zcw.togglebutton.ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(WeatherActivity.this);
                CustomCallingWeb customCallingWeb = new CustomCallingWeb(WeatherActivity.this);
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("weather", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyWeather(1);
                        weather_togglebtn.setToggleOn();
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
                        weather_togglebtn.setToggleOff();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //지역 Spinner
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_dropdown_item);
        MaterialBetterSpinner area_betterSpinner = (MaterialBetterSpinner) findViewById(R.id.weather_loc_spinner);
        area_betterSpinner.setAdapter(arrayAdapter);
        System.out.println("userInfo_weather_page :" + userInfo.getKeyId());
        System.out.println("userInfo_weather_page :" + userInfo.getKeyName());
        System.out.println("userInfo_weather_page :" + userInfo.getKeyWeatherLoc());
        String spinnerSelectedText = "";
        for (int i = 0; i < area_betterSpinner.getAdapter().getCount(); i++) {
            spinnerSelectedText = area_betterSpinner.getAdapter().getItem(i).toString();
            if (spinnerSelectedText.equals(userInfo.getKeyWeatherLoc())) {

                area_betterSpinner.setText(area_betterSpinner.getAdapter().getItem(i).toString());
                System.out.println("selected_spinner_text :" + spinnerSelectedText);
                textEnChange(spinnerSelectedText);
            }
        }
        area_betterSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("after_text_changed : " + s);
                textEnChange(s.toString());
                CustomMemberTask customMemberTask = new CustomMemberTask(getApplication());
                try {
                    String updateSuccessStr = customMemberTask.execute("weather_loc", weather_loc_text_en, s.toString(), userInfo.getKeyId().toString()).get();
                    System.out.println("update 성공 여부:" + updateSuccessStr);
                    userInfo.setKeyWeatherLoc(s.toString());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);

    }

    public class CustomDownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
//                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");

                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void textEnChange(String s) {

        switch (s.toString()) {
            case "서울시":
                weather_loc_text_en = "Seoul";
                break;
            case "경기도":
                weather_loc_text_en = "Gyeonggi-do";
                break;
            case "강원도":
                weather_loc_text_en = "chuncheon";
                break;
            case "충청도":
                weather_loc_text_en = "daejeon";
                break;
            case "전라도":
                weather_loc_text_en = "Jeonju";
                break;
            case "경상도":
                weather_loc_text_en = "busan";
                break;
        }
        System.out.println("weather_loc_text_en : " + weather_loc_text_en);
        taskLoadUp(weather_loc_text_en);
    }

    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            CustomDownloadWeatherTask task = new CustomDownloadWeatherTask();
            System.out.println("weather: " + query);
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
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

    //backButton 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}

