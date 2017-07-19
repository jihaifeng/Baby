package com.ruiyi.updatelib.apppublish;

public abstract class BasePesponseModel extends BaseModel {
  public static final String RESULT_TYPE = "r";
  public static final String RESULT_FAIL_CAUSE = "m";
  public static final int FAIL = 0;
  public static final int NO_UPDATE = 1;
  public static final int CAN_UPDATE = 2;
  public static final int MUST_UPDATE = 3;
  protected int resutType;
  protected String resultFailCause;

  public int getResutType() {
    return this.resutType;
  }

  public void setResutType(int resutType) {
    this.resutType = resutType;
  }

  public String getResultFailCause() {
    return this.resultFailCause;
  }

  public void setResultFailCause(String resultFailCause) {
    this.resultFailCause = resultFailCause;
  }
}