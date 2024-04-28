package com.uniads.flutter_gromore_plus.manager

import com.bytedance.sdk.openadsdk.TTRewardVideoAd

class FlutterGromoreRewardCache {
    companion object {
        /// 缓存插屏广告
        private var cacheAd: MutableMap<Int, TTRewardVideoAd> = mutableMapOf()
        private var cacheAdUnitId:MutableMap<Int,String> = mutableMapOf()

        /// 添加缓存插屏广告
        fun addCacheAd(id: Int, ad: TTRewardVideoAd) {
            cacheAd[id] = ad
        }

        /// 添加缓存插屏广告ID
        fun addCacheAdUnitId(id: Int, adUnitId: String) {
            cacheAdUnitId[id] = adUnitId
        }

        /// 获取缓存插屏广告
        fun getCacheAd(id: Int): TTRewardVideoAd? {
            return cacheAd[id]
        }

        /// 获取缓存插屏广告ID
        fun getCacheAdUnitId(id: Int): String? {
            return cacheAdUnitId[id]
        }

        /// 移除缓存插屏广告
        fun removeCacheAd(id: Int) {
            cacheAd.remove(id)
        }

        /// 移除缓存插屏广告ID
        fun removeCacheAdUnitId(id: Int) {
            cacheAdUnitId.remove(id)
        }
    }
}