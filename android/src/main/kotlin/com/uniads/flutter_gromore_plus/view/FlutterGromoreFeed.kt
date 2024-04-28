package com.uniads.flutter_gromore_plus.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.platform.PlatformView
import com.uniads.flutter_gromore_plus.constants.FlutterGromoreConstants
import com.uniads.flutter_gromore_plus.event.AdEventType
import com.uniads.flutter_gromore_plus.manager.FlutterGromoreFeedCache
import com.uniads.flutter_gromore_plus.utils.DataReportUtil
import com.uniads.flutter_gromore_plus.utils.Utils


class FlutterGromoreFeed(
        private val context: Context,
        private val activity: Activity,
        viewId: Int,
        creationParams: Map<String?, Any?>,
        binaryMessenger: BinaryMessenger
) :
        FlutterGromoreBase(binaryMessenger, "${FlutterGromoreConstants.feedViewTypeId}/$viewId"),
        PlatformView,
        TTAdDislike.DislikeInteractionCallback, MediationExpressRenderListener {

    private val TAG: String = this::class.java.simpleName

    // 信息流广告容器
    private var container: FrameLayout = FrameLayout(context)

    // 广告model
    private var mGMNativeAd: TTFeedAd? = null

    // 广告ID
    private var adUnitId: String = ""

    private var layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private val cachedAdId: String

    init {
        container.layoutParams = layoutParams
        cachedAdId = creationParams["feedId"] as String
        // 从缓存中取广告
        mGMNativeAd = FlutterGromoreFeedCache.getCacheFeedAd(cachedAdId)
        adUnitId = FlutterGromoreFeedCache.getCacheFeedAdUnitId(cachedAdId)?: ""
        initAd()
    }

    private fun showAd() {

        mGMNativeAd?.takeIf {
            it.mediationManager.isReady
        }?.let {

            // 是否有不喜欢按钮
            if (it.mediationManager.hasDislike()) {
                it.setDislikeCallback(activity, this)
            }

            if (it.mediationManager.isExpress) {
                it.setExpressRenderListener(this)
            }

            it.render()
        }
    }

    private fun removeAdView() {
        container.removeAllViews()
        mGMNativeAd?.destroy()
        mGMNativeAd = null
        FlutterGromoreFeedCache.removeCacheFeedAd(cachedAdId)
        FlutterGromoreFeedCache.removeCacheFeedAdUnitId(cachedAdId)
    }

    override fun getView(): View {
        return container
    }

    override fun dispose() {
        removeAdView()
    }

    override fun onAdClick() {
        Log.d(TAG, "onAdClick")
        postMessage("onAdClick")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onAdClick", null)
    }

    override fun onAdShow() {
        Log.d(TAG, "onAdShow")
        postMessage("onAdShow")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onAdShow", null)
    }

    override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
        Log.d(TAG, "onRenderFail - $p2 - $p1")
        postMessage("onRenderFail")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onRenderFail", null)
    }

    // 必须在onRenderSuccess进行广告展示，否则会导致广告无法展示
    override fun onRenderSuccess(p0: View?, width: Float, height: Float, isExpress: Boolean) {
        Log.d(TAG, "onRenderSuccess - $width - $height")

        val ad = mGMNativeAd?.adView
        ad?.let { view ->
            (view.parent as? ViewGroup)?.removeView(view)
        }

        ad?.apply {
            container.removeAllViews()
            container.setBackgroundColor(Color.WHITE)
            container.addView(ad)
            if (height > 0) {
                postMessage(
                        "onRenderSuccess",
                        mapOf("height" to height)
                )
                DataReportUtil.report(adUnitId, AdEventType.Feed, "onRenderSuccess", null)
                return
            }
        }

        // 兼容height小于等于0的情况，如 快手广告
        ad?.apply {
            // 计算渲染后的高度
            measure(View.MeasureSpec.makeMeasureSpec(Utils.getScreenWidthInPx(context), View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(Utils.getScreenHeightInPx(context), View.MeasureSpec.UNSPECIFIED))
            Log.d(TAG, "measuredHeight - $measuredHeight")
        }?.takeIf {
            it.measuredHeight > 0
        }?.let {
            postMessage(
                    "onRenderSuccess",
                    mapOf("height" to it.measuredHeight / Utils.getDensity(context))
            )
            DataReportUtil.report(adUnitId, AdEventType.Feed, "onRenderSuccess", null)
        }

    }

    override fun onCancel() {
        Log.d(TAG, "dislike-onCancel")
        postMessage("onCancel")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onCancel", null)
    }

    override fun onShow() {
        Log.d(TAG, "dislike-onShow")
        postMessage("onShow")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onShow", null)
    }

    // 用户选择不喜欢原因后，移除广告展示
    override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
        Log.d(TAG, "dislike-onSelected")
        postMessage("onSelected")
        DataReportUtil.report(adUnitId, AdEventType.Feed, "onSelected", null)
        removeAdView()
    }

    override fun initAd() {
        showAd()
    }
}