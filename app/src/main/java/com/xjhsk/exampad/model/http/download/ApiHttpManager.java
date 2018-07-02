package com.xjhsk.exampad.model.http.download;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.xjhsk.exampad.app.AppContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * http请求参数构建 回调处理
 * @author HongYu
 *
 * 2015年7月7日
 */
public class ApiHttpManager {
    static final String TAG = "ApiHttpManager";

    private static ApiHttpManager _httpManager;

    private AppContext _context;// 上下文

    private ApiHttpClient _httpClient;// 网络处理客户端
    private String _downloadUrl = "";//文件下载地址，暂时只用于音频下载
    /**
     * 网络请求返回的结果字段
     */
    public static final String KEY_RESPONSE_RESULT = "result";
    /**
     * 网络请求返回的结果在result中的id
     */
    public static final String key_RESPONSE_ID = "id";
    /**
     * 返回的状态ID
     */
    public static final String KEY_RESPONSE_STATUSID = "status_id";
    /**
     * 上传文件返回列表信息，如果无此字段表示发布评论
     */
    public static final String KEY_RESPONSE_FILEINFOS = "fileInfos";
    /**
     * 服务端返回的信息
     */
    public static final String KEY_RESPONSE_FEEDINFO = "feedInfo";
    
    private static final String NO_NET = "网络无法连接";



    public static ApiHttpManager getManager(){
        if(_httpManager == null)
        {
            _httpManager = new ApiHttpManager();
        }
        return _httpManager;
    }

    private ApiHttpManager(){
        _context = AppContext.getInstance();
        _httpClient = ApiHttpClient.getInstance();
    }

    //注释上传文件
//    public void handleUploadFile(Activity activity, List<UploadFileInfos> uploadFileInfosList, List<File> attachFileList,UploadFileInerface listen){
//        if(!Util.isWithNet(_context)){
//            listen.onFail(null, null, NO_NET);
//            return;
//        }
//        Map<String,File> fileMap = new HashMap<String, File>();
//        if(attachFileList.size() > 0)
//        {
//            for(File file : attachFileList){
//                fileMap.put(file.getName(), file);
//            }
//        }
//        if(activity == null)
//        {
//            ApiUploadClient.getClient().uploadFile(uploadFileInfosList, fileMap, listen);
//        }else
//        {
//            ApiUploadClient.getClient().uploadFileActivity(activity, uploadFileInfosList, fileMap, listen);
//        }
//    }

    /**
     * 音频下载
     * 防止多次下载当下载未完成接收到同样的url不会在做
     * 当接收到的url不匹配或者当前没有下载任务时会取消正在执行的任务并且将进行新的任务
     * @param url
     * @param response
     * 
     */
    public void downloadAudioFile(String url,final String filePath, final DownloadFileInterface response){
        if(!_downloadUrl.equals(url))
        {
            cancelTask(true);
        }else{
            return;
        }
        File playfile = new File(AppConstants.RECORD_DOWNLOAD_PATH);
        if (!playfile.exists()) 
        {
            playfile.mkdirs();
        }
        String[] allowedTypes = new String[] { RequestParams.APPLICATION_OCTET_STREAM,"text/html","application/zip;charset=UTF-8","image/jpeg;charset=UTF-8","application/zip","audio/mpeg","audio/x-wav","audio/x-ms-wma","application/hsk"};
        ApiHttpClient.getInstance().get(url, new BinaryHttpResponseHandler(allowedTypes) {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                File downloadFile = new File(filePath);

                if(downloadFile.exists())
                    downloadFile.delete();
                FileOutputStream oStream = null;
                try {
                    oStream = new FileOutputStream(downloadFile);
                    oStream.write(arg2);
                    oStream.flush();
                    oStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(oStream != null){
                        try {
                            oStream.flush();
                            oStream.close();
                            oStream = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                _downloadUrl = "";
                response.onSuccess(filePath);
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] arg2, Throwable arg3) {
            	_downloadUrl = "";
                response.onFailure(arg3.toString());
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                response.onProgress(bytesWritten,totalSize);
            }
        });
    }
    /**
     * 取消所有网络请求
     * @param isCancelRunning
     * 是否中断当前正在进行的网络
     */
    public void cancelTask(boolean isCancelRunning){
        //魏定强 2016年04月13日16:43:54  注释
        //ApiUploadClient.getClient().cancelTask(isCancelRunning);
    }


}
