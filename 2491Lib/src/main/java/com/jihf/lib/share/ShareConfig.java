package com.jihf.lib.share;

import android.util.Log;
import com.jihf.lib.utils.StringUtils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-11 10:38
 * Mail：jihaifeng@raiyi.com
 */

public class ShareConfig {

  private static String QQ_APP_ID = "101407009";
  private static String QQ_APP_KEY = "fd05414e2e732bf69488ae8d2978ed91";
  // 从官网申请的合法appId
  // AppID: wx834b689ee77fbf8a
  // AppSecret: 227b7bcada286f0bc5b535bb4dfc5943
  private static String WX_APP_ID = "wx834b689ee77fbf8a";
  private static String WX_APP_SECRET = "227b7bcada286f0bc5b535bb4dfc5943";

  public static void initQQShare(String appId, String appKey) {

    if (StringUtils.isEmpty(appId, appKey)) {
      Log.i("ShareConfig", "QQ分享初始化失败,appId或appkey为空！");
      return;
    }
    QQ_APP_ID = appId;
    QQ_APP_KEY = appKey;
    Log.i("share", "QQ分享初始化成功");
  }

  public static void initWxShare(String appId, String appSecret) {
    if (StringUtils.isEmpty(appId, appSecret)) {
      Log.i("ShareConfig", "微信分享初始化失败,appId或appkey为空！");
      return;
    }
    WX_APP_ID = appId;
    WX_APP_SECRET = appSecret;
    Log.i("ShareConfig", "微信分享初始化成功");
  }

  public static String getQqAppId() {
    return QQ_APP_ID;
  }

  public static String getQqAppKey() {
    return QQ_APP_KEY;
  }

  public static String getWxAppId() {
    return WX_APP_ID;
  }

  public static String getWxAppSecret() {
    return WX_APP_SECRET;
  }
}
