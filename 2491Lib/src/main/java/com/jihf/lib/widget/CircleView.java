package com.jihf.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
  private int mColor = Color.TRANSPARENT;
  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public CircleView(Context context) {
    super(context);
    init();
  }

  public CircleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  /**
   * 初始化画笔的颜色
   */
  private void init() {
    mPaint.setColor(mColor);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int width = getWidth();
    int height = getHeight();
    int radius = Math.min(width, height) / 2;
    canvas.drawCircle(width / 2, height / 2, radius, mPaint);
  }

  @Override public void setBackgroundColor(@ColorInt int color) {
    mPaint.setColor(color);
    invalidate();
  }
}