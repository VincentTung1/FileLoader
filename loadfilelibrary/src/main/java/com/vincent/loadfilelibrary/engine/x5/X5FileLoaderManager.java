package com.vincent.loadfilelibrary.engine.x5;

import android.content.Context;

import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;
import com.vincent.loadfilelibrary.engine.x5.callback.StringCallback;

import java.io.File;


/**
 *  腾讯x5内核文件加载器管理类
 */
public class X5FileLoaderManager {

    private static X5FileLoaderManager INSTANCE = new X5FileLoaderManager();

    private Context mContext;

    private X5Engine mEngine;

    public static X5FileLoaderManager get(){
        return INSTANCE;
    }

    public X5FileLoaderManager init(Context context){

        if (mContext == null){
            mContext = context;
            mEngine = new X5Engine(mContext);
        }
        return this;
    }


    /**
     *  文件是否能被打开
     * @param f
     * @param callback
     */
    public void isFileCanOpen(File f, BooleanCallback callback){
        mEngine.isFileCanRead(f,callback);
    }

    /**
     *  打开文件
     * @param f
     * @param callback
     */
    public void openFile(File f , StringCallback callback){
        mEngine.loadFile(f);
    }

}
