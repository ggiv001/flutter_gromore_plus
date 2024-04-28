package com.uniads.flutter_gromore_plus.utils

import com.uniads.flutter_gromore_plus.event.AdEventType
import com.uniads.flutter_gromore_plus.event.DataReportEvent
import com.uniads.flutter_gromore_plus.event.DataReportEventHandler

object DataReportUtil {
    fun report(adUnitId: String, adType: AdEventType, eventName: String, eventArgs: Map<String, Any?>?) {
        val eventArgsMap = HashMap<String, Any?>()
        if(eventArgs!= null) {
            eventArgsMap.putAll(eventArgs)
        }
        val dataReportEvent = DataReportEvent(adUnitId, adType, eventName, eventArgsMap)
        DataReportEventHandler.getInstance().sendDataReportEvent(dataReportEvent)
    }
}
