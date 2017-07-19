package com.ruiyi.updatelib.apppublish;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelIniter {
  public static CFUTransportModel initCFUTransportModel(Context context) {
    CFUTransportModel model = new CFUTransportModel();
    model.setApiVersion("1.4");
    model.setAppUniqueName(AppPhoneInfoReader.getAPPName(context));
    model.setAppVersion(AppPhoneInfoReader.getAppVersion(context));
    model.setDeviceId(AppPhoneInfoReader.getDeviceId(context));
    model.setHardWareVersion(AppPhoneInfoReader.getHardWareVersion());
    model.setOsBaseHandVersion(AppPhoneInfoReader.getBaseHandVersion());
    model.setOsKernelVersion(AppPhoneInfoReader.getOSKernelVersion());
    model.setOsPackageVersion(AppPhoneInfoReader.getPackageOsVersion());
    model.setOsVersion(AppPhoneInfoReader.getOSVersion());
    model.setPhoneMode(AppPhoneInfoReader.getPhoneModel());
    model.setAppTime(AppPhoneInfoReader.getInstanceTime(context));
    return model;
  }

  public static InstallFeedbackTransportModel initInstallFeedbackTransportModel(Context context) {
    InstallFeedbackTransportModel model = new InstallFeedbackTransportModel();
    model.setApiVersion("1.4");
    model.setAppUniqueName(AppPhoneInfoReader.getAPPName(context));
    model.setAppVersion(AppPhoneInfoReader.getAppVersion(context));
    model.setDeviceId(AppPhoneInfoReader.getDeviceId(context));
    model.setHardWareVersion(AppPhoneInfoReader.getHardWareVersion());
    model.setOsBaseHandVersion(AppPhoneInfoReader.getBaseHandVersion());
    model.setOsKernelVersion(AppPhoneInfoReader.getOSKernelVersion());
    model.setOsPackageVersion(AppPhoneInfoReader.getPackageOsVersion());
    model.setOsVersion(AppPhoneInfoReader.getOSVersion());
    model.setPhoneMode(AppPhoneInfoReader.getPhoneModel());

    return model;
  }

  protected static String initVerify(InstallFeedbackTransportModel model) {
    String str = "";
    try {
      String strs = model.getApiVersion()
          + model.getDeviceId().substring(0, 5)
          + model.getDeviceId()
          + model.getAppUniqueName()
          + model.getAppVersion()
          + model.getAppOrigin()
          + model.getOsPackageVersion()
          + model.getOsVersion()
          + model.getOsKernelVersion()
          + model.getOsBaseHandVersion()
          + model.getHardWareVersion()
          + model.getPhoneMode()
          + model.getRecommendPeople();
      str = AppPhoneInfoReader.MD5(strs);
    } catch (Exception e) {
      return str;
    }
    return str;
  }

  public static CFUResponseModel initCFUResponseModel(String result) {
    CFUResponseModel model = new CFUResponseModel();
    try {
      JSONObject object = new JSONObject(result);
      if (object.has("r")) {
        model.setResutType(object.getInt("r"));
      }
      if (object.has("m")) {
        model.setResultFailCause(object.getString("m"));
      }
      if (object.has("u")) {
        model.setAppLoadUrl(object.getString("u"));
      }
      if (object.has("v")) {
        model.setAppVersion(object.getString("v"));
      }
      if (object.has("c")) {
        model.setAppVersionComment(object.getString("c"));
      }
      if (object.has("t")) {
        model.setAppPublishTime("t");
      }
      if (object.has("p")) {
        model.setAppDescriptionImageUrl("p");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return model;
  }

  public static InstallFeedbackResponseModel initInstallFeedbackResponseModel(String result) {
    InstallFeedbackResponseModel model = new InstallFeedbackResponseModel();
    try {
      JSONObject object = new JSONObject(result);
      if (object.has("r")) {
        model.setResutType(object.getInt("r"));
      }
      if (object.has("m")) {
        model.setResultFailCause(object.getString("m"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return model;
  }
}