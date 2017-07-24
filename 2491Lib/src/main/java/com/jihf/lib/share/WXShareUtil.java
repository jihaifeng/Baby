package com.jihf.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;
import com.jihf.lib.utils.BitmapUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXShareUtil {

  private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

  //IWXAPI是第三方app和微信通信的openapi接口
  private IWXAPI api;
  private Context context;
  public static WXShareUtil instance;

  public static WXShareUtil getInstance(Context context) {
    if (null == instance) {
      synchronized (WXShareUtil.class) {
        if (null == instance) {
          instance = new WXShareUtil();
        }
      }
    }
    if (instance.api != null) {
      instance.api.unregisterApp();
    }
    instance.context = context;
    instance.regToWx();
    return instance;
  }

  //注册应用id到微信
  private void regToWx() {
    //通过WXAPIFactory工厂，获取IWXAPI的实例
    api = WXAPIFactory.createWXAPI(context, ShareConfig.getWxAppId(), true);
    //将应用的appId注册到微信
    api.registerApp(ShareConfig.getWxAppId());
  }

  /**
   * 分享文字到朋友圈或者好友
   *
   * @param text 文本内容
   * @param shareType 分享方式：好友还是朋友圈
   */
  public boolean shareText(String text, @WXShareType int shareType) {
    //初始化一个WXTextObject对象，填写分享的文本对象
    WXTextObject textObj = new WXTextObject();
    textObj.text = text;
    return share(textObj, text, shareType);
  }

  /**
   * 分享图片到朋友圈或者好友
   *
   * @param bmp 图片的Bitmap对象
   * @param shareType 分享方式：好友======>1  朋友圈====>2
   */
  public boolean sharePic(Bitmap bmp, @WXShareType int shareType) {
    //初始化一个WXImageObject对象
    WXImageObject imageObj = new WXImageObject(bmp);
    //设置缩略图
    Bitmap thumb = Bitmap.createScaledBitmap(bmp, 60, 60, true);
    bmp.recycle();
    return share(imageObj, thumb, shareType);
  }

  /**
   * 分享网页到朋友圈或者好友，视频和音乐的分享和网页大同小异，只是创建的对象不同。
   * 详情参考官方文档：
   * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340
   * &token=&lang=zh_CN
   *
   * @param url 网页的url
   * @param title 显示分享网页的标题
   * @param description 对网页的描述
   * @param shareType 分享方式：好友还是朋友圈
   * 分享到朋友圈，scene = SendMessageToWX.Req.WXSceneTimeline
   * 分享到好友， scene = SendMessageToWX.Req.WXSceneSession
   */
  public boolean shareUrl(String url, String title, Bitmap thumb, String description, @WXShareType int shareType) {
    //初试话一个WXWebpageObject对象，填写url
    WXWebpageObject webPage = new WXWebpageObject();
    webPage.webpageUrl = url;
    return share(webPage, title, thumb, description, shareType);
  }

  private boolean share(WXMediaMessage.IMediaObject mediaObject, Bitmap thumb, @WXShareType int shareType) {
    return share(mediaObject, null, thumb, null, shareType);
  }

  private boolean share(WXMediaMessage.IMediaObject mediaObject, String description, @WXShareType int shareType) {
    return share(mediaObject, null, null, description, shareType);
  }

  private boolean share(WXMediaMessage.IMediaObject mediaObject, String title, Bitmap thumb, String description,
      @WXShareType int shareType) {
    if (!isWXCanShare()) {
      return false;
    }
    //初始化一个WXMediaMessage对象，填写标题、描述
    WXMediaMessage msg = new WXMediaMessage(mediaObject);
    if (!TextUtils.isEmpty(title)) {
      msg.title = title;
    }
    if (!TextUtils.isEmpty(description)) {
      msg.description = description;
    }
    if (thumb != null) {
      msg.thumbData = BitmapUtils.cov2ByteArray(thumb,30);
    }
    //构造一个Req
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = String.valueOf(System.currentTimeMillis());
    req.message = msg;
    if (shareType == WXShareType.WX_FRIEND) {
      req.scene = SendMessageToWX.Req.WXSceneSession;
    } else {
      req.scene = SendMessageToWX.Req.WXSceneTimeline;
    }
    return api.sendReq(req);
  }
  public static Bitmap checkImageSize(Bitmap bitmap) {
    //防止超长图文件大小超过微信限制，需要进行截取，暂定比例上限为5
    final int MAX_RATIO = 5;
    Bitmap result = bitmap;
    if (bitmap != null) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      float ratio = width > height ? width*1.0f / height : height*1.0f / width;
      if (ratio > MAX_RATIO) {
        int size = Math.min(width, height);
        result = Bitmap.createBitmap(bitmap, 0, 0, size, size);
      }
    }
    return result;
  }
  private boolean isWXCanShare() {
    if (!api.isWXAppInstalled()) {
      Toast.makeText(context, "您未安装微信哦", Toast.LENGTH_SHORT).show();
      return false;
    } else if (!isSupportWX()) {
      Toast.makeText(context, "当前微信版本不支持分享", Toast.LENGTH_SHORT).show();
      return false;
    } else {
      return true;
    }
  }

  //判断是否支持转发到朋友圈
  //微信4.2以上支持，如果需要检查微信版本支持API的情况， 可调用IWXAPI的getWXAppSupportAPI方法,0x21020001及以上支持发送朋友圈
  private boolean isSupportWX() {
    int wxSdkVersion = api.getWXAppSupportAPI();
    return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
  }

}