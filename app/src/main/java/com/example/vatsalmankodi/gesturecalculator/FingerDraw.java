package com.example.vatsalmankodi.gesturecalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class FingerDraw extends View {
    static public Paint paint=new Paint();
    private Path path=new Path();
    public FingerDraw(Context context) {
        super(context);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);
        this.setBackgroundColor(Color.WHITE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos=event.getX();
        float yPos=event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_UP:
                this.setDrawingCacheEnabled(true);
                MainActivity.viewCapture= Bitmap.createBitmap(this.getDrawingCache());
                this.setDrawingCacheEnabled(false);
              //  MainActivity.processImage();

                return false;
            default:return false;
        }
        invalidate();
        return true;
    }
}
