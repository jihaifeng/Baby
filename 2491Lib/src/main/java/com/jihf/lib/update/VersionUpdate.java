package com.jihf.lib.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jihf.lib.R;
import com.jihf.lib.utils.AppUtils;
import com.jihf.lib.utils.StringUtils;
import com.jihf.lib.wxapi.ProgressDialogCommon;
import com.ruiyi.updatelib.apppublish.AppPublishHandler;
import com.ruiyi.updatelib.apppublish.CFUResponseModel;
import com.ruiyi.updatelib.update.UpdateDownloadService;

public class VersionUpdate {
  private static final String TAG = VersionUpdate.class.getSimpleName();
  private ProgressDialogCommon progres;
  private CFUResponseModel tempCFUResponseModel;
  private Dialog dialog;
  @SuppressLint ("StaticFieldLeak") private static VersionUpdate instance;
  private Context mContext;

  private static String APP_TAG = "2491Joke"; // 应用代码===》发布平台查看看

  private String TAG_MARKET = "2491";//

  public static void setUpdateTag(String tag) {
    if (StringUtils.isTrimEmpty(tag)) {
      return;
    }
    APP_TAG = tag;
  }

  public VersionUpdate(Context context) {
    if (null == context) {
      throw new NullPointerException("the context of versionUpdate is null.");
    }
    this.mContext = context;
    TAG_MARKET = AppUtils.getMarket();
  }

  public static VersionUpdate getInstance(Context context) {
    if (null == instance) {
      synchronized (VersionUpdate.class) {
        if (null == instance) {
          instance = new VersionUpdate(context);
        }
      }
    }
    return instance;
  }

  protected final Handler hand = new Handler(Looper.getMainLooper()) {
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      try {
        if (tempCFUResponseModel != null) {
          showUpdateDialog(tempCFUResponseModel.getAppVersion(), tempCFUResponseModel.getAppVersionComment(), msg.what);
        }
      } catch (Exception e) {
        Log.i(TAG, "handleMessage: " + e.getMessage());
      }
    }
  };

  @SuppressLint ("InflateParams") private void showUpdateDialog(String mCode, String mMsg, final int tag) {
    if (null == mContext) {
      throw new NullPointerException("the context of versionUpdate is null.");
    }
    dialog = new Dialog(mContext, R.style.dialog);
    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update, null);
    ImageView update = (ImageView) view.findViewById(R.id.btn_update);
    TextView code = (TextView) view.findViewById(R.id.tv_code);
    ImageView close = (ImageView) view.findViewById(R.id.iv_close);
    TextView msg = (TextView) view.findViewById(R.id.tv_msg);
    code.setText(mCode != null ? "V" + mCode : "v5.1.5");
    msg.setMovementMethod(ScrollingMovementMethod.getInstance());
    msg.setText(mMsg != null ? mMsg : "");
    update.setOnClickListener(v -> downLoadNewVersion());
    close.setOnClickListener(v -> hideUpdateDialog());
    dialog.setOnDismissListener(dialog1 -> {
      if (tag == 1) {
        // 强制更新
        Log.i(TAG, "showUpdateDialog: 强制更新");
      }
    });
    dialog.setContentView(view);
    dialog.show();
  }

  private void hideUpdateDialog() {
    if (null != dialog && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  //下载新版本
  private void downLoadNewVersion() {
    if (null == mContext) {
      throw new NullPointerException("the context of versionUpdate is null.");
    }
    hideUpdateDialog();
    String down_url = tempCFUResponseModel.getAppLoadUrl();
    if (down_url != null && down_url.contains("http")) {
      updateNewVersion(mContext, tempCFUResponseModel, getAppName() + "_v" + tempCFUResponseModel.getAppVersion());
    } else {
      Toast.makeText(mContext, "服务器正在更新，请稍后再试.", Toast.LENGTH_LONG).show();
    }
  }

  private void updateNewVersion(Context context, CFUResponseModel cFUResponseModel, String fileName) {
    if (null == context) {
      throw new NullPointerException("the context of versionUpdate is null.");
    }
    if (cFUResponseModel != null) {
      Intent intent = new Intent(context.getApplicationContext(), UpdateDownloadService.class);
      intent.setAction("down_apk");
      intent.putExtra("url", "" + cFUResponseModel.getAppLoadUrl());
      intent.putExtra("apk_name", "" + fileName);
      context.getApplicationContext().startService(intent);
    }
  }

  public void checkForUpdate(boolean ishowDialog, final boolean isToastMessage, final boolean isZg) {
    if (null == mContext) {
      throw new NullPointerException("the context of versionUpdate is null.");
    }
    if (ishowDialog) {
      try {
        if (progres == null) {
          progres = new ProgressDialogCommon(mContext, "正在检测是否有新的版本...");
        }
        progres.setCancelable(false);
        progres.showProgressDialog();
      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    AppPublishHandler app = new AppPublishHandler();
    app.setDebugMode(true);
    app.setOnUpdateListener(new AppPublishHandler.OnUpdateListener() {
      @Override public void httpFail(String cause) {
        if (progres != null) {
          progres.dismissDialog();
        }
        if (isToastMessage) {
          if (cause != null && !"".equals(cause.trim())) {
            Toast.makeText(mContext, "" + cause.trim(), Toast.LENGTH_LONG).show();
          } else {
            Toast.makeText(mContext, "网络请求服务器失败,", Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void noUpdate() {
        if (progres != null) {
          progres.dismissDialog();
        }
        if (isToastMessage) {
          Toast.makeText(mContext, "该版本已是最新版本", Toast.LENGTH_LONG).show();
        }
      }

      @Override public void mustUpdate(CFUResponseModel model) {
        Log.e("升级", "必须升级");
        if (progres != null) {
          progres.dismissDialog();
        }
        tempCFUResponseModel = model;
        hand.sendEmptyMessage(1);
      }

      @Override public void canUpdate(CFUResponseModel model) {
        Log.e("升级", "可以升级");
        if (progres != null) {
          progres.dismissDialog();
        }
        tempCFUResponseModel = model;
        hand.sendEmptyMessage(0);
      }
    });

    if (!isZg) {
      app.checkForUpdate(mContext, APP_TAG, null, TAG_MARKET, null, null, "1");
    } else {
      app.checkForUpdate(mContext, APP_TAG, null, TAG_MARKET, null, null);
    }
  }

  /**
   * 获取App名称
   *
   * @return App名称
   */
  private String getAppName() {
    if (null == mContext || isSpace(mContext.getPackageName())) {
      return "app";
    }
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
      return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static boolean isSpace(String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
