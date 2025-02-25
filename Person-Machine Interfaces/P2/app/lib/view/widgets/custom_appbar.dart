import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../state/custom_appbar_notifier.dart';

class CustomAppBar extends ConsumerWidget implements PreferredSizeWidget {
  final String title;

  const CustomAppBar({super.key, required this.title});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final CustomAppBarNotifier customAppBarNotifier = ref.watch(customAppBarProvider.notifier);

    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

    return isWearOS
        ? // For WearOS
    Padding(
      padding: const EdgeInsets.symmetric(horizontal: 6.0, vertical: 3.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            title,
            style: const TextStyle(
              color: Colors.indigo,
              fontSize: 14,
              fontWeight: FontWeight.bold,
            ),
          ),
          GestureDetector(
            onTap: () {
              customAppBarNotifier.logOut(context, ref);
            },
            child: const Icon(
              Icons.logout,
              color: Colors.indigo,
              size: 20,
            ),
          ),
        ],
      ),
    )
        : // For Mobile
    AppBar(
      title: Text(
        title,
        style: const TextStyle(
          color: Colors.white,
          fontSize: 24,
        ),
      ),
      backgroundColor: Colors.indigo,
      actions: [
        IconButton(
          onPressed: () {
            customAppBarNotifier.logOut(context, ref);
          },
          icon: const Icon(
            Icons.logout,
            color: Colors.white,
            size: 28,
          ),
        ),
      ],
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
