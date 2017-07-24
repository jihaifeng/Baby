package com.jihf.lib.utils;

import android.graphics.Bitmap;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-24 16:48
 * Mail：jihaifeng@raiyi.com
 */

public interface BitmapCovListener {
  void covSuccess(Bitmap bitmap);

  void covFailure(String errorMsg);
}
