package com.vincent.loadfilelibrary.engine.poi;

import android.content.Context;
import android.net.Uri;

import com.miracle.microsoft_documentviewer.MicrosoftDocumentViewerComponent;
import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.poi.activity.PoiDocumentPreviewActivity;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;

/**
 *   POI文档阅读器引擎
 */
public class PoiEngine extends Engine {

    public PoiEngine(Context context) {
        super(context);
        try {
            MicrosoftDocumentViewerComponent.INSTANCE.start(mContext);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void loadFile(File f) {
        Uri uri = Uri.fromFile(f);
        PoiDocumentPreviewActivity.Companion.startPreview(mContext, uri);
    }

    @Override
    public void isFileCanRead(File f, BooleanCallback callback) {
        if (callback != null) {
            callback.onSuccess(f.exists());
        }
    }
}
