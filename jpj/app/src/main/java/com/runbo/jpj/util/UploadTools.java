package com.runbo.jpj.util;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 文件上传 czz
 */
public class UploadTools {

    public static String uploadFile(String filePath,String fileKey,String uploadPath,Map<String, String> params){
        File file = new File(filePath);
        TreeMap<String,File> filemap = new TreeMap<>();
        filemap.put(fileKey,file);
        return uploadFile(uploadPath,params,filemap);
    }

    /**
     * @param uploadPath 上传服务地址
     * @param params   参数
     * @param fileParams   文件参数
     */
    public static String uploadFile(String uploadPath,Map<String, String> params,Map<String, File> fileParams){

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        InputStream input = null;
        String result = null;

        /// boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识;
        // 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
        String PREFIX = ("--");
        String LINE_END = "\r\n"; // 换行

        try {
            URL url = new URL(uploadPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方法
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            // 设置header
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            // 获取写输入流
            dos = new DataOutputStream(conn.getOutputStream());


            // 要上传的数据
            StringBuffer sb = null;
            String paramStr;

            // 发送非文件参数
            if(params != null && params.size() > 0){
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()){
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    Object value = params.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"")
                            .append(key).append("\"").append(LINE_END)
                            .append(LINE_END);
                    sb.append(value).append(LINE_END);
                    paramStr = sb.toString();

                    dos.write(paramStr.getBytes());
                    dos.flush();
                }
            }

            paramStr = null;
            // 发送文件参数，读取文件流写入post输出流
            if (fileParams != null && !fileParams.isEmpty()){
                for(Map.Entry<String,File> flie : fileParams.entrySet()){
                    sb = null;
                    sb = new StringBuffer();

                    // 设置边界标示，设置 Content-Disposition头传入文件流
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition:form-data; name=\"file\"; " +
                            "filename=\"" + flie.getKey()
                            + "\"" + LINE_END);
                    sb.append("Content-Type:multipart/form-data" + LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());

                    is = new FileInputStream(flie.getValue());

                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1){
                        dos.write(bytes,0,len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                    dos.flush();
                }
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
            }

            dos.close();
            int res = conn.getResponseCode();
            if(res == HttpURLConnection.HTTP_OK){
                input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int l;
                while ((l=input.read()) != -1){
                    sb1.append((char)l);
                }
                result = sb1.toString();
                LogUtil.e("上传成功:" + result.toString());
            }
        } catch (Exception e) {
            LogUtil.e("上传图片出错:" + e.toString());
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

}
