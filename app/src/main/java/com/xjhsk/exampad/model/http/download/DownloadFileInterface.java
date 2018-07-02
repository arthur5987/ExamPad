package com.xjhsk.exampad.model.http.download;
/**
 * 下载任务精简接口
 * @author HongYu
 *
 * 2015年7月14日
 */
public interface DownloadFileInterface {
    
    void onSuccess(String filePath);
    void onFailure(String des);
    void onProgress(long bytesWritten, long totalSize);
}
