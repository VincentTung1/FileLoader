package com.vincent.loadfilelibrary.engine.poi.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ldoublem.loadingviewlib.view.LVEatBeans
import com.miracle.documentviewer.*
import com.vincent.loadfilelibrary.BaseActivity
import com.vincent.loadfilelibrary.R
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

    private var mName : String = ""

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
                }
            }
        })
    }

    private fun initTopBar() {
        TopBarBuilder.buildLeftArrowTextById(mTopBar, this, R.string.back)
        mTopBar.setBackgroundColor(resources.getColor(R.color.white))
        mTopBar.setTextColorByLocation(NavigationBar.Location.LEFT_FIRST, resources.getColor(android.R.color.black))
        TopBarBuilder.buildCenterTextTitle(mTopBar, this, mName, resources.getColor(android.R.color.black))
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
}