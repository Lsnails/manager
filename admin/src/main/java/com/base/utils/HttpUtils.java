package com.base.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName HttpUtils
 * @Author zc
 * @Date 2019/10/8 15:58
 * @Version 1.0
 **/
public class HttpUtils {
    /**
     * Get请求
     *
     * @param urlStr    请求的地址
     * @param parameter 请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding  服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    public static String doGet(String urlStr, String parameter, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();  // 新建连接实例
            connection.setConnectTimeout(2000);                     // 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000);                        // 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);                           // 是否打开输出流 true|false
            connection.setDoInput(true);                            // 是否打开输入流true|false
            connection.setRequestMethod("Get");                     // 提交方法GET
            connection.setUseCaches(false);                         // 是否缓存true|false
            connection.connect();                                   // 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(parameter);                              // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();                                            // 刷新
            out.close();                                            // 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据 ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("MyHttpUtils==>通过url打开一个连接出现异常:" + e);
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    /**
     * Post请求
     *
     * @param urlStr    请求的地址
     * @param parameter 请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding  服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    public static String doPost(String urlStr, String parameter, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();  // 新建连接实例
            connection.setConnectTimeout(2500);                     // 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2500);                        // 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);                           // 是否打开输出流 true|false
            connection.setDoInput(true);                            // 是否打开输入流true|false
            connection.setRequestMethod("POST");                    // 提交方法POST
            connection.setUseCaches(false);                         // 是否缓存true|false
            connection.connect();                                   // 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(parameter);                              // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();                                            // 刷新
            out.close();                                            // 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据 ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("MyHttpUtils==>通过url打开一个连接出现异常:" + e);
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }
}
