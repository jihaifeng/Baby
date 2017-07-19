package com.ruiyi.updatelib.http;

import java.io.InputStream;
import java.io.Serializable;

public class HttpResponseResultModel implements Serializable {
  private static final long serialVersionUID = 1L;
  private String result;
  private String exception;
  private boolean IsSucess;
  private InputStream inputStream;

  public boolean isIsSucess() {
    return this.IsSucess;
  }

  public void setIsSucess(boolean isSucess) {
    this.IsSucess = isSucess;
  }

  public String getResult() {
    return this.result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getException() {
    return this.exception;
  }

  public void setException(String ex) {
    this.exception = ex;
  }

  public InputStream getInputStream() {
    return this.inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }
}