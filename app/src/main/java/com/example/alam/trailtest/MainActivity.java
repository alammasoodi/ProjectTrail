package com.example.alam.trailtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {
    protected boolean isDrawing = false;
    Chronometer mChronometer;
    /**
     * Indicates if the drawing is ended
     */
    protected boolean isDrawingEnded = false;

    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ll =(LinearLayout) findViewById(R.id.main_layout);
        mChronometer=(Chronometer) findViewById(R.id.chronometer);

        drawingView = new DrawingView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        ll.addView(drawingView);


    }

    public class DrawingView extends View {
        public static final float TOUCH_TOLERANCE = 4;
        public static final float TOUCH_STROKE_WIDTH = 5;
        protected Paint mPaintFinal;

        protected boolean isDrawing = false;
        protected boolean isContinuedLine = false;
        protected boolean isNearToCircle = false;

        protected boolean isDrawingEnded = false;
        boolean isLastTouch = false;
        boolean startingTime = false;
        long start, finish, timeTaken;
        protected float mStartX;
        protected float mStartY;
        int position = 0;
        protected float mx;
        protected float my;
        protected Path mPath;
        protected Paint mPaint, rePaint, transparentPaint, linePaint;
        protected Bitmap mBitmap;
        private ArrayList<Float> xPos = new ArrayList<Float>();
        private ArrayList<Float> yPos = new ArrayList<Float>();
        private ArrayList<String> labels = new ArrayList<String>();
        protected Canvas mCanvas, overCanvas;
        float radius = 50;


        public DrawingView(Context context) {
            super(context);
            init();

        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        protected void init() {
            xPos.add((float)800);
            xPos.add((float)500);
            xPos.add((float)200);
            xPos.add((float)400);
            xPos.add((float)100);
            xPos.add((float)300);
            xPos.add((float)100);
            xPos.add((float)500);
            xPos.add((float)400);
            xPos.add((float)600);
            xPos.add((float)800);
            xPos.add((float)700);
            xPos.add((float)900);
            yPos.add((float)1550);
            yPos.add((float)1450);
            yPos.add((float)1550);
            yPos.add((float)1000);
            yPos.add((float)700);
            yPos.add((float)400);
            yPos.add((float)200);
            yPos.add((float)150);
            yPos.add((float)600);
            yPos.add((float)500);
            yPos.add((float)100);
            yPos.add((float)1250);
            yPos.add((float)1100);
            labels.add("1");
            labels.add("A");
            labels.add("2");
            labels.add("B");
            labels.add("3");
            labels.add("C");
            labels.add("4");
            labels.add("D");
            labels.add("5");
            labels.add("E");
            labels.add("6");
            labels.add("F");
            labels.add("7");
            mPath = new Path();
            mPaint = new Paint(Paint.DITHER_FLAG);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(5);

            linePaint = new Paint(Paint.DITHER_FLAG);
            linePaint.setAntiAlias(true);
            linePaint.setDither(true);
            linePaint.setColor(getContext().getResources().getColor(R.color.blue_dark));
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeJoin(Paint.Join.ROUND);
            linePaint.setStrokeCap(Paint.Cap.ROUND);
            linePaint.setStrokeWidth(15);

            rePaint = new Paint(Paint.DITHER_FLAG);
            rePaint.setAntiAlias(true);
            rePaint.setDither(true);
            rePaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            rePaint.setStyle(Paint.Style.FILL);
            rePaint.setStrokeJoin(Paint.Join.ROUND);
            rePaint.setStrokeCap(Paint.Cap.ROUND);
            rePaint.setStrokeWidth(30);
            rePaint.setTextSize(60);

            transparentPaint = new Paint(Paint.DITHER_FLAG);
            transparentPaint.setAntiAlias(true);
            transparentPaint.setDither(true);
            transparentPaint.setColor(Color.TRANSPARENT);


        }
//touch method
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(!startingTime){
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                start = System.currentTimeMillis();
                startingTime = true;
            }
            //Retrieve the point
            mx = event.getX();
            my = event.getY();

            if(position<xPos.size()) {
                if(position == xPos.size()-1){
                    mChronometer.stop();
                    if(!isLastTouch) {
                        finish = System.currentTimeMillis();
                        timeTaken = finish - start;
                        isLastTouch = true;
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    alertDialogBuilder.setTitle("Your Time to complete the Test");
                    alertDialogBuilder.setMessage(String.valueOf(timeTaken/1000)+" seconds");

                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this,Result.class);
                            startActivity(intent);

                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show().getWindow();
                }
                float dx = Math.abs(mx - xPos.get(position));
                float dy = Math.abs(my - yPos.get(position));
                float R = radius;
                if (!isContinuedLine) {
                    if (!isDrawing) {
                        if (checkDistance(dx, dy, R)) {
                            try {

                                onTouchEventLine(event);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }
                    } else {

                        try {
                            onTouchEventLine(event);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();

                        }
                    }
                }
                else {
                    try {
                        onTouchEventLine(event);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();

                    }
                }
            }
            //}
            return true;
        }
        private void onTouchEventLine(MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
                    mStartX = mx;
                    mStartY = my;
                    mCanvas.drawCircle(xPos.get(position),yPos.get(position),60,mPaint);

                    //mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
                   // mCanvas.drawCircle(xPos.get(position),yPos.get(position),radius,rePaint);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(position<xPos.size()) {
                        if (position == xPos.size()-1)
                            break;
                        float dx = Math.abs(mx - xPos.get(position + 1));
                        float dy = Math.abs(my - yPos.get(position + 1));
                        float R = radius;
                        if(checkDistance(dx,dy,R)){
                            isContinuedLine = true;
                            mCanvas.drawCircle(xPos.get(position+1),yPos.get(position+1),60,mPaint);
                            mCanvas.drawLine(mStartX, mStartY, mx, my, linePaint);
                            mStartX = mx;
                            mStartY = my;
                            position++;
                            isNearToCircle = true;
                        }




                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    isDrawing = false;

                    if(isContinuedLine) {
                        isContinuedLine = false;
                    }
                    else {
                       position++;

                    }
                    if(isNearToCircle){
                        isNearToCircle = false;
                    }
                    else {
                        position--;
                        mStartX = mx;
                        mStartY = my;

                        mCanvas.drawLine(mStartX, mStartY, mx, my, transparentPaint);

                    }

//
                    // mCanvas.drawLine(mStartX, mStartY, mx, my, mPaintFinal);
                    invalidate();
                    break;

            }
        }

        private void onDrawLine(Canvas canvas) {

            float dx = Math.abs(mx - mStartX);
            float dy = Math.abs(my - mStartY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                canvas.drawLine(mStartX, mStartY, mx, my, linePaint);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            for(int i=0;i<xPos.size();i++){
                canvas.drawCircle(xPos.get(i), yPos.get(i), radius, mPaint);
                canvas.drawText(labels.get(i),xPos.get(i)-(int)(rePaint.measureText(labels.get(i))/2) ,yPos.get(i)-((rePaint.descent()+rePaint.ascent())/2),rePaint);


            }
//
            onDrawLine(canvas);

            //draw your element
        }


        boolean checkDistance(float dx,float dy,float R)
        {
            if(dx>R)
            {
                return false;//outside
            }
            else if(dy>R)
            {
                return false;//
            }
            else
            {
                return true;
            }
        }
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            setMeasuredDimension(800, 800);
//        }
    }

}

