package com.ruiyi.updatelib.apppublish;

import android.content.Context;
import android.util.Log;
import com.ruiyi.updatelib.http.HttpGetRequest;
import com.ruiyi.updatelib.http.HttpRequestCompletedListener;
import com.ruiyi.updatelib.http.HttpRequestHelper;
import com.ruiyi.updatelib.http.HttpRequestParameters;
import com.ruiyi.updatelib.http.HttpResponseResultModel;

public class AppPublishHandler extends PublishHandler {
  private static final String TAG = "AppPublishHandler";
  private static boolean DEBUG = false;
  private OnUpdateListener onUpdateListener;
  private OnIntallRListener onIntallRListener;
  private static String CHECK_UPDATE_URL = "http://app.raiyi.com/publish/checkversion";
  private static String INSTALL_RESPONSE_URL = "http://app.raiyi.com/publish/modifyclientinfo";

  public void setDebugMode(boolean isDebug) {
    DEBUG = isDebug;
    if (DEBUG) {
      Log.d("AppPublishHandler", "debug is on");
    }
  }

  public void setCheckUpdateUrl(String updateurl) {
    CHECK_UPDATE_URL = updateurl;
  }

  public void setInstallResponseUrl(String installResponseUrl) {
    INSTALL_RESPONSE_URL = installResponseUrl;
  }

  protected void checkForUpdateRequest(Context context, HttpRequestParameters ps) {
    HttpGetRequest httpGet = new HttpGetRequest(ps, CHECK_UPDATE_URL);
    if (DEBUG) {
      Log.d("AppPublishHandler", "获取更新信息完整url:" + httpGet.getRequestUrl() + httpGet.getRequestBody());
    }
    HttpRequestHelper httpRequestHelper = new HttpRequestHelper(httpGet);
    httpRequestHelper.setHttpRequestCompletedListener(new HttpRequestCompletedListener() {
      public void httprequestException(HttpResponseResultModel httpResponseResultModel) {
        if (AppPublishHandler.DEBUG) {
          Log.d("AppPublishHandler", "服务器返回错误.");
        }
        if (AppPublishHandler.this.onUpdateListener != null) {
          AppPublishHandler.this.onUpdateListener.httpFail(httpResponseResultModel.getException());
        }
      }

      public void httprequestCompleted(HttpResponseResultModel httpResponseResultModel, boolean isStream) {
        String result = httpResponseResultModel.getResult();
        if (AppPublishHandler.DEBUG) {
          Log.d("AppPublishHandler", "服务器成功,响应结果: " + result);
        }

        CFUResponseModel model = ModelIniter.initCFUResponseModel(result);
        if (AppPublishHandler.this.onUpdateListener != null) {
          AppPublishHandler.this.checkIsUpdateStatu(model);
        }
      }
    });
    httpRequestHelper.startHttpRequest(false);
  }

  protected void InstallFeedbackInfoRequest(Context context, HttpRequestParameters ps) {
    HttpGetRequest httpGet = new HttpGetRequest(ps, INSTALL_RESPONSE_URL);
    if (DEBUG) {
      Log.d("AppPublishHandler", "安装反馈完整url:" + httpGet.getRequestUrl() + httpGet.getRequestBody());
    }
    HttpRequestHelper HttpRequestHelper = new HttpRequestHelper(httpGet);
    HttpRequestHelper.setHttpRequestCompletedListener(new HttpRequestCompletedListener() {
      public void httprequestException(HttpResponseResultModel httpResponseResultModel) {
        if (AppPublishHandler.DEBUG) {
          Log.d("AppPublishHandler", "服务器错误.");
        }
      }

      public void httprequestCompleted(HttpResponseResultModel httpResponseResultModel, boolean isStream) {
        String result = httpResponseResultModel.getResult();
        if (AppPublishHandler.DEBUG) {
          Log.d("AppPublishHandler", "服务器成功,响应结果: " + result);
        }

        InstallFeedbackResponseModel model = ModelIniter.initInstallFeedbackResponseModel(result);
        if (AppPublishHandler.this.onIntallRListener != null) {
          AppPublishHandler.this.checkResponse(model);
        }
      }
    });
    HttpRequestHelper.startHttpRequest(false);
  }

  protected void ObtainNewAppInfoRequest(Context context, HttpRequestParameters ps) {
  }

  private void checkIsUpdateStatu(CFUResponseModel model) {
    if (model == null) {
      this.onUpdateListener.httpFail("");

      return;
    }
    int type = model.getResutType();
    if (type == 0) {
      this.onUpdateListener.httpFail(model.getResultFailCause());
    } else if (type == 1) {
      this.onUpdateListener.noUpdate();
    } else if (type == 2) {
      this.onUpdateListener.canUpdate(model);
    } else if (type == 3) {
      this.onUpdateListener.mustUpdate(model);
    } else {
      this.onUpdateListener.httpFail("");
    }
  }

  private void checkResponse(InstallFeedbackResponseModel model) {
    if (model == null) {
      this.onIntallRListener.httpFail("");
      return;
    }
    int type = model.getResutType();
    if (type == 1) {
      this.onIntallRListener.succeed();
    } else if (type == 0) {
      this.onIntallRListener.httpFail(model.getResultFailCause());
    } else {
      this.onIntallRListener.httpFail("");
    }
  }

  public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
    this.onUpdateListener = onUpdateListener;
  }

  public void setOnIntallRListener(OnIntallRListener onIntallRListener) {
    this.onIntallRListener = onIntallRListener;
  }

  public static abstract interface OnIntallRListener extends ResponseListener {
    public abstract void succeed();
  }

  public static abstract interface OnUpdateListener extends ResponseListener {
    public abstract void noUpdate();

    public abstract void mustUpdate(CFUResponseModel paramCFUResponseModel);

    public abstract void canUpdate(CFUResponseModel paramCFUResponseModel);
  }
}