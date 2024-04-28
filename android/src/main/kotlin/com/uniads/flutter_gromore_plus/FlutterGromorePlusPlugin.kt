package com.uniads.flutter_gromore_plus

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import com.uniads.flutter_gromore_plus.constants.FlutterGromoreConstants
import com.uniads.flutter_gromore_plus.event.AdEventHandler
import com.uniads.flutter_gromore_plus.event.DataReportEventHandler
import com.uniads.flutter_gromore_plus.factory.FlutterGromoreBannerFactory
import com.uniads.flutter_gromore_plus.factory.FlutterGromoreFeedFactory
import com.uniads.flutter_gromore_plus.factory.FlutterGromoreSplashFactory

/** FlutterGromorePlugin */
class FlutterGromorePlusPlugin : FlutterPlugin, ActivityAware {

    // 通道实例
    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var dataReportEventChannel: EventChannel


    // 代理
    private var pluginDelegate: PluginDelegate? = null
    private var adEventListener: AdEventHandler? = null
    private var dataReportEventListener: DataReportEventHandler? = null

    private lateinit var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    private lateinit var binaryMessenger: BinaryMessenger

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding
        binaryMessenger = flutterPluginBinding.binaryMessenger

        methodChannel = MethodChannel(binaryMessenger, FlutterGromoreConstants.methodChannelName)
        eventChannel = EventChannel(binaryMessenger, FlutterGromoreConstants.eventChannelName)
        dataReportEventChannel = EventChannel(binaryMessenger, FlutterGromoreConstants.dataReportEventChannelName)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        dataReportEventChannel.setStreamHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        adEventListener = adEventListener ?: AdEventHandler.getInstance()
        dataReportEventListener = dataReportEventListener ?: DataReportEventHandler.getInstance()
        pluginDelegate = pluginDelegate ?: PluginDelegate(
                flutterPluginBinding.applicationContext,
                binding.activity,
                binaryMessenger
        )

        methodChannel.setMethodCallHandler(pluginDelegate)
        eventChannel.setStreamHandler(adEventListener)
        dataReportEventChannel.setStreamHandler(dataReportEventListener)

        // 注册PlatformView
        flutterPluginBinding
                .platformViewRegistry
                .registerViewFactory(
                        FlutterGromoreConstants.feedViewTypeId,
                        FlutterGromoreFeedFactory(binding.activity, binaryMessenger)
                )

        flutterPluginBinding
                .platformViewRegistry
                .registerViewFactory(
                        FlutterGromoreConstants.splashTypeId,
                        FlutterGromoreSplashFactory(binding.activity, binaryMessenger)
                )

        flutterPluginBinding
                .platformViewRegistry
                .registerViewFactory(
                        FlutterGromoreConstants.bannerTypeId,
                        FlutterGromoreBannerFactory(binding.activity, binaryMessenger)
                )
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        pluginDelegate = null
    }
}
