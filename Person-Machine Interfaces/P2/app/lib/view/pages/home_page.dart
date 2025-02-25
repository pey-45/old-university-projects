import 'package:app/view/pages/medication_details_page.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../models/medication.dart';
import '../../models/posology.dart';
import '../../state/home_notifier.dart';
import '../widgets/custom_appbar.dart';

class HomePage extends ConsumerWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final HomeNotifier homeNotifier = ref.watch(homeProvider.notifier);
    final HomeState homeState = ref.watch(homeProvider);

    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

    return Scaffold(
      appBar: const CustomAppBar(title: "Home"),
      body: Padding(
        padding: isWearOS
            ? const EdgeInsets.all(0.0)
            : const EdgeInsets.symmetric(horizontal: 4.0, vertical: 20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            if (!isWearOS)
              const Text(
                "Pending intakes in:",
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
            SizedBox(height: isWearOS ? 0 : 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                _CustomPicker(
                  value: homeState.days,
                  itemCount: 31,
                  label: "d",
                  onChanged: (int days) => homeNotifier.setState(days: days),
                  isWearOS: isWearOS,
                ),
                SizedBox(width: isWearOS ? 4 : 12),
                _CustomPicker(
                  value: homeState.hours,
                  itemCount: 24,
                  label: "h",
                  onChanged: (int hours) => homeNotifier.setState(hours: hours),
                  isWearOS: isWearOS,
                ),
                SizedBox(width: isWearOS ? 0 : 12),
                IconButton(
                  onPressed: () {
                    homeNotifier.searchPendingIntakes(context);
                  },
                  icon: const Icon(Icons.search),
                  color: Colors.indigo,
                  iconSize: 28,
                  tooltip: "Search",
                ),
              ],
            ),
            SizedBox(height: isWearOS ? 0 : 20),
            Expanded(
              child: homeState.intakesDates.isEmpty
                  ? Center(
                child: Text(
                  "No pending intakes",
                  style: TextStyle(
                    fontSize: isWearOS ? 18 : 24,
                    color: Colors.black54,
                  ),
                  textAlign: TextAlign.center,
                ),
              )
                  : ListView.builder(
                itemCount: homeState.intakesDates.length,
                itemBuilder: (context, index) {
                  return _MedicationTile(
                    medication: homeState.intakesMedications[index],
                    medicationPosologies: homeState.intakesPosologies[index],
                    medicationName: homeState.intakesMedicationNames[index],
                    date: homeState.intakesDates[index],
                    isWearOS: isWearOS,
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _CustomPicker extends StatelessWidget {
  final int value;
  final int itemCount;
  final String label;
  final void Function(int) onChanged;
  final bool isWearOS;

  const _CustomPicker({
    required this.value,
    required this.itemCount,
    required this.label,
    required this.onChanged,
    required this.isWearOS,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: isWearOS ? 50 : 120,
      height: isWearOS ? 40 : 120,
      child: CupertinoPicker(
        backgroundColor: Colors.white,
        scrollController: FixedExtentScrollController(initialItem: value),
        itemExtent: isWearOS ? 25.0 : 45.0,
        onSelectedItemChanged: onChanged,
        children: List<Widget>.generate(
          itemCount,
              (index) => Center(
            child: Text(
              "$index $label",
              style: TextStyle(
                fontSize: isWearOS? 22 : 24,
                fontWeight: index == value ? FontWeight.bold : FontWeight.normal,
                color: index == value ? Colors.black : Colors.grey,
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _MedicationTile extends StatelessWidget {
  final Medication medication;
  final List<Posology> medicationPosologies;
  final String medicationName;
  final String date;
  final bool isWearOS;

  const _MedicationTile({
    required this.medication,
    required this.medicationPosologies,
    required this.medicationName,
    required this.date,
    required this.isWearOS,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: isWearOS ? 1 : 2,
      margin: EdgeInsets.symmetric(
        vertical: isWearOS ? 4 : 8,
        horizontal: isWearOS ? 4 : 16,
      ),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(isWearOS ? 8 : 12),
      ),
      child: ListTile(
        contentPadding: EdgeInsets.symmetric(
          horizontal: isWearOS ? 8 : 16,
          vertical: isWearOS ? 4 : 8,
        ),
        title: Text(
          medicationName,
          style: TextStyle(
            fontSize: isWearOS ? 14 : 20,
            fontWeight: FontWeight.bold,
          ),
        ),
        subtitle: Text(
          date,
          style: TextStyle(
            fontSize: isWearOS ? 12 : 18,
            color: Colors.black54,
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
  }
}
