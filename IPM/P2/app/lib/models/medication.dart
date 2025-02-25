class Medication {
  final int? id;
  final String name;
  final double dosage;
  final String startDate;
  final int treatmentDuration;
  final int patientId;

  // Constructor
  Medication({
    this.id,
    required this.name,
    required this.dosage,
    required this.startDate,
    required this.treatmentDuration,
    required this.patientId,
  });

  // JSON to object
  factory Medication.fromJson(Map<String, dynamic> json) {
    return Medication(
      id: json['id'],
      name: json['name'],
      dosage: json['dosage'],
      startDate: json['start_date'],
      treatmentDuration: json['treatment_duration'],
      patientId: json['patient_id'],
    );
  }

  // Object to JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'dosage': dosage,
      'start_date': startDate,
      'treatment_duration': treatmentDuration,
      'patient_id': patientId,
    };
  }

  @override
  String toString() {
    return 'Medication(id: $id, name: $name, dosage: $dosage, startDate: $startDate, treatmentDuration: $treatmentDuration, patientId: $patientId)';
  }
}
