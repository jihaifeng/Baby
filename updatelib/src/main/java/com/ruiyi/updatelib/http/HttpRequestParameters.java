package com.ruiyi.updatelib.http;

import android.net.Uri;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestParameters implements Serializable {
  private static final long serialVersionUID = 1L;
  private String methodName;
  private List<ParametersNameValuePair> parameters;
  private StringBuilder requsetString;

  public HttpRequestParameters() {
    this.parameters = new ArrayList();
    this.requsetString = new StringBuilder();
  }

  public void addParameters(String name, String value) {
    ParametersNameValuePair nameValuePair = new ParametersNameValuePair(name, value);
    this.parameters.add(nameValuePair);
    this.requsetString.append("&" + Uri.encode(name) + "=" + Uri.encode(value));
  }

  public String getRequsetString() {
    if ((this.requsetString == null) || (this.requsetString.length() == 0)) {
      return "";
    }
    return this.requsetString.toString().substring(1);
  }

  public String getMethodName() {
    return this.methodName;
  }

  public List<ParametersNameValuePair> getParameters() {
    return this.parameters;
  }
}