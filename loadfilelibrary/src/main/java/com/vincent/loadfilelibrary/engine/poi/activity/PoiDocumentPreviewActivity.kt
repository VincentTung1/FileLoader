package com.vincent.loadfilelibrary.engine.poi.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ldoublem.loadingviewlib.view.LVEatBeans
import com.miracle.documentviewer.*
import com.vincent.loadfilelibrary.BaseActivity
import com.vincent.loadfilelibrary.LoadFileManager
import com.vincent.loadfilelibrary.R
import com.vincent.loadfilelibrary.popwindow.listview.ActionItem
import com.vincent.loadfilelibrary.popwindow.listview.ListViewPopup
import com.vincent.loadfilelibrary.topbar.NavigationBar
import com.vincent.loadfilelibrary.topbar.NavigationBarListener
import com.vincent.loadfilelibrary.topbar.TopBarBuilder




/**
 *  基于POI为基础的office类型文件浏览器
 */
class PoiDocumentPreviewActivity : BaseActivity() {
    companion object {
        private const val PREVIEW_URI = "previewUri"

        fun startPreview(ctx: Context, uri: Uri) {
            val intent = Intent(ctx, PoiDocumentPreviewActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(PREVIEW_URI, uri)
            ctx.startActivity(intent)
        }
    }

    private lateinit var mLoadingView: LVEatBeans

    private lateinit var mTopBar: NavigationBar


    /**右上角菜单弹窗*/
    private var titlePopup: ListViewPopup? = null

    private var mName : String = ""

    internal var mTopBarDefaultHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_viewer)

        mTopBar = findViewById(R.id.mTopBar)
        mTopBar.fitsSystemWindows = false  //取消沉浸式状态栏

        mLoadingView = findViewById<LVEatBeans>(R.id.view_loading).apply {
            setViewColor(Color.BLACK)
            setEyeColor(Color.WHITE)
        }
        startLoading()
        val previewUri = intent.getParcelableExtra<Uri>(PREVIEW_URI) ?: kotlin.run {
            finish()
            return
        }
        val container = findViewById<FrameLayout>(R.id.preview_container)
        val viewer = DocumentViewerRepo.getViewerByUri(this, previewUri) ?: kotlin.run {
            toast(this, R.string.not_supported_file_in_builtin_app)
            finish()
            return
        }
        try {
            viewer.getParser().parse(previewUri).render(container, object : DocumentCallback<DVResult> {
                override fun onResult(result: DVResult) {
                    super.onResult(result)
                    println("render success with uri:$previewUri")
                    stopLoading()
                }

                override fun onException(exception: DocumentException) {
                    super.onException(exception)
                    exception.printStackTrace()
                    println("render failed with uri=$previewUri:"+ exception)
                    toast(this@PoiDocumentPreviewActivity, R.string.preview_error)
                    finish()
                }
            })
        } catch (ex: DocumentException) {
            toast(this, R.string.preview_error)
            finish()
        }



        val uriDecode = Uri.decode(previewUri.toString());
        mName = uriDecode.substring(uriDecode.lastIndexOf("/")+1)
        initTopBar()
        initListeners();
    }

    private fun initListeners() {
        mTopBar.setNavigationBarListener(object :NavigationBarListener{
            override fun onClick(containView: ViewGroup?, location: NavigationBar.Location?) {
                when (location) {
                    NavigationBar.Location.LEFT_FIRST -> finish()

                    NavigationBar.Location.RIGHT_FIRST -> showOptionsMenu(containView)
                }
            }
        })

        if (LoadFileManager.get().options != null && LoadFileManager.get().options.size > 0){
            TopBarBuilder.buildOnlyImageByDrawable(
                    mTopBar,this,NavigationBar.Location.RIGHT_FIRST,
                    resources.getDrawable(R.drawable.ic_more))

            titlePopup = ListViewPopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            LoadFileManager.get().options.forEach {

               titlePopup!!.addAction(ActionItem(null, it));
            }

            titlePopup!!.setItemOnClickListener(LoadFileManager.get().topBarItemClickListener)
        }
    }

    private fun initTopBar() {
        TopBarBuilder.buildLeftArrowTextById(mTopBar, this, R.string.back)
        mTopBar.setBackgroundColor(resources.getColor(R.color.white))
        mTopBar.setTextColorByLocation(NavigationBar.Location.LEFT_FIRST, resources.getColor(android.R.color.black))
        TopBarBuilder.buildCenterTextTitle(mTopBar, this, mName, resources.getColor(android.R.color.black))

        mTopBar.measure(0, 0)
        mTopBarDefaultHeight = mTopBar.measuredHeight
    }

    private fun startLoading() {
        if (mLoadingView.visibility != View.VISIBLE) {
            mLoadingView.visibility = View.VISIBLE
        }
        mLoadingView.startAnim(3000)
    }

    private fun stopLoading() {
        mLoadingView.stopAnim()
        if (mLoadingView.visibility != View.GONE) {
            mLoadingView.visibility = View.GONE
        }
    }


    internal var lastY = 0

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> lastY = ev.rawY.toInt()
            MotionEvent.ACTION_MOVE ->

                if (ev.rawY - lastY > 0) {
                    showTopBar()
                } else {
                    hideTopBar()
                }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 显示顶部栏
     */
    fun showTopBar() {

        if (mTopBar.height == 0) {
            val anim = ValueAnimator.ofInt(0, mTopBarDefaultHeight)
            updateTopBarLayout(anim)
        }
    }

    /**
     * 隐藏顶部栏
     */
    fun hideTopBar() {
        if (mTopBar.height == mTopBarDefaultHeight) {
            val anim = ValueAnimator.ofInt(mTopBarDefaultHeight, 0)
            updateTopBarLayout(anim)
        }
    }

    private fun updateTopBarLayout(anim: ValueAnimator) {
        anim.duration = 1000
        anim.addUpdateListener { animation ->
            val value = anim.animatedValue as Int
            val params = mTopBar.layoutParams
            params.height = value
            mTopBar.layoutParams = params
        }
        anim.start()
    }

    private fun showOptionsMenu(containView: ViewGroup?) {
        titlePopup!!.show(containView);
    }
}