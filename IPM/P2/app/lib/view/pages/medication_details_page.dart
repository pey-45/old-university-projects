import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../models/medication.dart';
import '../../models/posology.dart';
import '../../utils/formatters.dart';
import '../widgets/custom_child_appbar.dart';

class MedicationDetailsPage extends StatelessWidget {
  final Medication medication;
  final List<Posology> medicationPosologies;

  const MedicationDetailsPage({
    super.key,
    required this.medication,
    required this.medicationPosologies,
  });

  @override
  Widget build(BuildContext context) {
    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;
    final DateFormat dateFormatter = DateFormat('MM/dd/yyyy');

    return Scaffold(
      appBar: const CustomChildAppBar(title: "Medication Details"),
      backgroundColor: Colors.grey[100],
      body: Padding(
        padding:  EdgeInsets.all(isWearOS ? 8.0 : 16.0),
        child: ListView(
          children: [
            _DetailRow(
              title: "Name",
              value: medication.name,
              isWearOS: isWearOS,
            ),
            _DetailRow(
              title: "Dosage",
              value: medication.dosage.toString(),
              isWearOS: isWearOS,
            ),
            _DetailRow(
              title: "Start Date",
              value: dateFormatter.format(DateTime.parse(medication.startDate)),
              isWearOS: isWearOS,
            ),
            _DetailRow(
              title: "End Date",
              value: dateFormatter.format(
                DateTime.parse(medication.startDate).add(
                  Duration(days: medication.treatmentDuration),
                ),
              ),
              isWearOS: isWearOS,
            ),
            _DetailRow(
              title: "Posologies",
              value: Formatter.formatPosologies(medicationPosologies),
              isWearOS: isWearOS,
            ),
          ],
        ),
      ),
    );
  }
}

class _DetailRow extends StatelessWidget {
  final String title;
  final String value;
  final bool isWearOS;

  const _DetailRow({
    required this.title,
    required this.value,
    required this.isWearOS,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(
        horizontal: isWearOS ? 4 : 8,
        vertical: isWearOS ? 2 : 4,
      ),
      margin: EdgeInsets.only(bottom: isWearOS ? 8 : 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: TextStyle(
              fontSize: isWearOS ? 12 : 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          Text(
            value,
            style: TextStyle(
              fontSize: isWearOS ? 10 : 18,
              color: Colors.black54,
            ),
          ),
        ],
      )
    );
  }
}
