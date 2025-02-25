import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../widgets/custom_appbar.dart';

class NotificationsPage extends ConsumerWidget {
  const NotificationsPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;
    return Scaffold(
      appBar: const CustomAppBar(title: "Notifications"),
      body: Center(
        child: Text(
          "Coming soon...",
          style: TextStyle(fontSize: isWearOS ? 18 : 24),
        ),
      ),
    );
  }
}