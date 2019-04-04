package com.vincent.loadfilelibrary.engine.x5.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.TbsReaderView;
import com.vincent.loadfilelibrary.R;

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


    LinearLayout mRoot;

    TbsReaderView mReaderView;

    /**文件路径*/
    private String mPath = "";

    /**文件名*/
    private String mName = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
    }


    protected void initViews() {

        setContentView(R.layout.activity_x_five_file_loader);

        mRoot = f(R.id.mRoot);

        mReaderView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                Log.d("lm", "onCallBackAction: " + integer);
            }
        });

        //注意：为了显示效果，需要添加到content 布局。如果不添加，显示页面会有偏移误差。
//        ViewGroup viewGroup = findViewById(android.R.id.content);
        mReaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mRoot.addView(mReaderView);

    }

    protected void initData() {

        Intent intent = getIntent();
        mPath = intent.getStringExtra(FILE_PATH);
        mName = intent.getStringExtra(FILE_NAME);

//        initTopBar();



        boolean bool = mReaderView.preOpen(getFileType(mPath), false);
        if (bool) {
            Bundle bundle = new Bundle();
            bundle.putString("filePath", mPath);//存放pdf 的文件

            File tempDir = new File(TEMP_DIR);
            if (!tempDir.exists()) tempDir.mkdirs();
            bundle.putString("tempPath", TEMP_DIR);

            mReaderView.openFile(bundle);
        }

    }

    private void initTopBar() {
        ActionBar bar = getActionBar();
        // 返回箭头（默认不显示）
        bar.setDisplayHomeAsUpEnabled(true);
        // 显示标题
        bar.setDisplayShowTitleEnabled(false);
        //显示自定义视图
        bar.setDisplayShowCustomEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(mName);
        textView.setTextSize(15);
        textView.setTextColor(0xffffffff);
        LinearLayout actionbarLayout = new LinearLayout(this);
        bar.setCustomView(actionbarLayout,new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        ActionBar.LayoutParams mP = (ActionBar.LayoutParams) actionbarLayout
                .getLayoutParams();
        mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK| Gravity.CENTER_HORIZONTAL;
        actionbarLayout.addView(textView);
        bar.setCustomView(actionbarLayout, mP);
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

    private <T extends View> T f(int id){
        return (T)findViewById(id);
    }
}
