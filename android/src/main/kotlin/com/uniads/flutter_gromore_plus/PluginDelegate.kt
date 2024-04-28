package com.uniads.flutter_gromore_plus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.uniads.flutter_gromore_plus.manager.*
import com.uniads.flutter_gromore_plus.utils.DeviceUtil
import com.uniads.flutter_gromore_plus.utils.Utils
import com.uniads.flutter_gromore_plus.view.FlutterGromoreInterstitial
import com.uniads.flutter_gromore_plus.view.FlutterGromoreReward
import com.uniads.flutter_gromore_plus.view.FlutterGromoreSplash
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PluginDelegate(
    private val context: Context,
    private val activity: Activity,
    private val binaryMessenger: BinaryMessenger
) : MethodChannel.MethodCallHandler {
    private val TAG: String = this::class.java.simpleName

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val method: String = call.method
        val arguments = call.arguments as? Map<String, Any?>

        Log.d(TAG, method)

        when (method) {
            // 同时请求：READ_PHONE_STATE, COARSE_LOCATION, FINE_LOCATION, WRITE_EXTERNAL_STORAGE权限
            "requestPermissionIfNecessary" -> {
                TTAdSdk.getMediationManager().requestPermissionIfNecessary(activity)
                result.success(true)
            }
            // 初始化
            "initSDK" -> {
                InitGromore(context).initSDK(arguments, result)
            }
            // 开屏
            "showSplashAd" -> {
                showSplash(arguments)
                // 在开屏广告关闭后才会调用result.success
                Utils.splashResult = result
            }
            // 加载插屏广告
            "loadInterstitialAd" -> {
                require(arguments != null && arguments["adUnitId"] != null)
                FlutterGromoreInterstitialManager(arguments, activity, result)
            }
            // 展示插屏广告
            "showInterstitialAd" -> {
                require(arguments != null && arguments["interstitialId"] != null)
                FlutterGromoreInterstitial(activity, binaryMessenger, arguments, result)
            }
            // 移除插屏广告
            "removeInterstitialAd" -> {
                require(arguments != null && arguments["interstitialId"] != null)
                FlutterGromoreInterstitialCache.removeCacheInterstitialAd((arguments["interstitialId"] as String).toInt())
                result.success(true)
            }
            // 加载信息流广告
            "loadFeedAd" -> {
                require(arguments != null && arguments["adUnitId"] != null)
                FlutterGromoreFeedManager(arguments, context, result)
            }
            // 移除信息流广告
            "removeFeedAd" -> {
                require(arguments != null && arguments["feedId"] != null)
                FlutterGromoreFeedCache.removeCacheFeedAd(arguments["feedId"] as String)
                result.success(true)
            }
            // 加载激励广告
            "loadRewardAd" -> {
                require(arguments != null && arguments["adUnitId"] != null)
                FlutterGromoreRewardManager(arguments, activity, result)
            }
            // 展示激励广告
            "showRewardAd" -> {
                require(arguments != null && arguments["rewardId"] != null)
                FlutterGromoreReward(activity, binaryMessenger, arguments, result)
            }

            // 获取设备ID
            "getDeviceId" -> {
                result.success(DeviceUtil.widevine)
            }

            else -> {
                Log.d(TAG, "unknown method $method")
                result.success(true)
            }
        }
    }

    // 开屏广告
    private fun showSplash(arguments: Map<String, Any?>?) {

        require(arguments != null)

        val intent = Intent(context, FlutterGromoreSplash::class.java).apply {
            putExtra("id", arguments["id"] as? String)
            putExtra("adUnitId", arguments["adUnitId"] as? String)
            putExtra("logo", arguments["logo"] as? String)
            putExtra("muted", arguments["muted"] as? Boolean)
            putExtra("preload", arguments["preload"] as? Boolean)
            putExtra("volume", arguments["volume"] as? Float)
            putExtra("timeout", arguments["timeout"] as? Int)
            putExtra("useSurfaceView", arguments["useSurfaceView"] as? Boolean)
        }

        activity.apply {
            startActivity(intent)
            activity.overridePendingTransition(0, 0)
        }

    }

}