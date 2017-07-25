#### 关于Android开发不能调起微信分享可能原因：
- **检查是否使用的是签名包，非签名包不能调试微信分享功能**
- **检查是否有自定义分享缩略图（thumbData）,如果有，检查自定义缩略图的大小，微信规定自定义缩略图大小不能超过32kb**
- **检查是否有自定义描述内容（description）,如果有，检查description的长度，微信限制description长度不得超过1024**
- **关于微信分享的其他限制可查看源码WXMediaMessage.class，下面截取其中一段代码：**
```
 final boolean checkArgs() {
    if(this.getType() != 8 || this.thumbData != null && this.thumbData.length != 0) {
      if(this.thumbData != null && this.thumbData.length > '耀') {
        Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, thumbData is invalid");
        return false;
      } else if(this.title != null && this.title.length() > 512) {
        Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, title is invalid");
        return false;
      } else if(this.description != null && this.description.length() > 1024) {
        Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, description is invalid");
        return false;
      } else if(this.mediaObject == null) {
        Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, mediaObject is null");
        return false;
      } else {
        return this.mediaObject.checkArgs();
      }
    } else {
      Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, thumbData should not be null when send emoji");
      return false;
    }
  }
```