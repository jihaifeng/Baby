package com.jihf.lib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-05 11:56
 * Mail：jihaifeng@raiyi.com
 */

public class BitmapUtils {
  /**
   * 读取一个缩放后的图片，限定图片大小，避免OOM
   *
   * @param uri 图片uri，支持“file://”、“content://”
   * @param maxWidth 最大允许宽度
   * @param maxHeight 最大允许高度
   *
   * @return 返回一个缩放后的Bitmap，失败则返回null
   */
  public static Bitmap covBitmapFromUri(Context context, Uri uri, int maxWidth, int maxHeight) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true; //只读取图片尺寸
    resolveUri(context, uri, options);

    //计算实际缩放比例
    int scale = 1;
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      if ((options.outWidth / scale > maxWidth && options.outWidth / scale > maxWidth * 1.4) || (options.outHeight
          / scale > maxHeight && options.outHeight / scale > maxHeight * 1.4)) {
        scale++;
      } else {
        break;
      }
    }

    options.inSampleSize = scale;
    options.inJustDecodeBounds = false;//读取图片内容
    options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
    Bitmap bitmap = null;
    try {
      bitmap = covBitmapFromUri(context, uri, options);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  private static void resolveUri(Context context, Uri uri, BitmapFactory.Options options) {
    if (uri == null) {
      return;
    }

    String scheme = uri.getScheme();
    if (ContentResolver.SCHEME_CONTENT.equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
      InputStream stream = null;
      try {
        stream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(stream, null, options);
      } catch (Exception e) {
        Log.w("resolveUri", "Unable to open content: " + uri, e);
      } finally {
        if (stream != null) {
          try {
            stream.close();
          } catch (IOException e) {
            Log.w("resolveUri", "Unable to close content: " + uri, e);
          }
        }
      }
    } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
      Log.w("resolveUri", "Unable to close content: " + uri);
    } else {
      Log.w("resolveUri", "Unable to close content: " + uri);
    }
  }

  public static void covBitmapFromUrl(String url, BitmapCovListener listener) {
    if (null == listener) {
      throw new NullPointerException("BitmapCovListener cannot being null.");
    }
    new Thread(new Runnable() {
      @Override public void run() {

        try {
          URL iconUrl = new URL(url);
          URLConnection conn = iconUrl.openConnection();
          HttpURLConnection http = (HttpURLConnection) conn;

          int length = http.getContentLength();

          conn.connect();
          // 获得图像的字符流
          InputStream is = conn.getInputStream();
          BufferedInputStream bis = new BufferedInputStream(is, length);
          Bitmap bm = BitmapFactory.decodeStream(bis);
          if (null != bm) {
            listener.covSuccess(bm);
          } else {
            listener.covFailure("bitmap is null.");
          }
          bis.close();
          is.close();// 关闭流
        } catch (Exception e) {
          listener.covFailure(e.getMessage());
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static Bitmap covBitmapFromUri(Context context, Uri uri, BitmapFactory.Options options) {
    if (uri == null) {
      return null;
    }

    Bitmap bitmap = null;
    String scheme = uri.getScheme();
    if (ContentResolver.SCHEME_CONTENT.equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
      InputStream stream = null;
      try {
        stream = context.getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(stream, null, options);
      } catch (Exception e) {
        Log.w("resolveUriForBitmap", "Unable to open content: " + uri, e);
      } finally {
        if (stream != null) {
          try {
            stream.close();
          } catch (IOException e) {
            Log.w("resolveUriForBitmap", "Unable to close content: " + uri, e);
          }
        }
      }
    } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
      Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
    } else {
      Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
    }

    return bitmap;
  }

  public static Bitmap covBitmapFromUriWithOpt(Context context, Uri uri) {

    return covBitmapFromUri(context, uri, getOpts(context));
  }

  private static BitmapFactory.Options getOpts(Context context) {
    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inDensity = context.getResources().getDisplayMetrics().densityDpi;
    opts.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
    return opts;
  }

  public static Bitmap decodeResWithOpt(Context context, int resId) {
    return BitmapFactory.decodeResource(context.getResources(), resId, getOpts(context));
  }

  /**
   * 把Bitmap转Byte
   */
  public static byte[] bitmap2Bytes(Bitmap bm) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
    return baos.toByteArray();
  }

  public static byte[] cov2ByteArray(Bitmap bmp, float size) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] result;
    try {
      bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
      int options = 100;
      while (output.toByteArray().length / 1024 >= size) {  //循环判断如果压缩后图片是否大于size kb,大于继续压缩
        output.reset();//重置baos即清空baos
        bmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
        if (options == 1) {
          break;
        }
        options -= 10;//每次都减少20
        if (options <= 0) {
          options = 1;
        }
      }

      result = output.toByteArray();

      Log.i("compressBitmap", "compressBitmap return length = " + result.length);

      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        output.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
