package com.dungnn.batterycharge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;

/**
 * Created by nndun on 10/12/2017.
 */

public class CustomRoundCornerProgressBar extends LinearLayout {
    protected final static int DEFAULT_MAX_PROGRESS = 100;
    protected final static int DEFAULT_PROGRESS = 0;
    protected final static int DEFAULT_SECONDARY_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS = 30;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;

    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;
    private LinearLayout layoutSecondaryProgress;

    private int radius;
    private int padding;
    private int totalWidth;

    private float max;
    private float progress;
    private float secondaryProgress;

    private int colorBackground;
    private int colorProgress;
    private int colorSecondaryProgress;

    private boolean isReverse;

    private Timer timer;

    private Activity mContext;

    private OnProgressChangedListener progressChangedListener;
    private static Handler mHandler;
    int widthProgress;
    private int[] arrayColor = new int[]{
            0xFFFFFFFF, 0xFCFFFFFF, 0xFAFFFFFF, 0xF7FFFFFF, 0xF5FFFFFF, 0xF2FFFFFF, 0xF0FFFFFF, 0xEDFFFFFF, 0xEBFFFFFF, 0xE8FFFFFF,
            0xE6FFFFFF, 0xE3FFFFFF, 0xE0FFFFFF, 0xDEFFFFFF, 0xDBFFFFFF, 0xD9FFFFFF, 0xD6FFFFFF, 0xD4FFFFFF, 0xD1FFFFFF, 0xCFFFFFFF,
            0xCCFFFFFF, 0xC9FFFFFF, 0xC7FFFFFF, 0xC4FFFFFF, 0xC2FFFFFF, 0xBFFFFFFF, 0xBDFFFFFF, 0xBAFFFFFF, 0xB8FFFFFF, 0xB5FFFFFF,
            0xB3FFFFFF, 0xB0FFFFFF, 0xADFFFFFF, 0xABFFFFFF, 0xA8FFFFFF, 0xA6FFFFFF, 0xA3FFFFFF, 0xA1FFFFFF, 0x9EFFFFFF, 0x9CFFFFFF,
            0x99FFFFFF, 0x96FFFFFF, 0x94FFFFFF, 0x91FFFFFF, 0x8FFFFFFF, 0x8CFFFFFF, 0x8AFFFFFF, 0x87FFFFFF, 0x85FFFFFF, 0x82FFFFFF,
            0x80FFFFFF, 0x7DFFFFFF, 0x7AFFFFFF, 0x78FFFFFF, 0x75FFFFFF, 0x73FFFFFF, 0x70FFFFFF, 0x6EFFFFFF, 0x6BFFFFFF, 0x69FFFFFF,
            0x66FFFFFF, 0x63FFFFFF, 0x61FFFFFF, 0x5EFFFFFF, 0x5CFFFFFF, 0x59FFFFFF, 0x57FFFFFF, 0x54FFFFFF, 0x52FFFFFF, 0x4FFFFFFF,
            0x4DFFFFFF, 0x4AFFFFFF, 0x47FFFFFF, 0x45FFFFFF, 0x42FFFFFF, 0x40FFFFFF, 0x3DFFFFFF, 0x3BFFFFFF, 0x38FFFFFF, 0x36FFFFFF,
            0x33FFFFFF, 0x30FFFFFF, 0x2EFFFFFF, 0x2BFFFFFF, 0x29FFFFFF, 0x26FFFFFF, 0x24FFFFFF, 0x21FFFFFF, 0x1FFFFFFF, 0x1CFFFFFF,
            0x1AFFFFFF, 0x17FFFFFF, 0x14FFFFFF, 0x12FFFFFF, 0x0FFFFFFF, 0x0DFFFFFF, 0x0AFFFFFF, 0x08FFFFFF, 0x05FFFFFF, 0x03FFFFFF, 0x00FFFFFF,};

    public CustomRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }

        //arrayColor = new int[]{1,2,3};

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = (Activity) context;
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    private void previewLayout(Context context) {
        setGravity(Gravity.CENTER);
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(getClass().getSimpleName());
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.GRAY);
        addView(tv);
    }


    ///////////////////////////
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    protected void initStyleable(Context context, AttributeSet attrs) {

    }

    protected void initView() {

    }

    @SuppressWarnings("deprecation")
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                int radius, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
//        ((MarginLayoutParams) progressParams).leftMargin = 50;
        layoutProgress.setLayoutParams(progressParams);
    }

    @SuppressWarnings("deprecation")


    protected void drawProgress2(final LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                 int radius, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable2(arrayColor[0]);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        final int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        widthProgress =  (int) max / 10;
        progressParams.width = widthProgress;
        //((MarginLayoutParams) progressParams).leftMargin = 200;
        layoutProgress.setLayoutParams(progressParams);
    }

    public void startAnimation(boolean start) {

        runnableCode = new AnimRunnable(layoutProgress, layoutSecondaryProgress, widthProgress);
        if (start) {
            mHandler = new Handler();
            mHandler.postDelayed(runnableCode, 10);
        } else {
            mHandler.removeCallbacks(runnableCode);
            mHandler = null;
        }

    }

    private static class AnimRunnable implements Runnable {
        WeakReference<LinearLayout> layoutProgress;
        WeakReference<LinearLayout> layoutSecondaryProgress;
        int widthProgress;

        public AnimRunnable(LinearLayout layoutProgress, LinearLayout layoutSecondaryProgress, int widthProgress) {
            this.layoutProgress = new WeakReference<LinearLayout>(layoutProgress);
            this.layoutSecondaryProgress = new WeakReference<LinearLayout>(layoutSecondaryProgress);
            this.widthProgress = widthProgress;
        }

        @Override
        public void run() {
            MarginLayoutParams progressParams = (MarginLayoutParams) layoutProgress.get().getLayoutParams();
            int progressWidthCurrent = progressParams.width;
            if (progressParams.leftMargin + progressWidthCurrent == layoutSecondaryProgress.get().getLayoutParams().width) {
                if (progressWidthCurrent == 0) {
                    progressParams.width = progressWidthCurrent - 1;
//                                int per = (int) (progressParams.width*100 / (CustomRoundCornerProgressBar.this.max / 10));
//                                GradientDrawable backgroundDrawable = createGradientDrawable2(arrayColor[100-per]);
//                                Log.e(TAG, "run: " + arrayColor[100 - per]);
//                                int newRadius = CustomRoundCornerProgressBar.this.radius - (CustomRoundCornerProgressBar.this.padding / 2);
//                                backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    layoutProgress.setBackground(backgroundDrawable);
//                                } else {
//                                    layoutProgress.setBackgroundDrawable(backgroundDrawable);
//                                }

                } else {
                    progressParams.leftMargin = -widthProgress;
                    progressParams.width = widthProgress;

//                                GradientDrawable backgroundDrawable = createGradientDrawable2(arrayColor[0]);
//                                int newRadius = CustomRoundCornerProgressBar.this.radius - (CustomRoundCornerProgressBar.this.padding / 2);
//                                backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    layoutProgress.setBackground(backgroundDrawable);
//                                } else {
//                                    layoutProgress.setBackgroundDrawable(backgroundDrawable);
//                                }
                }


                layoutProgress.get().setLayoutParams(progressParams);
            } else {
                progressParams.leftMargin = progressParams.leftMargin + 1;
                layoutProgress.get().setLayoutParams(progressParams);

            }

            mHandler.postDelayed(this, 10);
        }
    }

    private  static Runnable runnableCode;

    //int width

//    private Runnable runnableCode = new Runnable() {
//
//        @Override
//        public void run() {
//            //Log.e(TAG, "run: abcxyz");
//            // Do something here on the main thread
//
//
//
//                //progressParams.width = progressWidth;
//                // Repeat this the same runnable code block again another 2 seconds
//           mHandler.postDelayed(runnableCode, 10);
//
//            }
//    };


    protected void onViewDraw() {

    }

    //////////////////////////////

    public void setup(Context context, AttributeSet attrs) {
        setupStyleable(context, attrs);

        removeAllViews();
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(initLayout(), this);
        // Initial default view
        layoutBackground = (LinearLayout) findViewById(R.id.layout_background);
        layoutProgress = (LinearLayout) findViewById(R.id.layout_progress);
        layoutSecondaryProgress = (LinearLayout) findViewById(R.id.layout_secondary_progress);

        initView();
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);

        radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));

        isReverse = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcReverse, false);

        max = typedArray.getFloat(R.styleable.RoundCornerProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorBackground = typedArray.getColor(R.styleable.RoundCornerProgress_rcBackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_progress_default);
        colorProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcProgressColor, colorProgressDefault);
        int colorSecondaryProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
        colorSecondaryProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcSecondaryProgressColor, colorSecondaryProgressDefault);
        typedArray.recycle();

        initStyleable(context, attrs);
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if (!isInEditMode()) {
            totalWidth = newWidth;
            drawAll();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawPrimaryProgress();
                    drawSecondaryProgress();
                }
            }, 5);
        }
    }

    // Redraw all view
    protected void drawAll() {
        drawBackgroundProgress();
        drawPadding();
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
        onViewDraw();
    }

    // Draw progress background
    @SuppressWarnings("deprecation")
    private void drawBackgroundProgress() {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorBackground);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutBackground.setBackground(backgroundDrawable);
        } else {
            layoutBackground.setBackgroundDrawable(backgroundDrawable);
        }
    }

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable2(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColors(new int[]{
                0x00FFFFF,
                color});
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return gradientDrawable;
    }

    private void drawPrimaryProgress() {
        drawProgress2(layoutProgress, max, progress, totalWidth, radius, padding, colorProgress, isReverse);

        drawProgress(layoutSecondaryProgress, max, secondaryProgress, totalWidth, radius, padding, colorSecondaryProgress, isReverse);
    }

    private void drawSecondaryProgress() {
        //drawProgress(layoutSecondaryProgress, max, secondaryProgress, totalWidth, radius, padding, colorSecondaryProgress, isReverse);
    }

    private void drawProgressReverse() {
        setupReverse(layoutProgress);
        setupReverse(layoutSecondaryProgress);
    }

    // Change progress position by depending on isReverse flag
    private void setupReverse(LinearLayout layoutProgress) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    private void drawPadding() {
        layoutBackground.setPadding(padding, padding, padding, padding);
    }

    // Remove all of relative align rule
    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        }
    }

    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius >= 0)
            this.radius = radius;
        drawBackgroundProgress();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        if (padding >= 0)
            this.padding = padding;
        drawPadding();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0)
            this.max = max;
        if (this.progress > max)
            this.progress = max;
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getLayoutWidth() {
        return totalWidth;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress < 0)
            this.progress = 0;
        else if (progress > max)
            this.progress = max;
        else
            this.progress = progress;
        drawPrimaryProgress();
        if (progressChangedListener != null)
            progressChangedListener.onProgressChanged(getId(), this.progress, true, false);
    }

    public float getSecondaryProgressWidth() {
        if (layoutSecondaryProgress != null)
            return layoutSecondaryProgress.getWidth();
        return 0;
    }

    public float getSecondaryProgress() {
        return secondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0)
            this.secondaryProgress = 0;
        else if (secondaryProgress > max)
            this.secondaryProgress = max;
        else
            this.secondaryProgress = secondaryProgress;
        drawSecondaryProgress();
        if (progressChangedListener != null)
            progressChangedListener.onProgressChanged(getId(), this.secondaryProgress, false, true);
    }

    public int getProgressBackgroundColor() {
        return colorBackground;
    }

    public void setProgressBackgroundColor(int colorBackground) {
        this.colorBackground = colorBackground;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return colorProgress;
    }

    public void setProgressColor(int colorProgress) {
        this.colorProgress = colorProgress;
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(int colorSecondaryProgress) {
        this.colorSecondaryProgress = colorSecondaryProgress;
        drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        progressChangedListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawAll();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.radius = this.radius;
        ss.padding = this.padding;

        ss.colorBackground = this.colorBackground;
        ss.colorProgress = this.colorProgress;
        ss.colorSecondaryProgress = this.colorSecondaryProgress;

        ss.max = this.max;
        ss.progress = this.progress;
        ss.secondaryProgress = this.secondaryProgress;

        ss.isReverse = this.isReverse;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.radius = ss.radius;
        this.padding = ss.padding;

        this.colorBackground = ss.colorBackground;
        this.colorProgress = ss.colorProgress;
        this.colorSecondaryProgress = ss.colorSecondaryProgress;

        this.max = ss.max;
        this.progress = ss.progress;
        this.secondaryProgress = ss.secondaryProgress;

        this.isReverse = ss.isReverse;
    }

    private static class SavedState extends View.BaseSavedState {
        float max;
        float progress;
        float secondaryProgress;

        int radius;
        int padding;

        int colorBackground;
        int colorProgress;
        int colorSecondaryProgress;

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.progress = in.readFloat();
            this.secondaryProgress = in.readFloat();

            this.radius = in.readInt();
            this.padding = in.readInt();

            this.colorBackground = in.readInt();
            this.colorProgress = in.readInt();
            this.colorSecondaryProgress = in.readInt();

            this.isReverse = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.progress);
            out.writeFloat(this.secondaryProgress);

            out.writeInt(this.radius);
            out.writeInt(this.padding);

            out.writeInt(this.colorBackground);
            out.writeInt(this.colorProgress);
            out.writeInt(this.colorSecondaryProgress);

            out.writeByte((byte) (this.isReverse ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnProgressChangedListener {
        public void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress);
    }
}
