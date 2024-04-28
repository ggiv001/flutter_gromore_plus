import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_gromore_plus/callback/gromore_banner_callback.dart';
import 'package:flutter_gromore_plus/callback/gromore_feed_callback.dart';
import 'package:flutter_gromore_plus/callback/gromore_interstitial_callback.dart';
import 'package:flutter_gromore_plus/callback/gromore_reward_callback.dart';
import 'package:flutter_gromore_plus/callback/gromore_splash_callback.dart';
import 'package:flutter_gromore_plus/config/gromore_interstitial_config.dart';
import 'package:flutter_gromore_plus/config/gromore_reward_config.dart';
import 'package:flutter_gromore_plus/config/gromore_splash_config.dart';
import 'package:flutter_gromore_plus/flutter_gromore_plus.dart';
import 'package:flutter_gromore_plus/utils/gromore_ad_size.dart';
import 'package:flutter_gromore_plus/utils/gromore_uuid.dart';
import 'package:flutter_gromore_plus/view/gromore_banner_view.dart';
import 'package:flutter_gromore_plus/view/gromore_feed_view.dart';

import 'ad_utils.dart';
import 'gromore_config.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  double _height = 0.1;
  bool _show = false;
  String? _feedAdId;

  @override
  void initState() {
    super.initState();
    initSdk();
  }

  Future<void> initSdk() async {
    await FlutterGromorePlus.initSDK(
        appId: GroMoreConfig.appId,
        appName: GroMoreConfig.appName,
        debug: true,
        useMediation: true,
        reportUrl: "https://notice.onecai.online/cms/adPoint",
        userTag: "开发测试",
        userId: "1783121245198491648"
    );
    await loadFeedAd();
  }

  /// 显示激励广告
  Future<void> showRewardAd() async {
    var rewardId = await FlutterGromorePlus.loadRewardAd(
        GromoreRewardConfig(adUnitId: GroMoreConfig.rewardAdId,rewardName: '金币',rewardAmount: 200));
    await FlutterGromorePlus.showRewardAd(
        rewardId: rewardId,
        adInstanceId: GromoreUUID.getUUID(),
        callback: GromoreRewardCallback(
            onRewardVerify: (bool verify, [dynamic arguments]) {
          print(
              "================================== showRewardAd verify: $verify =====================================");
          print("arguments: $arguments");
          if (verify) {
            print("恭喜你，获得奖励");
          }
        }, onRewardAgainVerify: (bool verify, [dynamic arguments]) {
          print(
              "================================== showRewardAd verify: $verify =====================================");
          print("arguments: $arguments");
          if (verify) {
            print("恭喜你，再次获得奖励");
          }
        }, onAdShow: ([dynamic arguments]) {
          print("arguments: $arguments");
          print(
              "================================== showRewardAd success =====================================");
        }, onAdClose: ([dynamic arguments]) {
          print("arguments: $arguments");
          print(
              "================================== showRewardAd close =====================================");
        }));
  }

  /// 显示开屏广告
  Future<void> showSplashAd() async {
    // 拉起开屏页（会等待广告关闭或广告渲染失败）
    await FlutterGromorePlus.showSplashAd(
        config: GromoreSplashConfig(
            adUnitId: GroMoreConfig.splashAdId, logo: "launch_image"),
        callback: GromoreSplashCallback(onAdShow: ([dynamic arguments]) {
          print("callback --- onAdShow");
        }));

    // // 自渲染（由于iOS端SDK未提供相关支持，因此仅适用于安卓，后续将不进行维护）
    // child: GromoreSplashView(
    //   creationParams: GromoreSplashConfig(
    //       adUnitId: GoMoreAdConfig.splashId, height: height - 80),
    //   callback: GromoreSplashCallback(
    //       onAdEnd: () {
    //         Navigator.pop(context);
    //       }
    //   ));
  }

  /// 显示插屏广告
  Future<void> showInterstitialAd() async {
    // 拉起插屏页（会等待广告关闭或广告渲染失败）
    // 加载全屏广告
    var interstitialId = await FlutterGromorePlus.loadInterstitialAd(
        GromoreInterstitialConfig(
            adUnitId: GroMoreConfig.interstitialAdId,
            size: GromoreAdSize.withPercent(
                MediaQuery.of(context).size.width * 2 / 3, 2 / 3)));
    if (interstitialId.isEmpty) {
      print("加载全屏广告失败");
    }

    /// 合适的时机展示插屏广告
    if (interstitialId.isNotEmpty) {
      await FlutterGromorePlus.showInterstitialAd(
          interstitialId: interstitialId,
          callback: GromoreInterstitialCallback(
              onInterstitialShow: ([dynamic arguments]) {
            print("===== showInterstitialAd success ======");
          }, onInterstitialClosed: ([dynamic arguments]) {
            FlutterGromorePlus.removeInterstitialAd(interstitialId);
            interstitialId = "";
            FlutterGromorePlus();
          }));
    }
  }

  /// 加载信息流广告
  Future<void> loadFeedAd() async {
    String? feedAdId = await AdUtils.getFeedAdId();
    print("loadFeedAd $feedAdId");
    if (feedAdId != null && feedAdId.isNotEmpty) {
      setState(() {
        _feedAdId = feedAdId;
        _show = true;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('GroMore分析'),
        ),
        // body: Center(
        //   child: Text('缓存激励ID: $_rewardAdId\n'),
        // ),
        body: Center(
            child: Column(
          children: [
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                initSdk();
              },
              child: const Text('初始化GroMore'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                showRewardAd();
              },
              child: const Text('显示激励广告'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                showSplashAd();
              },
              child: const Text('显示开屏广告'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                showInterstitialAd();
              },
              child: const Text('插全屏广告'),
            ),
            const SizedBox(height: 20),
            const Text('以下是Banner广告'),
            GroMoreConfig.showBannerAd
                ? SizedBox(
                    height: 150,
                    child: GromoreBannerView(
                        creationParams: const {
                          "adUnitId": GroMoreConfig.bannerAdId,
                          'height': 150,
                          'useSurfaceView': true
                        },
                        callback: GromoreBannerCallback(
                            onRenderSuccess: ([dynamic arguments]) {
                              print("GromoreBannerView | onRenderSuccess");
                            },
                            onSelected: ([dynamic arguments]) {},
                            onLoadError: ([dynamic arguments]) {},
                            onAdTerminate: ([dynamic arguments]) {})),
                  )
                : const SizedBox(),
            const SizedBox(height: 20),
            const Text('以下是信息流广告'),
            _show && GroMoreConfig.showFeedAd
                ? SizedBox(
                    height: _height,
                    child: GromoreFeedView(
                        creationParams: {"feedId": _feedAdId!},
                        callback: GromoreFeedCallback(
                            onRenderSuccess: (double height) {
                          print("GromoreFeedView | onRenderSuccess | $height");
                          setState(() {
                            _height = height;
                          });
                        }, onSelected: ([dynamic arguments]) {
                          setState(() {
                            _show = false;
                          });
                        }, onAdTerminate: ([dynamic arguments]) {
                          setState(([dynamic arguments]) {
                            _show = false;
                          });
                        })),
                  )
                : const SizedBox(),
          ],
        )),
      ),
    );
  }
}
