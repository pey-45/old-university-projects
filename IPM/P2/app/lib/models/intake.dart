class Intake {
  final int? id;
  final String date;
  final int medicationId;

  // Constructor
  Intake({
    this.id,
    required this.date,
    required this.medicationId,
  });

  // JSON to object
  factory Intake.fromJson(Map<String, dynamic> json) {
    return Intake(
      id: json['id'],
      date: json['date'],
      medicationId: json['medication_id'],
    );
  }

  // Object to JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'date': date,
      'medication_id': medicationId,
    };
  }

  @override
  String toString() {
    return 'Intake(id: $id, date: $date, medicationId: $medicationId)';
  }
}
