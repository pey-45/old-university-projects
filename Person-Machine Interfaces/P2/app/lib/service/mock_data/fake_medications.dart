import 'package:app/models/medication.dart';

List<Medication> fakeMedications = [
  Medication(
    id: 1,
    name: 'Paracetamol',
    dosage: 500.0,
    startDate: '2024-12-01',
    treatmentDuration: 20,
    patientId: 1,
  ),
  Medication(
    id: 2,
    name: 'Ibuprofen',
    dosage: 200.0,
    startDate: '2024-12-02',
    treatmentDuration: 50,
    patientId: 1,
  ),
  Medication(
    id: 3,
    name: 'Amoxicillin',
    dosage: 250.0,
    startDate: '2024-12-03',
    treatmentDuration: 30,
    patientId: 1,
  ),
];

