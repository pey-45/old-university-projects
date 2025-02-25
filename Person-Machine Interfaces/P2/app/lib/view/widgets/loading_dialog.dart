import 'package:flutter/material.dart';

void showLoadingDialog(BuildContext context, VoidCallback onCancel) {
  final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

  showDialog(
    context: context,
    barrierDismissible: false,
    builder: (BuildContext context) {
      return Dialog(
        backgroundColor: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16.0),
        ),
        child: Padding(
          padding: EdgeInsets.all(isWearOS ? 8.0 : 20.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Padding(
                padding: EdgeInsets.only(top: isWearOS ? 8.0 : 12.0), // Ajuste superior
                child: SizedBox(
                  width: isWearOS ? 20 : 50,
                  height: isWearOS ? 20 : 50,
                  child: const CircularProgressIndicator(
                    color: Colors.indigo,
                    strokeWidth: 3.0,
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Text(
                "Please wait...",
                style: TextStyle(
                  fontSize: isWearOS ? 12 : 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.black87,
                ),
                textAlign: TextAlign.center,
              ),
              SizedBox(
                  height: isWearOS ? 6 : 16,
              ),
              TextButton(
                onPressed: () {
                  Navigator.of(context, rootNavigator: true).pop();
                  onCancel();
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                  padding: EdgeInsets.all(isWearOS ? 6 : 12),
                ),
                child: Text(
                  "Cancel",
                  style: TextStyle(
                    color: Colors.indigo,
                    fontSize: isWearOS ? 14 : 20,
                  ),
                ),
              ),
            ],
          ),
        ),
      );
    },
  );
}

void closeLoadingDialog(BuildContext context) {
  if (Navigator.canPop(context)) {
    Navigator.of(context, rootNavigator: true).pop();
  }
}

