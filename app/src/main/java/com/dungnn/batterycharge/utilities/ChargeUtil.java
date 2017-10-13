package com.dungnn.batterycharge.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dungnn.batterycharge.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by nndun on 10/11/2017.
 */

public class ChargeUtil {
    private Context mContext;
    private WindowManager mWindowManager;
    private View viewMain;
    private WindowManager.LayoutParams params;


    //////

    IntentFilter intentFilter;
    private ArrayList<ChargeEntity> arrHistoryCharge;
    private TextView tvRemainingTime;
    private boolean firstRun;
    BroadcastReceiver mReceiverBattery;
    BroadcastReceiver mStopreceiver;

    private TextView tvBatteryLevel;
    private RoundCornerProgressBar waveProgressbar;

    private long oldTime;
    private int oldLevel;

    private String color_RED = "#f44242";
    private String color_green = "#00ff00";
    private String color_yellow = "#ffff00";

    private String color_wave = "#f1f1f1";

    private ProgressBar mProgressBar;

    private LinearLayout layoutWave;

    private WaveLoadingView mWaveLoadingView;

    private Animation rotateAnim;

    private ChargeEntity charge;

    private ImageView ivFirstCircle;
    private ImageView ivSecondCircle;
    private ImageView ivThirdCircle;

    class ChargeEntity {
        public int percent;
        public long timeCharge;
    }
    ///


    public ChargeUtil(Context context) {
        mContext = context;
        mWindowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.TRANSPARENT;
        params.flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SECURE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                //| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        viewMain = LayoutInflater.from(context).inflate(R.layout.activity_charge_screen, null);

        arrHistoryCharge = new ArrayList();
        firstRun = true;
        initControl();

        mStopreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //ChargeScreenActivity.this.unregisterReceiver(mReceiverBattery);
                //ChargeScreenActivity.this.unregisterReceiver(mStopreceiver);
                if(viewMain.getParent() != null) {
                    mWindowManager.removeView(viewMain);
                }
            }
        };


        mReceiverBattery = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                BatteryStatus batteryStatus = new BatteryStatus(intent);
                int level =batteryStatus.getLevel();
                tvBatteryLevel.setText(batteryStatus.getLevel() + "");
                waveProgressbar.setMax(100);

                waveProgressbar.setProgress(level);

                mProgressBar.setProgress(level);
                mProgressBar.setScaleY(2f);

                mWaveLoadingView.setProgressValue(level);

                if(firstRun) {
                    oldTime = System.currentTimeMillis();
                    oldLevel = level;
                    firstRun = false;
                } else {
                    if(oldLevel < level) {
                        long time = System.currentTimeMillis();
                        long diff = time - oldTime;
                        charge = new ChargeEntity();
                        charge.percent = level - oldLevel;
                        charge.timeCharge = diff;
                        if(arrHistoryCharge.size() == 1) {
                            arrHistoryCharge.remove(0);
                        }
                        arrHistoryCharge.add(charge);
                        oldTime = time;
                        oldLevel = level;
                    }
                }
                tvRemainingTime.setText(getRemainingTime(level));
                setAnimation(level);
                if(level >=0 && level < 20 ) {
                    waveProgressbar.setProgressColor(Color.parseColor(color_RED));

                } else if (level < 80) {
                    waveProgressbar.setProgressColor(Color.parseColor(color_green));
                } else {
                    waveProgressbar.setProgressColor(Color.parseColor(color_green));
                    if(level == 100) {
                        mWaveLoadingView.setAmplitudeRatio(0);
                    } else {
                        mWaveLoadingView.setAmplitudeRatio(20);
                    }
                }

            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext.getApplicationContext().registerReceiver(mReceiverBattery, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mContext.getApplicationContext().registerReceiver(mStopreceiver,intentFilter);
        viewMain.findViewById(R.id.ivFirstProcessCircle).startAnimation(rotateAnim);
        viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.removeView(viewMain);
            }
        });
    }

    public void createView() {
        if(viewMain.getParent() == null) {
            mWindowManager.addView(viewMain,params);
        }
    }

    private void setAnimation(int level) {
        if(level < 90) {
            ivFirstCircle.startAnimation(rotateAnim);
            ivFirstCircle.setVisibility(View.VISIBLE);
            ivSecondCircle.clearAnimation();
            ivSecondCircle.setVisibility(View.GONE);
            ivThirdCircle.clearAnimation();
            ivThirdCircle.setVisibility(View.GONE);
        } else if(level <= 95) {
            ivFirstCircle.clearAnimation();
            ivFirstCircle.setVisibility(View.GONE);
            ivSecondCircle.startAnimation(rotateAnim);
            ivSecondCircle.setVisibility(View.VISIBLE);
            ivThirdCircle.clearAnimation();
            ivThirdCircle.setVisibility(View.GONE);
        } else {
            ivFirstCircle.clearAnimation();
            ivFirstCircle.setVisibility(View.GONE);
            ivSecondCircle.clearAnimation();
            ivSecondCircle.setVisibility(View.GONE);
            ivThirdCircle.startAnimation(rotateAnim);
            ivThirdCircle.setVisibility(View.VISIBLE);

        }
    }
    private void initControl() {
        tvBatteryLevel = viewMain.findViewById(R.id.tvBatteryLevel);
        waveProgressbar = viewMain.findViewById(R.id.waveProgressbar);
        rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_clockwise);
        tvRemainingTime = viewMain.findViewById(R.id.tvRemainingTime);
        mProgressBar =  viewMain.findViewById(R.id.activity_battery_charging_progressbar);
        mProgressBar.getProgressDrawable().setColorFilter(mContext
                .getResources().getColor(R.color.activity_battery), PorterDuff.Mode.SRC_IN);
        //mWaveLoadingView = new WaveLoadingView(mContext);
        mWaveLoadingView = viewMain.findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setAnimDuration(2000);
        mWaveLoadingView.setAmplitudeRatio(20);
        mWaveLoadingView.setBorderWidth(0);
        mWaveLoadingView.setWaveColor(Color.parseColor(color_wave));

        ivFirstCircle = viewMain.findViewById(R.id.ivFirstProcessCircle);
        ivSecondCircle = viewMain.findViewById(R.id.ivSecondProcessCircle);
        ivThirdCircle = viewMain.findViewById(R.id.ivThirdProcessCircle);
    }

    public String getRemainingTime(int currenLevel) {
        if(arrHistoryCharge.size() == 0) {
            return "Chưa tính ra";
        }
        int totalPercent = 0;
        long totalTime = 0;
        for (ChargeEntity item : arrHistoryCharge) {
            totalPercent += item.percent;
            totalTime += item.timeCharge;
        }
        long average = (totalTime / totalPercent) * (100 - currenLevel);
        int hours = (int) (TimeUnit.MILLISECONDS.toHours(average) % TimeUnit.DAYS.toHours(1));
        int min = (int) (TimeUnit.MILLISECONDS.toMinutes(average) % TimeUnit.HOURS.toMinutes(1));
        return hours + ":" + min;
    }
}
