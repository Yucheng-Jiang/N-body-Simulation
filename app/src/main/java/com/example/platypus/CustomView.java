package com.example.platypus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import androidx.annotation.Nullable;

public class CustomView extends View {
    private float scale = 1;
    private Vector position = new Vector();
    private Vector mPreviousTouch = new Vector();

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
        canvas.scale(scale, scale);
        canvas.translate((float) position.getX(), (float) position.getY());
        for (int i = 0; i < Planet.planetList.size(); i++) {
            Planet p = Planet.planetList.get(i);
            Paint paint = new Paint();
            paint.setColor(p.getColor());

            canvas.drawPath(p.path, paint);
            canvas.drawCircle(
                    (float) p.getPosition().getX(),
                    (float) p.getPosition().getY(),
                    (float) Math.pow(p.getMass(), 0.5),
                    paint
            );
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousTouch.set(event.getX(), event.getY());
                //Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                Vector currentTouch = new Vector(event.getX(), event.getY());
                currentTouch.minus(mPreviousTouch);
                position.add(currentTouch.getX(), currentTouch.getY());
                Toast.makeText(getContext(), "move", Toast.LENGTH_SHORT).show();
                break;
        }
        mPreviousTouch.set(event.getX(), event.getY());
        return true;
    }

    public void setScale(float x) {
        scale = x;
    }
    public void setPosition(double x, double y) {
        position.set(x, y);
    }
}
