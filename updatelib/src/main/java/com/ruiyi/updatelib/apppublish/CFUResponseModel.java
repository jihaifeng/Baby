package com.ruiyi.updatelib.apppublish;

public class CFUResponseModel extends BasePesponseModel {
  public static final String APP_LOAD_URL = "u";
  public static final String APP_VERSION = "v";
  public static final String APP_VERSION_COMMENT = "c";
  public static final String APP_PUBLISH_TIME = "t";
  public static final String APP_DESCRIPTION_PRICTURE_URL = "p";
  private String appLoadUrl;
  private String appVersion;
  private String appVersionComment;
  private String appPublishTime;
  private String appDescriptionImageUrl;

  public String getAppLoadUrl() {
    return this.appLoadUrl;
  }

  public void setAppLoadUrl(String appLoadUrl) {
    this.appLoadUrl = appLoadUrl;
  }

  public String getAppVersion() {
    return this.appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getAppVersionComment() {
    return this.appVersionComment;
  }

  public void setAppVersionComment(String appVersionComment) {
    this.appVersionComment = appVersionComment;
  }

  public String getAppPublishTime() {
    return this.appPublishTime;
  }

  public void setAppPublishTime(String appPublishTime) {
    this.appPublishTime = appPublishTime;
  }

  public String getAppDescriptionImageUrl() {
    return this.appDescriptionImageUrl;
  }

  public void setAppDescriptionImageUrl(String appDescriptionImageUrl) {
    this.appDescriptionImageUrl = appDescriptionImageUrl;
  }

  protected boolean checkLegaled() {
    return true;
  }
}