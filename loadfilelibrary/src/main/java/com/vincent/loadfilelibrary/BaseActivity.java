package com.vincent.loadfilelibrary;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

public class BaseActivity extends AppCompatActivity {


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        slideRightToFinish(ev);

        return super.dispatchTouchEvent(ev);
    }


    float slideX = 0;
    /**
     *  侧边右滑关闭界面
     * @param ev
     */
    private void slideRightToFinish(MotionEvent ev) {
        View root = getWindow().getDecorView();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                slideX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (slideX < root.getWidth()/8){
                    root.setX(ev.getRawX());
                }
                break;
            case MotionEvent.ACTION_UP:

                if (root.getX() < root.getWidth()/3){    //假如滑动超过屏幕 1/3 才进行销毁界面，否则还原屏幕位置
                    ValueAnimator anim  = ValueAnimator.ofFloat(root.getX(),0);
                    updateRootViewLayout(anim);
                }else {
                    finish();
                }

                break;
        }

    }


    public void updateRootViewLayout(ValueAnimator anim){
        anim.setDuration(500);
        anim.addUpdateListener(animation -> {

            float value = (float) animation.getAnimatedValue();
            getWindow().getDecorView().setX(value);
        });
        anim.start();
    }
}
