package com.vincent.loadfilelibrary.topbar;

import android.graphics.drawable.Drawable;

public class NavigationBarBean {

    public enum Direction {
        LEFT(1),
        RIGHT(2);
        private int direction;

        Direction(int direction) {
            this.direction = direction;
        }

        public static Direction create(int direction) {
            switch (direction) {
                case 1:
                    return LEFT;
                case 2:
                    return RIGHT;
            }
            return null;
        }
    }

    public enum Placement {
        LEFT_IMAGE_RIGHT_TEXT(1),
        LEFT_TEXT_RIGHT_IMAGE(2);
        private int placement;

        Placement(int placement) {
            this.placement = placement;
        }

        public static Placement create(int placement) {
            switch (placement) {
                case 1:
                    return LEFT_IMAGE_RIGHT_TEXT;
                case 2:
                    return LEFT_TEXT_RIGHT_IMAGE;
            }
            return null;
        }
    }

    private String text;

    private float textSize;

    private int textColor;

    private Drawable drawable;

    private int drawableID;

    private float drawableScale;

    private float padding;

    private Direction direction;

    private Placement placement;

    private int centerImageSide;

    private String centerTag;

    private int centerMargin;

    public int marginLeft, marginTop, marginRight, marginBottom;

    public boolean clickable = true;

    public NavigationBarBean() {
        drawableScale = 1;
    }

    /**
     * 入参数值的类型都是dp
     *
     * @param text          按钮文字
     * @param textSize      文字大小
     * @param drawable      图片
     * @param drawableID    图片ID
     * @param drawableScale 图片缩放值
     * @param padding       图片边距值
     * @param direction     带箭头的按钮样式的箭头方向
     * @param placement     图片文字样式的图片和文字位置
     */
    public NavigationBarBean(String text, float textSize, Drawable drawable, int drawableID, float drawableScale, float padding, Direction direction, Placement placement) {
        this();
        this.text = text;
        this.textSize = textSize;
        this.drawable = drawable;
        this.drawableID = drawableID;
        this.drawableScale = drawableScale;
        this.padding = padding;
        this.direction = direction;
        this.placement = placement;
    }

    public NavigationBarBean(String text, float textSize, int textColor, Drawable drawable, int drawableID, float drawableScale, float padding, Direction direction, Placement placement) {
        this();
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.drawable = drawable;
        this.drawableID = drawableID;
        this.drawableScale = drawableScale;
        this.padding = padding;
        this.direction = direction;
        this.placement = placement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public float getDrawableScale() {
        return drawableScale;
    }

    public void setDrawableScale(float drawableScale) {
        this.drawableScale = drawableScale;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public int getCenterImageSide() {
        return centerImageSide;
    }

    public void setCenterImageSide(int centerImageSide) {
        this.centerImageSide = centerImageSide;
    }

    public String getCenterTag() {
        return centerTag;
    }

    public void setCenterTag(String centerTag) {
        this.centerTag = centerTag;
    }

    public int getCenterMargin() {
        return centerMargin;
    }

    public void setCenterMargin(int centerMargin) {
        this.centerMargin = centerMargin;
    }
}
