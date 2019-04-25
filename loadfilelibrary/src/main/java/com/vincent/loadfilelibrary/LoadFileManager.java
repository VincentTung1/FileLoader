package com.vincent.loadfilelibrary;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.vincent.loadfilelibrary.engine.Engine;
import com.vincent.loadfilelibrary.engine.image.ImageEngine;
import com.vincent.loadfilelibrary.engine.pdf.PdfEngine;
import com.vincent.loadfilelibrary.engine.poi.PoiEngine;
import com.vincent.loadfilelibrary.engine.x5.X5Engine;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;
import com.vincent.loadfilelibrary.engine.zip.ZipEngine;
import com.vincent.loadfilelibrary.popwindow.listview.ListViewPopup;

import java.io.File;
import java.util.ArrayList;

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

    Engine mPoiEngine;


    private ArrayList<String> mOptions;
    private ListViewPopup.OnItemOnClickListener mTopBarItemClickListener;

    /**是否启动滑动页面时隐藏顶部栏*/
    private boolean mEnableScrollHideTopbar;

    public static LoadFileManager get(){
        return INSTANCE;
    }

    public LoadFileManager init(Context context){
        mContext = context;
        mPdfEngine = new PdfEngine(mContext);
        mX5Engine = new X5Engine(mContext);
        mPoiEngine = new PoiEngine(mContext);
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

        String suffix = "";

        try {
            suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (suffix.equals(".pdf")){
            mPdfEngine.loadFile(file);
        }else if(suffix. endsWith( ".doc") || suffix. endsWith(".docx") ||
                suffix.endsWith(".xls") || suffix.endsWith(".xlsx") ||
                suffix.endsWith(".ppt") || suffix.endsWith(".pptx")){
            mPoiEngine.loadFile(file);
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

        String suffix = "";

        try {
            suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (suffix.equals(".pdf")){
            mPdfEngine.isFileCanRead(f, callback);
        }else if(suffix. endsWith( ".doc") || suffix. endsWith(".docx") ||
                suffix.endsWith(".xls") || suffix.endsWith(".xlsx") ||
                suffix.endsWith(".ppt") || suffix.endsWith(".pptx")){
            mPoiEngine.isFileCanRead(f,callback);
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


    /**
     *  设置文件浏览器顶部栏右上角菜单项
     * @param options
     * @param listener
     */
    public LoadFileManager setTopBarOptions(ArrayList<String> options, ListViewPopup.OnItemOnClickListener listener){
        mOptions = options;
        mTopBarItemClickListener = listener;
        return this;
    }


    /**
     *  获取顶部栏右上角所有菜单项
     * @return
     */
    public ArrayList<String> getOptions() {
        return mOptions;
    }

    /**
     *  获取顶部栏右上角所有菜单项监听事件
     * @return
     */
    public ListViewPopup.OnItemOnClickListener getTopBarItemClickListener() {
        return mTopBarItemClickListener;
    }


    /**
     *  设置是否启动滑动页面时隐藏顶部栏
     * @param enable
     */
    public LoadFileManager setScrollHideTopbar(boolean enable){
        mEnableScrollHideTopbar = enable;
        return this;
    }

    /**
     *  获取是否启动滑动页面时隐藏顶部栏
     * @return
     */
    public boolean enableScrollHideTopbar(){
        return mEnableScrollHideTopbar;
    }
}
