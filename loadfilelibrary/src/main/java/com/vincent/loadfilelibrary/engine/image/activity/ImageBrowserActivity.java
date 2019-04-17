package com.vincent.loadfilelibrary.engine.image.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.engine.image.adapter.ImageBrowserAdapter;
import com.vincent.loadfilelibrary.engine.image.viewpager.PhotoViewPager;
import com.vincent.loadfilelibrary.photoview.PhotoView;
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
        overridePendingTransition(R.anim.fade_scale_in,0);

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
        mTopBar.setVisibility(View.GONE);
    }

    private void initData() {
        Intent i = getIntent();
        mImages = i.getStringArrayListExtra(IMAGES);

        mVp.setAdapter(new ImageBrowserAdapter(mImages,this));

        photoViewDefaultY = getWindow().getDecorView().getY();
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



    int lastY = 0 ;

    float photoViewDefaultY = 0;

    /**
     *  下拉屏幕，销毁界面
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(ev.getPointerCount() > 1) return super.dispatchTouchEvent(ev);

        PhotoView view = (PhotoView) mVp.getChildAt(mVp.getCurrentItem());
        if (view != null) {     // 假如当前缩放倍数不是默认缩放倍数，则不进行整体界面缩放进入关闭界面模式
           if (view.getScale() != view.getMinimumScale()) return super.dispatchTouchEvent(ev);
        }


        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY  = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getRawY() - lastY > 0){

                    View root  = getWindow().getDecorView();
                    root.setScaleX(lastY /ev.getRawY());
                    root.setScaleY(lastY / ev.getRawY());

                    root.setY(ev.getRawY() / 2);
                }
            case MotionEvent.ACTION_UP:
                View root  = getWindow().getDecorView();


                float scaleTimes = root.getScaleY();

                int seconds  = 500;

                if (scaleTimes < 0.6f){
                    finish();
                   overridePendingTransition(0,R.anim.fade_scale_out);
                }

                if (scaleTimes == 1) break;

                ValueAnimator scaleAnim = ValueAnimator.ofFloat(scaleTimes,1.0f);
                scaleAnim.setDuration(seconds);
                scaleAnim.addUpdateListener(animation -> {

                    float value = (float) scaleAnim.getAnimatedValue();

                    root.setScaleX(value);
                    root.setScaleY(value);
                });
                scaleAnim.start();

                float transY = root.getY();

                ValueAnimator transAnim = ValueAnimator.ofFloat(transY,photoViewDefaultY);
                transAnim.setDuration(seconds);
                transAnim.addUpdateListener(animation -> {

                    float value = (float) animation.getAnimatedValue();
                    root.setY(value);
                });
                transAnim.start();

                break;


        }

        return super.dispatchTouchEvent(ev);
    }

    public <T extends View> T $(@IdRes int id){
        return (T)findViewById(id);
    }
}
