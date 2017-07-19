package com.ruiyi.updatelib.update;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class UpdateDownloader {
  private String urlstr;
  private String localfile;
  private int fileSize;
  private Context context;
  private UpdateDownloadInfo _info = null;
  private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
  private static final int DOWNLOADING = 2;
  private static final int PAUSE = 3;
  private int state = INIT;
  private boolean isUpdate = false;

  public boolean isUpdate() {
    return isUpdate;
  }

  public void setUpdate(boolean isUpdate) {
    this.isUpdate = isUpdate;
  }

  public String getUrl() {
    return urlstr == null ? "" : urlstr;
  }

  private static final int DOWNLOADSTATE_PAUSE = 0;
  private static final int DOWNLOADSTATE_FINISHED_PART = 1;
  private static final int DOWNLOADSTATE_FAILED = 2;

  private UpdateDownloadListener downloadListener;

  public void setDownloadListener(UpdateDownloadListener listener) {
    this.downloadListener = listener;
  }

  public UpdateDownloader(String urlstr, String localfile, Context context) {
    this.urlstr = urlstr;
    this.localfile = localfile;
    this.context = context;
  }

  public boolean isdownloading() {
    return state == DOWNLOADING;
  }

  private UpdateLoadInfo getDownloaderInfo() {
    if (isFirst(urlstr)) {
      Log.v("TAG", "isFirst");
      if (init()) {
        _info = new UpdateDownloadInfo(3, 0, fileSize - 1, 0, urlstr);
        UpdateDao.getInstance(context).saveInfos(_info);
        UpdateLoadInfo loadInfo = new UpdateLoadInfo(fileSize, 0, urlstr);
        return loadInfo;
      } else {
        return null;
      }
    } else {
      _info = UpdateDao.getInstance(context).getInfos(urlstr);
      if (_info != null) {
        int size = 0;
        int compeleteSize = 0;
        compeleteSize += _info.getCompeleteSize();
        size += _info.getEndPos() - _info.getStartPos() + 1;
        return new UpdateLoadInfo(size, compeleteSize, urlstr);
      } else {
        return null;
      }
    }
  }

  /**
   * 初始化下载
   */
  private boolean init() {
    try {
      URL url = new URL(urlstr);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(5000);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept-Encoding", "identity");
      connection.connect();
      fileSize = connection.getContentLength();

      File file = new File(localfile);
      if (!file.exists()) {
        file.createNewFile();
      } else {
        file.delete();
        file.createNewFile();
      }
      RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
      accessFile.setLength(fileSize);
      accessFile.close();
      connection.disconnect();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 判断是否存在该下载
   */
  private boolean isFirst(String urlstr) {
    return UpdateDao.getInstance(context).isHasInfors(urlstr);
  }

  /**
   * 开始下载
   */
  public void download() {
    new AsyncTask<Object, Object, UpdateLoadInfo>() {

      @Override protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        if (downloadListener != null) {
          downloadListener.onStart();
        }
      }

      @Override protected UpdateLoadInfo doInBackground(Object... params) {
        return getDownloaderInfo();
      }

      @Override protected void onPostExecute(UpdateLoadInfo result) {
        super.onPostExecute(result);
        if (result != null) {
          //                    if (result.getFileSize() > 25 * 1024 * 1024) {
          //                        state = DOWNLOADSTATE_FINISHED_PART;
          //                        Intent i = new Intent(Intent.ACTION_VIEW);
          //                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //                        i.setData(Uri.parse(result.getUrlstring()));
          //                        context.startActivity(i);
          //                        Toast.makeText(context, "文件较大，跳转至浏览器下载", Toast.LENGTH_SHORT).show();
          //                        return;
          //                    }
          if (downloadListener != null) {
            downloadListener.onGetFileInfo(result.getFileSize(), result.getComplete());
          }
          if (state == DOWNLOADING) {
            return;
          }
          state = DOWNLOADING;
          new downloadTask().execute(_info);
        } else {
          if (downloadListener != null) {
            downloadListener.onPause();
          }
        }
      }
    }.execute("");
  }

  long time = System.currentTimeMillis();
  boolean isUpdateOver = false;
  private final Object lock = "";

  private class downloadTask extends AsyncTask<UpdateDownloadInfo, Integer, Integer> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override protected Integer doInBackground(UpdateDownloadInfo... params) {
      final UpdateDownloadInfo info = params[0];
      if (info == null) {
        return DOWNLOADSTATE_FAILED;
      }
      setUpdate(true);
      HttpURLConnection connection = null;
      RandomAccessFile randomAccessFile = null;
      InputStream is = null;
      try {
        URL url = new URL(info.getUrl());
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("RANGE", "bytes=" + info.getCompeleteSize() + "-");
        connection.setRequestProperty("Accept-Encoding", "identity");//不使用压缩 -1 error
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.connect();
        randomAccessFile = new RandomAccessFile(localfile, "rwd");
        int code = connection.getResponseCode();
        if (code == HttpsURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {
          if (code == HttpURLConnection.HTTP_PARTIAL) {
            randomAccessFile.seek(info.getCompeleteSize());
          } else {
            info.setCompeleteSize(0);
            randomAccessFile.seek(info.getCompeleteSize());
          }
        }

        is = connection.getInputStream();
        byte[] buffer = new byte[4096];
        int length = -1;
        isUpdateOver = false;
        while ((length = is.read(buffer)) != -1) {
          synchronized (lock) {
            randomAccessFile.write(buffer, 0, length);
            info.setCompeleteSize(info.getCompeleteSize() + length);
            UpdateDao.getInstance(context).updataInfos(info.getThreadId(), info.getCompeleteSize(), urlstr);
            publishProgress(info.getCompeleteSize());
          }
          if (state == PAUSE) {
            return DOWNLOADSTATE_PAUSE;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        setUpdate(false);
        return DOWNLOADSTATE_FAILED;
      } finally {
        setUpdate(false);
        publishProgress(info.getCompeleteSize());
        isUpdateOver = true;
        if (randomAccessFile != null) {
          try {
            randomAccessFile.close();
            randomAccessFile = null;
          } catch (IOException e) {
            // TODO Auto-generated catch block
          }
        }
        if (is != null) {
          try {
            is.close();
            is = null;
          } catch (IOException e) {
            // TODO Auto-generated catch block
          }
        }
        if (connection != null) {
          connection.disconnect();
          connection = null;
        }
      }
      return DOWNLOADSTATE_FINISHED_PART;
    }

    @Override protected void onProgressUpdate(Integer... values) {
      // TODO Auto-generated method stub
      super.onProgressUpdate(values);
      // 这里更新通知的时候会比较耗电和cpu性能
      if (System.currentTimeMillis() - time > 1500 || isUpdateOver) {
        time = System.currentTimeMillis();
        downloadListener.onProgress(values[0]);
      }
    }

    @Override protected void onPostExecute(Integer result) {
      super.onPostExecute(result);
      switch (result) {
        case DOWNLOADSTATE_FINISHED_PART:
          downloadListener.onFinished();
          break;
        case DOWNLOADSTATE_FAILED:
          if (downloadListener != null) {
            downloadListener.onFailed();
          }
          break;
        case DOWNLOADSTATE_PAUSE:
          if (downloadListener != null) {
            downloadListener.onPause();
          }
          break;
        default:
          break;
      }
    }
  }

  public void delete(String urlstr) {
    UpdateDao.getInstance(context).delete(urlstr);
  }

  // 设置暂停
  public void pause() {
    state = PAUSE;
  }

  // 重置下载状态
  public void reset() {
    state = INIT;
  }
}