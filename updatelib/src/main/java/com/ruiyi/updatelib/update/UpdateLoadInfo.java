package com.ruiyi.updatelib.update;

/**
 * 自定义的一个记载下载器详细信息的类
 */
public class UpdateLoadInfo {
	public int fileSize;// 文件大小
	private int complete;// 完成度
	private String urlstring;// 下载器标识

	public UpdateLoadInfo(int fileSize, int complete, String urlstring) {
		this.fileSize = fileSize;
		this.complete = complete;
		this.urlstring = urlstring;
	}

	public UpdateLoadInfo() {
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public String getUrlstring() {
		return urlstring;
	}

	public void setUrlstring(String urlstring) {
		this.urlstring = urlstring;
	}

	@Override
	public String toString() {
		return "LoadInfo [fileSize=" + fileSize + ", complete=" + complete
				+ ", urlstring=" + urlstring + "]";
	}
}