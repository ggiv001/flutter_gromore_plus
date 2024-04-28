package com.uniads.flutter_gromore_plus.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdNative.RewardVideoAdListener
import com.bytedance.sdk.openadsdk.TTRewardVideoAd
import com.bytedance.sdk.openadsdk.TTRewardVideoAd.RewardAdInteractionListener
import com.uniads.flutter_gromore_plus.constants.FlutterGromoreConstants
import com.uniads.flutter_gromore_plus.event.AdEventType
import com.uniads.flutter_gromore_plus.manager.FlutterGromoreRewardCache
import com.uniads.flutter_gromore_plus.utils.DataReportUtil
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel


class FlutterGromoreReward(
        private val activity: Activity,
        messenger: BinaryMessenger,
        creationParams: Map<String, Any?>,
        private val result: MethodChannel.Result) :
        FlutterGromoreBase(messenger, "${FlutterGromoreConstants.rewardTypeId}/${creationParams["rewardId"]}") {

    private val TAG: String = this::class.java.simpleName

    private var mttRewardAd: TTRewardVideoAd? = null
    private var rewardId: Int = 0
    private var adUnitId: String = ""
    private var adInstanceId: String = ""

    // 广告展示监听器
    private var mRewardVideoAdInteractionListener: RewardAdInteractionListener? = null

    // 再看一个广告展示监听器
    private var mRewardVideoAdPlayAgainInteractionListener: RewardAdInteractionListener? = null


    private fun initListeners() {
        val map = mapOf("adInstanceId" to adInstanceId)
        // 广告展示监听器
        mRewardVideoAdInteractionListener = object : RewardAdInteractionListener {
            override fun onAdShow() {
                Log.d(TAG, "onAdShow")
                postMessage("onAdShow")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdShow", map)
            }

            // 广告的下载bar点击回调，非所有广告商的广告都会触发
            override fun onAdVideoBarClick() {
                Log.d(TAG, "onAdVideoBarClick")
                postMessage("onAdVideoBarClick")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdVideoBarClick", map)
            }

            // 广告关闭的回调
            override fun onAdClose() {
                Log.d(TAG, "onAdClose")
                postMessage("onAdClose")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdClose", map)
                result.success(true)
                destroyAd()
            }

            // 视频播放完毕的回调，非所有广告商的广告都会触发
            override fun onVideoComplete() {
                Log.d(TAG, "onVideoComplete")
                postMessage("onVideoComplete")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onVideoComplete", map)
            }

            // 视频播放失败的回调
            override fun onVideoError() {
                Log.d(TAG, "onVideoError")
                postMessage("onVideoError")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onVideoError", map)
                result.error("0", "视频播放失败", "视频播放失败")
            }

            // 聚合不支持、激励视频播放完毕，验证是否有效发放奖励的回调
            @Deprecated("Deprecated in Java")
            override fun onRewardVerify(p0: Boolean, p1: Int, p2: String?, p3: Int, p4: String?) {
                Log.d(TAG, "onRewardVerify")
                postMessage("onRewardVerify", mapOf("verify" to p0))
            }

            // 激励视频播放完毕，验证是否有效发放奖励的回调
            override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {
                Log.d(TAG, "onRewardVerify")
                postMessage("onRewardVerify", mapOf("verify" to p0))
                val map2 = mapOf("verify" to p0, "rewardAmount" to p1, "rewardName" to p2?.getString("rewardName"),"adInstanceId" to adInstanceId)
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onRewardVerify", map2)
            }

            // 跳过广告
            override fun onSkippedVideo() {
                Log.d(TAG, "onSkippedVideo")
                postMessage("onSkippedVideo")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onSkippedVideo", map)
            }
        }
        // 再看一个广告展示监听器
        mRewardVideoAdPlayAgainInteractionListener = object : RewardAdInteractionListener {
            override fun onAdShow() {
                Log.d(TAG, "onAdShow")
                postMessage("onAdShow")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdShowAgain", map)
            }

            // 广告的下载bar点击回调，非所有广告商的广告都会触发
            override fun onAdVideoBarClick() {
                Log.d(TAG, "onAdVideoBarClick")
                postMessage("onAdVideoBarClick")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdVideoBarClickAgain", map)
            }

            // 广告关闭的回调
            override fun onAdClose() {
                Log.d(TAG, "onAdClose")
                postMessage("onAdClose")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onAdCloseAgain", map)
                result.success(true)
                destroyAd()
            }

            // 视频播放完毕的回调，非所有广告商的广告都会触发
            override fun onVideoComplete() {
                Log.d(TAG, "onVideoComplete")
                postMessage("onVideoComplete")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onVideoCompleteAgain", map)
            }

            // 视频播放失败的回调
            override fun onVideoError() {
                Log.d(TAG, "onVideoError")
                postMessage("onVideoError")
                result.error("0", "视频播放失败", "视频播放失败")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onVideoErrorAgain", map)
            }

            // 聚合不支持、激励视频播放完毕，验证是否有效发放奖励的回调
            @Deprecated("Deprecated in Java")
            override fun onRewardVerify(p0: Boolean, p1: Int, p2: String?, p3: Int, p4: String?) {
                Log.d(TAG, "onRewardVerify")
                postMessage("onRewardAgainVerify", mapOf("verify" to p0))
            }

            // 激励视频播放完毕，验证是否有效发放奖励的回调
            override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {
                Log.d(TAG, "onRewardVerify")
                postMessage("onRewardVerify", mapOf("verify" to p0))
                val map2 = mapOf("verify" to p0, "rewardAmount" to p1, "rewardName" to p2?.getString("rewardName"),"adInstanceId" to adInstanceId)
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onRewardVerifyAgain", map2)
            }

            // 跳过广告
            override fun onSkippedVideo() {
                Log.d(TAG, "onSkippedVideo")
                postMessage("onSkippedVideo")
                DataReportUtil.report(adUnitId, AdEventType.Rewarded, "onSkippedVideoAgain", map)
            }
        }
    }

    init {
        rewardId = (creationParams["rewardId"] as String).toInt()
        adInstanceId = creationParams["adInstanceId"] as String
        mttRewardAd = FlutterGromoreRewardCache.getCacheAd(rewardId)
        adUnitId = FlutterGromoreRewardCache.getCacheAdUnitId(rewardId) ?: ""
        initListeners()
        initAd()
    }

    override fun initAd() {
        mttRewardAd?.takeIf {
            it.mediationManager.isReady
        }?.let {
            // 真正展示
            it.setRewardAdInteractionListener(mRewardVideoAdInteractionListener)
            // 再次展示
            it.setRewardPlayAgainInteractionListener(mRewardVideoAdPlayAgainInteractionListener)
            it.showRewardVideoAd(activity)
        }
    }

    private fun destroyAd() {
        mttRewardAd?.mediationManager?.destroy()
        mttRewardAd = null

        FlutterGromoreRewardCache.removeCacheAd(rewardId)
        FlutterGromoreRewardCache.removeCacheAdUnitId(rewardId)
    }
}