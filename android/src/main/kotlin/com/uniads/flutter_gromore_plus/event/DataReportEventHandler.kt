package com.uniads.flutter_gromore_plus.event

import android.util.Log
import io.flutter.plugin.common.EventChannel

class DataReportEventHandler: EventChannel.StreamHandler {
    private val TAG: String = this::class.java.simpleName
    private var eventSink: EventChannel.EventSink? = null

    companion object {
        // 单例
        fun getInstance() = InstanceHelper.instance
    }

    object InstanceHelper {
        val instance = DataReportEventHandler()
    }

    fun sendDataReportEvent(dataReportEvent: DataReportEvent) {
        eventSink?.success(dataReportEvent.toMap())
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        Log.d(TAG, "onListen")
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        Log.d(TAG, "onCancel")
        eventSink = null
    }
}