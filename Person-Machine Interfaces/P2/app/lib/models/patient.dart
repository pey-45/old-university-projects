class Patient {
  final int? id;
  final String code;
  final String name;
  final String surname;

  // Constructor
  Patient({
    this.id,
    required this.code,
    required this.name,
    required this.surname,
  });

  // JSON to object
  factory Patient.fromJson(Map<String, dynamic> json) {
    return Patient(
      id: json['id'],
      code: json['code'],
      name: json['name'],
      surname: json['surname'],
    );
  }

  // Object to JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'code': code,
      'name': name,
      'surname': surname,
    };
  }

  @override
  String toString() {
    return 'Patient(id: $id, code: $code, name: $name, surname: $surname)';
  }
}
