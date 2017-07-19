package com.ruiyi.updatelib.http;

import android.util.Log;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class HttpGetRequest implements AbstractHttpRequest {
  private String requestBody = "";
  private String requestUrl;
  private boolean isSingleton = false;
  public static boolean DEBUG = false;

  public HttpGetRequest(HttpRequestParameters httpRequestlParameters, String requestUrl) {
    this.requestUrl = requestUrl;

    if (httpRequestlParameters != null) {
      this.requestBody = obtainRequestString(httpRequestlParameters);
      if (DEBUG) {
        Log.e("HttpGetRequest", "requestUrl:" + requestUrl);
        Log.e("HttpGetRequest", "requestBody:" + this.requestBody);
      }
    }
  }

  public HttpResponseResultModel getResponse(boolean isStream) {
    HttpResponseResultModel httpResponseResultModel = new HttpResponseResultModel();

    HttpGet getRequest = new HttpGet(this.requestUrl + this.requestBody);

    int statusCode = 0;
    try {
      HttpClient httpClient = null;
      if (this.isSingleton) {
        httpClient = HttpClientFactory.getSingletonHttpClient();
      } else {
        HttpClientFactory f = new HttpClientFactory();
        httpClient = f.getHttpClient(-1);
      }

      HttpResponse response = httpClient.execute(getRequest);
      statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        httpResponseResultModel.equals(Boolean.valueOf(false));
        if ((statusCode >= 400) && (statusCode < 500)) {
          httpResponseResultModel.setException("网络请求失败,没有对应服务或达不到服务器");
        } else if (statusCode >= 500) {
          httpResponseResultModel.setException("服务器端程序错误");
        } else {
          httpResponseResultModel.setException("程序异常");
        }
        httpResponseResultModel.setResult(null);
      } else {
        httpResponseResultModel.setIsSucess(true);
        HttpEntity entity = response.getEntity();
        if (isStream) {
          httpResponseResultModel.setInputStream(entity.getContent());
        } else {
          byte[] bytes = EntityUtils.toByteArray(entity);
          String result = new String(bytes, "utf-8");
          httpResponseResultModel.setResult(result);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return httpResponseResultModel;
  }

  public InputStream getResponseLongFile(int timout) {
    return null;
  }

  private String obtainRequestString(HttpRequestParameters httpRequestlParameters) {
    String s = "?" + httpRequestlParameters.getRequsetString();
    return s;
  }

  public String getRequestBody() {
    return this.requestBody;
  }

  public String getRequestUrl() {
    return this.requestUrl;
  }

  public boolean isSingleton() {
    return this.isSingleton;
  }

  public void setSingleton(boolean isSingleton) {
    this.isSingleton = isSingleton;
  }
}