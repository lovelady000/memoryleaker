package com.dungnn.batterycharge;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dungnn.batterycharge.services.ChargeService;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private int mInterval = 5; // 5 seconds by default, can be changed later
    private Handler mHandler;
    CustomRoundCornerProgressBar progress;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, ChargeService.class));

        progress = (CustomRoundCornerProgressBar) findViewById(R.id.rcProgressBar);

        //progress.startAnimation(true);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.startAnimation(!isStart);
                isStart = !isStart;
            }
        });
        progressRunning();
    }
    private boolean isStart = false;


    private void requestPermission() {
        String[] permission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permission, 11);
            if (!Settings.canDrawOverlays(MainActivity.this)) {

                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(myIntent, 101);
            }
        }
    }
    private int count = 0;
    protected void progressRunning() {

//        progress.setLeft(30);
//        mHandler = new Handler();
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressValue = progress.getProgress();
//                        secondValue = progress.getSecondaryProgress();
//                        if (progressValue == secondValue) {
//                            progress.setProgress(0);
//                            count++;
//                            if(count == 3) {
//                                timer.cancel();
//                            }
//                        } else {
//                            progress.setProgress(progressValue + 1);
//                        }
//
//
//                    }
//                });
//            }
//        }, 10, 10);

        //startRepeatingTask();


    }

    float progressValue;
    float secondValue;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                float progressValue = progress.getProgress();
                float secondValue = progress.getSecondaryProgress();
                if (progressValue == secondValue) {
                    progress.setProgress(0);
                } else {
                    progress.setProgress(progressValue + 1);
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }


    ////


}
