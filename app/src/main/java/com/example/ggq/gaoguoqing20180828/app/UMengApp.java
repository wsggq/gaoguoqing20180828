package com.example.ggq.gaoguoqing20180828.app;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class UMengApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initUM();
    }

    private void initUM() {

        //umeng配置

        UMConfigure.init(this,"5b7243858f4a9d524d000057"

                ,"umeng", UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0



        //qq登录配置

        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");



        UMConfigure.setLogEnabled(true);//可以打印堆栈信息

    }
}
