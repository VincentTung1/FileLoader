package com.vincent.loadfilelibrary.engine.pdf;

import android.content.Context;

import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;

public class PdfEngine extends Engine {


    public PdfEngine(Context context) {
        super(context);
    }

    @Override
    public void loadFile(File f) {
        PdfPreviewActivity.start(mContext,f.getAbsolutePath());
    }

    @Override
    public void isFileCanRead(File f, BooleanCallback callback) {
        if (callback != null) {
            callback.onSuccess(f.exists());
        }
    }
}
