package com.saviorsoft;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Rose extends ImageView {
  Paint paint;
  int direction = 0;

  public Rose(Context context) {
    super(context);

    paint = new Paint();
    paint.setColor(Color.WHITE);
    paint.setStrokeWidth(2);
    paint.setStyle(Style.STROKE);

    this.setImageResource(R.drawable.compass_rose);
  }


  public Rose(Context context, AttributeSet attrs)
  {
      super(context, attrs);
      // TODO Auto-generated constructor stub
  }


  public Rose(Context context, AttributeSet attrs, int defStyle)
  {
      super(context, attrs, defStyle);
      // TODO Auto-generated constructor stub
  }
  
  
  
  @Override
  public void onDraw(Canvas canvas) {
    int height = this.getHeight();
    int width = this.getWidth();
    
    float cx = width/2;
    float cy = height/2;
    int compangle = 350;
    int rad = 90;

    Paint paint = new Paint();
    paint.setAlpha(150);
    paint.setColor(Color.GREEN);
    canvas.drawCircle(cx, cy, rad, paint);
    
    float x2 = (float) (cx + (rad * Math.sin(Math.toRadians(compangle))));
    float y2 = (float) (cy - (rad * Math.cos(Math.toRadians(compangle))));
    Paint paint2 = new Paint();
    paint2.setAlpha(150);
    paint2.setColor(Color.MAGENTA);
    canvas.drawCircle(x2, y2, 5, paint2);

        
    super.onDraw(canvas);
  }

  public void setDirection(int direction) {
    this.direction = direction;
    this.invalidate();
  }

}