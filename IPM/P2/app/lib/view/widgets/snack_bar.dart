import 'package:flutter/material.dart';

bool _isSnackBarActive = false;

void showSnackBar(BuildContext context, String message, {Color? color}) {
  if (_isSnackBarActive) {
    return;
  }

  final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

  _isSnackBarActive = true;

  final snackBar = SnackBar(
    content: Text(
      message,
      style: TextStyle(color: Colors.white, fontSize: isWearOS ? 14 : 16),
      textAlign: TextAlign.center,
    ),
    backgroundColor: color ?? Colors.black87,
    behavior: SnackBarBehavior.floating,
    margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(12),
    ),
    duration: const Duration(seconds: 2),
    elevation: 4,
  );

  ScaffoldMessenger.of(context).showSnackBar(snackBar).closed.then((_) {
    _isSnackBarActive = false;
  });
}
