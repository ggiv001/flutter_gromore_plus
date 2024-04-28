import 'package:uuid/uuid.dart';

class GromoreUUID {
  static String getUUID() {
    // 将"-"替换为""
    return const Uuid().v4().replaceAll("-", "");
  }
}