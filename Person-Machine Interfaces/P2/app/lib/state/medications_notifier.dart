import 'package:app/state/service_notifier.dart';
import 'package:app/view/widgets/loading_dialog.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import '../../exceptions/connection_exception.dart';
import '../../exceptions/model_exception.dart';
import '../../models/intake.dart';
import '../../models/medication.dart';
import '../../models/posology.dart';
import '../../view/widgets/show_confirmation_dialog.dart';
import '../../view/widgets/snack_bar.dart';
import 'app_notifier.dart';


final StateNotifierProvider<MedicationsNotifier, MedicationsState> medicationsProvider =
StateNotifierProvider<MedicationsNotifier, MedicationsState>((ref) => MedicationsNotifier(ref));


class MedicationsNotifier extends StateNotifier<MedicationsState> {
  final Ref ref;

  MedicationsNotifier(this.ref) : super(
    MedicationsState(
      medications: ref.read(appProvider).medications,
      posologies: ref.read(appProvider).posologies,
    ),
  );

  void setState({List<Medication>? medications, List<List<Posology>>? posologies}) {
    state = state.copyWith(
      medications: medications ?? state.medications,
      posologies: posologies ?? state.posologies,
    );
  }


  Future<void> markIntake({required context, required Medication medication, fake = false}) async {
    final DateFormat dateTimeFormatter = DateFormat('yyyy-MM-ddTHH:mm');
    showConfirmationDialog(
      context,
      "Confirmation",
      "Are you sure you want to mark an intake on this medication?",
          () async {
        bool isCancelled = false;

        showLoadingDialog(
          context,
              () {
            isCancelled = true;
            closeLoadingDialog(context);
          },
        );

        try {
          final apiService = ref.read(serviceProvider).currentService;

          Intake intake = Intake(
            date: dateTimeFormatter.format(DateTime.now()),
            medicationId: medication.id as int,
          );

          // await Future.delayed(const Duration(seconds: 3)); //DEBUG

          if (isCancelled) return;
          await apiService.postIntake(
            medication.patientId,
            medication.id as int,
            intake,
          );

          if (!isCancelled) {
            closeLoadingDialog(context);
            showSnackBar(context, "Intake marked", color: Colors.blue);
          }

        } on ModelException catch (e) {
          // await Future.delayed(const Duration(seconds: 3)); // DEBUG
          if (!isCancelled && context.mounted) {
            closeLoadingDialog(context);
            showSnackBar(context, e.message, color: Colors.red);
          }
        } on ConnectionException catch(e) {
          // await Future.delayed(const Duration(seconds: 3)); // DEBUG
          if (!isCancelled && context.mounted) {
            closeLoadingDialog(context);
            showSnackBar(context, e.message, color: Colors.red);
          }
        } catch (e) {
          // await Future.delayed(const Duration(seconds: 3)); // DEBUG
          if (!isCancelled && context.mounted) {
            closeLoadingDialog(context);
            showSnackBar(context, "Unexpected error: $e", color: Colors.red);
          }
        }
      },
    );
  }
}


class MedicationsState {
  List<Medication> medications;
  List<List<Posology>> posologies;

  MedicationsState({
    this.medications = const [],
    this.posologies = const [],
  });

  MedicationsState copyWith({
    List<Medication>? medications,
    List<List<Posology>>? posologies,
  })
  {
    return MedicationsState(
      medications: medications ?? this.medications,
      posologies: posologies ?? this.posologies,
    );
  }
}
