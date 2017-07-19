package com.ruiyi.updatelib.apppublish;

import android.content.Context;

public abstract interface AppPublishApi {
  public static final String CHECK_UPDATE_URL = "http://app.raiyi.com/publish/checkversion";
  public static final String INSTALL_RESPONSE_URL = "http://app.raiyi.com/publish/modifyclientinfo";
  public static final String OBTAIN_APPINFO_URL = "http://app.raiyi.com/publish/getnewversion";
  public static final String API_VERSION = "1.4";

  public abstract void checkForUpdate(Context paramContext, String paramString1, String paramString2,
      String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);

  public abstract void checkForUpdate(Context paramContext, String paramString1, String paramString2,
      String paramString3, String paramString4, String paramString5, String paramString6);

  public abstract void checkForUpdate(Context paramContext, String paramString1, String paramString2,
      String paramString3, String paramString4, String paramString5);

  public abstract void installFeedbackInfo(Context paramContext, String paramString1, String paramString2,
      String paramString3, String paramString4, String paramString5);

  public abstract void obtainNewAppInfo(String paramString1, String paramString2, String paramString3);
}