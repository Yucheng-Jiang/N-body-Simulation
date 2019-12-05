package com.example.platypus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import androidx.annotation.Nullable;

public class CustomView extends View {
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(), new SimpleScaleListenerImpl());
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new SimpleGestureListenerImpl());
    private float mPosX = 0;
    private float mPosY = 0;
    private float mFocusX;
    private float mFocusY;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        canvas.translate(mPosX, mPosY);
        for (int i = 0; i < Planet.planetList.size(); i++) {
            Planet p = Planet.planetList.get(i);
            Paint paint = new Paint();
            paint.setColor(p.getColor());
            canvas.drawCircle(
                    (float) p.getPosition().getX(),
                    (float) p.getPosition().getY(),
                    (float) Math.pow(p.getMass(), 0.5),
                    paint
            );
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(p.path, paint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public void setmPosXY(float x, float y) {
        mPosX = x;
        mPosY = y;
    }

    public float getmPosX() {
        return mPosX;
    }

    public float getmPosY() {
        return mPosY;
    }

    private class SimpleScaleListenerImpl extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            //
            mScaleFactor = Math.max(0.3f, Math.min(mScaleFactor, 3.0f));
            mFocusX = detector.getFocusX();
            mFocusY = detector.getFocusY();
            invalidate();
            return true;
        }
    }

    private class SimpleGestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mPosX -= distanceX;
            mPosY -= distanceY;
            invalidate();
            return true;
        }
    }
}