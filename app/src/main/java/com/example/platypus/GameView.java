package com.example.platypus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GameView extends View {
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(), new SimpleScaleListenerImpl());
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new SimpleGestureListenerImpl());
    private float mPosX;
    private float mPosY;
    private float mFocusX;
    private float mFocusY;
    private boolean isPlayerPlanetTouched;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPosX = context.getResources().getDisplayMetrics().widthPixels / 2;
        mPosY = context.getResources().getDisplayMetrics().heightPixels / 2;
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        canvas.translate(mPosX, mPosY);
        Paint paint = new Paint();
        for (int i = 0; i < Planet.planetList.size(); i++) {
            Planet p = Planet.planetList.get(i);
            paint.setColor(p.getColor());
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    (float) p.getPosition().getX(),
                    (float) p.getPosition().getY(),
                    (float) Math.pow(p.getMass(), 0.5),
                    paint
            );
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(p.path, paint);
        }
        paint.setColor(Color.RED);
        canvas.drawCircle(0, 0, GameActivity.PLAYER_MOVE_RANGE, paint);
        if (GameActivity.isRunning) {
            invalidate();
        }
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
            if (isPlayerPlanetTouched == true && GameActivity.isRunning == false) {
                GameActivity.playerPlanet.getPosition().minus(new Vector(distanceX, distanceY));
                invalidate();
            } else {
                mPosX -= distanceX;
                mPosY -= distanceY;
                invalidate();
            }
            return true;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            GameActivity.playerPlanet.setExtraForce(e.getX(), e.getY());

            Planet p = GameActivity.playerPlanet;
            Vector v = new Vector(e.getX() / mScaleFactor - mPosX, e.getY() / mScaleFactor - mPosY);
            if (v.distance(p.getPosition()) < Math.sqrt(p.getMass()) + 20) {
                isPlayerPlanetTouched = true;
            } else {
                isPlayerPlanetTouched = false;
            }
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(getContext(), "onsingletapconfrimed", Toast.LENGTH_SHORT).show();
           // GameActivity.playerPlanet.setExtraForce(e.getX(), e.getY());
            return true;
        }
    }
}