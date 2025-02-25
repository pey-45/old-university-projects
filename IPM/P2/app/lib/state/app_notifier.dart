import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../models/medication.dart';
import '../../models/patient.dart';
import '../../models/posology.dart';


final StateNotifierProvider<AppNotifier, AppState> appProvider =
StateNotifierProvider<AppNotifier, AppState>((ref) => AppNotifier(ref));


class AppNotifier extends StateNotifier<AppState>{
  final Ref ref;

  AppNotifier(this.ref) : super(
    AppState(),
  );

  void setState({Patient? patient, List<Medication>? medications, List<List<Posology>>? posologies,}) {
    state = state.copyWith(
      patient: patient ?? state.patient,
      medications: medications ?? state.medications,
      posologies: posologies ?? state.posologies,
    );
  }
}


class AppState {
  final Patient? patient;
  final List<Medication> medications;
  final List<List<Posology>> posologies;

  AppState({
    this.patient,
    this.medications = const [],
    this.posologies = const [],
  });

  AppState copyWith({
    Patient? patient,
    List<Medication>? medications,
    List<List<Posology>>? posologies,
  }) {
    return AppState(
      patient: patient ?? this.patient,
      medications: medications ?? this.medications,
      posologies: posologies ?? this.posologies,
    );
  }
}
