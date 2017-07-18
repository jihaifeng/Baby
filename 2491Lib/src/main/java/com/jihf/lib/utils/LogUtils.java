package com.jihf.lib.utils;

import android.util.Log;

/**
 * Func：log日志管理
 * Desc:
 * Author：jihf
 * Data：2017-02-06 11:16
 * Mail：jihaifeng@raiyi.com
 */
public class LogUtils {
  private static boolean LOG_SWITCH = true;

  public static void setLogSwitch(boolean logSwitch) {
    LOG_SWITCH = logSwitch;
  }

  public static void d(String tag, String msg) {
    if (LOG_SWITCH) {
      Log.d(tag, msg);
    }
  }

  public static void e(String tag, String msg) {
    if (LOG_SWITCH) {
      Log.e(tag, msg);
    }
  }

  public static void i(String tag, String msg) {
    if (LOG_SWITCH) {
      Log.i(tag, msg);
    }
  }

  public static void v(String tag, String msg) {
    if (LOG_SWITCH) {
      Log.v(tag, msg);
    }
  }
}
