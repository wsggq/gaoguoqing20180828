package com.example.ggq.gaoguoqing20180828.utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OKHttpUtil {
    private static OKHttpUtil instance;
    private OkHttpClient okHttpClient;

    public static OKHttpUtil getInstance() {
        //DCL双重检验锁获取实例对象
        if(null == instance){
            //用锁防止多线程高并发的访问
            synchronized (OKHttpUtil.class){
                if(null == instance){
                    instance = new OKHttpUtil();
                }
            }
        }
        return instance;
    }

    private OKHttpUtil() {
        if(null == okHttpClient){
            synchronized (OkHttpClient.class){
                if(null == okHttpClient){
                    //添加缓冲拦截器
                     okHttpClient = new OkHttpClient
                            .Builder()
                            .build();
                }
            }
        }
    }
    public void get(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public void post(String url, FormBody formBody,Callback callback){
        Request request = new Request.Builder().method("POST", formBody).url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
