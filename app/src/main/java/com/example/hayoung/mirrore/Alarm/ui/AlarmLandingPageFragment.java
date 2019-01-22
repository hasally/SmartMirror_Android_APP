package com.example.hayoung.mirrore.Alarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hayoung.mirrore.Alarm.service.AlarmInitTask;
import com.example.hayoung.mirrore.MotionActivity;
import com.example.hayoung.mirrore.R;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);

        final Button dismiss = (Button) v.findViewById(R.id.dismiss_btn);

        dismiss.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dismiss_btn:
                startActivity(new Intent(getContext(), MotionActivity.class));
                AlarmInitTask alarmInitTask = new AlarmInitTask(getContext());
                alarmInitTask.execute();
                getActivity().finish();
                break;
        }

    }
}
