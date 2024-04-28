package com.uniads.flutter_gromore_plus.manager

import com.bytedance.sdk.openadsdk.TTFeedAd

class FlutterGromoreFeedCache {
    companion object {
        /// 缓存信息流广告
        var cacheFeedAd: MutableMap<String, TTFeedAd> = mutableMapOf()
        var cacheFeedAdUnitId: MutableMap<String, String> = mutableMapOf()

        /// 添加缓存信息流广告
        fun addCacheFeedAd(id: String, ad: TTFeedAd) {
            cacheFeedAd[id] = ad
        }

        /// 添加缓存信息流广告id
        fun addCacheFeedAdUnitId(id: String, adUnitId: String) {
            cacheFeedAdUnitId[id] = adUnitId
        }

        /// 获取缓存信息流广告
        fun getCacheFeedAd(id: String): TTFeedAd? {
            return cacheFeedAd[id]
        }

        /// 获取缓存信息流广告id
        fun getCacheFeedAdUnitId(id: String): String? {
            return cacheFeedAdUnitId[id]
        }

        /// 移除缓存信息流广告
        fun removeCacheFeedAd(id: String) {
            cacheFeedAd.remove(id)
        }

        /// 移除缓存信息流广告id
        fun removeCacheFeedAdUnitId(id: String) {
            cacheFeedAdUnitId.remove(id)
        }
    }
}