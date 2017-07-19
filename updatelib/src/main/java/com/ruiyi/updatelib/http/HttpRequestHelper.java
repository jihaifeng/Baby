package com.ruiyi.updatelib.http;

import android.os.Handler;
import android.os.Message;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpRequestHelper implements Runnable {
  private AbstractHttpRequest abstractHttpRequest;
  private Handler handler;
  private HttpRequestCompletedListener httpRequestCompletedListener;
  private boolean IsLongFile = false;

  private HttpResponseResultModel httpResponseResultModel = null;
  private InputStream in;
  private boolean needStream = false;

  private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

  private static ThreadPoolExecutor executor =
      (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

  public HttpRequestHelper(AbstractHttpRequest abstractHttpRequest) {
    this.abstractHttpRequest = abstractHttpRequest;
    this.handler = new Handler() {
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (HttpRequestHelper.this.httpResponseResultModel.isIsSucess()) {
          HttpRequestHelper.this.httpRequestCompletedListener.httprequestCompleted(
              HttpRequestHelper.this.httpResponseResultModel, HttpRequestHelper.this.needStream);
        } else {
          HttpRequestHelper.this.httpRequestCompletedListener.httprequestException(
              HttpRequestHelper.this.httpResponseResultModel);
        }
      }
    };
  }

  public void startHttpRequest(boolean needStream) {
    this.needStream = needStream;
    executor.execute(this);
  }

  public void run() {
    if (this.IsLongFile) {
      this.in = this.abstractHttpRequest.getResponseLongFile(300000);
    } else {
      this.httpResponseResultModel = this.abstractHttpRequest.getResponse(this.needStream);
    }
    this.handler.sendEmptyMessage(0);
  }

  public AbstractHttpRequest getAbstractHttpRequest() {
    return this.abstractHttpRequest;
  }

  public void setAbstractHttpRequest(AbstractHttpRequest abstractHttpRequest) {
    this.abstractHttpRequest = abstractHttpRequest;
  }

  public void setHttpRequestCompletedListener(HttpRequestCompletedListener httpRequestCompletedListener) {
    this.httpRequestCompletedListener = httpRequestCompletedListener;
  }

  public boolean isIsLongFile() {
    return this.IsLongFile;
  }

  public void setIsLongFile(boolean isLongFile) {
    this.IsLongFile = isLongFile;
  }

  public InputStream getIn() {
    return this.in;
  }
}