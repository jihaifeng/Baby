package com.jihf.lib.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.Tencent;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-22 18:07
 * Mail：jihaifeng@raiyi.com
 */

public class QQShareUtil {
  private static final String TAG = QQShareUtil.class.getSimpleName().trim();
  private static QQShareUtil instance;
  private Tencent mTencent;
  private Activity activity;
  private ShareListener listener;

  public static QQShareUtil getInstance(Activity activity) {
    if (null == instance) {
      synchronized (WXShareUtil.class) {
        if (null == instance) {
          instance = new QQShareUtil();
        }
      }
    }
    instance.activity = activity;
    instance.regToQQ();
    return instance;
  }

  private void regToQQ() {
    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    // 其中APP_ID是分配给第三方应用的appid，类型为String。
    mTencent = Tencent.createInstance(ShareConfig.getQqAppId(), activity);
    listener = new ShareListener(activity);
  }

  public void shareUrl(String url, String title, String desc, @QQShareType int type) {
    if (mTencent.isSessionValid() && mTencent.getOpenId() == null) {
      Toast.makeText(activity, "您还未安装QQ", Toast.LENGTH_SHORT).show();
    }

    final Bundle params = new Bundle();

    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
    params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
    if (type == QQShareType.QZONE) {
      params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
    }
    // QQ分享要在主线程做
    ThreadManager.getMainHandler().post(() -> mTencent.shareToQQ(activity, params, listener));
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Tencent.onActivityResultData(requestCode, resultCode, data, listener);
  }
}
