package com.vincent.loadfilelibrary.engine;


import android.content.Context;

import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;

public abstract class Engine {

    protected Context mContext;

    public Engine(Context context) {
        this.mContext = context;
    }


    /**加载文件*/
    public abstract void loadFile(File f);

    /**文件是否可读*/
    public abstract void isFileCanRead(File f , BooleanCallback callback);
}
