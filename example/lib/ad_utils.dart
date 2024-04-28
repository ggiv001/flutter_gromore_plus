import 'package:flutter_gromore_plus/config/gromore_feed_config.dart';
import 'package:flutter_gromore_plus/flutter_gromore_plus.dart';

import 'gromore_config.dart';

class AdUtils {
  static List<String> feedAdIdList = [];
  static List<String> bannerAdIdList = [];

  /// 获取信息流广告id
  static Future<String?> getFeedAdId() async {
    if (feedAdIdList.isNotEmpty) {
      return feedAdIdList.removeLast();
    }

    // 加载信息流广告
    List<String> idList = await FlutterGromorePlus.loadFeedAd(
        GromoreFeedConfig(adUnitId: GroMoreConfig.feedAdId));

    if (idList.isNotEmpty) {
      String id = idList.removeLast();
      feedAdIdList.addAll(idList);
      return id;
    }
    return null;
  }
}
