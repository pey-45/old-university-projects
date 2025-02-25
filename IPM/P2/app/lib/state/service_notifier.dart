import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../service/api_service.dart';
import '../service/service_interface.dart';


final StateNotifierProvider<ServiceNotifier, ServiceState> serviceProvider =
StateNotifierProvider<ServiceNotifier, ServiceState>((ref) => ServiceNotifier(ref));


class ServiceNotifier extends StateNotifier<ServiceState> {
  final Ref ref;

  ServiceNotifier(this.ref) : super (ServiceState(currentService: ApiService()));

  void setState({required ServiceInterface service}) {
    state = state.copyWith(
      service: service
    );
  }
}


class ServiceState {
  final ServiceInterface currentService;

  ServiceState({required this.currentService});

  ServiceState copyWith({
    required ServiceInterface service,
  })
  {
    return ServiceState(
        currentService: service
    );
  }
}