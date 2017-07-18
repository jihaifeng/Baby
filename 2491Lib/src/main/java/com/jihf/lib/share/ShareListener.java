package com.jihf.lib.share;

import android.app.Activity;
import android.widget.Toast;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class ShareListener implements IUiListener {
  private Activity activity;

  public ShareListener(Activity activity) {
    this.activity = activity;
  }

  @Override public void onCancel() {
    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
  }

  @Override public void onComplete(Object arg0) {
    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
  }

  @Override public void onError(UiError arg0) {
    Toast.makeText(activity, "分享出错", Toast.LENGTH_SHORT).show();
  }
}