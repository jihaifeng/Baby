package com.jihf.lib.constans;

import android.util.Log;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.StringUtils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-22 15:01
 * Mail：jihaifeng@raiyi.com
 */

public class UmengConfig {
  private static String UMENG_APPKEY = "594b70383eae253485001208";
  private static String UMENG_CHANNEL = "2491_joke";

  public static void init(String key) {
    UMENG_CHANNEL = AppUtils.getMarket();
    if (StringUtils.isTrimEmpty(key)) {
      Log.i("UmengConfig", "umeng初始化失败，appkey为空");
      return;
    }
    UMENG_APPKEY = key;
    Log.i("UmengConfig", "umeng初始化成功，appkey= " + UMENG_APPKEY + "channel= " + UMENG_CHANNEL);
  }

  public static String getUmengAppkey() {
    return UMENG_APPKEY;
  }

  public static String getUmengChannel() {
    return UMENG_CHANNEL;
  }
}
