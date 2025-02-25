import 'package:app/view/pages/medication_details_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../models/medication.dart';
import '../../models/posology.dart';
import '../../state/medications_notifier.dart';
import '../../utils/formatters.dart';
import '../widgets/custom_appbar.dart';

class MedicationsPage extends ConsumerWidget {
  const MedicationsPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final MedicationsNotifier medicationsNotifier = ref.watch(medicationsProvider.notifier);
    final MedicationsState medicationsState = ref.watch(medicationsProvider);

    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

    return Scaffold(
      appBar: const CustomAppBar(title: "Medications"),
      body: medicationsState.medications.isEmpty
          ? Center(
        child: Text(
          "No medications",
          style: TextStyle(
            fontSize: isWearOS ? 18 : 24,
            color: Colors.black54,
          ),
          textAlign: TextAlign.center,
        ),
      )
          : ListView.builder(
        padding: EdgeInsets.symmetric(
          horizontal: isWearOS ? 4 : 16,
          vertical: isWearOS ? 10 : 20,
        ),
        itemCount: medicationsState.medications.length,
        itemBuilder: (context, index) {
          final Medication medication = medicationsState.medications[index];
          final List<Posology> medicationPosologies = medicationsState.posologies[index];
          return Card(
            elevation: isWearOS ? 1 : 3,
            margin: EdgeInsets.symmetric(vertical: isWearOS ? 4 : 8),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(isWearOS ? 8 : 12),
            ),
            child: ListTile(
              contentPadding: isWearOS
                  ? const EdgeInsets.symmetric(horizontal: 8, vertical: 4)
                  : const EdgeInsets.all(16),
              title: Text(
                medication.name,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontSize: isWearOS ? 14 : 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              subtitle: Text(
                Formatter.formatPosologies(medicationPosologies),
                style: TextStyle(
                  fontSize: isWearOS ? 12 : 18,
                  color: Colors.black54,
                ),
              ),
              trailing: InkWell(
                onTap: () {
                  medicationsNotifier.markIntake(context: context, medication: medication);
                },
                borderRadius: BorderRadius.circular(8),
                overlayColor: WidgetStateProperty.resolveWith((states) {
                  if (states.contains(WidgetState.pressed)) {
                    return Colors.indigo.withOpacity(0.1);
                  }
                  return null;
                }),
                child: Padding(
                  padding: const EdgeInsets.all(4.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        Icons.check,
                        color: Colors.indigo,
                        size: isWearOS ? 25 : 30,
                      ),
                      Text(
                        "Intake",
                        style: TextStyle(
                          fontSize: isWearOS ? 10 : 12,
                          fontWeight: FontWeight.bold,
                          color: Colors.indigo,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => MedicationDetailsPage(
                      medication: medication,
                      medicationPosologies: medicationPosologies,
                    ),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }

}