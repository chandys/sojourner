package com.saviorsoft;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;


class CompassView extends SurfaceView implements SurfaceHolder.Callback {
    class CompassThread extends Thread {
    	public int threadDelay = 100;
        private Paint mTextPaintLargeBold = new Paint();
        private Paint mTextPaintSmall = new Paint();
        private Canvas mCanvas;
        private RectF mTextBoxRect;
        private Paint mTextBoxPaint;



        

        private Bitmap mBackgroundImage;
        private Bitmap mRoseImage;
        private Handler mHandler;
//        private long mLastTime;
//        private int mMode;
        private boolean mRun = false;
        private SurfaceHolder mSurfaceHolder;
        private int mCanvasHeight = 1;
        private int mCanvasWidth = 1;

        /*
         * State-tracking constants
         */
		private static final String KEY_WAYPOINTANGLE = "0";
		private static final String KEY_WAYPOINTDISTANCE = "0";

        

		public CompassThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;

            Resources res = context.getResources();
            mBackgroundImage = BitmapFactory.decodeResource(res,
                  R.drawable.stainless);
            mRoseImage = BitmapFactory.decodeResource(res,
                    R.drawable.compass_rose_browns);
            
            //text color and size
            mTextPaintSmall.setTextSize(12);
            mTextPaintSmall.setColor(Color.BLACK);
            mTextPaintSmall.setAntiAlias(true);

            mTextPaintLargeBold.setTextSize(24);
            mTextPaintLargeBold.setColor(Color.BLACK);
            mTextPaintLargeBold.setAntiAlias(true);
            mTextPaintLargeBold.setTypeface(Typeface.DEFAULT_BOLD);
            
            mTextBoxRect = new RectF(5,13, 140, 120);
            mTextBoxPaint = new Paint();
            mTextBoxPaint.setColor(Color.GREEN);
            mTextBoxPaint.setAlpha(100);

        }


        /**
         * Restores state from the indicated Bundle. Typically called when
         * the Activity is being restored after having been previously
         * destroyed.
         * 
         * @param savedState Bundle containing the compass state
         */
        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                mWayPointAngle = savedState.getFloat(KEY_WAYPOINTANGLE);
                mWayPointAngle = savedState.getFloat(KEY_WAYPOINTDISTANCE);
            }
        }

        @Override
        public void run() {
            while (mRun) {
            //while (true) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                        try {
							sleep(threadDelay);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    }
                }                
            }
        }

        /**
         * Dump game state to the provided Bundle. Typically called when the
         * Activity is being suspended.
         * 
         * @return Bundle with this view's state
         */
        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
                if (map != null) {
                    map.putFloat(KEY_WAYPOINTANGLE, Float.valueOf(mWayPointAngle));
                    map.putFloat(KEY_WAYPOINTDISTANCE, Float.valueOf(mWayPointDistance));
                }
            }
            return map;
        }

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }


        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;

                // don't forget to resize the background image
                mBackgroundImage = mBackgroundImage.createScaledBitmap(
                        mBackgroundImage, width, height, true);
            }
        }


        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {

        	canvas.drawBitmap(mBackgroundImage, 0, 0, null);
            
            int height = canvas.getHeight();
            int width = canvas.getWidth();
            
            float cx = width/2;
            float cy = height/2 + 46;
            
            
            float rx = cx - (mRoseImage.getWidth()/2);
            float ry = cy - (mRoseImage.getHeight()/2);
            
            int rad = (mRoseImage.getWidth()/2) + 10;

            
            float x2 = (float) (cx - (rad * Math.sin(Math.toRadians(mAzimuth))));
            float y2 = (float) (cy - (rad * Math.cos(Math.toRadians(mAzimuth))));
            Paint paint2 = new Paint();
            paint2.setAlpha(100);
            paint2.setColor(Color.MAGENTA);
            canvas.drawCircle(x2, y2, 5, paint2);

            //draw ring around compass
            Paint p = new Paint();
            p.setARGB(50, 0, 0, 0);
            canvas.drawCircle(cx, cy, rad + 10, p);
            
            //draw bearing lines
            canvas.drawLine(cx, cy, cx - rad - 10, cy, mTextPaintSmall);
            canvas.drawLine(cx, cy, cx + rad + 10, cy, mTextPaintSmall);
            canvas.drawLine(cx, cy, cx, cy - rad - 30, mTextPaintSmall);
            canvas.drawLine(cx, cy, cx, cy + rad + 10, mTextPaintSmall);
            //arrow
            canvas.drawLine(cx, cy-rad-30, cx-7, cy-rad-23, mTextPaintSmall);
            canvas.drawLine(cx, cy-rad-30, cx+7, cy-rad-23, mTextPaintSmall);
            
            canvas.save();
            canvas.rotate(-mAzimuth,cx, cy);
            canvas.drawBitmap(mRoseImage, rx, ry, null);
            canvas.restore();
            
            mCanvas = canvas;
            printDirection();
            
        }

        
	   private void printDirection() {
	      String out = new String("");
	      if (mAzimuth < 22)
	         out = "N";
	      else if (mAzimuth >= 22 && mAzimuth < 67)
	    	 out = "NE";
	      else if (mAzimuth >= 67 && mAzimuth < 112)
	    	 out = "E";
	      else if (mAzimuth >= 112 && mAzimuth < 157)
	    	 out = "SE";
	      else if (mAzimuth >= 157 && mAzimuth < 202)
	    	 out = "S";
	      else if (mAzimuth >= 202 && mAzimuth < 247)
	    	 out = "SW";
	      else if (mAzimuth >= 247 && mAzimuth < 292)
	    	 out = "W";
	      else if (mAzimuth >= 292 && mAzimuth < 337)
	    	 out = "NW";
	      else if (mAzimuth >= 337)
	    	 out = "N";
	      else
	       	 out = "";

	      mCanvas.drawRoundRect(mTextBoxRect, 10f, 10f, mTextBoxPaint);
	      mCanvas.drawText(out, 15, 40, mTextPaintLargeBold);
	      
	      
	      //draw text stats
	      out = String.format("Azimuth:    %.2f", mAzimuth);
	      mCanvas.drawText(out, 15, 60, mTextPaintSmall);
	      out = String.format("Pitch:    %.2f", mPitch, mRoll);
	      mCanvas.drawText(out, 15, 80, mTextPaintSmall);
	      out = String.format("Roll:    %.2f", mRoll);
	      mCanvas.drawText(out, 15, 100, mTextPaintSmall);	      
	   }

	   public void threadShutdown(){
	        boolean retry = true;
	        setRunning(false);
	        while (retry) {
	            try {
	                thread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {
	            }
	        }
	   }
        
    }//end of compass thread
    
    
    //$$$$$$$$$$$$$$$$$$$$$$$$$ start of CompassView $$$$$$$$$$$$$$$$$$$
    public float mLastCompassAngle = 0;
    public float mWayPointAngle = 240;
    public float mWayPointDistance = 123;
    public float mAzimuth = 0;
    public float mPitch = 0;
    public float mRoll = 0;
    private CompassThread thread;
    private Context mContext;

    
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mContext = context;
        setFocusable(true); // make sure we get key events
    }

    public CompassThread getThread() {
        return thread;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        if (!hasWindowFocus){
//        	if(thread != null){
//        		thread.threadShutdown();
//        		thread = null;
//        	}
//        }
//        else{
//        	if(thread == null){
//                thread = new CompassThread(getHolder(), mContext, new Handler() {
//                    @Override
//                    public void handleMessage(Message m) {
//                        //mStatusText.setVisibility(m.getData().getInt("viz"));
//                        //mStatusText.setText(m.getData().getString("text"));
//                    }
//                });
//        		thread.setRunning(true);
//        		thread.start(); 
//        	}
//        }
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
    	if(thread == null){
            thread = new CompassThread(holder, mContext, new Handler() {
                @Override
                public void handleMessage(Message m) {
                    //mStatusText.setVisibility(m.getData().getInt("viz"));
                    //mStatusText.setText(m.getData().getString("text"));
                }
            });
            thread.setRunning(true);
            thread.start();
    	}
        
    }

    
    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
    	if(thread != null){
    		thread.threadShutdown();
    		thread = null;
    	}
    }
}//End of CompassView
