package com.vincent.loadfilelibrary.engine.x5.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.sdk.TbsReaderView;
import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.topbar.NavigationBar;
import com.vincent.loadfilelibrary.topbar.TopBarBuilder;

import java.io.File;

/**
 *  腾讯x5内核文件加载器展示页
 */
public class X5FileLoaderActivity extends AppCompatActivity {

    /* Activity标记 */
    protected final String TAG = this.getClass().getSimpleName();

    public static final String FILE_PATH = "file_path";

    public static final String FILE_NAME = "file_name";

    private String TEMP_DIR = Environment.getExternalStorageDirectory() + File.separator +"TbsReaderTemp";


    FrameLayout mRoot;

    TbsReaderView mReaderView;

    NavigationBar mTopBar;

    /**文件路径*/
    private String mPath = "";

    /**文件名*/
    private String mName = "";


    int mTopBarDefaultHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        initListeners();
    }


    protected void initViews() {

        setContentView(R.layout.activity_x_five_file_loader);

        mRoot = $(R.id.mRoot);
        mTopBar = $(R.id.mTopBar);

        mTopBar.setFitsSystemWindows(false);  //取消沉浸式状态栏

        mReaderView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                Log.d("lm", "onCallBackAction: " + integer);
            }
        });

        mReaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mRoot.addView(mReaderView);

    }

    protected void initData() {

        Intent intent = getIntent();
        mPath = intent.getStringExtra(FILE_PATH);
        mName = intent.getStringExtra(FILE_NAME);

        initTopBar();


        boolean bool = mReaderView.preOpen(getFileType(mPath), false);
        if (bool) {
            Bundle bundle = new Bundle();
            bundle.putString("filePath", mPath);//存放pdf 的文件

            File tempDir = new File(TEMP_DIR);
            if (!tempDir.exists()) tempDir.mkdirs();
            bundle.putString("tempPath", TEMP_DIR);

            mReaderView.openFile(bundle);
        }


        mTopBar.measure(0,0);
        mTopBarDefaultHeight = mTopBar.getMeasuredHeight();
    }

    private void initListeners() {
        mTopBar.setNavigationBarListener((view,location) -> {

             switch (location){
                 case LEFT_FIRST:
                     finish();
                     break;
             }
        });
    }

    private void initTopBar() {
        TopBarBuilder.buildLeftArrowTextById(mTopBar,this,R.string.back);
        mTopBar.setBackgroundColor(getResources().getColor(R.color.white));
        mTopBar.setTextColorByLocation(NavigationBar.Location.LEFT_FIRST,getResources().getColor(android.R.color.black));
        TopBarBuilder.buildCenterTextTitle(mTopBar,this,mName,getResources().getColor(android.R.color.black));
    }



    int lastY = 0 ;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (ev.getRawY() - lastY > 0){
                    showTopBar();
                }else {
                    hideTopBar();
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     *  显示顶部栏
     */
    public void showTopBar(){

        if (mTopBar.getHeight() == 0){
            ValueAnimator anim = ValueAnimator.ofInt(0,mTopBarDefaultHeight);
            updateTopBarLayout(anim);
        }
    }

    /**
     *  隐藏顶部栏
     */
    public void hideTopBar(){
        if (mTopBar.getHeight() == mTopBarDefaultHeight){
            ValueAnimator anim = ValueAnimator.ofInt(mTopBarDefaultHeight,0);
            updateTopBarLayout(anim);
        }
    }

    private void updateTopBarLayout(ValueAnimator anim) {
        anim.setDuration(1000);
        anim.addUpdateListener(animation -> {
            int value = (int) anim.getAnimatedValue();
            ViewGroup.LayoutParams params = mTopBar.getLayoutParams();
            params.height = value;
            mTopBar.setLayoutParams(params);
        });
        anim.start();
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d(TAG, "paramString---->null");
            return str;
        }
        Log.d(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.d(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }




    @Override
    protected void onDestroy() {

        if (mReaderView != null) {
            //在展示结束的时候，一定要调用。否则一直处于加载状态
            mReaderView.onStop();
        }


        super.onDestroy();
    }

    private <T extends View> T $(int id){
        return (T)findViewById(id);
    }
}
