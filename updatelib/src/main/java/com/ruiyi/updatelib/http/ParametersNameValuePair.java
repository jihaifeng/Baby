package com.ruiyi.updatelib.http;

import java.io.Serializable;
import org.apache.http.NameValuePair;

public class ParametersNameValuePair implements NameValuePair, Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String value;

  public ParametersNameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}