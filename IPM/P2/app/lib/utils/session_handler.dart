import 'package:shared_preferences/shared_preferences.dart';

class SessionHandler {

  static Future<void> saveSession(String? token) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    if (token == null) {
      await prefs.remove('token');
    } else {
      await prefs.setString('token', token);
    }
  }

  static Future<String?> getSession() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }
}

