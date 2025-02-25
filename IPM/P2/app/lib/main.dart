import 'package:app/service/api_service.dart';
import 'package:app/service/mock_service.dart';
import 'package:app/state/login_notifier.dart';
import 'package:app/state/service_notifier.dart';
import 'package:app/utils/session_handler.dart';
import 'package:app/view/screens/login_screen.dart';
import 'package:app/view/screens/main_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';


void main({bool? mock}) {
  runApp(ProviderScope(child: MyApp(mock: mock ?? false)));
}


class MyApp extends StatelessWidget {
  final bool mock;
  const MyApp({super.key, required this.mock});

  Future<String?> _initializeApp(BuildContext context, WidgetRef ref) async {
    Future.microtask(() {
      final serviceNotifier = ref.read(serviceProvider.notifier);
      serviceNotifier.setState(service: mock ? MockService() : ApiService());
    });
    final String? sessionData = await SessionHandler.getSession();
    if (sessionData != null) {
      final loginNotifier = ref.read(loginProvider.notifier);
      if (context.mounted) {
        await loginNotifier.setPatient(context, ref, sessionData);
      }
    }
    return sessionData;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Consumer(
        builder: (context, ref, _) {
          return FutureBuilder<String?>(
            future: _initializeApp(context, ref),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              } else if (snapshot.hasError) {
                return const Center(child: Text('Error during initialization'));
              } else if(snapshot.hasData){
                return const MainScreen();
              } else{
                return const LoginScreen();
              }
            },
          );
        },
      ),
    );
  }
}
