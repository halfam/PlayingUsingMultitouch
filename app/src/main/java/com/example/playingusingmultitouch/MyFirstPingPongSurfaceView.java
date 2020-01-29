package com.example.playingusingmultitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyFirstPingPongSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static int radius = 20;
    private static int ballColor = Color.BLUE;
    public float meitatX = 100;
    boolean clicPartEsquerre;
    Paint table = new Paint();
    Paint ball = new Paint();
    Paint paleta1 = new Paint();
    Paint paleta2 = new Paint();
    private PingPongThread pingPongThread = null;
    private int x;
    private int y;
    private int xDirection = 10;
    private int yDirection = 10;
    private float distanciaX = 100, distanciaY = 10;
    private float ample = 10, alt = 150;
    private float paleta1X, paleta1Y, paleta2X, paleta2Y;
    private float mLastTouchX, mLastTouchY;

    public MyFirstPingPongSurfaceView(Context ctx) {
        super(ctx);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        x = getWidth() / 2;
        y = getHeight() / 2;
        paleta1X = distanciaX;
        paleta1Y = distanciaY;
        paleta2X = getWidth() - distanciaX;
        paleta2Y = distanciaY;
        meitatX = getWidth() / 2;
        if (pingPongThread != null) {
            return;
        }
        pingPongThread = new PingPongThread(getHolder());
        pingPongThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void stopThread() {
        if (pingPongThread != null) {
            pingPongThread.stop = true;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                clicPartEsquerre = mLastTouchX < meitatX;
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                if (clicPartEsquerre) {
                    paleta1Y += dy;
                    if (paleta1Y < 0)
                        paleta1Y = 0;
                    if (paleta1Y + alt > getHeight())
                        paleta1Y = getHeight() - alt;

                } else {
                    paleta2Y += dy;
                    if (paleta2Y < 0)
                        paleta2Y = 0;
                    if (paleta2Y + alt > getHeight())
                        paleta2Y = getHeight() - alt;

                }
                mLastTouchX = x;
                mLastTouchY = y;
                invalidate();
                break;
        }
        return true;
    }

    public void newDraw(Canvas canvas) {
        //the table
        table.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), table);
        //the ball
        ball.setColor(ballColor);
        canvas.drawCircle(x, y, radius, ball);
        //the palettes
        paleta1.setColor(Color.RED);
        paleta2.setColor(Color.BLUE);
        canvas.drawRect(paleta1X, paleta1Y, paleta1X + ample, paleta1Y + alt, paleta1);
        canvas.drawRect(paleta2X, paleta2Y, paleta2X + ample, paleta2Y + alt, paleta2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class PingPongThread extends Thread {
        public boolean stop = false;
        private SurfaceHolder surfaceHolder;

        public PingPongThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            while (!stop) {
                x += xDirection;
                y += yDirection;
                if (x + radius < 0) {
                    xDirection = -xDirection;
                    x = (int) (2 * distanciaX);
                }
                if (x > getWidth() - radius) {
                    xDirection = -xDirection;
                    x = (int) (getWidth() - 2 * distanciaX);
                }
                if (y < 0) {
                    yDirection = -yDirection;
                    y = radius;
                }
                if (y > getHeight() - radius) {
                    yDirection = -yDirection;
                    y = getHeight() - radius - 1;
                }
                //Left Palette
                if (y + radius > paleta1Y && y + radius < paleta2Y + alt) {
                    if (x + radius > paleta1X && x - radius < paleta1X + ample) {
                        xDirection = -xDirection;
                        x += 10;
                    }
                }
                //Right palette
                if (y + radius > paleta2Y && y + radius < paleta2Y + alt) {
                    if (x + radius > paleta2X && x - radius < paleta2X + ample) {
                        xDirection = -xDirection;
                        x -= 10;
                    }
                }
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        newDraw(c);
                    }
                } finally {
                    if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                }
            }

        }
    }
}
