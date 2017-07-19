package com.ruiyi.updatelib.apppublish;

import android.content.Context;
import com.ruiyi.updatelib.http.HttpRequestParameters;

public abstract class PublishHandler implements AppPublishApi {
  public void checkForUpdate(Context context, String appUnipueName, String appVersion, String appOrigin,
      String recommendPeople, String responseDataMode) {
    checkForUpdate(context, appUnipueName, appVersion, appOrigin, recommendPeople, responseDataMode, null, null);
  }

  public void checkForUpdate(Context context, String appUnipueName, String appVersion, String appOrigin,
      String recommendPeople, String responseDataMode, String checkType) {
    checkForUpdate(context, appUnipueName, appVersion, appOrigin, recommendPeople, responseDataMode, null, checkType);
  }

  public void checkForUpdate(Context context, String appUnipueName, String appVersion, String appOrigin,
      String recommendPeople, String responseDataMode, String appTime, String checkType) {
    CFUTransportModel model = ModelIniter.initCFUTransportModel(context);

    if ((!"".equals(appUnipueName)) && (appUnipueName != null)) {
      model.setAppUniqueName(appUnipueName);
    }
    if ((!"".equals(appVersion)) && (appVersion != null)) {
      model.setAppVersion(appVersion);
    }
    if ((!"".equals(responseDataMode)) && (responseDataMode != null)) {
      model.setResponseDataMode(responseDataMode);
    }
    if (appOrigin == null) {
      model.setAppOrigin("");
    } else {
      model.setAppOrigin(appOrigin);
    }
    if (recommendPeople == null) {
      model.setRecommendPeople("");
    } else {
      model.setRecommendPeople(recommendPeople);
    }

    if ((appTime != null) && (!"".equals(appTime))) {
      model.setAppTime(appTime);
    }

    if ((checkType != null) && (!"".equals(checkType))) {
      model.setAppType(checkType);
    }

    if (model.checkLegaled()) {
      checkForUpdateRequest(context, obtainCFUHttpParameters(model));
    }
  }

  public void installFeedbackInfo(Context context, String appUnipueName, String appVersion, String appOrigin,
      String recommendPeople, String responseDataMode) {
    InstallFeedbackTransportModel model = ModelIniter.initInstallFeedbackTransportModel(context);

    if ((!"".equals(appUnipueName)) && (appUnipueName != null)) {
      model.setAppUniqueName(appUnipueName);
    }
    if ((!"".equals(appVersion)) && (appVersion != null)) {
      model.setAppVersion(appVersion);
    }
    if ((!"".equals(responseDataMode)) && (responseDataMode != null)) {
      model.setResponseDataMode(responseDataMode);
    }
    if (appOrigin == null) {
      model.setAppOrigin("");
    } else {
      model.setAppOrigin(appOrigin);
    }
    if (recommendPeople == null) {
      model.setRecommendPeople("");
    } else {
      model.setRecommendPeople(recommendPeople);
    }

    model.setVerify(ModelIniter.initVerify(model));
    if (model.checkLegaled()) {
      InstallFeedbackInfoRequest(context, obtainInstallHttpParameters(model));
    }
  }

  public void obtainNewAppInfo(String appUnipueName, String appVersion, String responseDataMode) {
  }

  protected HttpRequestParameters obtainCFUHttpParameters(CFUTransportModel model) {
    HttpRequestParameters ps = new HttpRequestParameters();
    ps.addParameters("a", model.getApiVersion());
    ps.addParameters("c", model.getAppOrigin());
    ps.addParameters("n", model.getAppUniqueName());
    ps.addParameters("v", model.getAppVersion());
    ps.addParameters("i", model.getDeviceId());
    ps.addParameters("hv", model.getHardWareVersion());
    ps.addParameters("bv", model.getOsBaseHandVersion());
    ps.addParameters("kv", model.getOsKernelVersion());
    ps.addParameters("ev", model.getOsPackageVersion());
    ps.addParameters("ov", model.getOsVersion());
    ps.addParameters("m", model.getPhoneMode());
    ps.addParameters("r", model.getRecommendPeople());
    ps.addParameters("rt", model.getResponseDataMode());
    ps.addParameters("ct", model.getAppType());
    ps.addParameters("it", model.getAppTime());

    return ps;
  }

  protected HttpRequestParameters obtainInstallHttpParameters(InstallFeedbackTransportModel model) {
    HttpRequestParameters ps = new HttpRequestParameters();
    ps.addParameters("a", model.getApiVersion());
    ps.addParameters("c", model.getAppOrigin());
    ps.addParameters("n", model.getAppUniqueName());
    ps.addParameters("v", model.getAppVersion());
    ps.addParameters("i", model.getDeviceId());
    ps.addParameters("hv", model.getHardWareVersion());
    ps.addParameters("bv", model.getOsBaseHandVersion());
    ps.addParameters("kv", model.getOsKernelVersion());
    ps.addParameters("ev", model.getOsPackageVersion());
    ps.addParameters("ov", model.getOsVersion());
    ps.addParameters("m", model.getPhoneMode());
    ps.addParameters("r", model.getRecommendPeople());
    ps.addParameters("verify", model.getVerify());
    ps.addParameters("rt", model.getResponseDataMode());

    return ps;
  }

  protected abstract void checkForUpdateRequest(Context paramContext, HttpRequestParameters paramHttpRequestParameters);

  protected abstract void InstallFeedbackInfoRequest(Context paramContext,
      HttpRequestParameters paramHttpRequestParameters);

  protected abstract void ObtainNewAppInfoRequest(Context paramContext,
      HttpRequestParameters paramHttpRequestParameters);
}