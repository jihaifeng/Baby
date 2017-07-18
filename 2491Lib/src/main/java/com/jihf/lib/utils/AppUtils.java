package com.jihf.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import com.jihf.lib.app.BaseApplication;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 14:40
 * Mail：jihaifeng@raiyi.com
 */

public class AppUtils {
  private static Context mContext;
  private static String TAG_MARKET = "2491";// 默认渠道 2491_joke

  public static void init(Context context) {
    mContext = context;
  }

  /**
   * 获取渠道号
   */
  public static String getMarket() {
    try {
      PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
      ApplicationInfo applicationInfo =
          packageManager.getApplicationInfo(BaseApplication.getInstance().getPackageName(),
              PackageManager.GET_META_DATA);
      TAG_MARKET = applicationInfo.metaData.getString("UMENG_CHANNEL");
      if (TextUtils.isEmpty(TAG_MARKET)) {
        TAG_MARKET = applicationInfo.metaData.getInt("UMENG_CHANNEL") + "";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (TextUtils.isEmpty(TAG_MARKET)) {
      return "";
    } else {
      return TAG_MARKET;
    }
  }
  /**
   * 获取App名称
   *
   * @return App名称
   */
  public static String getAppName() {
    if (null == mContext) {
      return null;
    }
    if (isSpace(mContext.getPackageName())) {
      return null;
    }
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
      return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取App版本码
   *
   * @return App版本码
   */
  public static int getAppVersionCode() {
    if (null == mContext) {
      return -1;
    }
    if (isSpace(mContext.getPackageName())) {
      return -1;
    }
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
      return pi == null ? -1 : pi.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * 获取App版本码
   *
   * @return App版本码
   */
  public static String getAppVersionName() {
    if (null == mContext) {
      return null;
    }
    if (isSpace(mContext.getPackageName())) {
      return null;
    }
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
      return pi == null ? null : pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取App包名
   *
   * @return App包名
   */
  public static String getAppPackageName() {
    if (null == mContext) {
      return null;
    }
    return mContext.getPackageName();
  }

  private static boolean isSpace(String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 获取App签名
   *
   * @return App签名
   */
  public static Signature[] getAppSignature() {
    return getAppSignature(mContext.getPackageName());
  }

  /**
   * 获取App签名
   *
   * @param packageName 包名
   *
   * @return App签名
   */
  public static Signature[] getAppSignature(String packageName) {
    if (isSpace(packageName)) {
      return null;
    }
    try {
      PackageManager pm = mContext.getPackageManager();
      @SuppressLint ("PackageManagerGetSignatures") PackageInfo pi =
          pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
      return pi == null ? null : pi.signatures;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
