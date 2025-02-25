import 'package:app/view/widgets/snack_bar.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../view/screens/login_screen.dart';
import '../../../view/widgets/show_confirmation_dialog.dart';
import '../../../view/widgets/loading_dialog.dart';
import '../../utils/session_handler.dart';


final StateNotifierProvider<CustomAppBarNotifier, CustomAppBarState> customAppBarProvider =
StateNotifierProvider<CustomAppBarNotifier, CustomAppBarState>((ref) => CustomAppBarNotifier(ref));


class CustomAppBarNotifier extends StateNotifier<CustomAppBarState> {
  final Ref ref;

  CustomAppBarNotifier(this.ref) : super(
    CustomAppBarState(),
  );

  void logOut(BuildContext context, WidgetRef ref) {
    showConfirmationDialog(
      context,
      "Log out",
      "Are you sure you want to log out?",
          () async {
        bool isCancelled = false;

        showLoadingDialog(context, () {
          isCancelled = true;
          closeLoadingDialog(context);
        });

        // await Future.delayed(const Duration(seconds: 3)); // DEBUG
        if (isCancelled) return;
        await SessionHandler.saveSession(null);

        // await Future.delayed(const Duration(seconds: 3)); // DEBUG
        if (context.mounted && !isCancelled) {
          closeLoadingDialog(context);
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(
              builder: (context) => const LoginScreen(),
            ),
          );
          showSnackBar(context, "Logged out", color: Colors.blue);
        }
      },
    );
  }
}


class CustomAppBarState {

  CustomAppBarState();

  CustomAppBarState copyWith()
  {
    return CustomAppBarState();
  }
}