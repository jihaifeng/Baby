package com.jihf.lib.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.jihf.lib.R;
import com.jihf.lib.app.BaseApplication;
import java.util.Random;

/**
 * Func：默认背景
 * Desc:
 * Author：jihf
 * Data：2017-03-08 16:08
 * Mail：jihaifeng@raiyi.com
 */

public class DefaultBgUtils {

  static Drawable drawable = ContextCompat.getDrawable(BaseApplication.getInstance(), R.drawable.default_bg_white);

  private DefaultBgUtils() {
    throw new RuntimeException("DefIconFactory cannot be initialized!");
  }

  private final static int[] defaultColors = new int[] {
      R.color.light_pink, R.color.light_blue, R.color.light_green, R.color.light_orange, R.color.light_purple
  };

  public static Drawable provideDrawable() {
    int index = new Random().nextInt(defaultColors.length);
    return DrawableUtils.tintDrawable(drawable,
        ContextCompat.getColor(BaseApplication.getInstance(), defaultColors[index]));
  }
}
