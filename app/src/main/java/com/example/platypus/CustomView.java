package com.example.platypus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    private float scaleX = 1;
    private float scaleY = 1;

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
        canvas.scale(scaleX, scaleY);
        for (int i = 0; i < Planet.planetList.size(); i++) {
            Planet p = Planet.planetList.get(i);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawCircle(
                    (float) p.getPosition().getX(),
                    (float) p.getPosition().getY(),
                    (float) Math.pow(p.getMass(), 0.5),
                    paint
            );
        }
        System.out.println(scaleX);
        invalidate();
    }

    public void setScale(float x) {
        scaleX = x;
        scaleY = x;
    }
}
