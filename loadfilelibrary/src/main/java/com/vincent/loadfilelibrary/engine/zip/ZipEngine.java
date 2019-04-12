package com.vincent.loadfilelibrary.engine.zip;

import android.content.Context;

import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;
import com.vincent.loadfilelibrary.engine.zip.activity.ZipPreviewActivity;

import java.io.File;

/**
 *  Zip文件打开引擎
 */
public class ZipEngine extends Engine {

    public ZipEngine(Context context) {
        super(context);
    }

    @Override
    public void loadFile(File f) {
        ZipPreviewActivity.start(mContext,f.getAbsolutePath());
    }

    @Override
    public void isFileCanRead(File f, BooleanCallback callback) {
        if (callback != null) {
            callback.onSuccess(f.exists());
        }
    }
}
