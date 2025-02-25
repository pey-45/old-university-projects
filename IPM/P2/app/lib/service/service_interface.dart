

import '../models/intake.dart';
import '../models/medication.dart';
import '../models/patient.dart';
import '../models/posology.dart';

const String serverUrl = "http://10.0.2.2:8000";

abstract class ServiceInterface {
  Future<Patient> getPatientByCode(String code);
  Future<List<Medication>> getMedications(int patientId);
  Future<List<Posology>> getPosologies(int patientId, int medicationId);
  Future<void> postIntake(int patientId, int medicationId, Intake intake);
}




