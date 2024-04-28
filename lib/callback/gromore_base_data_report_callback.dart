import 'package:flutter_gromore_plus/report/data_report.dart';

/// 数据上报事件回调基类
abstract class GromoreBaseDataReportCallback {
  /// 针对不同回调事件的处理
  void exec(DataReport dataReport);
}
