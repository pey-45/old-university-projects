import 'package:app/service/mock_data/fake_patients.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:flutter/material.dart';
import 'package:integration_test/integration_test.dart';
import 'package:app/main.dart' as app;


void main() {
  const fakePatientCode = "000000000";
  const fakePatientCodeNoMeds = "000000001";

  IntegrationTestWidgetsFlutterBinding.ensureInitialized();


  Future<void> expectSnackbarWithTextAndRemoveIt(WidgetTester tester, String text) async {
    await Future.delayed(const Duration(milliseconds: 400)); // Wait a little bit for it to appear
    expect(find.text(text), findsOneWidget);
    var snackBar = find.byType(SnackBar).first;
    await tester.drag(snackBar, const Offset(0, 100));
    await tester.pumpAndSettle();
  }

  Future<void> expectSnackbarWithPartialTextAndRemoveIt(WidgetTester tester, String partialText) async {
    await Future.delayed(const Duration(milliseconds: 400)); // Wait a little bit for it to appear
    final snackBarFinder = find.byWidgetPredicate((widget) {
      if (widget is SnackBar) {
        return widget.content is Text &&
            (widget.content as Text).data?.contains(partialText) == true;
      }
      return false;
    });

    expect(snackBarFinder, findsOneWidget);
    await tester.drag(snackBarFinder.first, const Offset(0, 100));
    await tester.pumpAndSettle();
  }

  Future<void> loginIfNotLogged(WidgetTester tester, String code) async {
    if (find.text('Welcome').evaluate().isNotEmpty) {
      await tester.enterText(find.byType(TextField), code);
      await tester.tap(find.byType(ElevatedButton));
      await tester.pumpAndSettle();
    }
  }

  Future<void> logoutIfLogged(WidgetTester tester, {bool tryCancel = false}) async {
    if (find.byIcon(Icons.logout).evaluate().isNotEmpty) {
      final logoutButton = find.byIcon(Icons.logout);
      expect(logoutButton, findsOneWidget);
      await tester.tap(logoutButton);
      await tester.pumpAndSettle();

      final okButton = find.text('Ok');
      expect(okButton, findsOneWidget);

      if (tryCancel) {
        final cancelButton = find.text('Cancel');
        expect(cancelButton, findsOneWidget);
        await tester.tap(cancelButton);
        await tester.pumpAndSettle();
        await tester.tap(logoutButton);
        await tester.pumpAndSettle();
      }

      await tester.tap(okButton);
      await tester.pumpAndSettle();
    }
  }

  testWidgets('1. Incorrect Login Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await logoutIfLogged(tester);

    expect(find.text("Welcome"), findsOneWidget);

    await tester.tap(find.byType(ElevatedButton));
    await tester.pumpAndSettle();
    expect(find.text("Welcome"), findsOneWidget);
    await expectSnackbarWithTextAndRemoveIt(tester, "Enter a patient code");

    await tester.enterText(find.byType(TextField), '12345');
    await tester.tap(find.byType(ElevatedButton));
    await tester.pumpAndSettle();
    expect(find.text('Welcome'), findsOneWidget);
    await expectSnackbarWithTextAndRemoveIt(tester, "Patient not found");
  });

  testWidgets('2. Login Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await logoutIfLogged(tester);
    await loginIfNotLogged(tester, fakePatientCode);

    expect(find.text('Home'), findsWidgets);
    await expectSnackbarWithTextAndRemoveIt(tester, "Logged in as ${fakePatient.name}");
  });

  testWidgets('3. Home Page Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCode);

    expect(find.text('Home'), findsWidgets);

    final dayPicker = find.byType(ListWheelScrollView).first;
    final hourPicker = find.byType(ListWheelScrollView).last;

    await tester.drag(dayPicker, const Offset(0, 100));
    await tester.pumpAndSettle();
    await tester.drag(dayPicker, const Offset(0, -100));
    await tester.pumpAndSettle();
    await tester.drag(hourPicker, const Offset(0, 100));
    await tester.pumpAndSettle();
    await tester.drag(hourPicker, const Offset(0, -100));
    await tester.pumpAndSettle();

    await tester.tap(find.byIcon(Icons.search));
    await tester.pumpAndSettle();
    await expectSnackbarWithPartialTextAndRemoveIt(tester, "Found ");

    expect(find.byType(ListView), findsOneWidget);

    final pendingIntakesList = find.byType(ListView).first;
    await tester.drag(pendingIntakesList, const Offset(0, -100));
    await tester.pumpAndSettle();
    await tester.drag(pendingIntakesList, const Offset(0, 100));
    await tester.pumpAndSettle();

    if (find.byType(Card).evaluate().isNotEmpty) {
      final firstPendingIntakeCard = find.byType(Card).first;
      await tester.tap(firstPendingIntakeCard);
      await tester.pumpAndSettle();

      expect(find.text('Medication Details'), findsOneWidget);

      expect(find.text('Name'), findsOneWidget);
      expect(find.text('Dosage'), findsOneWidget);
      expect(find.text('Start Date'), findsOneWidget);
      expect(find.text('End Date'), findsOneWidget);
      await tester.dragFrom(tester.getCenter(find.byType(Scaffold)), const Offset(0, -100));
      await tester.pumpAndSettle();
      expect(find.text('Posologies'), findsOneWidget);
    } else {
      fail('No pending intakes cards found.');
    }
  });

  testWidgets('4. Medications Page Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCode);

    await tester.tap(find.byIcon(Icons.medication));
    await tester.pumpAndSettle();

    expect(find.text('Medications'), findsWidgets);

    expect(find.byType(ListView), findsOneWidget);
    if (find.byType(Card).evaluate().isNotEmpty) {
      final firstMedicationCard = find.byType(Card).first;

      await tester.tap(find.descendant(
        of: firstMedicationCard,
        matching: find.byIcon(Icons.check),
      ));
      await tester.pumpAndSettle();

      final cancelButton = find.text('Cancel');
      final okButton = find.text('Ok');
      expect(cancelButton, findsOneWidget);
      expect(okButton, findsOneWidget);
      await tester.tap(cancelButton);
      await tester.pumpAndSettle();
      await tester.tap(find.descendant(
        of: firstMedicationCard,
        matching: find.byIcon(Icons.check),
      ));
      await tester.pumpAndSettle();
      await tester.tap(okButton);
      await tester.pumpAndSettle();
      await expectSnackbarWithTextAndRemoveIt(tester, "Intake marked");
    } else {
      fail('No medication cards found.');
    }
  });

  testWidgets('5. Medication Details Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCode);

    await tester.tap(find.byIcon(Icons.medication));
    await tester.pumpAndSettle();

    if (find.byType(Card).evaluate().isNotEmpty) {
      final firstMedicationCard = find.byType(Card).first;
      await tester.tap(firstMedicationCard);
      await tester.pumpAndSettle();

      expect(find.text('Medication Details'), findsOneWidget);

      expect(find.text('Name'), findsOneWidget);
      expect(find.text('Dosage'), findsOneWidget);
      expect(find.text('Start Date'), findsOneWidget);
      expect(find.text('End Date'), findsOneWidget);
      await tester.dragFrom(tester.getCenter(find.byType(Scaffold)), const Offset(0, -300));
      await tester.pumpAndSettle();
      expect(find.text('Posologies'), findsOneWidget);
    } else {
      fail('No medication cards found.');
    }
  });

  testWidgets('6. Notifications Page Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCode);

    await tester.tap(find.byIcon(Icons.notifications));
    await tester.pumpAndSettle();

    expect(find.text('Notifications'), findsWidgets);
    expect(find.text('Coming soon...'), findsOneWidget);
  });

  testWidgets('7. Logout Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCode);

    await logoutIfLogged(tester, tryCancel: true);

    expect(find.text('Welcome'), findsOneWidget);
    expect(find.byType(TextField), findsOneWidget);
    expect(find.byType(ElevatedButton), findsOneWidget);
    await expectSnackbarWithTextAndRemoveIt(tester, "Logged out");
  });

  testWidgets('8. Patient With No Medications Test', (WidgetTester tester) async {
    app.main(mock: true);
    await tester.pumpAndSettle();
    await loginIfNotLogged(tester, fakePatientCodeNoMeds);
    await expectSnackbarWithTextAndRemoveIt(tester, "Logged in as ${fakePatientNoMeds.name}");

    expect(find.text('Home'), findsWidgets);

    final dayPicker = find.byType(ListWheelScrollView).first;
    final hourPicker = find.byType(ListWheelScrollView).last;

    await tester.drag(dayPicker, const Offset(0, 100));
    await tester.pumpAndSettle();
    await tester.drag(dayPicker, const Offset(0, -100));
    await tester.pumpAndSettle();
    await tester.drag(hourPicker, const Offset(0, 100));
    await tester.pumpAndSettle();
    await tester.drag(hourPicker, const Offset(0, -100));
    await tester.pumpAndSettle();

    await tester.tap(find.byIcon(Icons.search));
    await tester.pumpAndSettle();
    await expectSnackbarWithTextAndRemoveIt(tester, "Found 0 pending intakes");

    expect(find.byType(ListView), findsNothing);
    expect(find.byType(Card), findsNothing);
    expect(find.text("No pending intakes"), findsOneWidget);

    await tester.tap(find.byIcon(Icons.medication));
    await tester.pumpAndSettle();

    expect(find.text('Medications'), findsWidgets);
    expect(find.byType(ListView), findsNothing);
    expect(find.byType(Card), findsNothing);
    expect(find.text("No medications"), findsOneWidget);

    await logoutIfLogged(tester);

    expect(find.text('Welcome'), findsOneWidget);
    expect(find.byType(TextField), findsOneWidget);
    expect(find.byType(ElevatedButton), findsOneWidget);
    await expectSnackbarWithTextAndRemoveIt(tester, "Logged out");

  });
}