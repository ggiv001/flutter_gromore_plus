package com.uniads.flutter_gromore_plus.view

import android.app.Activity
import android.util.Log
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import com.uniads.flutter_gromore_plus.constants.FlutterGromoreConstants
import com.uniads.flutter_gromore_plus.event.AdEventType
import com.uniads.flutter_gromore_plus.manager.FlutterGromoreInterstitialCache
import com.uniads.flutter_gromore_plus.utils.DataReportUtil


class FlutterGromoreInterstitial(private val activity: Activity,
                                 binaryMessenger: BinaryMessenger,
                                 creationParams: Map<String, Any?>,
                                 private val result: MethodChannel.Result) :
        FlutterGromoreBase(binaryMessenger, "${FlutterGromoreConstants.interstitialTypeId}/${creationParams["interstitialId"]}"), TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {

    private val TAG: String = this::class.java.simpleName

    private var mInterstitialAd: TTFullScreenVideoAd? = null
    private var interstitialId: Int = 0
    private var adUnitId: String = ""

    init {
        interstitialId = (creationParams["interstitialId"] as String).toInt()
        mInterstitialAd = FlutterGromoreInterstitialCache.getCacheInterstitialAd(interstitialId)
        adUnitId = FlutterGromoreInterstitialCache.getCacheInterstitialAdUnitId(interstitialId) ?: ""
        initAd()
    }

    private fun showAd() {
        mInterstitialAd.takeIf {
            it != null && it.mediationManager.isReady
        }?.let {
            // 真正展示
            it.setFullScreenVideoAdInteractionListener(this)
            it.showFullScreenVideoAd(activity)
        }
    }

    // 初始化插屏广告
    override fun initAd() {
        showAd()
    }

    private fun destroyAd() {
        mInterstitialAd?.mediationManager?.destroy()
        mInterstitialAd = null

        FlutterGromoreInterstitialCache.removeCacheInterstitialAd(interstitialId)
        FlutterGromoreInterstitialCache.removeCacheInterstitialAdUnitId(interstitialId)
    }

    // 广告展示
    override fun onAdShow() {
        Log.d(TAG, "onInterstitialShow")
        postMessage("onInterstitialShow")
        DataReportUtil.report(adUnitId, AdEventType.Interstitial, "onInterstitialShow", null)
    }

    // 广告被点击
    override fun onAdVideoBarClick() {
        Log.d(TAG, "onInterstitialAdClick")
        postMessage("onInterstitialAdClick")
        DataReportUtil.report(adUnitId, AdEventType.Interstitial, "onInterstitialAdClick", null)
    }

    // 关闭广告
    override fun onAdClose() {
        Log.d(TAG, "onInterstitialClosed")
        result.success(true)
        postMessage("onInterstitialClosed")
        DataReportUtil.report(adUnitId, AdEventType.Interstitial, "onInterstitialClosed", null)
        destroyAd()
    }

    // 跳过视频
    override fun onSkippedVideo() {
        Log.d(TAG, "onSkippedVideo")
        postMessage("onSkippedVideo")
        DataReportUtil.report(adUnitId, AdEventType.Interstitial, "onSkippedVideo", null)
    }

    // 播放视频完成
    override fun onVideoComplete() {
        Log.d(TAG, "onVideoComplete")
        postMessage("onVideoComplete")
        DataReportUtil.report(adUnitId, AdEventType.Interstitial, "onVideoComplete", null)
    }
}