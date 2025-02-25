import 'package:flutter/material.dart';

void showConfirmationDialog(BuildContext context, String title, String msg, VoidCallback onConfirm) {
  final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300; // Detecta Wear OS basado en tamaño de pantalla

  showDialog(
    context: context,
    builder: (BuildContext context) {
      return Dialog(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16.0),
        ),
        insetPadding: EdgeInsets.all(isWearOS ? 8.0 : 24.0),
        child: Padding(
          padding: EdgeInsets.all(isWearOS ? 12.0 : 24.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Título del cuadro de diálogo
              Text(
                title,
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: isWearOS ? 18 : 26,
                ),
                textAlign: TextAlign.center,
              ),
              SizedBox(height: isWearOS ? 10 : 20),
              Text(
                msg,
                style: TextStyle(
                  fontSize: isWearOS ? 12 : 20,
                  color: Colors.black87,
                  height: 1.5,
                ),
                textAlign: TextAlign.center,
              ),
              SizedBox(height: isWearOS ? 0 : 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  Expanded(
                    child: TextButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                      child: Text(
                        'Cancel',
                        style: TextStyle(
                          fontSize: isWearOS ? 14 : 20,
                          color: Colors.indigo,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(width: 20),
                  Expanded(
                    child: TextButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                        onConfirm();
                      },
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                      child: Text(
                        'Ok',
                        style: TextStyle(
                          fontSize: isWearOS ? 14 : 20,
                          color: Colors.indigo,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      );
    },
  );
}
