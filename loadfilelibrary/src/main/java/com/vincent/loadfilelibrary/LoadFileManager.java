package com.vincent.loadfilelibrary;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.image.ImageEngine;
import com.vincent.loadfilelibrary.engine.pdf.PdfEngine;
import com.vincent.loadfilelibrary.engine.x5.X5Engine;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;
import com.vincent.loadfilelibrary.engine.zip.ZipEngine;

import java.io.File;

/**
 *  文件加载管理类
 */
public class LoadFileManager {

    private static LoadFileManager INSTANCE = new LoadFileManager();

    private Context mContext;

    Engine mPdfEngine;

    Engine mX5Engine;

    Engine mImageEngine;

    Engine mZipEngine;

    public static LoadFileManager get(){
        return INSTANCE;
    }

    public LoadFileManager init(Context context){
        mContext = context;
        mPdfEngine = new PdfEngine(mContext);
        mX5Engine = new X5Engine(mContext);
        mImageEngine = new ImageEngine(mContext);
        mZipEngine = new ZipEngine(mContext);
        return this;
    }

    /**
     *  加载文件
     * @param file
     */
    public void loadFile(File file){

        if (!file.exists()){
            throw  new RuntimeException("文件不存在！");
        }

        String name = file.getName();

        String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();


        if (suffix.equals(".pdf")){
            mPdfEngine.loadFile(file);
        }else if(suffix.equals(".zip") || suffix.equals(".rar")){
            mZipEngine.loadFile(file);
        }else if(isImageFile(file.getAbsolutePath())){
            mImageEngine.loadFile(file);
        }else{
            mX5Engine.loadFile(file);
        }
    }


    /**
     *  文件是否可读
     * @param f
     * @param callback
     */
    public void isFileCanRead(File f, BooleanCallback callback){

        if (!f.exists()){
           if (callback != null) callback.onSuccess(false);
        }

        String name = f.getName();

        String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();

        if (suffix.equals(".pdf")){
            mPdfEngine.isFileCanRead(f, callback);
        }else if(suffix.equals(".zip") || suffix.equals(".rar")){
            mZipEngine.isFileCanRead(f, callback);
        }else if(isImageFile(f.getAbsolutePath())){
            mImageEngine.isFileCanRead(f, callback);
        }else{
            mX5Engine.isFileCanRead(f, callback);
        }

    }


    /**
     * 判断文件是否为图片文件
     * @param filePath
     * @return
     */
    private boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }
}
