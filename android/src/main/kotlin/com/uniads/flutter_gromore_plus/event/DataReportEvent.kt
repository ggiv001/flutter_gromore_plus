package com.uniads.flutter_gromore_plus.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataReportEvent(
        private val adUnitId: String,
        private val adType: AdEventType,
        private val eventName: String,
        private val eventArgs: Map<String,Any?>?
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun toMap(): HashMap<String, Any?> {
//        var jsonArgs = ""
//        if (eventArgs != null && eventArgs.toString().isNotEmpty()) {
//            jsonArgs = Json.encodeToString(eventArgs)
//        }
//        return hashMapOf(
//                "adUnitId" to adUnitId,
//                "adType" to adType.name,
//                "eventName" to eventName,
//                "eventArgs" to jsonArgs
//        )
        return HashMap<String, Any?>().apply {
            set("adUnitId", adUnitId)
            set("adType", adType.name)
            set("eventName", eventName)
            set("eventArgs", eventArgs)
        }
    }
}
