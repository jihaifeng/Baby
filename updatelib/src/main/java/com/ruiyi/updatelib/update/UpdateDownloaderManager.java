package com.ruiyi.updatelib.update;

import android.content.Context;

public class UpdateDownloaderManager {
    private static UpdateDownloaderManager downloaderManager;
    UpdateDownloader updateDownloader;
   
    private UpdateDownloaderManager() {

    }

    public static UpdateDownloaderManager getInstance() {
        if (downloaderManager == null)
            downloaderManager = new UpdateDownloaderManager();
        return downloaderManager;
    }

    public void startTask(Context context, String url, String localPath, UpdateDownloadListener listener) {
        updateDownloader = new UpdateDownloader(url, localPath, context);
        if (listener != null)
            updateDownloader.setDownloadListener(listener);
        updateDownloader.download();
    }

    public void continueOldTask(String url) {
        if (updateDownloader == null)
            return;
        updateDownloader.download();
    }

    public boolean isUpdate() {
        if (updateDownloader == null)
            return false;
        return updateDownloader.isUpdate();
    }

    public void pauseTask(String url) {
        if (updateDownloader == null)
            return;
        updateDownloader.pause();
    }

    /**
     * 下载完成之后需调用
     * 
     * @param url
     */
    public void removeTask(String url) {
        updateDownloader.delete(url);
    }

}