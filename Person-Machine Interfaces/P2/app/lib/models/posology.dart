class Posology {
  final int? id;
  final int hour;
  final int minute;
  final int medicationId;

  // Constructor
  Posology({
    this.id,
    required this.hour,
    required this.minute,
    required this.medicationId,
  });

  // JSON to object
  factory Posology.fromJson(Map<String, dynamic> json) {
    return Posology(
      id: json['id'],
      hour: json['hour'],
      minute: json['minute'],
      medicationId: json['medication_id'],
    );
  }

  // Object to JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'hour': hour,
      'minute': minute,
      'medication_id': medicationId,
    };
  }

  @override
  String toString() {
    return 'Posology(id: $id, hour: $hour, minute: $minute, medicationId: $medicationId)';
  }
}
