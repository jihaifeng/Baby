package com.ruiyi.updatelib.apppublish;

public class CFUTransportModel extends BaseUploadModel {
  protected boolean checkLegaled() {
    super.checkLegaled();
    if (("".equals(this.appOrigin)) || (this.appOrigin == null)) {
      return false;
    }
    return true;
  }
}