package com.example.mac.mygame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mac on 2017/8/12.
 */

public class GameView extends View{
    private int[] ballRes ={R.drawable.ball,R.drawable.ball1,R.drawable.ball2,
    R.drawable.ball3};
    private Bitmap[] bitmap=new Bitmap[ballRes.length];
    private Resources res;
    private Paint paintball;
    private int viewH,viewW;
    private boolean isInit;
    private Matrix matrix;
    private Timer timer;
    private LinkedList<BallTask> balls;
    private float ballh,ballw,dx,dy;

    //手勢偵測
    private GestureDetector gd;

    public GameView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.bg);
        context.getResources();
        res = context.getResources();
        paintball = new Paint();
        paintball.setAlpha(255);
        matrix = new Matrix();
        timer = new Timer();
        balls = new LinkedList<>();

        gd = new GestureDetector(context, new MyGD());
    }

    private void init(){
        isInit = true ;
        viewW = getWidth(); viewH = getHeight();
        ballw = viewW/8f ; ballh=viewH/8f;
        dx = viewW / 256f; dy = viewH / 256f;

        for (int i = 0;i<ballRes.length;i++) {
            bitmap[i] = BitmapFactory.decodeResource(res, ballRes[i]);
            matrix.reset();
            matrix.postScale(ballw / bitmap[i].getWidth(), ballh / bitmap[i].getHeight());
            bitmap[i] = Bitmap.createBitmap(bitmap[i], 0, 0,
                    bitmap[i].getWidth(), bitmap[i].getHeight(),
                    matrix, false);
        }

        timer.schedule(new Refresh(), 0, 60);
    }

        private class MyGD extends GestureDetector.SimpleOnGestureListener{
            @Override
            public boolean onDown(MotionEvent e) {
                //Log.i("test","onDown");
                return super.onDown(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float vX, float vY) {
                //Log.i("test","onFling"+vX +"x" + vY);
                if(Math.abs(vX)>Math.abs(vY)+1000){
                    if(vX>0){
                        Log.i("test","Right");
                    }else{
                        Log.i("test","left");
                    }

                }else if(Math.abs(vY)>Math.abs(vX)+1000){
                    if(vY>0){
                        Log.i("test","up");
                    }else{
                        Log.i("test","down");
                    }
                }
                return super.onFling(e1, e2, vX, vY);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i("test","onScroll");
                return true; //super.onScroll(e1, e2, distanceX, distanceY);

            }
        }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float ex = event.getX(),ey=event.getY();
//        BallTask ballTask = new BallTask(ex-ballw/2f,ey-ballh/2f,dx,dy);
//        timer.schedule(ballTask,500,30);
//        balls.add(ballTask);

        return gd.onTouchEvent(event); //false; //super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit) init();

        for(BallTask ballTask:balls) {
            canvas.drawBitmap(bitmap[ballTask.bmp], ballTask.ballX, ballTask.ballY, paintball);
        }
    }
    //球動
    private class BallTask extends TimerTask {
        float ballX, ballY, dx, dy;
        int bmp;
        BallTask(float ballX,float ballY,float dx,float dy){
            this.ballX=ballX;this.ballY=ballY;this.dx=dx;this.dy=dy;
            bmp=(int)(Math.random()*4);
        }

        @Override
        public void run() {
            if(ballX<0|| ballX+ballw>viewW){
                dx*=-1;
            }
            if(ballY<0|| ballY+ballh>viewH){
                dy*=-1;
            }
            ballX += dx;
            ballY += dy;

        }
    }
    //畫面更新
    private class Refresh extends TimerTask{

        @Override
        public void run() {
            postInvalidate();
        }
    }
}
