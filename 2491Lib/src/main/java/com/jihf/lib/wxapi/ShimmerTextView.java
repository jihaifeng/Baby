package com.jihf.lib.wxapi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.jihf.lib.R;

/**
 * 闪闪发光字体效果
 *
 * @author raiyi-suzhou
 */
public class ShimmerTextView extends AppCompatTextView {

  private LinearGradient mLinearGradient;
  private Matrix mGradientMatrix;
  private Paint mPaint;
  private int mViewWidth = 0;
  private int mTranslate = 0;

  private boolean mAnimating = true;

  public ShimmerTextView(Context context) {
    super(context);
  }

  public ShimmerTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public ShimmerTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mAnimating && mGradientMatrix != null) {
      mTranslate += mViewWidth / 10;
      if (mTranslate > 2 * mViewWidth) {
        mTranslate = mViewWidth / 10;
      }
      mGradientMatrix.setTranslate(mTranslate, 0);
      mLinearGradient.setLocalMatrix(mGradientMatrix);
      postInvalidateDelayed(70);
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (mViewWidth == 0) {
      mViewWidth = getMeasuredWidth();
      if (mViewWidth > 0) {
        mPaint = getPaint();
        mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0, new int[] {
            getResources().getColor(R.color.progress_dialog_text_color), getResources().getColor(R.color.white),
            getResources().getColor(R.color.progress_dialog_text_color)
        }, new float[] { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mGradientMatrix = new Matrix();
      }
    }
  }
}
