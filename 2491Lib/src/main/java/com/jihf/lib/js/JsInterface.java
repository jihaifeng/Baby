package com.jihf.lib.js;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-26 09:43
 * Mail：jihaifeng@raiyi.com
 */

public class JsInterface {
  private static final String TAG = JsInterface.class.getSimpleName().trim();
  private Context mContext;

  public JsInterface(Context context) {
    mContext = context;
  }

  @SuppressLint ("JavascriptInterface") @JavascriptInterface public void shareImg(String imgUrl) {
    Log.i(TAG, "shareImg：" + imgUrl);
    //WXShareUtil.getInstance(mContext).sharePic(imgUrl, WXShareType.WX_FRIEND);
  }
}
