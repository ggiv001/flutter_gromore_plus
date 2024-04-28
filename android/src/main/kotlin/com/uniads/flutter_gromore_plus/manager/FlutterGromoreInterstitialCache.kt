package com.uniads.flutter_gromore_plus.manager

import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd


class FlutterGromoreInterstitialCache {
    companion object {
        /// 缓存插屏广告
        private var cacheInterstitialAd: MutableMap<Int, TTFullScreenVideoAd> = mutableMapOf()
        /// 缓存插屏广告ID
        private var cacheInterstitialAdUnitId:MutableMap<Int,String> = mutableMapOf()

        /// 添加缓存插屏广告
        fun addCacheInterstitialAd(id: Int, ad: TTFullScreenVideoAd) {
            cacheInterstitialAd[id] = ad
        }

        /// 添加缓存插屏广告ID
        fun addCacheInterstitialAdUnitId(id: Int, adUnitId: String) {
            cacheInterstitialAdUnitId[id] = adUnitId
        }

        /// 获取缓存插屏广告
        fun getCacheInterstitialAd(id: Int): TTFullScreenVideoAd? {
            return cacheInterstitialAd[id]
        }

        /// 获取缓存插屏广告ID
        fun getCacheInterstitialAdUnitId(id: Int): String? {
            return cacheInterstitialAdUnitId[id]
        }

        /// 移除缓存插屏广告
        fun removeCacheInterstitialAd(id: Int) {
            cacheInterstitialAd.remove(id)
        }

        /// 移除缓存插屏广告ID
        fun removeCacheInterstitialAdUnitId(id: Int) {
            cacheInterstitialAdUnitId.remove(id)
        }
    }
}