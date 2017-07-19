package com.ruiyi.updatelib.common;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeneralDeviceId {

  public static byte[] encoderByMd5(byte[] str) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
    }
    md5.update(str);
    return md5.digest();
  }

  public static String bytes2Hex(byte[] bts) {
    String des = "";
    String tmp = null;

    for (int i = 0; i < bts.length; i++) {
      tmp = (Integer.toHexString(bts[i] & 0xFF));
      if (tmp.length() == 1) {
        des += "0";
      }
      des += tmp;
    }
    return des;
  }

  public static String EncoderByMd5(String str) {
    if (TextUtils.isEmpty(str)) {
      return "";
    }
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    md5.update(str.getBytes());
    String strDes = bytes2Hex(md5.digest()); // to HexString
    return strDes;
  }

  /**
   * 系统方式读取设备号
   */
  private static String getMoblieID(Context ct) {
    if (ContextCompat.checkSelfPermission(ct, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_DENIED) {
      return "";
    }
    try {
      boolean isZore = false;
      android.telephony.TelephonyManager teleMgr =
          (android.telephony.TelephonyManager) getContext(ct).getSystemService(Context.TELEPHONY_SERVICE);
      String deviceId = teleMgr.getDeviceId();
      if ("unknown".equalsIgnoreCase(deviceId)) {
        deviceId = "0";
      }
      try {
        if (Integer.parseInt(deviceId) == 0) {
          isZore = true;
        }
      } catch (Exception e) {
        isZore = false;
      }
      if (TextUtils.isEmpty(deviceId) || isZore || deviceId.length() < 11) {
        deviceId = "";
      }
      return deviceId;
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 获取机器的序列号
   */
  private static String getSerialno() {
    String serialnum = null;
    try {
      Class<?> c = Class.forName("android.os.SystemProperties");
      Method get = c.getMethod("get", String.class, String.class);
      serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
    } catch (Exception ignored) {
      serialnum = null;
    }
    if (TextUtils.isEmpty(serialnum) || "unknown".equalsIgnoreCase(serialnum)) {
      serialnum = "";
    }
    String serialnum2 = null;
    try {
      @SuppressWarnings ("rawtypes") Class myclass = Class.forName("android.os.SystemProperties");
      Method[] methods = myclass.getMethods();
      Object[] params = new Object[] {
          new String("ro.serialno"), new String("Unknown")
      };
      serialnum2 = (String) (methods[2].invoke(myclass, params));
    } catch (Exception ignored) {
      serialnum2 = null;
    }
    if (TextUtils.isEmpty(serialnum2) || "unknown".equalsIgnoreCase(serialnum2) || serialnum2.equalsIgnoreCase(
        serialnum)) {
      serialnum2 = "";
    }
    String data = serialnum + serialnum2;
    return data;
  }

  /**
   * 获取设备硬件信息
   */
  private static String getMobileHardInfo() {
    String m_szDevIDShort = android.os.Build.BOARD
        + android.os.Build.CPU_ABI
        + android.os.Build.DEVICE
        + android.os.Build.DISPLAY
        + android.os.Build.HOST
        + android.os.Build.MANUFACTURER
        + android.os.Build.MODEL
        + android.os.Build.PRODUCT
        + android.os.Build.TYPE
        + android.os.Build.USER;
    // we make this look like a valid IMEI
    return m_szDevIDShort;
  }

  private static boolean isValidToken(String token) {
    String str = "^[0-9a-fA-F]+$";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(str);
    java.util.regex.Matcher m = p.matcher(token);
    return m.matches();
  }

  /**
   * 获取设备唯一编号
   */
  public static String getDeviceUUID(Context context) {
    Context ct = getContext(context);
    // 读取应用缓存
    SharedPreferences mPrefs = ct.getSharedPreferences("sp_device", Context.MODE_PRIVATE);
    if (mPrefs != null && mPrefs.contains("UNION_ZD_DEVICEID")) {
      String dvid = mPrefs.getString("UNION_ZD_DEVICEID", "");
      if (dvid != null && dvid.length() > 30 && isValidToken(dvid)) {
        return dvid;
      } else {
        mPrefs.edit().remove("UNION_ZD_DEVICEID").commit();
      }
    }
    // 读取存储在SD卡中的设备号
    String did = getUUIDFromSDFile(ct);
    // 读取SD异常则重新计算并存储
    if (TextUtils.isEmpty(did)) {
      String deviceid = getMoblieID(ct) + getSerialno() + getMobileHardInfo();
      did = GeneralDeviceId.EncoderByMd5("" + deviceid);
    }
    if (did == null || did.length() < 30 || !isValidToken(did)) {
      did = GeneralDeviceId.EncoderByMd5("raiyi" + System.currentTimeMillis());
    }
    mPrefs.edit().putString("UNION_ZD_DEVICEID", "" + did).commit();
    return did;
  }

  public static String getPushDeviceUUID(Context context) {
    return getDeviceUUID(context);
  }

  /**
   * 手机唯一标识 以v2_*********
   */
  public static String getMobileUUID(Context context) {
    return "v2_" + getDeviceUUID(context);
    //		return getDeviceUUID(context);
  }

  /**
   * 从SD卡中读取设备编号
   */
  private static String getUUIDFromSDFile(Context context) {
    String sID = "";
    try {
      String INSTALLATION = "INSTALLZ.ini";
      java.io.File installation =
          new java.io.File(Environment.getExternalStorageDirectory() + "/Android/INSTALL/" + INSTALLATION);
      if (!installation.exists()) {
        sID = writeInstallationFile(installation, context);
      } else {
        sID = readInstallationFile(installation);
      }
      if (TextUtils.isEmpty(sID) || !isValidToken(sID)) {
        sID = "";
        installation.delete();
      }
    } catch (Exception e) {
      return "";
    }
    return sID;
  }

  /**
   * 从文件中读取编号
   */
  private static String readInstallationFile(java.io.File installation) throws java.io.IOException {
    java.io.RandomAccessFile f = new java.io.RandomAccessFile(installation, "r");
    byte[] bytes = new byte[(int) f.length()];
    f.readFully(bytes);
    f.close();
    return new String(bytes);
  }

  /**
   * 将编号写入sd卡文件
   */
  private static String writeInstallationFile(java.io.File installation, Context ct) throws java.io.IOException {
    File pFile = installation.getParentFile();
    if (pFile == null || !pFile.exists()) {
      pFile.mkdirs();
    }
    if (!installation.exists()) {
      installation.createNewFile();
    }
    java.io.FileOutputStream out = new java.io.FileOutputStream(installation);
    String deviceid = GeneralDeviceId.getMoblieID(getContext(ct)) + getSerialno() + GeneralDeviceId.getMobileHardInfo();
    String did = GeneralDeviceId.EncoderByMd5("" + deviceid);
    out.write(did.getBytes());
    out.close();
    return did;
  }

  private static Context getContext(Context ct) {
    if (ct != null && ct.getApplicationContext() != null) {
      return ct.getApplicationContext();
    }
    return ct;
  }
}
