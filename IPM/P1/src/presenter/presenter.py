from datetime import datetime
from threading import Thread

from entities import Medication, Posology
from model import Model
from view import View, ViewHandler, run_on_main_thread, run
from constants import Constants
constants = Constants()

import gettext
_=gettext.gettext

MAX_THREADS = 8

class Presenter(ViewHandler):
    def __init__(self, view: View, model: Model) -> None:
        self.view = view
        self.model = model


    def run(self, application_id: str) -> None:
        self.view.set_handler(self)
        run(application_id, on_activate=self.view.on_activate)


    def on_search_patient(self, code: str) -> None:
        if code.strip() == "":
            run_on_main_thread(self.view.show_error_message, _("Specify a patient code"))
            return
 
        def do() -> None:
            try:
                run_on_main_thread(self.view.show_window_spinner, True)
                run_on_main_thread(self.view.set_sensitive_search_patient_button, False)
                patient = self.model.get_patient_by_code(code)
                medications = self.model.get_medications(patient.id)
                posologies = [self.model.get_posologies(patient.id, item.id) for item in medications]

                for medication in medications:
                    medication.start_date = self.to_view_date(medication.start_date)
                        
                run_on_main_thread(self.view.set_current_medications, medications)
                run_on_main_thread(self.view.set_current_posologies, posologies)
                run_on_main_thread(self.view.set_patient, patient, medications, posologies)
                run_on_main_thread(self.view.show_window_spinner, False)
                run_on_main_thread(self.view.set_sensitive_search_patient_button, True)
            except Exception as e:
                run_on_main_thread(self.view.show_error_message, str(e))
                run_on_main_thread(self.view.show_window_spinner, False)
                run_on_main_thread(self.view.set_sensitive_search_patient_button, True)

        Thread(target=do).start()


    def save_new_medication(self, medication: Medication, new_posologies: list[Posology]) -> None:
        medication = self.model.post_medication(medication.patient_id, medication)

        # Additions
        for i, posology_formatted in enumerate(new_posologies):
            new_posologies[i].id = self.model.post_posology(medication.patient_id, medication.id, new_posologies[i]).id

        medication.start_date = self.to_view_date(medication.start_date)

        run_on_main_thread(self.view.add_row, medication, new_posologies)


    def save_edited_medication(self, medication: Medication, new_posologies: list[Posology]) -> None:
        index = self.view.get_selection_index()

        current_medication = self.view.get_current_medications()[index]
        if current_medication.__dict__ != medication.__dict__:
            self.model.patch_medication(medication.patient_id, medication.id, medication)

        current_posologies = self.view.get_current_posologies()[index]

        # Updates
        for i in range(min(len(new_posologies), len(current_posologies))):
            new_posologies[i].id = current_posologies[i].id
            if new_posologies[i].__dict__ != current_posologies[i].__dict__:
                self.model.patch_posology(medication.patient_id, medication.id, new_posologies[i].id, new_posologies[i])

        # Deletions
        if len(new_posologies) < len(current_posologies):
            for i in range(len(new_posologies), len(current_posologies)):
                self.model.delete_posology(medication.patient_id, medication.id, current_posologies[i].id)

        # Additions
        if len(new_posologies) > len(current_posologies):
            for i in range(len(current_posologies), len(new_posologies)):
                new_posologies[i].id = self.model.post_posology(medication.patient_id, medication.id, new_posologies[i]).id

        medication.start_date = self.to_view_date(medication.start_date)

        run_on_main_thread(self.view.update_row, medication, new_posologies)


    def on_save_medication(self, medication: Medication, posologies_formatted: list[str]) -> None:
        if posologies_formatted and posologies_formatted[0].strip() == "":
            posologies_formatted = []
        new_posologies = [Posology(None, int(item.split(":")[0]), int(item.split(":")[1]), medication.id) for item in posologies_formatted]

        def do() -> None:
            try:
                run_on_main_thread(self.view.show_edit_medication_spinner, True)
                run_on_main_thread(self.view.set_sensitive_save_medication_button, False)

                medication.start_date = self.to_model_date(medication.start_date)

                if medication.id is None: # New
                    self.save_new_medication(medication, new_posologies)
                else: # Edit
                    self.save_edited_medication(medication, new_posologies)
                run_on_main_thread(self.view.close_edit_medication_window)
                run_on_main_thread(self.view.show_edit_medication_spinner, False)
                run_on_main_thread(self.view.set_sensitive_save_medication_button, True)
            except Exception as e:
                run_on_main_thread(self.view.show_error_message, str(e))
                run_on_main_thread(self.view.show_edit_medication_spinner, False)
                run_on_main_thread(self.view.set_sensitive_save_medication_button, True)

        Thread(target=do).start()


    def on_delete_medication(self, medication_id: int) -> None:
        def do() -> None:
            try:
                patient_id = self.view.get_selected_patient_id()
                self.model.delete_medication(patient_id, medication_id)
            except Exception as e:
                run_on_main_thread(self.view.show_error_message, str(e))

        Thread(target=do).start()


    def to_view_date(self, date: str):
        if _("__lang__") in constants.MM_DD_DATES:
            return datetime.strptime(date.strip(), '%Y-%m-%d').strftime('%m-%d-%Y')
        elif _("__lang__") in constants.DD_MM_DATES:
            return datetime.strptime(date.strip(), '%Y-%m-%d').strftime('%d-%m-%Y')
        else:
            return date

    def to_model_date(self, date: str):
        if _("__lang__") in constants.MM_DD_DATES:
            return datetime.strptime(date.strip(), '%m-%d-%Y').strftime('%Y-%m-%d')
        elif _("__lang__") in constants.DD_MM_DATES:
            return datetime.strptime(date.strip(), '%d-%m-%Y').strftime('%Y-%m-%d')
        else:
            return date
