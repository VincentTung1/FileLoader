package com.vincent.loadfilelibrary.topbar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.topbar.utils.DensityUtil;
import com.vincent.loadfilelibrary.topbar.utils.ResourcesUtil;


public class TopBarBuilder {

    /**
     * 生成中间文字标题
     * @param bar 顶部栏控件
     * @param context 上下文
     * @param title 标题内容
     */
    public static void buildCenterTextTitle(NavigationBar bar, Context context, String title, int...textColor) {
        float textSize = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_titletext_textsize);
        NavigationBarBean centerBean = new NavigationBarBean(title, textSize, textColor.length <= 0 ? 0 : textColor[0], null, 0, 0, 0, null, null);
        bar.addCenterContent(NavigationBar.Style.STYLE_ONLYTEXT, centerBean);
    }

    public static void buildCenterTextTitleById(NavigationBar bar, Context context, int textResourceId, int...textColor) {
        String title = ResourcesUtil.getResourcesString(context, textResourceId);
        float textSize = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_titletext_textsize);
        NavigationBarBean centerBean = new NavigationBarBean(title, textSize, textColor.length <= 0 ? 0 : textColor[0], null, 0, 0, 0, null, null);
        bar.addCenterContent(NavigationBar.Style.STYLE_ONLYTEXT, centerBean);
    }

    public static void buildCenterDrawableById(NavigationBar bar, Context context, int imgResId, int side, float scale, String tag) {
        NavigationBarBean centerImgBean = new NavigationBarBean(null, 0, null, imgResId, scale, 0, null, null);
        centerImgBean.setCenterImageSide(DensityUtil.dip2pxInt(context, side));
        centerImgBean.setCenterTag(tag);
        centerImgBean.setCenterMargin(DensityUtil.dip2pxInt(context, 7));
        bar.addCenterContent(NavigationBar.Style.STYLE_ONLYIMAGE, centerImgBean);
    }

    public static void clearCenterTextDrawableByTag(NavigationBar bar, String tag) {
        bar.removeCenterContentByTag(tag);
    }

    public static void buildLeftArrowText(NavigationBar bar, Context context, String text, int...textColor) {
        float padding = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_arrowtext_padding);
        float textSize = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_buttontext_textsize);
        bar.setContent(
                NavigationBar.Location.LEFT_FIRST,
                NavigationBar.Style.STYLE_ARROWTEXT,
                new NavigationBarBean(text, textSize, textColor.length <= 0 ? 0 : textColor[0], null, 0, 0, padding, NavigationBarBean.Direction.LEFT, null));
    }

    public static void buildLeftArrowTextById(NavigationBar bar, Context context, int resId, int...textColor) {
        String text = ResourcesUtil.getResourcesString(context, resId);
        float padding = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_arrowtext_padding);
        float textSize = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_buttontext_textsize);
        bar.setContent(
                NavigationBar.Location.LEFT_FIRST,
                NavigationBar.Style.STYLE_ARROWTEXT,
                new NavigationBarBean(text, textSize, textColor.length <= 0 ? 0 : textColor[0], null, 0, 0, padding, NavigationBarBean.Direction.LEFT, null));
    }

    public static void buildOnlyImageById(NavigationBar bar, Context context, NavigationBar.Location location, int imageResourceId) {
        float padding = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_onlyimage_padding);
        NavigationBarBean rightSecondBean = new NavigationBarBean(null, 0, null, imageResourceId, 0, padding, null, null);
        bar.setContent(location, NavigationBar.Style.STYLE_ONLYIMAGE, rightSecondBean);
    }

    public static void buildOnlyImageByDrawable(NavigationBar bar, Context context, NavigationBar.Location location, Drawable drawable) {
        float padding = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_onlyimage_padding);
        NavigationBarBean rightSecondBean = new NavigationBarBean(null, 0, drawable, 0, 0, padding, null, null);
        bar.setContent(location, NavigationBar.Style.STYLE_ONLYIMAGE, rightSecondBean);
    }

    public static void buildOnlyText(NavigationBar bar, Context context, NavigationBar.Location location, String text, int...textColor) {
        float padding = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_onlytext_padding);
        float textSize = ResourcesUtil.getResourcesFloat(context, R.string.navigationbarbean_buttontext_textsize);
        NavigationBarBean bean = new NavigationBarBean(text, textSize, textColor.length <= 0 ? 0 : textColor[0], null, 0, 0, padding, null, null);
        bar.setContent(location, NavigationBar.Style.STYLE_ONLYTEXT, bean);
    }

    public static void buildOnlyTextById(NavigationBar bar, Context context, NavigationBar.Location location, int resId, int...textColor) {
        String text = ResourcesUtil.getResourcesString(context, resId);
        buildOnlyText(bar, context, location, text, textColor);
    }



}
