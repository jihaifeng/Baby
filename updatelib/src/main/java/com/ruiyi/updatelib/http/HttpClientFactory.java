package com.ruiyi.updatelib.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientFactory {
  private HttpClient httpClient = null;

  private static HttpClient httpClientSingleton = null;

  public HttpClient getHttpClient(int timeout) {
    this.httpClient = createDefaultHttpClient(timeout);

    return this.httpClient;
  }

  public static HttpClient getSingletonHttpClient() {
    if (httpClientSingleton == null) {
      httpClientSingleton = createDefaultHttpClient(-1);
    }

    return httpClientSingleton;
  }

  public void shutdownHttpClient() {
    if ((this.httpClient != null) && (this.httpClient.getConnectionManager() != null)) {
      this.httpClient.getConnectionManager().shutdown();
    }
  }

  private static HttpClient createDefaultHttpClient(int timeout) {
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    HttpProtocolParams.setUseExpectContinue(params, true);

    if (timeout >= 0) {
      HttpConnectionParams.setConnectionTimeout(params, timeout);
      HttpConnectionParams.setSoTimeout(params, timeout);
    } else {
      HttpConnectionParams.setConnectionTimeout(params, 30000);

      HttpConnectionParams.setSoTimeout(params, 30000);
    }

    SchemeRegistry schReg = new SchemeRegistry();
    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

    return new DefaultHttpClient(conMgr, params);
  }
}