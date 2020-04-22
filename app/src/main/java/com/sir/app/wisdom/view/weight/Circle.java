package com.sir.app.wisdom.view.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.sir.app.wisdom.R;

/**
 * 人脸识别
 * Created by zhuyinan on 2020/4/13.
 */
public class Circle extends View {


    private float mRadius;
    private ValueAnimator mAnimator;
    private float mCurrentRotateDegrees;
    private float mCenterX;//旋转圆的中心横坐标
    private float mCenterY;//旋转圆的中心纵坐标

    private Bitmap bitmap;
    private Bitmap bitmapBG;


    public Circle(Context context) {
        this(context, null);
    }

    public Circle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        startAnimator();

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_spin);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_spin_bg);
        bitmapBG = temp.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void startAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, (float) Math.PI);
        mAnimator.setDuration(8000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(-1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentRotateDegrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        //背景色
        canvas.drawBitmap(bitmapBG, 0, 0, mPaint);
        //设置混合模式
        mPaint.setColor(Color.WHITE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();

        //旋转画布
        canvas.rotate((float) (mCurrentRotateDegrees * 360), mCenterX, mCenterY);
        canvas.drawBitmap(bitmap, mCenterX - bitmap.getWidth() / 2, mCenterY - bitmap.getHeight() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCenterX = MeasureSpec.getSize(widthMeasureSpec) / 2;

        mCenterY = MeasureSpec.getSize(heightMeasureSpec) / 2.1f;

        mRadius = MeasureSpec.getSize(widthMeasureSpec) / 2.7f;
    }
}
