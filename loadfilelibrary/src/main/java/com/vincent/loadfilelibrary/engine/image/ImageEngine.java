package com.vincent.loadfilelibrary.engine.image;

import android.content.Context;

import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.image.activity.ImageBrowserActivity;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;
import java.util.ArrayList;

public class ImageEngine extends Engine {

    public ImageEngine(Context context) {
        super(context);
    }

    @Override
    public void loadFile(File f) {
        ArrayList<String> images = new ArrayList<>();
        images.add(f.getAbsolutePath());
        ImageBrowserActivity.start(mContext,images);
    }

    @Override
    public void isFileCanRead(File f, BooleanCallback callback) {
        if (callback != null) {
            callback.onSuccess(f.exists());
        }
    }
}
