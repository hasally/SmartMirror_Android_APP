package com.example.hayoung.mirrore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class MotionActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {
    String TAG = "Hello";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

//        if (PermissionUtility.checkCameraPermission(this)) {
//            LocalOpenCV loader = new LocalOpenCV(MotionActivity.this, MotionActivity.this, MotionActivity.this);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV loader = new LocalOpenCV(MotionActivity.this, MotionActivity.this, MotionActivity.this);
        }
    }

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Up");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MotionActivity.this, "Hand Motion Up", Toast.LENGTH_SHORT).show();
                try {
                    CustomMotionTask customMostionTask=  new CustomMotionTask(getApplicationContext());
                    customMostionTask.execute("up").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Down");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MotionActivity.this, "Hand Motion Down", Toast.LENGTH_SHORT).show();
                try {
                    CustomMotionTask customMostionTask= new CustomMotionTask(getApplicationContext());
                    customMostionTask.execute("down").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Left");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MotionActivity.this, "Hand Motion Left", Toast.LENGTH_SHORT).show();
                try {
                    CustomMotionTask customMostionTask= new CustomMotionTask(getApplicationContext());
                    customMostionTask.execute("left").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "RIght");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MotionActivity.this, "Hand Motion Right", Toast.LENGTH_SHORT).show();
                try {
                    CustomMotionTask customMostionTask = new CustomMotionTask(getApplicationContext());
                    customMostionTask.execute("right").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSensorClick(ClickSensor caller) {
        Log.i(TAG, "Click");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}
