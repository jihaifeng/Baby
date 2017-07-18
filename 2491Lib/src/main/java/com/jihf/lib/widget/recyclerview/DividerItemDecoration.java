package com.jihf.lib.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-21 14:37
 * Mail：jihaifeng@raiyi.com
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

  public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

  private Drawable mDivider;
  private int dividerSize = 1;

  private int mOrientation;

  public DividerItemDecoration(Context context, int orientation) {
    this(context, Color.TRANSPARENT, orientation);
  }

  public DividerItemDecoration(Context context, int color, int orientation) {
    this(context, new ColorDrawable(color), orientation);
  }

  public DividerItemDecoration(Context context, Drawable mDivider, int orientation) {
    this.mDivider = mDivider;
    setOrientation(orientation);
  }

  public void setDividerSize(int size) {
    dividerSize = size;
  }

  public void setOrientation(int orientation) {
    if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
      throw new IllegalArgumentException("invalid orientation");
    }
    mOrientation = orientation;
  }

  @Override public void onDraw(Canvas c, RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  public void drawVertical(Canvas c, RecyclerView parent) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int top = child.getBottom() + params.bottomMargin;
      final int bottom = top + dividerSize;
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  public void drawHorizontal(Canvas c, RecyclerView parent) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getHeight() - parent.getPaddingBottom();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int left = child.getRight() + params.rightMargin;
      final int right = left + dividerSize;
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  @Override public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      outRect.set(0, 0, 0, dividerSize);
    } else {
      outRect.set(0, 0, dividerSize, 0);
    }
  }
}