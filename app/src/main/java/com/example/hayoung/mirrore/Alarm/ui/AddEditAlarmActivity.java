package com.example.hayoung.mirrore.Alarm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hayoung.mirrore.Alarm.data.DatabaseHelper;
import com.example.hayoung.mirrore.Alarm.model.Alarm;
import com.example.hayoung.mirrore.Alarm.service.LoadAlarmsService;
import com.example.hayoung.mirrore.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AddEditAlarmActivity extends AppCompatActivity {

    public static final String ALARM_EXTRA = "alarm_extra";
    public static final String MODE_EXTRA = "mode_extra";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EDIT_ALARM, ADD_ALARM, UNKNOWN})
    @interface Mode {
    }

    public static final int EDIT_ALARM = 1;
    public static final int ADD_ALARM = 2;
    public static final int UNKNOWN = 0;

    Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        //ToolBar 추가하기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        getToolbarTitle();

        final Alarm alarm = getAlarm();

        final FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        if (getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container) == null) {
            System.out.println("getSupportFragmentManager()");
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.edit_alarm_frag_container, AddEditAlarmFragment.newInstance(alarm))
                    .commit();
        }

    }

    private Alarm getAlarm() {
        switch (getMode()) {
            case EDIT_ALARM:
                System.out.println("getAlarm()/EDIT_ALARM");
                return getIntent().getParcelableExtra(ALARM_EXTRA);
            case ADD_ALARM:
                System.out.println("getAlarm()/ADD_ALARM");
                final long id = DatabaseHelper.getInstance(this).addAlarm();
                LoadAlarmsService.launchLoadAlarmsService(this);
                return new Alarm(id);
            case UNKNOWN:
            default:
                System.out.println("getAlarm()/DEFAULT_ALARM");
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
    }

    private @Mode
    int getMode() {
        System.out.println("Mode_MODE_EXTRA");
        final @Mode int mode = getIntent().getIntExtra(MODE_EXTRA, UNKNOWN);
        return mode;
    }

    private void getToolbarTitle() {
        int titleResId;
        switch (getMode()) {
            case EDIT_ALARM:
                System.out.println("getToolbarTitle()/EDIT_ALARM");
                titleResId = R.string.edit_alarm;
                toolbar_title.setText(titleResId);
                break;
            case ADD_ALARM:
                System.out.println("getToolbarTitle()/ADD_ALARM");
                titleResId = R.string.add_alarm;
                toolbar_title.setText(titleResId);
                break;
            case UNKNOWN:
            default:
                System.out.println("getToolbarTitle()/Default");
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("Menu create");
        getMenuInflater().inflate(R.menu.edit_alarm_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("home_back_button");
                final FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

                if (getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container) == null) {
                    System.out.println("getSupportFragmentManager()");
                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .commit();
                }
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent buildAddEditAlarmActivityIntent(Context context, @Mode int mode) {
        System.out.println("buildAddEditAlarmActivityIntent_mode");
        final Intent i = new Intent(context, AddEditAlarmActivity.class);
        i.putExtra(MODE_EXTRA, mode);
        return i;
    }

}
