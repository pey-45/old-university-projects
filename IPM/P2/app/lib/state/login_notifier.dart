import 'package:app/exceptions/connection_exception.dart';
import 'package:app/exceptions/model_exception.dart';
import 'package:app/state/service_notifier.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../models/medication.dart';
import '../../models/patient.dart';
import '../../models/posology.dart';
import '../../utils/session_handler.dart';
import '../../view/screens/main_screen.dart';
import '../../view/widgets/loading_dialog.dart';
import '../../view/widgets/snack_bar.dart';
import 'app_notifier.dart';
import 'home_notifier.dart';
import 'medications_notifier.dart';


final StateNotifierProvider<LoginNotifier, LoginState> loginProvider =
StateNotifierProvider<LoginNotifier, LoginState>((ref) => LoginNotifier(ref));


class LoginNotifier extends StateNotifier<LoginState> {
  final Ref ref;

  LoginNotifier(this.ref) : super(
    LoginState(),
  );

  Future<void> setPatient(BuildContext context, WidgetRef ref, String code, {bool fake = false}) async {
    if (code.trim().isEmpty) {
      throw ModelException("Enter a patient code");
    }

    final AppNotifier appNotifier = ref.read(appProvider.notifier);
    final MedicationsNotifier medicationsNotifier = ref.read(medicationsProvider.notifier);
    final HomeNotifier homeNotifier = ref.read(homeProvider.notifier);

    appNotifier.setState(
      patient: null,
      medications: [],
      posologies: [],
    );
    medicationsNotifier.setState(
      medications: [],
      posologies: [],
    );
    homeNotifier.setState(
      medications: [],
      posologies: [],
      days: 1,
      hours: 0,
      intakesMedications: [],
      intakesPosologies: [],
      intakesMedicationNames: [],
      intakesDates: [],
    );
    homeNotifier.setPendingIntakes();

    try {
      final apiService = ref.read(serviceProvider).currentService;
      Patient patient = await apiService.getPatientByCode(code);

      List<Medication> medications = await apiService.getMedications(patient.id as int);
      List<List<Posology>> posologies = [];
      for (Medication medication in medications) {
        List<Posology> medicationPosologies = await apiService.getPosologies(
          patient.id as int,
          medication.id as int,
        );
        posologies.add(medicationPosologies);
      }

      appNotifier.setState(patient: patient, medications: medications, posologies: posologies);
      medicationsNotifier.state = MedicationsState(medications: medications, posologies: posologies);
      homeNotifier.state = HomeState(
        medications: medications,
        posologies: posologies,
        days: 1,
        hours: 0,
        intakesMedications: [],
        intakesPosologies: [],
        intakesMedicationNames: [],
        intakesDates: [],
      );

      homeNotifier.setPendingIntakes();

    } on ModelException {
      rethrow;
    } on ConnectionException catch (e) {
      throw ConnectionException(e.message);
    } catch(e) {
      throw Exception();
    }
  }

  void logIn(BuildContext context, WidgetRef ref, LoginNotifier loginNotifier, TextEditingController controller) async {
    bool isCancelled = false;

    showLoadingDialog(context, () {
      isCancelled = true;
      closeLoadingDialog(context);
    });

    try {
      final AppNotifier appNotifier = ref.read(appProvider.notifier);
      appNotifier.setState(patient: null, medications: [], posologies: []);

      // await Future.delayed(const Duration(seconds: 3)); // DEBUG
      if (isCancelled || !context.mounted) return;
      await setPatient(context, ref, controller.text);

      Patient? patient = ref.read(appProvider).patient;

      // await Future.delayed(const Duration(seconds: 3)); // DEBUG
      if (isCancelled) return;
      if (context.mounted) {
        closeLoadingDialog(context);
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => const MainScreen()),
        );
        showSnackBar(context, "Logged in as ${patient?.name}", color: Colors.blue);
      }

      SessionHandler.saveSession(patient?.code);
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
  }
}


class LoginState {

  LoginState();

  LoginState copyWith() {
    return LoginState();
  }
}

