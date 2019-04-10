package com.vincent.loadfilelibrary.topbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.topbar.utils.DensityUtil;
import com.vincent.loadfilelibrary.topbar.utils.ImageUtils;
import com.vincent.loadfilelibrary.topbar.utils.ResourcesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NavigationBar extends RelativeLayout {

//    private static final String TAG = NavigationBar.class.getSimpleName();

    public enum Location {
        LEFT_FIRST(1),
        LEFT_SECOND(2),
        CENTER(3),
        RIGHT_SECOND(4),
        RIGHT_FIRST(5);

        private int location;

        Location(int location) {
            this.location = location;
        }

        public int getLocationCode() {
            return location;
        }

        public static Location create(int location) {
            switch (location) {
                case 1:
                    return LEFT_FIRST;
                case 2:
                    return LEFT_SECOND;
                case 3:
                    return CENTER;
                case 4:
                    return RIGHT_SECOND;
                case 5:
                    return RIGHT_FIRST;
            }
            return null;
        }
    }

    public enum Style {
        STYLE_ARROWTEXT(1),
        STYLE_IMAGETEXT(2),
        STYLE_ONLYIMAGE(3),
        STYLE_ONLYTEXT(4);

        private int style;

        Style(int style) {
            this.style = style;
        }

        public static Style create(int style) {
            switch (style) {
                case 1:
                    return STYLE_ARROWTEXT;
                case 2:
                    return STYLE_IMAGETEXT;
                case 3:
                    return STYLE_ONLYIMAGE;
                case 4:
                    return STYLE_ONLYTEXT;
            }
            return null;
        }
    }

//    private Paint mLinePaint;
    private int mWidth;
    private int mHeight;
    private View mRootView;

    private Map<Location, ViewGroup> mRLMap;

    private NavigationBarListener mListener;

    private View mUnderLine;

    private boolean mIsFitSystemWindow;
    private boolean mShouldFitSystemWindow;

    private static final int ANIMATION_EXTEND = 1;
    private static final int ANIMATION_SHRINK = 2;

    private ValueAnimator mAnimator;
    private int mMinScaleHeight;
    private int mMaxScaleYHeight = -1;
    private int mDuration;
    private int mCurrentAnmiation;
    private Animator.AnimatorListener mAnimatorListener;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(ResourcesUtil.getResourcesColor(getContext(), R.color.white));
        initFields();
        initRootView();
        initChildView();
        initChildListener();
    }

    private void initFields() {
//        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        float strokeWidth = getContext().getResources().getDimension(R.dimen.dividingline_width);
//        mLinePaint.setStrokeWidth(strokeWidth);
//        int color = getContext().getResources().getColor(R.color.black);
//        mLinePaint.setColor(color);
        mRLMap = new HashMap<>();
//        mDrawLine = true;
        mDuration = 500;
        mMinScaleHeight = getStatusBarHeight(getContext());
    }

    private void initRootView() {
        mRootView = View.inflate(getContext(), R.layout.view_navigationbar, null);
        addView(mRootView);
        setFitsSystemWindows(true);
    }

    @SuppressWarnings("unchecked")
    private <V extends View> V findViewFromId(int id) {
        return (V) findViewById(id);
    }

    private void initChildView() {
        RelativeLayout mRLLeftFirst = findViewFromId(R.id.rl_left_first);
        mRLMap.put(Location.LEFT_FIRST, mRLLeftFirst);

        RelativeLayout mRLLeftSecond = findViewFromId(R.id.rl_left_second);
        mRLMap.put(Location.LEFT_SECOND, mRLLeftSecond);

        LinearLayout mRLCenter = findViewFromId(R.id.rl_center);
        mRLMap.put(Location.CENTER, mRLCenter);

        RelativeLayout mRLRightSecond = findViewFromId(R.id.rl_right_second);
        mRLMap.put(Location.RIGHT_SECOND, mRLRightSecond);

        RelativeLayout mRLRightFirst = findViewFromId(R.id.rl_right_first);
        mRLMap.put(Location.RIGHT_FIRST, mRLRightFirst);

        mUnderLine = findViewById(R.id.underline);
    }

    private void initChildListener() {
        Set<Location> locations = mRLMap.keySet();
        for (final Location location : locations) {
            ViewGroup layout = mRLMap.get(location);
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick((ViewGroup) v, location);
                    }
                }
            });
            if (Location.CENTER != location) {
                layout.addOnLayoutChangeListener(onLayoutChangeListener);
            }
            layout.setClickable(false);
        }
    }

    private OnLayoutChangeListener onLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int width = right - left;
            int oldWidth = oldRight - oldLeft;
            if (oldWidth > 0 && width != oldWidth) {
                changeCenterLayout();
            }
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            if (mMaxScaleYHeight < 0) {
                mMaxScaleYHeight = b - t;
            }
            changeCenterLayout();
        }
    }

    private void changeCenterLayout() {
        post(new Runnable() {
            @Override
            public void run() {
                int leftSecondWidth = mRLMap.get(Location.LEFT_SECOND).getVisibility() == GONE ? 0 : mRLMap.get(Location.LEFT_SECOND).getWidth();
                int leftTotalWidth = mRLMap.get(Location.LEFT_FIRST).getWidth() + leftSecondWidth;

                int rightSecondWidth = mRLMap.get(Location.RIGHT_SECOND).getVisibility() == GONE ? 0 : mRLMap.get(Location.RIGHT_SECOND).getWidth();
                ViewGroup rightFirst = mRLMap.get(Location.RIGHT_FIRST);
                ViewGroup.MarginLayoutParams rfParams = (MarginLayoutParams) rightFirst.getLayoutParams();
                int rightTotalWidth = rightFirst.getWidth() + rfParams.rightMargin + rightSecondWidth;

                LinearLayout layout = (LinearLayout) mRLMap.get(Location.CENTER);
                ViewGroup.LayoutParams params = layout.getLayoutParams();
                /*间距*/
                int margin = DensityUtil.dip2pxInt(getContext(), 5);
                /*选择宽度大的一边*/
                int maxSideWidth = Math.max(leftTotalWidth, rightTotalWidth);
                int centerWidth = mWidth - (maxSideWidth + margin) * 2;
                params.width = centerWidth;
                measureCenterChildrenIfNeed(layout, centerWidth);
                layout.requestLayout();
            }
        });
    }

    private void measureCenterChildrenIfNeed(LinearLayout layout, int centerWidth) {
        float tvWidthTotal = 0;
        List<TextView> tvList = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                tvList.add(tv);
                TextPaint paint = tv.getPaint();
                tvWidthTotal += paint.measureText(tv.getText().toString());
            }
        }
        if (tvWidthTotal > centerWidth) {
            for (TextView child :
                    tvList) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                params.weight = 1;
            }
        }
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        if (mDrawLine) {
//            float height = mHeight - mLinePaint.getStrokeWidth();
//            canvas.drawLine(0, height, mWidth, height, mLinePaint);
//        }
//    }

    public void setNavigationBarListener(NavigationBarListener listener) {
        mListener = listener;
    }

    public void setLeftSingle(boolean single) {
        setSingle(single, 0);
    }

    public void setRightSingle(boolean single) {
        setSingle(single, 1);
    }

    /**
     * 0--left, 1--right
     */
    private void setSingle(boolean single, int leftRight) {
        RelativeLayout layout;
        if (leftRight == 0) {
            layout = (RelativeLayout) mRLMap.get(Location.LEFT_SECOND);
        } else if (leftRight == 1) {
            layout = (RelativeLayout) mRLMap.get(Location.RIGHT_SECOND);
        } else {
            return;
        }

        if (layout != null) {
            if (single) {
                layout.setVisibility(GONE);
            } else {
                layout.setVisibility(VISIBLE);
            }
        }
    }

    public void setCenterTextByPosition(int position, String text) {
        View view = mRLMap.get(Location.CENTER).getChildAt(position);

        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    public void setCenterTextColorByPosition(int position, int color) {
        View view = mRLMap.get(Location.CENTER).getChildAt(position);

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    public void setCenterImageByPosition(int position, Bitmap image) {
        View view = mRLMap.get(Location.CENTER).getChildAt(position);

        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(image);
        }
    }

    public void setTextByLocation(Location location, String text) {
        View view = mRLMap.get(location).getChildAt(0);

        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    public CharSequence getTextByLocation(Location location) {
        View view = mRLMap.get(location).getChildAt(0);

        if (view instanceof TextView) {
            return ((TextView) view).getText();
        } else {
            return null;
        }
    }

    public void setTextColorByLocation(Location location, int color) {
        View view = mRLMap.get(location).getChildAt(0);

        if (view != null && view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    public void setVisibleByLocation(Location location, int visible) {
        View view = mRLMap.get(location).getChildAt(0);
        if (view != null) view.setVisibility(visible);
    }

    public void setEnableByLocation(Location location, boolean enable) {
        View view = mRLMap.get(location);
        if (view != null) view.setEnabled(enable);
    }

    public void setImageByLocation(Location location, Bitmap image) {
        View view = mRLMap.get(location).getChildAt(0);

        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(image);
        }
    }

    public View getViewByLocation(Location location) {
        return mRLMap.get(location);
    }

    public void addCenterContent(Style style, NavigationBarBean bean) {
        View view = createView(style, bean);
        if (view != null) {
            LinearLayout layout = (LinearLayout) mRLMap.get(Location.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (Style.STYLE_ONLYIMAGE.equals(style)) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                int sideLength = bean.getCenterImageSide();
                if (sideLength > 0 || sideLength == LayoutParams.MATCH_PARENT) {
                    params.width = sideLength;
                    params.height = sideLength;
                }
                params.leftMargin = bean.getCenterMargin();
            }
            view.setLayoutParams(params);

            String centerTag = bean.getCenterTag();
            if (!TextUtils.isEmpty(centerTag)) {
                view.setTag(centerTag);
            }


            if (view instanceof TextView){
                TextView tv = (TextView) view;
                tv.setSingleLine();
                tv.setMaxEms(8);   //顶部栏中间文字限制最大字数为8个字符
            }

            layout.addView(view);
            layout.setClickable(true);
        } else {
            throw new RuntimeException("生成内容失败");
        }
    }

    public void removeCenterContentByTag(String tag) {
        LinearLayout layout = (LinearLayout) mRLMap.get(Location.CENTER);
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View child = layout.getChildAt(i);
            Object childTag = child.getTag();
            if (childTag != null && childTag.equals(tag)) {
                layout.removeView(child);
            }
        }
        if (layout.getChildCount() == 0) {
            layout.setClickable(false);
        }
    }

    public void clearCenterContent() {
        LinearLayout layout = (LinearLayout) mRLMap.get(Location.CENTER);
        layout.removeAllViews();
        layout.setClickable(false);
    }

    public void setContent(Location location, Style style, NavigationBarBean bean) {
        if (location == Location.CENTER) {
            throw new RuntimeException("Location参数不能为CENTER，如果需要对中间控件进行内容填充，请调用addCenterContent方法");
        }

        View view = createView(style, bean);
        if (view != null) {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.addRule(CENTER_VERTICAL);
            view.setLayoutParams(params);

            RelativeLayout layout = (RelativeLayout) mRLMap.get(location);
            layout.removeAllViews();
            layout.addView(view);
            if (bean.clickable && !layout.isClickable()) {
                layout.setClickable(true);
            }
        } else {
            throw new RuntimeException("生成内容失败");
        }
    }

    public void clearContent(Location location) {
        RelativeLayout layout = (RelativeLayout) mRLMap.get(location);
        layout.removeAllViews();
        layout.setClickable(false);
    }

    public void drawLine(boolean drawLine) {
        if (drawLine) {
            mUnderLine.setVisibility(VISIBLE);
        } else {
            mUnderLine.setVisibility(GONE);
        }
    }

    private View createView(Style style, NavigationBarBean bean) {
        if (bean == null) {
            throw new RuntimeException("缺少NavigationBarBean");
        }
        View view = null;
        switch (style) {
            case STYLE_ARROWTEXT:
                view = createArrowText(bean);
                break;
            case STYLE_IMAGETEXT:
                view = createImageText(bean);
                break;
            case STYLE_ONLYIMAGE:
                view = createOnlyImage(bean);
                break;
            case STYLE_ONLYTEXT:
                view = createOnlyText(bean);
                break;
        }
        if (view != null) {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.leftMargin = bean.marginLeft;
            params.topMargin = bean.marginTop;
            params.rightMargin = bean.marginRight;
            params.bottomMargin = bean.marginBottom;
        }
        return view;
    }

    private View createArrowText(NavigationBarBean bean) {
        Drawable arrow;
        if (NavigationBarBean.Direction.LEFT == bean.getDirection()) {
            arrow = getContext().getResources().getDrawable(R.drawable.navigation_back);
        } else {
            throw new RuntimeException("暂无此样式");
        }

        if (arrow == null) {
            throw new RuntimeException("获取资源图片失败");
        }
        return createTextWithImage(
                bean.getText(),
                bean.getTextSize(),
                bean.getTextColor(),
                NavigationBarBean.Placement.LEFT_IMAGE_RIGHT_TEXT,
                arrow,
                bean.getDrawableScale(),
                bean.getPadding());
    }

    private View createImageText(NavigationBarBean bean) {
        Drawable image = bean.getDrawable();
        if (image == null) {
            int drawableID = bean.getDrawableID();
            image = getContext().getResources().getDrawable(drawableID);
            if (image == null) {
                throw new RuntimeException(drawableID + "--该ID对应的图片资源无效");
            }
        }
        return createTextWithImage(
                bean.getText(),
                bean.getTextSize(),
                bean.getTextColor(),
                bean.getPlacement(),
                bean.getDrawable(),
                bean.getDrawableScale(),
                bean.getPadding());
    }

    private TextView createTextWithImage(String text, float textSize, int textColor, NavigationBarBean.Placement placement, Drawable image, float scale, float padding) {
        /*基础参数配置*/
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setIncludeFontPadding(false);
        textView.setText(text);
        textView.setTextSize(textSize);
        if (textColor != 0) {
            textView.setTextColor(textColor);
        }
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        /*缩放图片*/
        int imageWidth, imageHeight;
        if (scale > 0) {
            image = zoomDrawableToDrawable(getContext(), image, scale);
        }
        if (image instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
            if (scale > 0) {
                bitmap = ImageUtils.BitmapUtil.zoomBitmap(bitmap, scale);
            }
            imageWidth = bitmap.getWidth();
            imageHeight = bitmap.getHeight();
        } else {
            imageWidth = image.getIntrinsicWidth();
            imageHeight = image.getIntrinsicHeight();
        }
        /*设置图片到对应位置*/
        image.setBounds(0, 0, imageWidth, imageHeight);
        if (NavigationBarBean.Placement.LEFT_IMAGE_RIGHT_TEXT == placement) {
            textView.setCompoundDrawables(image, null, null, null);
        } else if (NavigationBarBean.Placement.LEFT_TEXT_RIGHT_IMAGE == placement) {
            textView.setCompoundDrawables(null, null, image, null);
        } else {
            throw new RuntimeException("参数错误，请查看ImageTextLocation是否正确");
        }
        /*设置图片Padding值*/
        int drawablePadding = DensityUtil.dip2pxIntNonCompat(getContext(), 6);
        textView.setCompoundDrawablePadding(drawablePadding);
        /*设置TextView的Padding值*/
        int viewPadding = DensityUtil.dip2pxIntNonCompat(getContext(), padding);
        textView.setPadding(viewPadding, viewPadding, viewPadding, viewPadding);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    private View createOnlyText(NavigationBarBean bean) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setText(bean.getText());
        textView.setTextSize(bean.getTextSize());
        if (bean.getTextColor() != 0) {
            textView.setTextColor(bean.getTextColor());
        }
        /*设置TextView的Padding值*/
        int padding = DensityUtil.dip2pxInt(getContext(), bean.getPadding());
        textView.setPadding(padding, padding, padding, padding);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    private View createOnlyImage(NavigationBarBean bean) {
        Drawable image = bean.getDrawable();
        if (image == null) {
            image = getContext().getResources().getDrawable(bean.getDrawableID());
            if (image == null) {
                throw new RuntimeException("无法获取图片");
            }
        }
        /*缩放图片*/
        float scale = bean.getDrawableScale();
        if (scale > 0) {
            image = zoomDrawableToDrawable(getContext(), image, scale);
        }
        if (image instanceof BitmapDrawable) {
            ((BitmapDrawable) image).setTargetDensity(DensityUtil.getNonCompatDensityDpi(getContext()));
        }
        /*基础参数配置*/
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(image);
        /*设置ImageView的Padding值*/
        int padding = DensityUtil.dip2pxIntNonCompat(getContext(), bean.getPadding());
        imageView.setPadding(padding, padding, padding, padding);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);

        return imageView;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        if (mShouldFitSystemWindow) {
            setFitsSystemWindows(true);
            mShouldFitSystemWindow = false;
        }
    }

    @Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            if (fitSystemWindows) mShouldFitSystemWindow = true;
            return;
        }
        int statusBarHeight = getStatusBarHeight(getContext());
        if (fitSystemWindows) {
            if (!mIsFitSystemWindow) {
                if (params.height >= 0) {
                    params.height += statusBarHeight;
                    requestLayout();
                }
                int paddingTop = getPaddingTop() + statusBarHeight;
                setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), getPaddingBottom());
                mIsFitSystemWindow = true;
            }
        } else {
            if (mIsFitSystemWindow) {
                if (params.height >= 0) {
                    params.height -= statusBarHeight;
                    requestLayout();
                }
                int paddingTop = getPaddingTop() - statusBarHeight;
                setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), getPaddingBottom());
                mIsFitSystemWindow = false;
            }
        }
    }

    public void animationShrink() {
        if (mCurrentAnmiation != ANIMATION_SHRINK) {
            doScaleAnimation(mMinScaleHeight);
            mCurrentAnmiation = ANIMATION_SHRINK;
        }
    }

    public void animationExtend() {
        if (mCurrentAnmiation != ANIMATION_EXTEND) {
            doScaleAnimation(mMaxScaleYHeight);
            mCurrentAnmiation = ANIMATION_EXTEND;
        }
    }

    private void doScaleAnimation(int endHeight) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        final ViewGroup.LayoutParams params = getLayoutParams();
        final int startHeight = params.height;
        if (startHeight == endHeight) return;
        mAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                params.height = height;
                requestLayout();
                mRootView.setAlpha((height - mMinScaleHeight) * 1f / (mMaxScaleYHeight - mMinScaleHeight));
            }
        });
        if (mAnimatorListener != null) mAnimator.addListener(mAnimatorListener);
        mAnimator.start();
    }

    public void setAnimatorListener(Animator.AnimatorListener listener) {
        mAnimatorListener = listener;
    }

    private static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height","dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static Bitmap zoomDrawableToBitmap(Drawable drawable, float scale) {
        Bitmap bitmap = ImageUtils.DrawableUtil.drawableToBitmap(drawable);
        return ImageUtils.BitmapUtil.zoomBitmap(bitmap, scale);
    }

    private static Drawable zoomDrawableToDrawable(Context context, Drawable drawable, float scale) {
        Bitmap bitmap = zoomDrawableToBitmap(drawable, scale);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
