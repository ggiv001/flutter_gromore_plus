import 'package:flutter_gromore_plus/global/global.dart';

class DataReport {

  final String appId;
  final String appName;
  final bool debug;
  final bool useMediation;
  final String? deviceId;
  final String? userId;
  final String? userTag;
  final String adUnitId;
  final String? adInstanceId;
  final String adType;
  final String eventName;
  final String? eventArgs;

  DataReport({
    required this.appId,
    required this.appName,
    required this.debug,
    required this.useMediation,
    this.deviceId,
    this.userId,
    this.userTag,
    required this.adUnitId,
    this.adInstanceId,
    required this.adType,
    required this.eventName,
    this.eventArgs,
  });

  // from
  static DataReport from(Map<Object?, Object?> map) {
    var adInstanceId = '';
    if(map['eventArgs'] is Map) {
      Map eventArgs = map['eventArgs'] as Map;
      adInstanceId = eventArgs['adInstanceId']?.toString() ?? '';
    }
    return DataReport(
      appId: Global.appId,
      appName: Global.appName,
      debug: Global.debug,
      useMediation: Global.useMediation,
      deviceId: Global.deviceId,
      userId: Global.userId,
      userTag: Global.userTag,
      adUnitId: map['adUnitId'].toString(),
      adInstanceId: adInstanceId,
      adType: map['adType'].toString(),
      eventName: map['eventName'].toString(),
      eventArgs: map['eventArgs']?.toString(),
    );
  }

  // toJson
  Map<String, dynamic> toJson() {
    return {
      'appId': appId,
      'appName': appName,
      'debug': debug,
      'useMediation': useMediation,
      'deviceId': deviceId,
      'userId': userId,
      'userTag': userTag,
      'adUnitId': adUnitId,
      'adInstanceId': adInstanceId,
      'adType': adType,
      'eventName': eventName,
      'eventArgs': eventArgs,
    };
  }

}
