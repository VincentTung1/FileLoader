package com.vincent.loadfilelibrary.topbar.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;

public class ThemeUtil {

    public static float getThemeDimension(Context context, @AttrRes int resId) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{resId});
        return privateGetThemeDimension(typedArray);
    }

    public static float getThemeDimension(Context context, @StyleRes int styleResId, @AttrRes int attrResId) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(styleResId, new int[]{attrResId});
        return privateGetThemeDimension(typedArray);
    }

    private static float privateGetThemeDimension(TypedArray typedArray) {
        float value = typedArray.getDimension(0, -1);
        typedArray.recycle();
        return value;
    }

    public static float getThemeFloat(Context context, @AttrRes int resId) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{resId});
        return privateGetThemeFloat(typedArray);
    }

    public static float getThemeFloat(Context context, @StyleRes int styleResId, @AttrRes int attrResId) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(styleResId, new int[]{attrResId});
        return privateGetThemeFloat(typedArray);
    }

    private static float privateGetThemeFloat(TypedArray typedArray) {
        float value = typedArray.getFloat(0, -1);
        typedArray.recycle();
        return value;
    }

}
