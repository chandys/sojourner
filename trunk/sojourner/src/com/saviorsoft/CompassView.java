package com.saviorsoft;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


class CompassView extends SurfaceView implements SurfaceHolder.Callback {
    class CompassThread extends Thread {
    	public int threadDelay = 100;
        private Paint mTextPaintLargeBold = new Paint();
        private Paint mTextPaintSmall = new Paint();
        private Paint mMagentaPaint = new Paint();
        private Paint mWhiteOverlayPaint = new Paint();
        private Canvas mCanvas;
        private RectF mDirectionBoxRect;
        private Paint mDirectionBoxPaint;
        private String mTextString = new String();
        private Bitmap mCompBackground;
        private RectF mWaypointBoxRect;
        private Paint mWaypointBoxPaint;
        private MyAppContext mAppContext;

        

        private Bitmap mBackgroundImage;
        private Bitmap mRoseImage;
        private Bitmap mTicsImage;
        private Handler mHandler;
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
            mAppContext = (MyAppContext) context.getApplicationContext();

            Resources res = context.getResources();
            mBackgroundImage = BitmapFactory.decodeResource(res,
                  R.drawable.stork320x480);
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
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                		doDraw(c);
                    }
                }
                finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                        try {
							sleep(threadDelay);
						} 
                        catch (InterruptedException e) {
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
                mBackgroundImage = Bitmap.createScaledBitmap(
                        mBackgroundImage, width, height, true);
                
                buildBackground();
                
            }
        }


        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
            int height = canvas.getHeight();
            int width = canvas.getWidth();
            
            canvas.drawBitmap(mCompBackground, 0, 0, null);
            
            float cx = width/2;
            float cy = height/2 + 46;

            //draw waypoint 
            float transpoint = mAppContext.mWaypointAngle - mAzimuth;
            int rad = (mRoseImage.getWidth()/2) + 10;
            float x2 = (float) (cx - ((rad+15) * Math.sin(Math.toRadians(transpoint))));
            float y2 = (float) (cy - ((rad+15) * Math.cos(Math.toRadians(transpoint))));
            canvas.drawCircle(x2, y2, 5, mMagentaPaint);

            
            canvas.save();
            canvas.rotate(-mAzimuth,cx, cy);
            canvas.drawText("^", cx-8, cy-rad+40, mDirectionBoxPaint);
            
            //draw waypoint
            //canvas.drawCircle(x2, y2, 5, mMagentaPaint);
            
//            float rx = cx - (mRoseImage.getWidth()/2);
//            float ry = cy - (mRoseImage.getHeight()/2);
//            canvas.drawBitmap(mRoseImage, rx, ry, null);
            canvas.restore();
            
            
            mCanvas = canvas;
            printDirection();
            
        }

        
	   private void printDirection() {
	      if (mAzimuth < 22)
	         mTextString = "N";
	      else if (mAzimuth >= 22 && mAzimuth < 67)
	    	  mTextString = "NE";
	      else if (mAzimuth >= 67 && mAzimuth < 112)
	    	  mTextString = "E";
	      else if (mAzimuth >= 112 && mAzimuth < 157)
	    	  mTextString = "SE";
	      else if (mAzimuth >= 157 && mAzimuth < 202)
	    	  mTextString = "S";
	      else if (mAzimuth >= 202 && mAzimuth < 247)
	    	  mTextString = "SW";
	      else if (mAzimuth >= 247 && mAzimuth < 292)
	    	  mTextString = "W";
	      else if (mAzimuth >= 292 && mAzimuth < 337)
	    	  mTextString = "NW";
	      else if (mAzimuth >= 337)
	    	  mTextString = "N";
	      else
	    	  mTextString = " ";

	      //mCanvas.drawRoundRect(mDirectionBoxRect, 10f, 10f, mDirectionBoxPaint);
	      mCanvas.drawText(mTextString, 15, 40, mTextPaintLargeBold);
	      	      
	      //draw bearings text stats
	      mTextString = String.format("Azimuth:    %.2f", mAzimuth);
	      mCanvas.drawText(mTextString, 15, 60, mTextPaintSmall);
	      mTextString = String.format("Pitch:    %.2f", mPitch, mRoll);
	      mCanvas.drawText(mTextString, 15, 72, mTextPaintSmall);
	      mTextString = String.format("Roll:    %.2f", mRoll);
	      mCanvas.drawText(mTextString, 15, 84, mTextPaintSmall);	      

	      //waypoint text stats
	      mCanvas.drawText("Waypoint", mWaypointBoxRect.left+10, 
	    		  mWaypointBoxRect.top+27, mTextPaintLargeBold);
	      mTextString = String.format("Direction:    %.2f", mAppContext.mWaypointAngle);
	      mCanvas.drawText(mTextString, mWaypointBoxRect.left+10, 
	    		  mWaypointBoxRect.top+47, mTextPaintSmall);
	      mTextString = String.format("Distance:    %.2f ft.", mAppContext.mWaypointDistance);
	      mCanvas.drawText(mTextString, mWaypointBoxRect.left+10, 
	    		  mWaypointBoxRect.top+59, mTextPaintSmall);
	   }
	   
	   
	   private void buildBackground()
	   {
		   mCompBackground = BitmapFactory.decodeResource(getResources(),
	                  R.drawable.stork320x480);
		   mCompBackground = Bitmap.createBitmap(mCompBackground, 0, 0, 
				   mCanvasWidth, mCanvasHeight); 
           //mCompBackground = Bitmap.createBitmap(mBackgroundImage);
           Canvas c = new Canvas(mCompBackground);
           

           int height = mCanvasHeight;
           int width = mCanvasWidth;
           
           
           //initial drawing items
           mRoseImage = BitmapFactory.decodeResource(getResources(),
                   R.drawable.compass_rose_browns);
           mTicsImage = BitmapFactory.decodeResource(getResources(),
                   R.drawable.modern_nautical_compass_rose);
           
           //text color and size
           mTextPaintSmall.setTextSize(12);
           mTextPaintSmall.setColor(Color.BLACK);
           mTextPaintSmall.setAntiAlias(true);
           mTextPaintSmall.setStrokeWidth(2);

           mTextPaintLargeBold.setTextSize(24);
           mTextPaintLargeBold.setColor(Color.BLACK);
           mTextPaintLargeBold.setAntiAlias(true);
           mTextPaintLargeBold.setTypeface(Typeface.DEFAULT_BOLD);
           
           Paint mWhitePaint = new Paint();
           mWhitePaint.setStyle(Style.FILL);
           mWhitePaint.setARGB(10, 255, 255, 255);
           
           //color for white overlay text
           mWhiteOverlayPaint.setColor(Color.WHITE);
           mWhiteOverlayPaint.setAntiAlias(true);
           
           //bearings box
           mDirectionBoxRect = new RectF(5, 13, 140, 100);
           mDirectionBoxPaint = new Paint();
           mDirectionBoxPaint.setColor(Color.GREEN);
           mDirectionBoxPaint.setAlpha(100);
           mDirectionBoxPaint.setStyle(Style.STROKE);
           mDirectionBoxPaint.setStrokeWidth(2);
           mDirectionBoxPaint.setTextSize(30);
 	       c.drawRoundRect(mDirectionBoxRect, 10f, 10f, mDirectionBoxPaint);
 	       //c.drawRoundRect(mDirectionBoxRect, 10f, 10f, mWhitePaint);

           //waypoint box
           mWaypointBoxRect = new RectF(180, 13, 315, 100);
           mWaypointBoxPaint = new Paint();
           mWaypointBoxPaint.setColor(Color.MAGENTA);
           mWaypointBoxPaint.setAlpha(100);
           mWaypointBoxPaint.setStyle(Style.STROKE);
           mWaypointBoxPaint.setStrokeWidth(2);
           mWaypointBoxPaint.setTextSize(30);
           c.drawRoundRect(mWaypointBoxRect, 10f, 10f, mWaypointBoxPaint);
 	       //c.drawRoundRect(mWaypointBoxRect, 10f, 10f, mWhitePaint);

           
           
           mMagentaPaint.setAlpha(100);
           mMagentaPaint.setColor(Color.MAGENTA);
           mMagentaPaint.setTextSize(30);

           
           
           float cx = width/2;
           float cy = height/2 + 46;
           
           int rad = (mRoseImage.getWidth()/2) + 10;

           //draw ring around compass
           //Paint p = new Paint();
           //p.setARGB(100, 0, 0, 0); //add gray shawdow
           //c.drawCircle(cx, cy, rad + 10, p);
           
           //draw bearing lines
           c.drawLine(cx, cy, cx - rad - 20, cy, mTextPaintSmall);
           c.drawLine(cx, cy, cx + rad + 20, cy, mTextPaintSmall);
           c.drawLine(cx, cy, cx, cy - rad - 30, mTextPaintSmall);
           c.drawLine(cx, cy, cx, cy + rad + 20, mTextPaintSmall);
           //arrow
           c.drawLine(cx, cy-rad-30, cx-7, cy-rad-23, mTextPaintSmall);
           c.drawLine(cx, cy-rad-30, cx+7, cy-rad-23, mTextPaintSmall);

           float tx = cx - (mTicsImage.getWidth()/2);
           float ty = cy - (mTicsImage.getHeight()/2);
           c.drawBitmap(mTicsImage, tx, ty, null);
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
    public float mWayPointAngle = 90;
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
