package com.example.platypus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private boolean isVelocityArrowTouched;

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
            paint.setColor(Color.RED);
            if (p.isPlayer) {
                paint.setColor(Color.BLUE);
            }
            canvas.drawCircle(
                    (float) p.getPosition().getX(),
                    (float) p.getPosition().getY(),
                    (float) Math.pow(p.getMass(), 0.5),
                    paint
            );
        }
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, GameActivity.PLAYER_MOVE_RANGE, paint);
        if (!GameActivity.isRunning) {
            drawVelocity(canvas);
            invalidate();
        }
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

    private void drawVelocity(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        Planet p = GameActivity.playerPlanet;
        paint.setColor(Color.BLUE);

        float fromX = (float) p.getPosition().getX();
        float fromY = (float) p.getPosition().getY();
        float toX = (float) (p.getSpeed().getX() + p.getPosition().getX());
        float toY = (float) (p.getSpeed().getY() + p.getPosition().getY());

        float height = 20;
        float bottom = 10;

        canvas.drawLine(fromX, fromY, toX, toY, paint);
        float distance = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                + (toY - fromY) * (toY - fromY));
        float distanceX = toX - fromX;
        float distanceY = toY - fromY;
        float pointX = toX - (height / distance * distanceX);
        float pointY = toY - (height / distance * distanceY);

        Path path = new Path();
        path.moveTo(toX, toY);

        path.lineTo(pointX + (bottom / distance * distanceY), pointY
                - (bottom / distance * distanceX));
        path.lineTo(pointX - (bottom / distance * distanceY), pointY
                + (bottom / distance * distanceX));
        path.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }

    private Vector getOriginalCoordinate(Vector v) {
        v.minus(new Vector(mFocusX, mFocusY));
        v.multiply( 1 / mScaleFactor);
        v.add(new Vector(mFocusX, mFocusY));
        v.minus(new Vector(mPosX, mPosY));
        return v;
    }

    private class SimpleScaleListenerImpl extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            //
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 2.0f));
            mFocusX = detector.getFocusX();
            mFocusY = detector.getFocusY();
            invalidate();
            return true;
        }
    }

    private class SimpleGestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!GameActivity.isRunning) {
                if (isVelocityArrowTouched) {
                    Vector v = new Vector(distanceX, distanceY);
                    v.multiply(1 / mScaleFactor);
                    GameActivity.playerPlanet.setSpeed(
                            GameActivity.playerPlanet.getSpeed().getMinus(v)
                    );
                } else if (isPlayerPlanetTouched) {
                    Vector v = new Vector(distanceX, distanceY);
                    v.multiply(1 / mScaleFactor);
                    GameActivity.playerPlanet.setPosition(
                            GameActivity.playerPlanet.getPosition().getMinus(v)
                    );
                    invalidate();
                } else if (e2.getPointerCount() == 2) {
                    mPosX -= distanceX;
                    mPosY -= distanceY;
                    invalidate();
                }
            }
            return true;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            Planet p = GameActivity.playerPlanet;
            if (GameActivity.isRunning) {
                p.setExtraSpeed((e.getX() - mPosX) / mScaleFactor, (e.getY() - mPosY) / mScaleFactor);
            }
            Vector vv = new Vector(e.getX(), e.getY());
            Vector v = getOriginalCoordinate(vv);

            //Vector v = new Vector((e.getX() / mScaleFactor) - mPosX, (e.getY() / mScaleFactor) - mPosY);
            Vector position = new Vector(p.getPosition());
            position.add(p.getSpeed());
            isVelocityArrowTouched = false;
            isPlayerPlanetTouched = false;
            if (v.distance(position) < 40) {
                isVelocityArrowTouched = true;
            } else if (v.distance(p.getPosition()) < Math.sqrt(p.getMass()) + 20) {
                isPlayerPlanetTouched = true;
            }
            return true;
        }
    }
}