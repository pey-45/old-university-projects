import '../exceptions/model_exception.dart';
import '../models/intake.dart';
import '../models/medication.dart';
import '../models/patient.dart';
import '../models/posology.dart';
import 'service_interface.dart';
import 'mock_data/fake_medications.dart';
import 'mock_data/fake_patients.dart';
import 'mock_data/fake_posologies.dart';

class MockService implements ServiceInterface {

  @override
  Future<Patient> getPatientByCode(String code) async {
    if (code == fakePatient.code) {
      return fakePatient;
    } else if (code == fakePatientNoMeds.code) {
      return fakePatientNoMeds;
    } else {
      throw ModelException("Patient not found");
    }

  }

  @override
  Future<List<Medication>> getMedications(int patientId) async {
    return patientId == 1 ? fakeMedications : [];
  }

  @override
  Future<List<Posology>> getPosologies(int patientId, int medicationId) async {
    return patientId == 1 ? fakePosologies[medicationId - 1] : [];  // Valid ONLY for mock testing
  }

  @override
  Future<void> postIntake(int patientId, int medicationId, Intake intake) async {
    await Future.delayed(const Duration(milliseconds: 200));
  }
}