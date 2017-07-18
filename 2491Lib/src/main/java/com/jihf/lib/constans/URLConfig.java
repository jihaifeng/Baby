package com.jihf.lib.constans;

import android.util.Log;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.DeviceUtils;
import com.jihf.lib.utils.StringUtils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-06-20 15:06
 * Mail：jihaifeng@raiyi.com
 */

public class URLConfig {

  private static String DEFAULT_WEB_URL = "http://hdpdmp.api.wo116114.com/gaoxiao.html";

  private static String BASE_URL = "http://hdpdmp.api.wo116114.com";
  private static String PATH_LIST = "/fun";
  private static String PATH_DETAILS = "/gaoxiao/template/freemarker/getNewsDetail";
  private static String DETAILS_TAGID = "006";

  private static String UA = "Android-fun"
      + "("
      + "AppName= joke"
      + "|AppVersion= "
      + AppUtils.getAppVersionName()
      + "|DeviceInfo= "
      + DeviceUtils.getModel()
      + " "
      + DeviceUtils.getReleaseVersion()
      + ")";

  public static void init(String baseUrl, String pathList, String pathDetails, String tagId, String defaultWebUrl) {
    if (StringUtils.isEmpty(baseUrl, pathDetails, pathDetails, tagId)) {
      Log.i("URLConfig", "url初始化失败，缺少相应的baseUrl或path地址或者tagId！");
      return;
    }
    BASE_URL = baseUrl;
    PATH_LIST = pathList;
    PATH_DETAILS = pathDetails;
    DETAILS_TAGID = tagId;
    DEFAULT_WEB_URL = defaultWebUrl;
    if (StringUtils.isEmpty(defaultWebUrl)) {
      Log.i("URLConfig", "初始化成功，默认web链接为空！");
    } else {
      Log.i("URLConfig", "初始化成功！");
    }
  }

  public static void setUA(String UAstr) {
    if (StringUtils.isEmpty(UAstr)) {
      return;
    }
    UA = UAstr;
  }

  public static void setUA(String port, String appName) {
    if (StringUtils.isEmpty(port, appName)) {
      return;
    }
    UA = port
        + "("
        + "AppName= "
        + appName
        + "|AppVersion= "
        + AppUtils.getAppVersionName()
        + "|DeviceInfo= "
        + DeviceUtils.getModel()
        + " "
        + DeviceUtils.getReleaseVersion()
        + ")";
  }

  public static String getUA() {
    return UA;
  }

  public static String getDefaultWebUrl() {
    return DEFAULT_WEB_URL;
  }

  public static String getBaseUrl() {
    return BASE_URL;
  }

  public static String getPathList() {
    return PATH_LIST;
  }

  public static String getPathDetails() {
    return PATH_DETAILS;
  }

  public static String getDetailsTagid() {
    return DETAILS_TAGID;
  }
}
