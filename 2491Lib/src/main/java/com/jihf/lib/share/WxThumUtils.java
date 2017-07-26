package com.jihf.lib.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.jihf.lib.utils.BitmapCovListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static com.tencent.mm.sdk.platformtools.Util.MAX_DECODE_PICTURE_SIZE;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Data：2017-07-25 08:41
 * Mail：jihaifeng@raiyi.com
 */

public class WxThumUtils {
  private static final String TAG = WxThumUtils.class.getSimpleName().trim();
  private int mWidth = 300;
  private int mHeight = 300;
  private int mMaxSize = 30;//Kb
  private static BitmapFactory.Options mOptions;

  public static WxThumUtils get() {
    return new WxThumUtils();
  }

  public WxThumUtils width(int w) {
    mWidth = w;
    return this;
  }

  public WxThumUtils height(int h) {
    mHeight = h;
    return this;
  }

  public WxThumUtils maxSize(int size) {
    mMaxSize = size;
    return this;
  }

  /**
   * 第一步 获取一个较小的缩略图
   *
   * @return
   */
  public static Bitmap covBitmapFromUrl(String url, BitmapCovListener listener) {
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
          if (null == mOptions) {
            mOptions = new BitmapFactory.Options();
          }
          mOptions.inJustDecodeBounds = true;
          Bitmap bitmap = BitmapFactory.decodeStream(bis);
          Bitmap bm = BitmapFactory.decodeStream(bis, null, mOptions);
          if (null != bitmap) {
            listener.covSuccess(bitmap);
          } else {
            listener.covFailure("bitmap is null.");
          }
          if (null != bm) {
            bm.recycle();
            bm = null;
          }
          bis.close();
          is.close();// 关闭流
        } catch (Exception e) {
          listener.covFailure(e.getMessage());
          e.printStackTrace();
        }
      }
    }).start();
    return null;
  }

  public static Bitmap extractThumbNail(Bitmap bitmap, final int height, final int width, final boolean crop) {
    BitmapFactory.Options options = mOptions;
    if (null == options) {
      options = new BitmapFactory.Options();
    }
    options.outHeight = options.outHeight == 0 ? -1 : options.outHeight;
    options.outWidth = options.outWidth == 0 ? -1 : options.outWidth;
    if (height <= 0 || width <= 0) {
      Log.i(TAG, "extractThumbNail: \nheight：" + height + "\nwidth：" + width + "\noptions：" + options);
      return null;
    }
    try {
      options.inJustDecodeBounds = true;
      final double beY = options.outHeight * 1.0 / height;
      final double beX = options.outWidth * 1.0 / width;
      Log.i(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
      options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
      if (options.inSampleSize <= 1) {
        options.inSampleSize = 1;
      }

      // NOTE: out of memory error
      while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
        options.inSampleSize++;
      }

      int newHeight = height;
      int newWidth = width;
      if (crop) {
        if (beY > beX) {
          newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
        } else {
          newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
        }
      } else {
        if (beY < beX) {
          newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
        } else {
          newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
        }
      }

      options.inJustDecodeBounds = false;

      Log.i(TAG, "newWidth="
          + newWidth
          + "newHeight="
          + newHeight
          + ", options.outWidth="
          + options.outWidth
          + "options.outHeight"
          + options.outHeight
          + ", options.inSampleSize="
          + options.inSampleSize);
      Log.i(TAG, "bitmap decoded size=" + bitmap.getWidth() + "x" + bitmap.getHeight());
      final Bitmap scale = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
      if (scale != null) {
        bitmap.recycle();
        bitmap = scale;
      }

      if (crop) {
        final Bitmap cropped =
            Bitmap.createBitmap(bitmap, (bitmap.getWidth() - width) >> 1, (bitmap.getHeight() - height) >> 1, width,
                height);
        if (cropped == null) {
          return bitmap;
        }

        bitmap.recycle();
        bitmap = cropped;
        Log.i(TAG, "bitmap croped size=" + bitmap.getWidth() + "x" + bitmap.getHeight());
      }
      return bitmap;
    } catch (final OutOfMemoryError e) {
      Log.e(TAG, "decode bitmap failed: " + e.getMessage());
      options = null;
    }

    return null;
  }

  /**
   * 第二步 裁剪该缩略图，防止是那种长图
   *
   * @param bitmap
   *
   * @return
   */
  public static Bitmap checkImageSize(Bitmap bitmap) {
    //防止超长图文件大小超过微信限制，需要进行截取，暂定比例上限为5
    final int MAX_RATIO = 5;
    Bitmap result = bitmap;
    if (bitmap != null) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      float ratio = width > height ? width * 1.0f / height : height * 1.0f / width;
      if (ratio > MAX_RATIO) {
        int size = Math.min(width, height);
        result = Bitmap.createBitmap(bitmap, 0, 0, size, size);
      }
    }
    return result;
  }

  /**
   * 第三步 压缩图片使其在32k以下
   *
   * @param bitmapp
   * @param size
   *
   * @return
   */
  public static byte[] compressBitmapToData(Bitmap bitmapp, float size) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] result;
    try {
      bitmapp.compress(Bitmap.CompressFormat.JPEG, 100, output);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
      int options = 100;
      while (output.toByteArray().length / 1024 >= size) {  //循环判断如果压缩后图片是否大于size kb,大于继续压缩
        output.reset();//重置baos即清空baos
        bitmapp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
        if (options == 1) {
          break;
        }
        options -= 10;//每次都减少20
        if (options <= 0) {
          options = 1;
        }
      }

      result = output.toByteArray();

      Log.i(TAG, "compressBitmap return length = " + result.length);

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
