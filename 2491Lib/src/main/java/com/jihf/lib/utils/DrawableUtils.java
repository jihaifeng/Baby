package com.jihf.lib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Func：修改图片颜色
 * User：jihf
 * Data：2016-12-22
 * Time: 13:24
 * Mail：jihaifeng@raiyi.com
 */
public class DrawableUtils {
  public static Drawable tintDrawable(Drawable drawable, int color) {
    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
    DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
    return wrappedDrawable;
  }

  public static Drawable tintDrawable(Context context, int drawableId, int colorId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);
    int color = ContextCompat.getColor(context, colorId);
    return tintDrawable(drawable, color);
  }
}
