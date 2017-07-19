package com.ruiyi.updatelib.apppublish;

public class BaseUploadModel extends BaseModel {
  public static final String API_VERSION = "a";
  public static final String DEVICE_ID = "i";
  public static final String APP_UNIQUE_NAME = "n";
  public static final String APP_VERSION_NAME = "v";
  public static final String APP_ORIGIN = "c";
  public static final String OS_PACKAGE_VERSION = "ev";
  public static final String OS_VERSION = "ov";
  public static final String OS_KERNEL_VERSION = "kv";
  public static final String OS_BASEHAND_VERSION = "bv";
  public static final String HARDWARE_VERSION = "hv";
  public static final String PHONE_MODE = "m";
  public static final String RECOMMEND_PEOPLE = "r";
  public static final String RESPONSE_DATA_MODE = "rt";
  public static final String APP_CT = "ct";
  public static final String APP_INS_TIEM = "it";
  protected String apiVersion = "";
  protected String deviceId = "";
  protected String appUniqueName = "";
  protected String appVersion = "";
  protected String appOrigin = "";
  protected String osPackageVersion = "";
  protected String osVersion = "";
  protected String osKernelVersion = "";
  protected String osBaseHandVersion = "";
  protected String hardWareVersion = "";
  protected String phoneMode = "";
  protected String recommendPeople = "";

  protected String responseDataMode = "1";
  protected String appTime = "";
  protected String appType = "0";

  public String getAppTime() {
    return this.appTime;
  }

  public void setAppTime(String appTime) {
    this.appTime = appTime;
  }

  public String getAppType() {
    return this.appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public String getApiVersion() {
    return this.apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public String getDeviceId() {
    return this.deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getAppUniqueName() {
    return this.appUniqueName;
  }

  public void setAppUniqueName(String appUniqueName) {
    this.appUniqueName = appUniqueName;
  }

  public String getAppVersion() {
    return this.appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getAppOrigin() {
    return this.appOrigin;
  }

  public void setAppOrigin(String appOrigin) {
    this.appOrigin = appOrigin;
  }

  public String getOsPackageVersion() {
    return this.osPackageVersion;
  }

  public void setOsPackageVersion(String osPackageVersion) {
    this.osPackageVersion = osPackageVersion;
  }

  public String getOsVersion() {
    return this.osVersion;
  }

  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }

  public String getOsKernelVersion() {
    return this.osKernelVersion;
  }

  public void setOsKernelVersion(String osKernelVersion) {
    this.osKernelVersion = osKernelVersion;
  }

  public String getOsBaseHandVersion() {
    return this.osBaseHandVersion;
  }

  public void setOsBaseHandVersion(String osBaseHandVersion) {
    this.osBaseHandVersion = osBaseHandVersion;
  }

  public String getHardWareVersion() {
    return this.hardWareVersion;
  }

  public void setHardWareVersion(String hardWareVersion) {
    this.hardWareVersion = hardWareVersion;
  }

  public String getPhoneMode() {
    return this.phoneMode;
  }

  public void setPhoneMode(String phoneMode) {
    this.phoneMode = phoneMode;
  }

  public String getRecommendPeople() {
    return this.recommendPeople;
  }

  public void setRecommendPeople(String recommendPeople) {
    this.recommendPeople = recommendPeople;
  }

  public String getResponseDataMode() {
    return this.responseDataMode;
  }

  public void setResponseDataMode(String responseDataMode) {
    this.responseDataMode = responseDataMode;
  }

  protected boolean checkLegaled() {
    if (("".equals(this.apiVersion)) || (this.apiVersion == null)) {
      return false;
    }
    if (("".equals(this.appUniqueName)) || (this.appUniqueName == null)) {
      return false;
    }
    if (("".equals(this.appVersion)) || (this.appVersion == null)) {
      return false;
    }
    return true;
  }
}