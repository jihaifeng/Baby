package com.ruiyi.updatelib.http;

import java.io.InputStream;

public abstract interface AbstractHttpRequest {
  public abstract InputStream getResponseLongFile(int paramInt);

  public abstract HttpResponseResultModel getResponse(boolean paramBoolean);
}