package com.vincent.loadfilelibrary.engine.pdf;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import com.vincent.loadfilelibrary.BaseActivity;
import com.vincent.loadfilelibrary.LoadFileManager;
import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.popwindow.listview.ActionItem;
import com.vincent.loadfilelibrary.popwindow.listview.ListViewPopup;
import com.vincent.loadfilelibrary.topbar.NavigationBar;
import com.vincent.loadfilelibrary.topbar.TopBarBuilder;

import java.io.File;
import java.util.List;

public class PdfPreviewActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    public static final String SAMPLE_FILE = "sample.pdf";

    private static final String TAG = PdfPreviewActivity.class.getSimpleName();

    private static final String FILE_PATH = "filepath" ;


    private PDFView pdfView;

    private NavigationBar mTopBar;

    private ListViewPopup titlePopup;

    /**文件路径*/
    String mFilePath = "";
    /**文件名*/
    String mFfileName = "";

    String pdfFileName;

    Integer pageNumber = 0;

    int mTopBarDefaultHeight = 0;

    /**是否设置顶部栏隐藏*/
    boolean isEnableTopBarHide = false;

    public static void start(Context context, String filePath){
        Intent intent = new Intent(context,PdfPreviewActivity.class);
        intent.putExtra(FILE_PATH,filePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfpreview);

//        startActivity(new Intent(this,PDFViewActivity.class));
        initData();
        initViews();
        initListeners();
    }

    private void initData() {
        Intent intent = getIntent();
        mFilePath= intent.getStringExtra(FILE_PATH);
    }

    private void initViews() {
       mTopBar = findViewById(R.id.mTopBar);
       pdfView = (PDFView) findViewById(R.id.pdfView);
       TopBarBuilder.buildLeftArrowTextById(mTopBar,this,R.string.back);
       mTopBar.setBackgroundColor(getResources().getColor(R.color.white));
       mTopBar.setTextColorByLocation(NavigationBar.Location.LEFT_FIRST,getResources().getColor(android.R.color.black));
       mFfileName = mFilePath.substring(mFilePath.lastIndexOf("/")+1);
       TopBarBuilder.buildCenterTextTitle(mTopBar,this,mFfileName,getResources().getColor(android.R.color.black));

       mTopBar.setFitsSystemWindows(false);   //取消沉浸式状态栏

       displayFromAsset(SAMPLE_FILE);

        mTopBar.measure(0,0);
        mTopBarDefaultHeight = mTopBar.getMeasuredHeight();

        pdfView.setPadding(0,mTopBarDefaultHeight,0,0);

        isEnableTopBarHide = LoadFileManager.get().enableScrollHideTopbar();
    }

    private void initListeners() {
        mTopBar.setNavigationBarListener((v,location)->{

            switch (location){
                case LEFT_FIRST:
                    finish();
                    break;

                case RIGHT_FIRST:
                    showOptionsMenu(v);
                    break;
            }
        });

        if (LoadFileManager.get().getOptions() != null && LoadFileManager.get().getOptions().size ()> 0){
            TopBarBuilder.buildOnlyImageByDrawable(
                    mTopBar,this,NavigationBar.Location.RIGHT_FIRST,
                    getResources().getDrawable(R.drawable.ic_more));

            titlePopup =new ListViewPopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            for (String opt : LoadFileManager.get().getOptions()) {
                titlePopup.addAction(new ActionItem(null, opt));

            }

            titlePopup.setItemOnClickListener(LoadFileManager.get().getTopBarItemClickListener());
        }
    }


    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromFile(new File(mFilePath))
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();

    }


    int lastY = 0 ;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!isEnableTopBarHide) return super.dispatchTouchEvent(ev);

       switch (ev.getAction()){
           case MotionEvent.ACTION_DOWN:
               lastY = (int) ev.getRawY();
               break;
           case MotionEvent.ACTION_MOVE:

               if (ev.getRawY() - lastY > 0){
                   showTopBar();
               }else {
                   hideTopBar();
               }

               break;
       }
        return super.dispatchTouchEvent(ev);
    }


    /**
     *  显示顶部栏
     */
    public void showTopBar(){

        if (mTopBar.getY() == -mTopBarDefaultHeight){
            ValueAnimator anim = ValueAnimator.ofInt(-mTopBarDefaultHeight,0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pdfView.setPadding(0,mTopBarDefaultHeight,0,0);
                }
            });
            updateTopBarLayout(anim);
        }
    }

    /**
     *  隐藏顶部栏
     */
    public void hideTopBar(){

        if (mTopBar.getY() == 0){
            ValueAnimator anim = ValueAnimator.ofInt(0,-mTopBarDefaultHeight);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pdfView.setPadding(0,0,0,0);
                }
            });
            updateTopBarLayout(anim);
        }
    }

    private void updateTopBarLayout(ValueAnimator anim) {
        anim.setDuration(200);
        anim.addUpdateListener(animation -> {
            int value = (int) anim.getAnimatedValue();
            mTopBar.setY(value);
        });
        anim.start();
    }

    /**
     *  显示右上角菜单弹窗
     * @param containView
     */
    private void showOptionsMenu(ViewGroup containView) {
        titlePopup.show(containView);
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }


    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
