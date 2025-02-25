
import '../models/posology.dart';

class Formatter {
  static String formatDateTodayTomorrow(DateTime date) {
    if (date.day == DateTime.now().day && date.month == DateTime.now().month) {
      return "Today ${date.hour.toString().padLeft(2, "0")}:${date.minute.toString().padLeft(2, "0")}";
    } else if (date.day == DateTime.now().add(const Duration(days: 1)).day && date.month == DateTime.now().add(const Duration(days: 1)).month) {
      return "Tomorrow ${date.hour.toString().padLeft(2, "0")}:${date.minute.toString().padLeft(2, "0")}";
    } else {
      return "${date.month.toString().padLeft(2, "0")}/${date.day.toString().padLeft(2, "0")} ${date.hour.toString().padLeft(2, "0")}:${date.minute.toString().padLeft(2, "0")}";
    }
  }

  static String formatPosologies(List<Posology> posologies) {
    List<String> formateado = [];

    for (Posology posology in posologies) {
      formateado.add('${posology.hour.toString().padLeft(2, '0')}:${posology.minute.toString().padLeft(2, '0')}');
    }

    return formateado.join(', ');
  }
}