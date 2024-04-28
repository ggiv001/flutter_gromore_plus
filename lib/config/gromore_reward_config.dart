import 'package:flutter_gromore_plus/config/gromore_base_config.dart';

/// 激励广告配置
class GromoreRewardConfig extends GromoreBaseAdConfig {
  /// 广告ID
  final String adUnitId;

  /// 播放方向。竖屏：1，横屏：2。默认为竖屏。仅Android端有效
  final int? orientation;

  /// 是否静音，默认为true
  final bool? muted;

  /// 音量，默认为0
  final double? volume;

  /// 是否使用SurfaceView，默认为true
  final bool? useSurfaceView;

  /// rewardName
  final String? rewardName;
  /// rewardAmount
  final int? rewardAmount;

  GromoreRewardConfig(
      {required this.adUnitId,
      this.orientation,
      this.muted,
      this.volume,
      this.useSurfaceView = true,
      this.rewardName,
      this.rewardAmount
      });

  @override
  Map toJson() {
    Map<String, dynamic> result = {
      "adUnitId": adUnitId,
      "orientation": orientation,
      "muted": muted,
      "volume": volume,
      "useSurfaceView": useSurfaceView,
      "rewardName": rewardName,
      "rewardAmount": rewardAmount
    };

    result.removeWhere((key, value) => value == null);

    return result;
  }
}
