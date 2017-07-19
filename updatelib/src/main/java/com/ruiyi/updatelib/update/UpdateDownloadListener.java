package com.ruiyi.updatelib.update;

public interface UpdateDownloadListener {
	void onStart();

	/**
	 * 
	 * @param fileSize 文件大小
	 * @param completeSize 完成大小
	 */
	void onGetFileInfo(int fileSize, int completeSize);

	void onProgress(int increaseSize);

	void onFinished();

	void onFailed();

	void onPause();

}
