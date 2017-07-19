package com.ruiyi.updatelib.http;

public abstract interface HttpRequestCompletedListener {
  public abstract void httprequestCompleted(HttpResponseResultModel paramHttpResponseResultModel, boolean paramBoolean);

  public abstract void httprequestException(HttpResponseResultModel paramHttpResponseResultModel);
}