
```mermaid

classDiagram
    class Patient {
        +int? id
        +String code
        +String name
        +String surname
        +Patient(id: int?, code: String, name: String, surname: String)
        +fromJson(Map<String, dynamic>): Patient
        +toJson(): Map<String, dynamic>
        +toString(): String
    }

    class Medication {
        +int? id
        +String name
        +double dosage
        +String startDate
        +int treatmentDuration
        +int patientId
        +Medication(id: int?, name: String, dosage: double, startDate: String, treatmentDuration: int, patientId: int)
        +fromJson(Map<String, dynamic>): Medication
        +toJson(): Map<String, dynamic>
        +toString(): String
    }

    class Posology {
        +int? id
        +int hour
        +int minute
        +int medicationId
        +Posology(id: int?, hour: int, minute: int, medicationId: int)
        +fromJson(Map<String, dynamic>): Posology
        +toJson(): Map<String, dynamic>
        +toString(): String
    }

    class Intake {
        +int? id
        +String date
        +int medicationId
        +Intake(id: int?, date: String, medicationId: int)
        +fromJson(Map<String, dynamic>): Intake
        +toJson(): Map<String, dynamic>
        +toString(): String
    }

    class RequestHandler {
        +handleRequest(method: String, url: String, errorMessage: String, data: Map<String, dynamic>?): dynamic
    }

    class PatientService {
        +String serverUrl
        +postPatient(patient: Patient): Patient
        +getPatients(startIndex: int?, count: int?): List<Patient>
        +getPatientByCode(code: String): Patient
        +getPatient(patientId: int): Patient
        +patchPatient(patientId: int, patient: Patient): void
        +deletePatient(patientId: int): void
    }

    class MedicationService {
        +String serverUrl
        +postMedication(patientId: int, medication: Medication): Medication
        +getMedications(patientId: int): List<Medication>
        +getMedication(patientId: int, medicationId: int): Medication
        +patchMedication(patientId: int, medicationId: int, medication: Medication): void
        +deleteMedication(patientId: int, medicationId: int): void
    }

    class PosologyService {
        +String serverUrl
        +postPosology(patientId: int, medicationId: int, posology: Posology): Posology
        +getPosologies(patientId: int, medicationId: int): List<Posology>
        +patchPosology(patientId: int, medicationId: int, posologyId: int, posology: Posology): void
        +deletePosology(patientId: int, medicationId: int, posologyId: int): void
    }

    class IntakeService {
        +String serverUrl
        +postIntake(patientId: int, medicationId: int, intake: Intake): Intake
        +getIntakesByMedication(patientId: int, medicationId: int): List<Intake>
        +getIntakesByPatient(patientId: int): List<Intake>
        +deleteIntake(patientId: int, medicationId: int, intakeId: int): void
    }

    class RiverpodState {
        +StateProvider<Patient?> patientProvider
        +StateProvider<List<Medication>> medicationsProvider
        +StateProvider<List<List<Posology>>> posologiesProvider
    }

    class PatientCodeFormatter {
        +formatEditUpdate(oldValue: TextEditingValue, newValue: TextEditingValue): TextEditingValue
    }

    class DateFormatter {
        +formatWithHHMM(date: String): String
        +formatWithoutHHMM(date: String): String
    }

    class PosologyFormatter {
        +formatPosologies(posologies: List<Posology>): String
    }

    class Formatter {
        +formatWithHHMM(date: String): String
        +formatWithoutHHMM(date: String): String
        +formatDateTodayTomorrow(date: DateTime): String
        +formatPosologies(posologies: List<Posology>): String
        +formatCode(input: String): String
    }

    class ConnectionException {
        +String message
        +ConnectionException(message: String)
        +toString(): String
    }

    class MainScreenApp {
        +runApp(): void
        +isWatch(context: BuildContext): bool
    }

    class MedicationDetailsPage {
        +Medication medication
        +List<Posology> medicationPosologies
        +MedicationDetailsPage(medication: Medication, medicationPosologies: List<Posology>)
        +build(): Widget
    }

    class MedicationsPage {
        +build(context: BuildContext): Widget
    }

    class NotificationsPage {
        +build(context: BuildContext): Widget
    }

    class MobileMainScreen {
        +_currentIndex: int
        +_screens: List<Widget>
        +build(context: BuildContext): Widget
    }

    class CustomAppBar {
        +String title
        +CustomAppBar(title: String)
        +build(context: BuildContext): Widget
    }

    class MedicationCard {
        +Medication medication
        +List<Posology> posologies
        +VoidCallback? onTap
        +build(context: BuildContext): Widget
    }

    class SearchPage {
        +int _selectedDay
        +int _selectedHour
        +build(context: BuildContext): Widget
    }

    class WearMainScreen {
        +List<Widget> _pages
        +build(context: BuildContext): Widget
    }

    class IntakeDialog {
        +VoidCallback onConfirm
        +build(context: BuildContext): Widget
    }

    class NotificationCard {
        +String title
        +String message
        +VoidCallback onTap
        +build(context: BuildContext): Widget
    }

    class SearchBarw {
        +Function(String day, String time) onSearch
        +build(context: BuildContext): Widget
    }

    class WatchLayout {
        +int _currentPage
        +PageController _pageController
        +build(context: BuildContext): Widget
    }

    class WearLoginScreen {
        +TextEditingController _controller
        +bool _isLoading
        +build(context: BuildContext): Widget
        +_handleLogin(): Future<void>
    }

    class _WearLoginScreenState {
        +build(context: BuildContext): Widget
        +_handleLogin(): Future<void>
    }

    PatientService --> Patient
    MedicationService --> Medication
    PosologyService --> Posology
    IntakeService --> Intake
    MedicationService --> Patient
    PosologyService --> Medication
    IntakeService --> Medication
    RequestHandler --> PatientService
    RequestHandler --> MedicationService
    RequestHandler --> PosologyService
    RequestHandler --> IntakeService
    RiverpodState --> Patient
    RiverpodState --> Medication
    RiverpodState --> Posology
    MainScreenApp --> PatientCodeFormatter
    MainScreenApp --> DateFormatter
    MainScreenApp --> PosologyFormatter
    MobileMainScreen --> MedicationsPage
    MobileMainScreen --> HomePage
    MobileMainScreen --> NotificationsPage
    MedicationsPage --> MedicationCard
    MedicationCard --> MedicationDetailsPage
    MainScreenApp --> MobileMainScreen
    MainScreenApp --> WearMainScreen
    MainScreenApp --> MobileMainScreen
    MedicationCard --> PosologyFormatter
    MedicationDetailsPage --> PosologyFormatter
    MedicationService --> PosologyService
    PatientService --> MedicationService
    WearMainScreen --> SearchPage
    WearMainScreen --> PatientCodePage
    WearMainScreen --> MedicationsPage
    WearMainScreen --> NotificationsPage
    SearchPage --> IntakeDialog
    WatchLayout --> SearchBarw
    WatchLayout --> NotificationCard
    WatchLayout --> MedicationCard
    WatchLayout --> IntakeDialog
    WatchLayout --> NotificationCard
    WearLoginScreen --> _WearLoginScreenState
    WearLoginScreen --> Formatter
    WearLoginScreen --> ConnectionException

