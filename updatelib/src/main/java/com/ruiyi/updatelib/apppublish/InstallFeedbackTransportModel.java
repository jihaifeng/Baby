package com.ruiyi.updatelib.apppublish;

public class InstallFeedbackTransportModel extends BaseUploadModel {
  public static final String VERIFY = "verify";
  private String verify;

  public String getVerify() {
    return this.verify;
  }

  public void setVerify(String verify) {
    this.verify = verify;
  }

  protected boolean checkLegaled() {
    super.checkLegaled();
    if (("".equals(this.verify)) || (this.verify == null)) {
      return false;
    }
    if (("".equals(this.deviceId)) || (this.deviceId == null)) {
      return false;
    }
    if (("".equals(this.osVersion)) || (this.osVersion == null)) {
      return false;
    }
    return true;
  }
}