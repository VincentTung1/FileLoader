package com.vincent.loadfilelibrary.engine.zip.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.vincent.loadfilelibrary.BaseActivity;
import com.vincent.loadfilelibrary.LoadFileManager;
import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.engine.x5.utils.FileUtils;
import com.vincent.loadfilelibrary.engine.zip.adapter.ZipPreviewAdapter;
import com.vincent.loadfilelibrary.engine.zip.utils.ZipUtils;
import com.vincent.loadfilelibrary.topbar.NavigationBar;
import com.vincent.loadfilelibrary.topbar.TopBarBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 *  压缩包文件浏览器
 */
public class ZipPreviewActivity extends BaseActivity {

    private static final String FILE_PATH = "filepath" ;

    NavigationBar mTopBar;

    ListView mLv;


    /**文件路径*/
    private String mFilePath = "";

    private File mFile;

    /**解压后的临时文件夹目录*/
    private File mTempFileDir;

    public static void start(Context context, String filePath){
        Intent intent = new Intent(context,ZipPreviewActivity.class);
        intent.putExtra(FILE_PATH,filePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        initListeners();
    }

    private void initViews() {
        setContentView(R.layout.activity_zip_preview);
        mTopBar = $(R.id.mTopBar);
        mLv  = $(R.id.mLv);
    }

    private void initData() {
        Intent intent = getIntent();
        mFilePath= intent.getStringExtra(FILE_PATH);

        TopBarBuilder.buildLeftArrowTextById(mTopBar,this,R.string.back);
        mTopBar.setFitsSystemWindows(false);

        mFile = new File(mFilePath);
        if (mFile.exists()){
            TopBarBuilder.buildCenterTextTitle(mTopBar,this,mFile.getName(),getResources().getColor(android.R.color.black));
        }

        try {

            mTempFileDir = new File(mFilePath.substring(0,mFilePath.lastIndexOf(".")));
            if (mTempFileDir.exists()){
                FileUtils.deleteDirWihtFile(mTempFileDir);
            }

            ZipUtils.UnZipFolder(mFilePath, mTempFileDir.getAbsolutePath());

            ArrayList<File> fs = new ArrayList<>();
            File[] files = mTempFileDir.listFiles();
            for (File file : files) {
                fs.add(file);
            }

            mLv.setAdapter(new ZipPreviewAdapter(this,fs));

            mLv.setOnItemClickListener((parent, view, position, id) -> {

                File file = fs.get(position);

                LoadFileManager.get().loadFile(file);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        mTopBar.setNavigationBarListener((v,location)->{

            switch (location){
                case LEFT_FIRST:
                    finish();
                    break;
            }
        });
    }


    @Override
    protected void onDestroy() {

        if (mTempFileDir.exists()){
            FileUtils.deleteDirWihtFile(mTempFileDir);
        }
        super.onDestroy();
    }

    public <T extends View> T $(@IdRes int id){
        return (T)findViewById(id);
    }
}
