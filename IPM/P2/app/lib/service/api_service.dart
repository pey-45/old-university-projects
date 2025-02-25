import 'dart:convert';

import 'package:http/http.dart' as http;

import '../exceptions/connection_exception.dart';
import '../exceptions/model_exception.dart';
import '../models/intake.dart';
import '../models/medication.dart';
import '../models/patient.dart';
import '../models/posology.dart';
import 'service_interface.dart';

class ApiService implements ServiceInterface {

  static Future<dynamic> handleRequest(String method, String url, String errorMessage, {Map<String, dynamic>? data}) async {
    try {
      final Uri uri = Uri.parse(url);
      late http.Response response;

      if (method == 'POST') {
        response = await http.post(uri, headers: {'Content-Type': 'application/json'}, body: jsonEncode(data));
      } else if (method == 'GET') {
        response = await http.get(uri, headers: {'Content-Type': 'application/json'});
      } else if (method == 'PATCH') {
        response = await http.patch(uri, headers: {'Content-Type': 'application/json'}, body: jsonEncode(data));
      } else if (method == 'DELETE') {
        response = await http.delete(uri, headers: {'Content-Type': 'application/json'});
      } else {
        throw ModelException("Invalid method");
      }

      if (response.statusCode == 200 || response.statusCode == 201) {
        return jsonDecode(response.body);
      } else {
        throw ModelException(errorMessage);
      }
    } on ModelException {
      rethrow;
    } catch (e) {
      throw ConnectionException("Connection error");
    }
  }

  @override
  Future<Patient> getPatientByCode(String code) async {
    final String url = '$serverUrl/patients?code=$code';
    try {
      final Map<String, dynamic> response = await handleRequest('GET', url, "Patient not found");
      return Patient.fromJson(response);
    } on ModelException catch (e) {
      throw ModelException(e.message);
    } on ConnectionException catch(e) {
      throw ConnectionException(e.message);
    }
  }

  @override
  Future<List<Medication>> getMedications(int patientId) async {
    final String url = '$serverUrl/patients/$patientId/medications';
    try {
      final List<dynamic> response = await handleRequest('GET', url, "Medications not found");
      return response.map((item) => Medication.fromJson(item)).toList();
    } on ModelException catch(e) {
      throw ModelException(e.message);
    } on ConnectionException catch(e) {
      throw ConnectionException(e.message);
    }
  }

  @override
  Future<List<Posology>> getPosologies(int patientId, int medicationId) async {
    final String url = '$serverUrl/patients/$patientId/medications/$medicationId/posologies';
    try {
      final List<dynamic> response = await handleRequest('GET', url, "Posologies not found");
      return (response).map((item) => Posology.fromJson(item)).toList();
    } on ModelException catch (e) {
      throw ModelException(e.message);
    } on ConnectionException catch(e) {
      throw ConnectionException(e.message);
    }
  }

  @override
  Future<void> postIntake(int patientId, int medicationId, Intake intake) async {
    final String url = '$serverUrl/patients/$patientId/medications/$medicationId/intakes';
    try {
      await handleRequest('POST', url, 'Failed to add intake', data: intake.toJson());
    } on ModelException catch (e) {
      throw ModelException(e.message);
    } on ConnectionException catch(e) {
      throw ConnectionException(e.message);
    }
  }
}