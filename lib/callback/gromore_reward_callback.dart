import 'package:flutter_gromore_plus/callback/gromore_base_callback.dart';
import 'package:flutter_gromore_plus/types.dart';

class GromoreRewardCallback extends GromoreBaseAdCallback {
  /// 广告的展示回调
  final GromoreVoidCallback? onAdShow;

  /// 广告的下载bar点击回调，非所有广告商的广告都会触发
  final GromoreVoidCallback? onAdVideoBarClick;

  /// 广告关闭的回调
  final GromoreVoidCallback? onAdClose;

  /// 视频播放完毕的回调，非所有广告商的广告都会触发
  final GromoreVoidCallback? onVideoComplete;

  /// 视频播放失败的回调
  final GromoreVoidCallback? onVideoError;

  /// 激励视频播放完毕，验证是否有效发放奖励的回调
  final void Function(bool verified,[dynamic arguments])? onRewardVerify;
  /// 激励视频再次播放，验证是否有效发放奖励的回调
  final void Function(bool verified,[dynamic arguments])? onRewardAgainVerify;

  /// 跳过视频播放
  final GromoreVoidCallback? onSkippedVideo;

  GromoreRewardCallback({
    this.onAdShow,
    this.onAdVideoBarClick,
    this.onAdClose,
    this.onVideoComplete,
    this.onVideoError,
    this.onRewardVerify,
    this.onRewardAgainVerify,
    this.onSkippedVideo,
  });

  @override
  void exec(String callbackName, [arguments]) {
    if (callbackName == "onAdShow") {
      onAdShow?.call(arguments);
    } else if (callbackName == "onAdVideoBarClick") {
      onAdVideoBarClick?.call(arguments);
    } else if (callbackName == "onAdClose") {
      onAdClose?.call(arguments);
    } else if (callbackName == "onVideoComplete") {
      onVideoComplete?.call(arguments);
    } else if (callbackName == "onVideoError") {
      onVideoError?.call(arguments);
    } else if (callbackName == "onRewardVerify") {
      onRewardVerify?.call(arguments["verify"],arguments);
    } else if (callbackName == "onSkippedVideo") {
      onSkippedVideo?.call(arguments);
    } else if (callbackName == "onRewardAgainVerify") {
      onRewardAgainVerify?.call(arguments["verify"],arguments);
    }
  }
}
