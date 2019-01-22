package com.example.hayoung.mirrore.Alarm.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hayoung.mirrore.Alarm.adapter.AlarmsAdapter;
import com.example.hayoung.mirrore.Alarm.model.Alarm;
import com.example.hayoung.mirrore.Alarm.service.LoadAlarmsReceiver;
import com.example.hayoung.mirrore.Alarm.service.LoadAlarmsService;
import com.example.hayoung.mirrore.Alarm.view.DividerItemDecoration;
import com.example.hayoung.mirrore.Alarm.view.EmptyRecyclerView;
import com.example.hayoung.mirrore.R;

import java.util.ArrayList;

public final class AlarmFragment extends Fragment
        implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        final EmptyRecyclerView rv = (EmptyRecyclerView) v.findViewById(R.id.recycler);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getContext()));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAdapter.setAlarms(alarms);
    }

}