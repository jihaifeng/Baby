package com.jihf.lib.share;

import android.app.Activity;
import android.util.Log;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class ShareListener implements IUiListener {
  private static final String TAG = ShareListener.class.getSimpleName().trim();
  private Activity activity;

  public ShareListener(Activity activity) {
    this.activity = activity;
  }

  @Override public void onCancel() {
    Log.i(TAG, "onCancel: 分享取消");
  }

  @Override public void onComplete(Object arg0) {
    Log.i(TAG, "onComplete: 分享成功");
  }

  @Override public void onError(UiError arg0) {
    Log.i(TAG, "onError: 分享出错");
  }
}