package com.vincent.loadfilelibrary.engine.image.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.engine.image.adapter.ImageBrowserAdapter;
import com.vincent.loadfilelibrary.engine.image.viewpager.PhotoViewPager;
import com.vincent.loadfilelibrary.topbar.NavigationBar;
import com.vincent.loadfilelibrary.topbar.TopBarBuilder;

import java.util.ArrayList;


/**
 *  图片浏览器
 */
public class ImageBrowserActivity extends AppCompatActivity {

    public static final String IMAGES = "images";

    NavigationBar mTopBar;

    PhotoViewPager mVp;

    /**图片路径集合*/
    private ArrayList<String> mImages;


    /**
     *
     * @param c  环境
     * @param images 图片路径集合
     */
    public static void start(Context c,ArrayList<String> images){
        Intent i = new Intent(c,ImageBrowserActivity.class);
        i.putExtra(IMAGES,images);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        initListeners();
    }


    private void initViews() {
        setContentView(R.layout.activity_image_browser);
        mTopBar = $(R.id.mTopBar);
        mVp = $(R.id.mVp);

        mTopBar.setFitsSystemWindows(false);  //取消沉浸式状态栏
        TopBarBuilder.buildLeftArrowTextById(mTopBar,this,R.string.back);
    }

    private void initData() {
        Intent i = getIntent();
        mImages = i.getStringArrayListExtra(IMAGES);

        mVp.setAdapter(new ImageBrowserAdapter(mImages,this));
    }

    private void initListeners() {

       mTopBar.setNavigationBarListener((v,location) ->{
           switch (location){
               case LEFT_FIRST:
                   finish();
                   break;
           }
       });
    }


    public <T extends View> T $(@IdRes int id){
        return (T)findViewById(id);
    }
}
