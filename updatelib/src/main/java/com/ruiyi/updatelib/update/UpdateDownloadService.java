package com.ruiyi.updatelib.update;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.ruiyi.updatelib.R;
import java.io.File;

public class UpdateDownloadService extends Service {
  private String file_name = "116114";

  public static final int NOTIFY_DOWNLOAD_ID = 1161141;

  public enum versionInfoField {
    filename, filetype, version, description
  }

  @Override public void onCreate() {
    super.onCreate();
  }

  @SuppressWarnings ("deprecation") @Override public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    if (intent != null) {
      String action = intent.getAction();
      if (action != null) {
        url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(apk_name)) {
          apk_name = intent.getStringExtra("apk_name");
          if (apk_name.contains("114")) {
            apk_name = "114天气";
          }
        }
        if (action.equals("down_apk") && !isNotifiction && !UpdateDownloaderManager.getInstance().isUpdate()) {
          isNotifiction = true;
          // 测试代码
          // url =
          // "http://file2.bao.raiyi.com/access/3242701/116114_4.5.13_huanliang.apk";//
          // intent.getStringExtra("url");//"http://appapple.sinaapp.com/apphtml/anzhuoshichang_87.apk";
          // "http://file2.bao.raiyi.com/access/3242701/116114_4.5.13_huanliang.apk";//
          // 正规代码

          downLoad(apk_name, url);
        } else if (action.equals("down_apk") && UpdateDownloaderManager.getInstance().isUpdate()) {

          if (UpdateDownloaderManager.getInstance().updateDownloader != null && !url.equals(
              UpdateDownloaderManager.getInstance().updateDownloader.getUrl())) {
            Toast.makeText(this, "正在下载其他应用，请稍后", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "正在下载，请稍后", Toast.LENGTH_SHORT).show();
          }
        }
        Log.v("url==>", url);
        Log.v("apk_name==>", apk_name);
      }
    }
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  public void downLoad(String fileName, final String url) {
    file_name = fileName;
    String apkFileParent =
        new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
            .append("")
            .toString();
    if (!file_name.endsWith(".apk")) {
      apk_path = apkFileParent + file_name + ".apk";
    } else {
      apk_path = apkFileParent + file_name;
    }
    UpdateDownloaderManager.getInstance()
        .startTask(getApplicationContext(), url, apk_path, new UpdateDownloadListener() {

          private int fileSize = 0;

          @Override public void onStart() {
            // TODO Auto-generated method stub

          }

          @Override public void onProgress(int compplete) {
            // TODO Auto-generated method stub
            Message msg = new Message();
            float percent = ((float) compplete / (float) fileSize);
            msg.arg1 = (int) (percent * 100) < 0 ? 99 : (int) (percent * 100);
            msg.what = 0;
            // updateHandler.sendMessage(msg);
            updateProgress(msg);
          }

          @Override public void onPause() {
            // TODO Auto-generated method stub

          }

          @Override public void onGetFileInfo(int fileSize, int completeSize) {
            // TODO Auto-generated method stub
            this.fileSize = fileSize;
          }

          @Override public void onFinished() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = 1;
            // updateHandler.sendMessage(msg);
            updateProgress(msg);
            UpdateDownloaderManager.getInstance().removeTask(url);
          }

          @Override public void onFailed() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = -1;
            // updateHandler.sendMessage(msg);
            updateProgress(msg);
            UpdateDownloaderManager.getInstance().pauseTask(url);
          }
        });
  }

  private String url = "";
  private String apk_name = "";
  private String apk_path = "";
  private RemoteViews updateContentView;
  private NotificationManager manager;
  private Notification notification;
  boolean isUpdate = false;
  boolean isNotifiction = false;
  long currentTime = System.currentTimeMillis();
  PendingIntent contextIntent;
  PendingIntent contentIntent;
  @SuppressLint ("HandlerLeak") private Handler updateHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
    }

    ;
  };
  Notification.Builder builder;

  @TargetApi (Build.VERSION_CODES.JELLY_BEAN) @SuppressWarnings ("deprecation")
  public void updateProgress(Message msg) {
    builder = new Notification.Builder(getApplicationContext());
    if (updateContentView == null) {
      manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      updateContentView = new RemoteViews(getPackageName(), R.layout.dloadnotification_layout);
      updateContentView.setProgressBar(R.id.progress, 100, 0, false);
      notification = new Notification(R.mipmap.ic_launcher, "下载中", System.currentTimeMillis());
      notification.contentView = updateContentView;
      isNotifiction = true;
      Toast.makeText(this, "开始下载，请稍后", Toast.LENGTH_SHORT).show();
    }

    // 正在下载
    if (msg.what == 0) {
      updateContentView.setProgressBar(R.id.progress, 100, msg.arg1, false);
      updateContentView.setTextViewText(R.id.rate, msg.arg1 + "%");
      if (!isUpdate) {
        isUpdate = true;
        notification = new Notification(R.mipmap.ic_launcher, "下载中", System.currentTimeMillis());
        //notification.icon = R.drawable.icon_down;
        notification.contentView = updateContentView;
        manager.cancel(0114116);
        // notification.tickerText = "正在为您更新新版本";
        notification.contentView.setTextViewText(R.id.fileName, "正在下载 " + apk_name);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;// 放入正在运行组
      }
    }

    // 下载完成
    if (msg.what == 1) {
      updateContentView.setTextViewText(R.id.rate, msg.arg1 + "%");
      manager.cancel(0114116);
      Intent apkIntent = new Intent();
      apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      apkIntent.setAction(Intent.ACTION_VIEW);
      Uri uri = Uri.fromFile(new File(apk_path));
      apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
      // context.startActivity(intent);
      contextIntent = PendingIntent.getActivity(UpdateDownloadService.this, 0, apkIntent, 0);
      builder.setTicker("应用下载完成");
      builder.setContentTitle("下载完成，点击安装 " + apk_name);
      builder.setContentText(apk_name + " 下载完成，点击安装");
      builder.setContentIntent(contextIntent);
      builder.setSmallIcon(R.mipmap.ic_launcher);
      notification = builder.build();
      //notification.setLatestEventInfo(getApplicationContext(), "下载完成，点击安装 " + apk_name , apk_name + " 下载完成，点击安装",
      // contextIntent);
      //notification.tickerText = "应用下载完成";
      notification.flags |= Notification.FLAG_AUTO_CANCEL; // 在通知栏上点击此通知后自动清除此通知
      notification.flags |= Notification.FLAG_ONGOING_EVENT;// 放入正在运行组
      isUpdate = false;
      isNotifiction = false;
      apk_name = "";
      //自动安装
      //Intent intent=new Intent(Intent.ACTION_VIEW);
      //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动新的activity
      //intent.setDataAndType(uri, "application/vnd.android.package-archive");
      startActivity(apkIntent);
    }

    // 下载失败
    if (msg.what == -1) {
      manager.cancel(0114116);
      Toast.makeText(getApplicationContext(), "下载暂停，请重试", Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(getApplicationContext(), UpdateDownloadService.class);
      intent.setAction("down_apk");
      intent.putExtra("url", url);
      intent.putExtra("apk_name", apk_name);
      contentIntent =
          PendingIntent.getService(UpdateDownloadService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
      //notification = new Notification(R.drawable.icon_down, "下载暂停", System.currentTimeMillis());
      //notification.icon = R.drawable.icon_down;
      //notification.contentView = updateContentView;
      //notification.setLatestEventInfo(getApplicationContext(), "下载暂停，点击继续", "下载暂停，可能是网络原因", contentIntent);
      builder.setTicker("下载暂停");
      builder.setWhen(System.currentTimeMillis());
      builder.setContentIntent(contentIntent);
      builder.setContentTitle("下载暂停，点击继续");
      builder.setContentText("下载暂停，可能是网络原因");
      builder.setContent(updateContentView);

      notification = builder.build();
      notification.flags |= Notification.FLAG_ONGOING_EVENT;// 放入正在运行组
      isUpdate = false;
      isNotifiction = false;
    }
    builder.setSmallIcon(R.mipmap.ic_launcher);
    try {
      manager.notify(0114116, notification);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public IBinder onBind(Intent intent) {
    return new iBinder();
  }

  public class iBinder extends Binder {
    public String getname() {
      return getName();
    }
  }

  /**
   * 在服务中自定义一个方法，我们要在activity中调用这个方法
   */
  public String getName() {
    return "";
  }
}
