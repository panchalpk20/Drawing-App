package com.example.root.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawPadView extends View {
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    private Paint paint;
    private Paint BitmapPaint;
    private Path path;
    private int height;
    private int width;
    /** Last saved X-coordinate */
    private float pX;
    /** Last saved Y-coordinate*/
    private float pY;

    /** Initial color */
    private int paintColor = Color.RED;

    /** Paint Point size */
    private static int paintWidth = 3;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        height = h;
        width = w;
        init();
    }

    private void init(){
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cacheBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        BitmapPaint = new Paint();
        updatePaint();
    }

    private void updatePaint(){
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(paintWidth);
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPadView(Context context){
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                pX = event.getX();
                pY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(pX, pY, event.getX(), event.getY());
                pX = event.getX();
                pY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();

        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Canvas canvas1 = canvas;
        BitmapPaint = new Paint();
        canvas.drawBitmap(cacheBitmap, 0,0, BitmapPaint);
        canvas.drawPath(path, paint);

    }


    public void setColor(int color){
        paintColor = color;
        updatePaint();
    }


    public void setPaintWidth(int width){
        paintWidth = width;
        updatePaint();
    }




    /** clear your drawing*/
    public void clearScreen(){
   //     if(canvas != null){
       /*     Paint backPaint = new Paint();
        *   backPaint.setColor(Color.WHITE);
        *   canvas.drawRect(new Rect(0, 0, width, height), backPaint);
        *   cacheCanvas.drawRect(new Rect(0, 0, width, height), backPaint);
        */


         //   canvas.drawRect(new Rect(0, 0, width, height), paint);
      // canvas.drawColor(Color.WHITE);

            cacheCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

    //    }
        invalidate();
    }

}