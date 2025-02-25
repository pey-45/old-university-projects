import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../view/widgets/loading_dialog.dart';
import '../../models/medication.dart';
import '../../models/posology.dart';
import '../../utils/formatters.dart';
import '../../view/widgets/snack_bar.dart';
import 'app_notifier.dart';


final StateNotifierProvider<HomeNotifier, HomeState> homeProvider =
StateNotifierProvider<HomeNotifier, HomeState>((ref) => HomeNotifier(ref));


class HomeNotifier extends StateNotifier<HomeState> {
  final Ref ref;

  HomeNotifier(this.ref) : super(
    HomeState(
      medications: ref.read(appProvider).medications,
      posologies: ref.read(appProvider).posologies,
    ),
  );

  void setState({
    List<Medication>? medications,
    List<List<Posology>>? posologies,
    int? days,
    int? hours,
    List<Medication>? intakesMedications,
    List<List<Posology>>? intakesPosologies,
    List<String>? intakesMedicationNames,
    List<String>? intakesDates,
  }) {
    state = state.copyWith(
      medications: medications ?? state.medications,
      posologies: posologies ?? state.posologies,
      days: days ?? state.days,
      hours: hours ?? state.hours,
      intakesMedications: intakesMedications ?? state.intakesMedications,
      intakesPosologies: intakesPosologies ?? state.intakesPosologies,
      intakesMedicationNames: intakesMedicationNames ?? state.intakesMedicationNames,
      intakesDates: intakesDates ?? state.intakesDates,
    );
  }


  List<MapEntry<DateTime, Medication>> _getPendingIntakesOneDay(DateTime start,
      int hours,
      List<Medication> medications,
      List<List<Posology>> posologies) {
    DateTime endTime = start.add(Duration(hours: hours));

    List<Posology> sortedPosologies = posologies.expand((list) => list).toList();

    sortedPosologies.sort((a, b) {
      if (a.hour == b.hour) {
        return a.minute.compareTo(b.minute);
      } else {
        return a.hour.compareTo(b.hour);
      }
    });

    List<MapEntry<DateTime, Medication>> result = [];
    for (Posology posology in sortedPosologies) {
      DateTime posologyTime = DateTime(
          start.year, start.month, start.day, posology.hour, posology.minute);

      if (posologyTime.isBefore(start)) {
        posologyTime = posologyTime.add(const Duration(days: 1));
      }

      if (posologyTime.isAfter(start) && posologyTime.isBefore(endTime)) {
        final Medication medication = medications.firstWhere(
              (med) => med.id == posology.medicationId,
          orElse: () =>
          throw Exception(
              'Medication not found for posology ${posology.medicationId}'),
        );

        if (posologyTime.isAfter(DateTime.parse(medication.startDate)) &&
            posologyTime.isBefore(DateTime.parse(medication.startDate).
            add(Duration(days: medication.treatmentDuration)))) {
          result.add(MapEntry(posologyTime, medication));
        }
      }
    }

    result.sort((a, b) => a.key.compareTo(b.key));

    return result;
  }

  Future<void> setPendingIntakes() async {
    final List<Medication> medications = ref.read(appProvider).medications;
    final List<List<Posology>> posologies = ref.read(appProvider).posologies;
    List<List<MapEntry<DateTime, Medication>>> pendingIntakes = [];
    for (int i = 0; i <= state.days; i++) {
      int hoursToAdd = (i == state.days)
          ? state.hours
          : 24;

      pendingIntakes.add(
        _getPendingIntakesOneDay(
          DateTime.now().add(Duration(days: i)),
          hoursToAdd,
          medications,
          posologies,
        ),
      );
    }

    List<MapEntry<DateTime, Medication>> expandedPendingIntakes = pendingIntakes.expand((list) => list).toList();
    List<Medication> intakesMedications = expandedPendingIntakes.map((entry) => entry.value).toList();
    List<List<Posology>> intakesPosologies = intakesMedications.map(
            (entry) => ref.read(appProvider).posologies
            .where((posologies) => posologies.isNotEmpty && posologies[0].medicationId == entry.id)
            .expand((posologies) => posologies)
            .toList()
    ).toList();
    List<String> intakesMedicationNames = expandedPendingIntakes.map((entry) => entry.value.name).toList();
    List<String> intakesDates = expandedPendingIntakes.map((entry) => Formatter.formatDateTodayTomorrow(entry.key)).toList();
    state = state.copyWith(
      intakesMedications: intakesMedications,
      intakesPosologies: intakesPosologies,
      intakesMedicationNames: intakesMedicationNames,
      intakesDates: intakesDates,
    );
  }

  Future<void> searchPendingIntakes(BuildContext context) async {
    bool isCancelled = false;

    showLoadingDialog(context, () {
      isCancelled = true;
      closeLoadingDialog(context);
    });

    // await Future.delayed(const Duration(seconds: 3)); // DEBUG

    if (isCancelled) return;
    await setPendingIntakes();

    if (!isCancelled && context.mounted) {
      closeLoadingDialog(context);
      showSnackBar(context, "Found ${state.intakesDates.length} pending intakes", color: Colors.blue);
    }
  }
}


class HomeState {
  List<Medication> medications;
  List<List<Posology>> posologies;
  final int days;
  final int hours;
  final List<Medication> intakesMedications;
  final List<List<Posology>> intakesPosologies;
  final List<String> intakesMedicationNames;
  final List<String> intakesDates;

  HomeState({
    this.medications = const [],
    this.posologies = const [],
    this.days = 0,
    this.hours = 0,
    this.intakesMedications = const [],
    this.intakesPosologies = const [],
    this.intakesMedicationNames = const [],
    this.intakesDates = const []
  });

  HomeState copyWith({
    List<Medication>? medications,
    List<List<Posology>>? posologies,
    int? days,
    int? hours,
    List<Medication>? intakesMedications,
    List<List<Posology>>? intakesPosologies,
    List<String>? intakesMedicationNames,
    List<String>? intakesDates
  })
  {
    return HomeState(
      medications: medications ?? this.medications,
      posologies: posologies ?? this.posologies,
      days: days ?? this.days,
      hours: hours ?? this.hours,
      intakesMedications: intakesMedications ?? this.intakesMedications,
      intakesPosologies: intakesPosologies ?? this.intakesPosologies,
      intakesMedicationNames: intakesMedicationNames ?? this.intakesMedicationNames,
      intakesDates: intakesDates ?? this.intakesDates,
    );
  }
}
