package com.ruiyi.updatelib.apppublish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.ruiyi.updatelib.common.GeneralDeviceId;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

public class AppPhoneInfoReader {
  public static String getDeviceId(Context context) {
    String pushDeviceID = GeneralDeviceId.getPushDeviceUUID(context);
    return pushDeviceID;
  }

  public static String getPhoneModel() {
    return Build.MODEL;
  }

  public static String getOSVersion() {
    return Build.VERSION.RELEASE;
  }

  public static String getBaseHandVersion() {
    String BaseHandVersion = "";
    try {
      Class cl = Class.forName("android.os.SystemProperties");
      Object invoker = cl.newInstance();

      Method m = cl.getMethod("get", new Class[] {
          String.class, String.class
      });

      Object result = m.invoke(invoker, new Object[] {
          "gsm.version.baseband", ""
      });

      BaseHandVersion = (String) result;
    } catch (Exception localException) {
    }
    return BaseHandVersion;
  }

  public static String getOSKernelVersion() {
    String oSKernelVersion = "";
    Process process = null;
    try {
      process = Runtime.getRuntime().exec("cat /proc/version");
    } catch (IOException e) {
      e.printStackTrace();
    }

    InputStream outs = process.getInputStream();
    InputStreamReader isrout = new InputStreamReader(outs);
    BufferedReader brout = new BufferedReader(isrout, 8192);
    String result = "";
    try {
      String line;
      while ((line = brout.readLine()) != null) {
        result = result + line;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (result != "") {
      String Keyword = "version ";
      int index = result.indexOf(Keyword);
      String line = result.substring(index + Keyword.length());
      index = line.indexOf(" ");
      oSKernelVersion = line.substring(0, index);
    }
    return oSKernelVersion;
  }

  public static String getHardWareVersion() {
    String result = "";
    String str = "";
    try {
      String[] args = { "system/bin/cat", "proc/cpuinfo" };
      ProcessBuilder cmd = new ProcessBuilder(args);
      Process process = cmd.start();
      InputStream in = process.getInputStream();
      byte[] re = new byte[1024];
      while (in.read(re) != -1) {
        result = result + new String(re);
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      int start = result.indexOf("Hardware");
      int end = result.indexOf("Revision");
      String str1 = result.substring(start, end);
      int offest = str1.indexOf(':');
      str = str1.substring(offest + 1).replaceAll("\n", "");
    } catch (Exception e) {
      return "";
    }

    return str;
  }

  public static String getAppVersion(Context context) {
    String appVersion = "";
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

      appVersion = info.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return appVersion;
    }
    return appVersion;
  }

  public static String getAPPName(Context context) {
    String appName = "";
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

      appName = info.packageName + "." + info.applicationInfo.loadLabel(context.getPackageManager()).toString();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return appName;
    }
    return appName;
  }

  public static String getPackageOsVersion() {
    return "";
  }

  public static String MD5(String sStr) {
    String sReturnCode = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(sStr.getBytes("UTF-8"));
      byte[] b = md.digest();

      StringBuffer sb = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        int i = b[offset];
        if (i < 0) {
          i += 256;
        }
        if (i < 16) {
          sb.append("0");
        }
        sb.append(Integer.toHexString(i));
      }

      sReturnCode = sb.toString();
    } catch (Exception localException) {
    }
    return sReturnCode;
  }

  public static String getInstanceTime(Context context) {
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

      String currentVersion = info.versionName;

      if (currentVersion != null) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("VERSION_TIME", 0);
        String version = sharedPreferences.getString("version", null);

        if ((version != null) && (!"".equals(version.trim())) && (version.equals(currentVersion))) {
          String time = sharedPreferences.getString("time", null);
          if ((time != null) && (time.length() > 5)) {
            return time;
          }
        }

        return updateVersionTme(sharedPreferences, currentVersion);
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String updateVersionTme(SharedPreferences sharedPreferences, String version) {
    String currentTime = getCurrentTime();
    sharedPreferences.edit().putString("version", version).commit();
    sharedPreferences.edit().putString("time", currentTime).commit();
    return currentTime;
  }

  @SuppressLint ({ "SimpleDateFormat" }) private static String getCurrentTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String dateString = formatter.format(Long.valueOf(System.currentTimeMillis()));
    return dateString;
  }
}