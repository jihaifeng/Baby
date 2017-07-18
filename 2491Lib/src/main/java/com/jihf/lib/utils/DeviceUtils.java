package com.jihf.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 14:56
 * Mail：jihaifeng@raiyi.com
 */

public class DeviceUtils {

  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
  }

  /**
   * 获取设备SDK版本号
   *
   * @return 设备系统版本号
   */
  public static int getSDKVersion() {
    return android.os.Build.VERSION.SDK_INT;
  }

  /**
   * 获取设备系统版本号
   *
   * @return 设备系统版本号
   */
  public static String getReleaseVersion() {
    return android.os.Build.VERSION.RELEASE;
  }

  /**
   * 获取设备AndroidID
   *
   * @return AndroidID
   */
  @SuppressLint ("HardwareIds") public static String getAndroidID() {
    return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  /**
   * 获取设备厂商
   * <p>如Xiaomi</p>
   *
   * @return 设备厂商
   */

  public static String getManufacturer() {
    return Build.MANUFACTURER;
  }

  /**
   * 获取设备型号
   * <p>如MI2SC</p>
   *
   * @return 设备型号
   */
  public static String getModel() {
    String model = Build.MODEL;
    if (model != null) {
      model = model.trim().replaceAll("\\s*", "");
    } else {
      model = "";
    }
    return model;
  }
}
