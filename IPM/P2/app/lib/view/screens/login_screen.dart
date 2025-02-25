import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:wear_plus/wear_plus.dart';

import '../../state/login_notifier.dart';

class LoginScreen extends ConsumerWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final TextEditingController controller = TextEditingController();
    final LoginNotifier loginNotifier = ref.watch(loginProvider.notifier);

    final bool isWearOS = MediaQuery
        .of(context)
        .size
        .shortestSide < 300;

    return isWearOS
        ? // For WearOS
    WatchShape(
      builder: (context, shape, child) {
        return Scaffold(
          body: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Center(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const SizedBox(height: 10),
                    Text(
                      "Welcome",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: isWearOS ? 18 : 36,
                        color: Colors.indigo,
                      ),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 10),
                    _buildTextField(controller, isWearOS: isWearOS),
                    const SizedBox(height: 10),
                    _buildSubmitButton(context, ref, controller, loginNotifier,
                        isWearOS: isWearOS),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    )
        : // For mobile
    Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const SizedBox(height: 100),
              Text(
                "Welcome",
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: isWearOS ? 18 : 36,
                  color: Colors.indigo,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 50),
              _buildTextField(controller, isWearOS: isWearOS),
              const SizedBox(height: 20),
              _buildSubmitButton(
                  context, ref, controller, loginNotifier, isWearOS: isWearOS),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(TextEditingController controller,
      {bool isWearOS = false}) {
    return TextField(
      keyboardType: TextInputType.number,
      controller: controller,
      inputFormatters: [
        FilteringTextInputFormatter.digitsOnly,
        _CodeInputFormatter(),
      ],
      decoration: InputDecoration(
        labelText: 'Patient Code',
        labelStyle: TextStyle(
          fontSize: isWearOS ? 12 : 14,
        ),
        hintText: 'XXX-XX-XXXX',
        hintStyle: TextStyle(
          fontSize: isWearOS ? 12 : 14,
        ),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(isWearOS ? 8 : 12),
        ),
        filled: true,
        fillColor: isWearOS ? Colors.white : Colors.white,
      ),
      style: TextStyle(color: isWearOS ? Colors.black : Colors.black),
    );
  }

  Widget _buildSubmitButton(BuildContext context, WidgetRef ref,
      TextEditingController controller, LoginNotifier loginNotifier,
      {bool isWearOS = false}) {
    return isWearOS
        ? SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: () {
          loginNotifier.logIn(context, ref, loginNotifier, controller);
        },
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 12),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          backgroundColor: Colors.indigo,
        ),
        child: const Text(
          'Submit',
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),
      ),
    )
        : ElevatedButton(
      onPressed: () {
        loginNotifier.logIn(context, ref, loginNotifier, controller);
      },
      style: ElevatedButton.styleFrom(
        padding: const EdgeInsets.symmetric(vertical: 16),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
        backgroundColor: Colors.indigo,
      ),
      child: const Text(
        'Submit',
        style: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.bold,
          color: Colors.white,
        ),
      ),
    );
  }
}


  class _CodeInputFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(
      TextEditingValue oldValue, TextEditingValue newValue) {
    final String digitsOnly = newValue.text.replaceAll(RegExp(r'\D'), '');

    final StringBuffer formatted = StringBuffer();
    for (int i = 0; i < digitsOnly.length; i++) {
      if (i == 3 || i == 5) {
        formatted.write('-');
      }
      formatted.write(digitsOnly[i]);
    }

    return TextEditingValue(
      text: formatted.toString(),
      selection: TextSelection.collapsed(
        offset: formatted.length,
      ),
    );
  }
}
