```mermaid
sequenceDiagram
  actor User as User
  participant View as View
  participant Presenter as Presenter
  participant Model as Model

  Note over User, Model: Search Patient (with error handling)
  User ->> View: Enter patient code
  User ->> View: Click "Search"
  View ->>+ Presenter: on_search_patient(code)
  Note over Presenter: Start new thread
  Presenter ->>+ Model: get_patient_by_code(code)
  alt Success
    Model -->> Presenter: Return patient
    Presenter ->> Model: get_medications(patient_id)
    alt Success
      Model -->> Presenter: Return medications
      par For each medication (fetch posologies)
        Presenter ->> Model: get_posologies(patient_id, medication_id)
        alt Success
          Model -->> Presenter: Return posologies
        else Error
          Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
          Presenter ->> View: show_error_message("Error fetching posologies")
          View -->> User: Display error message
        end
      end
      Presenter ->> View: set_patient(patient, medications, posologies)
      View -->> User: Display patient and medications
    else Error
      Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
      Presenter ->> View: show_error_message("Error fetching medications")
      View -->> User: Display error message
    end
  else Error
    Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
    Presenter ->> View: show_error_message("Error searching patient")
    View -->> User: Display error message
  end
  Note over Presenter: End thread
  Note over User, Model: Add Medication (with error handling)
  User ->> View: Click "Add medication"
  View ->> Presenter: add_medication()
  Presenter ->> View: show_edit_medication_window()
  View ->> User: Show medication form
  User ->> View: Fill medication form
  User ->> View: Click "Save"
  View ->>+ Presenter: on_save_medication(medication, posologies)
  Note over Presenter: Start new thread
  Presenter ->>+ Model: post_medication(patient_id, medication)
  alt Success
    Model -->> Presenter: Return new medication
    loop For each posology
      Presenter ->> Model: post_posology(patient_id, medication_id, posology)
      alt Success
        Model -->> Presenter: Return new posology
      else Error
        Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
        Presenter ->> View: show_error_message("Error saving posology")
        View -->> User: Display error message
      end
    end
    Presenter ->> View: add_row(medication, new_posologies)
    View -->> User: Update medication list
  else Error
    Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
    Presenter ->> View: show_error_message("Error saving medication")
    View -->> User: Display error message
  end
  Note over Presenter: End thread
  Note over User, Model: Edit Medication (with error handling)
  User ->> View: Double-click on medication
  View ->> Presenter: edit_medication()
  Presenter ->> View: show_edit_medication_window()
  View -->> User: Display medication details
  User ->> View: Modify medication form
  User ->> View: Click "Save"
  View ->>+ Presenter: on_save_medication(medication, posologies)
  Note over Presenter: Start new thread
  par Medication update
    Presenter ->>+ Model: patch_medication(patient_id, medication_id, medication)
    alt Success
      Model -->> Presenter: Confirm update
    else Error
      Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
      Presenter ->> View: show_error_message("Error updating medication")
      View -->> User: Display error message
    end
  and Posologies update
    loop For each posology
      alt New posology
        Presenter ->> Model: post_posology(patient_id, medication_id, posology)
      else Existing posology
        Presenter ->> Model: patch_posology(patient_id, medication_id, posology_id, posology)
      else Deleted posology
        Presenter ->> Model: delete_posology(patient_id, medication_id, posology_id)
      end
      alt Success
        Model -->> Presenter: Confirm update
      else Error
        Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
        Presenter ->> View: show_error_message("Error updating posology")
        View -->> User: Display error message
      end
    end
  end
  Presenter ->> View: update_row(medication, updated_posologies)
  View -->> User: Update medication list
  Note over Presenter: End thread
  Note over User, Model: Delete Medication (with error handling)
  User ->> View: Select medication
  User ->> View: Click "Delete medication"
  View ->> View: show_delete_medication_confirmation()
  View -->> User: Display confirmation dialog
  User ->> View: Confirm deletion
  View ->>+ Presenter: on_delete_medication(medication_id)
  Note over Presenter: Start new thread
  Presenter ->>+ Model: delete_medication(patient_id, medication_id)
  alt Success
    Model -->> Presenter: Confirm deletion
    Presenter ->> View: Remove medication from list
    View -->> User: Update medication list
  else Error
    Model -->> Presenter: Throw ModelException (e.g., connection, timeout)
    Presenter ->> View: show_error_message("Error deleting medication")
    View -->> User: Display error message
  end
  Note over Presenter: End thread
