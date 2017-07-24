package com.jihf.lib.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.jihf.lib.share.ShareConfig;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
  public static final String TAG = WXEntryActivity.class.getSimpleName().trim();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IWXAPI api = WXAPIFactory.createWXAPI(this, ShareConfig.getWxAppId(), false);
    api.handleIntent(getIntent(), this);
    finish();
  }

  @Override public void onReq(BaseReq baseReq) {

  }

  @Override public void onResp(BaseResp baseResp) {
    String result;
    switch (baseResp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        result = "分享成功";
        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        result = null;
        break;
      default:
        result = "分享失败";
        break;
    }

    if (!TextUtils.isEmpty(result)) {
      Log.i(TAG, "onResp: " + result);
    }
    Log.i(TAG, "onResp: " + baseResp.errStr + "\n" + baseResp.errCode);
  }
}