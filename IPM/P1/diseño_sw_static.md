Las clases Patient, Medication y Posology son usadas por las 3 capas.

```mermaid
classDiagram
    ViewHandler <-- View
    View <-- Presenter
    Model <-- Presenter
    ModelException <-- Model
    ViewHandler <|-- Presenter


    class ViewHandler {
        +on_search_patient(code) Any
        +on_save_medication(medication, posologies_formatted) Any
        +on_delete_medication(medication_id) Any
    }

    class View {
        ViewHandler handler
        Adw.ApplicationWindow window
        Gtk.Button search_button
        Gtk.Spinner window_spinner
        Gtk.Label patient_title_label
        Gtk.ListStore patients_store
        Gtk.TreeView patients_tree_view
        Gtk.Button add_medication_button
        Gtk.Button delete_medication_button
        Adw.Window edit_medication_window
        Gtk.Spinner edit_medication_spinner
        Gtk.Label edit_medication_title_label
        list<Gtk.Entry> medication_entries
        Gtk.Button medication_save_button
        Adw.AboutWindow about
        int medication_id_for_edit
        Adw.Window current_window
        int selected_patient_id
        list<Medication> selected_medications
        list<list<Posology>> selected_posologies

        +View() None
        +on_activate(Adw.Application app) None
        +set_handler(ViewHandler handler) None
        +build_ui(Adw.Application app) None
        +build_edit_medication() None
        +build_about() None
        +set_patient(Patient patient, list<Medication> medications, list<list<Posology>> posologies) None
        +update_row(Medication medication, list<Posology> posologies) None
        +add_row(Medication medication, list<Posology> posologie) None
        +on_medication_selection_changed(Gtk.TreeSelection selection) None
        +edit_medication(model) None
        +add_medication() None
        +show_delete_medication_confirmation() None
        +validate_medication_form() None
        +show_window_spinner(bool show) None
        +show_edit_medication_spinner(bool show) None
        +set_sensitive_save_medication_button(bool show) None
        +set_sensitive_search_patient_button(bool show) None
        +set_current_window( Adw.Window window) None
        +close_edit_medication_window() None
        +show_about() None
        +show_message(str heading, str msg, str button_msg) None
        +show_error_message(str msg) None
        +get_selection_index() int
        +get_selected_patient_id() int
        +get_current_medications() list<Medication>
        +get_current_posologies() list<list<Posology>>
        +set_selected_patient_id(int patient_id) None
        +set_current_medications(list[Medication] medications) None
        +set_current_posologies(list<list<Posology>> posologies)
        +spacer() Gtk.Box
        +posologies_to_string(list<Posology> posologies) str
    }

    class Presenter {
        View view
        Model model
        + Presenter( View view, Model model) None
        + run( str application_id) None
        +on_search_patient(str code) None
        +save_new_medication( Medication medication, lis<Posology> new_posologies) None
        +save_edited_medication( Medication medication, list<Posology> new_posologies) None
        +on_save_medication(Medication medication, list<str> posologies_formatted) None
        +on_delete_medication(int medication_id) None
    }

    class Model{
        +Model()
        +request_handler( method, url, model_exception_msg, data=None) None
        +post_patient(Patient patient) Patient
        +get_patients(int start_index, int count) list<Patient>
        +get_patient_by_code(str code) Patient
        +get_patient(int patient_id) Patient
        +patch_patient(int patient_id, Patient patient) None
        +delete_patient(int patient_id) None
        +post_medication(int patient_id, Medication medication) Medication
        +get_medications(int patient_id) list<Medication>
        +get_medication(int patient_id, int medication_id) Medication
        +patch_medication(int patient_id,int medication_id, Medication medication) None
        +delete_medication(int patient_id, int medication_id) None
        +post_posology(int patient_id, int medication_id, Posology posology) Posology
        +get_posologies(int patient_id, int medication_id) list<Posology>
        +patch_posology(int patient_id, int medication_id, int posology_id, Posology posology) None
        +delete_posology(int patient_id, int medication_id, int posology_id) None
    }

    class ModelException {
        +ModelException(str msg) None
    }


    class Patient{
      int id
      str code 
      str name 
      str surname 
      +Patient(int id, str code, str name, str surname) None
    }
    class Medication{
      int id 
      str name 
      float dosage 
      str start_date 
      int treatment_duration 
      int patient_id 
      +Medication(int id, str name, float dosage, str start_date, int treatment_duration, int patient_id ) None
    }
    class Posology{
      int id 
      int hour 
      int minute 
      int medication_id 
      +Posology(int id, int hour, int minute, int medication_id ) None
    }

