package com.vincent.loadfilelibrary.topbar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Size;

public class ImageUtils {

    /**
     * 获取图片缩放值
     * @param width         目标宽
     * @param height        目标高
     * @param imageWidth   图片宽
     * @param imageHeight  图片高
     * @return 缩放值
     */
    public static float getImageScale(float width, float height, float imageWidth, float imageHeight) {
        float widthDiff = width - imageWidth;
        float heightDiff = height - imageHeight;
        if (widthDiff < heightDiff) {
            return width / imageWidth;
        } else {
            return height / imageHeight;
        }
    }

    /**
     * 获取宽高根据给定宽高按比例缩放后的宽高值
     * @param width         给定宽
     * @param height        给定高
     * @param imageWidth    原始宽
     * @param imageHeight   原始高
     * @return  按比例缩放后的宽高
     */
    public static @Size(2) int[] getImageScaleWH(int width, int height, int imageWidth, int imageHeight) {
        float scale = ImageUtils.getImageScale(width, height, imageWidth, imageHeight);
        imageWidth *= scale;
        imageHeight *= scale;
        return new int[]{imageWidth, imageHeight};
    }

    public static class BitmapUtil {

        /**
         * 图片缩放
         * @param bitmap 需要缩放的图片
         * @param scale 缩放率
         * @return 缩放后图片
         */
        public static Bitmap zoomBitmap(Bitmap bitmap, float scale) {
            int width = bitmap.getWidth();// 获取原图的宽
            int height = bitmap.getHeight();// 获取原图的高
            Matrix matrix = new Matrix();// 创建Matrix矩阵对象
            matrix.setScale(scale, scale);// 设置宽高的缩放比
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }

        public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
            return new BitmapDrawable(context.getResources(), bitmap);
        }

        public static Bitmap getBitmapById(Context context, int id) {
            Drawable drawable = context.getResources().getDrawable(id);
            return DrawableUtil.drawableToBitmap(drawable);
        }

        public static Bitmap buildRoundBitmap(Bitmap bitmap, float round) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            RectF rectF = new RectF(0, 0, width, height);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);

            Bitmap roundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(roundBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawRoundRect(rectF, round, round, paint);

            return roundBitmap;
        }
    }

    public static class DrawableUtil {

        public static Bitmap drawableToBitmap(Drawable drawable) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            return buildBitmap(drawable, width, height);
        }

        public static Bitmap drawableToBitmap(Drawable drawable, float scale) {
            int width = (int) (drawable.getIntrinsicWidth() * scale);
            int height = (int) (drawable.getIntrinsicHeight() * scale);
            return buildBitmap(drawable, width, height);
        }

        private static Bitmap buildBitmap(Drawable drawable, int width, int height) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            return bitmap;
        }

    }

}
