package com.jihf.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 16:27
 * Mail：jihaifeng@raiyi.com
 */

public class NetworkUtils {

  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
  }

  /**
   * 打开网络设置界面
   * <p>3.0以下打开设置界面</p>
   */
  public static void openWirelessSettings() {
    if (android.os.Build.VERSION.SDK_INT > 10) {
      mContext.startActivity(
          new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    } else {
      mContext.startActivity(
          new Intent(android.provider.Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
  }

  public static boolean isNetworkConnected() {
    return isNetWorkAvailable() && (isWifiConnected() || isMobileConnected());
  }

  public static boolean isNetWorkAvailable() {
    if (null != mContext) {
      ConnectivityManager mConnectivityManager =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  //判断WIFI网络是否可用
  public static boolean isWifiConnected() {
    if (null != mContext) {
      ConnectivityManager mConnectivityManager =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      if (mWiFiNetworkInfo != null) {
        return mWiFiNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  //判断MOBILE网络是否可用
  public static boolean isMobileConnected() {
    if (null != mContext) {
      ConnectivityManager mConnectivityManager =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      if (mMobileNetworkInfo != null) {
        return mMobileNetworkInfo.isAvailable();
      }
    }
    return false;
  }
}
