package com.vincent.loadfilelibrary.topbar.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

public class ResourcesUtil {

    public static float getResourcesFloat(Context context, int id) {
        return Float.parseFloat(context.getResources().getString(id));
    }

    public static String getResourcesString(Context context, int id) {
        return context.getResources().getString(id);
    }


    public static int getResourcesColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static ColorStateList getResourcesColorList(Context context, int id) {
        return context.getResources().getColorStateList(id);
    }

    public static float getResourcesDimen(Context context, int id) {
        return context.getResources().getDimension(id);
    }

    public static Drawable getResourcesDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }

    public static int getResourceInteger(Context context, int id, @Nullable DefaultValueListener<Integer> listener) {
        int value = context.getResources().getInteger(id);
        if (listener != null) {
            Integer defaultValue = listener.onValue(value);
            return defaultValue == null ? value : defaultValue;
        } else {
            return value;
        }
    }

    public static Drawable getRippleDrawable(Context context) {
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attribute);
        Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable;
    }

    public interface DefaultValueListener<V> {
        @Nullable
        V onValue(V v);
    }
}
