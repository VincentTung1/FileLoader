package com.vincent.loadfilelibrary.engine.x5;


import android.content.Context;
import android.content.Intent;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.x5.activity.X5FileLoaderActivity;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;

/**
 *   腾讯x5内核文件加载器
 */
public class X5Engine extends Engine implements QbSdk.PreInitCallback {


    public X5Engine(Context context) {
        super(context);
        QbSdk.initX5Environment(context,this);
    }


    @Override
    public void isFileCanRead(File file, final BooleanCallback callback) {

        QbSdk.canOpenFile(mContext, file.getAbsolutePath(), new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {

                if (callback != null) {
                    callback.onSuccess(aBoolean);
                }
            }
        });

    }

    @Override
    public void loadFile(File f) {
        Intent intent = new Intent(mContext,X5FileLoaderActivity.class);
        intent.putExtra(X5FileLoaderActivity.FILE_PATH, f.getAbsolutePath());//存放pdf 的文件
        intent.putExtra(X5FileLoaderActivity.FILE_NAME, f.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public void onCoreInitFinished() {

    }

    @Override
    public void onViewInitFinished(boolean b) {
        System.out.println("腾讯x5内核是否初始化成功:"+b);
    }

}
