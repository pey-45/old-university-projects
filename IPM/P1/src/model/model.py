from exceptions import ModelException
from entities import Patient, Medication, Posology

import requests

import gettext
_=gettext.gettext

SERVER_URL = "http://localhost:8000"
TIMEOUT = 10

class Model:
    def __init__(self) -> None:
        pass


    def request_handler(self, method, url, model_exception_msg, data=None):
        try:
            response = requests.request(method, url, json=data, timeout=TIMEOUT)

            if response.ok:
                return response.json() if response.content else None
            else:
                raise ModelException(model_exception_msg)
        except requests.exceptions.ConnectionError:
            raise requests.exceptions.ConnectionError(_("Connection error"))
        except requests.exceptions.Timeout:
            raise requests.exceptions.Timeout(_("Connection timed out"))
        except requests.exceptions.RequestException:
            raise requests.exceptions.RequestException(_("Something went wrong"))


    def post_patient(self, patient: Patient) -> Patient:
        url = f"{SERVER_URL}/patients"
        data = self.request_handler("POST", url, _("Failed to add patient"), patient.__dict__)
        return Patient(data["id"], data["code"], data["name"], data["surname"])


    def get_patients(self, start_index: int = None, count: int = None) -> list[Patient]:
        url = f"{SERVER_URL}/patients"
        if start_index is not None or count is not None:
            url += "?"
        if start_index is not None:
            url += f"start_index={start_index}&"
        if count is not None:
            url += f"count={count}"
        data = self.request_handler("GET", url, _("Patients not found"))
        return [Patient(item["id"], item["code"], item["name"], item["surname"]) for item in data]


    def get_patient_by_code(self, code: str) -> Patient:
        url = f"{SERVER_URL}/patients?code={code}"
        data = self.request_handler("GET", url, _("Patient not found"))
        return Patient(data["id"], data["code"], data["name"], data["surname"])


    def get_patient(self, patient_id: int) -> Patient:
        url = f"{SERVER_URL}/patients/{patient_id}"
        data = self.request_handler("GET", url, _("Patient not found"))
        return Patient(data["id"], data["code"], data["name"], data["surname"])


    def patch_patient(self, patient_id: int, patient: Patient) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}"
        self.request_handler("PATCH", url, _("Failed to update patient"), patient.__dict__)


    def delete_patient(self, patient_id: int) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}"
        self.request_handler("DELETE", _("Failed to delete patient"), url)


    def post_medication(self, patient_id: int, medication: Medication) -> Medication:
        url = f"{SERVER_URL}/patients/{patient_id}/medications"
        data = self.request_handler("POST", url, _("Failed to add medication"), medication.__dict__)
        return Medication(data["id"], data["name"], data["dosage"], data["start_date"], data["treatment_duration"], data["patient_id"])


    def get_medications(self, patient_id: int) -> list[Medication]:
        url = f"{SERVER_URL}/patients/{patient_id}/medications"
        data = self.request_handler("GET", url, _("Medications not found"))
        return [Medication(item["id"], item["name"], item["dosage"], item["start_date"], item["treatment_duration"], item["patient_id"]) for item in data]


    def get_medication(self, patient_id: int, medication_id: int) -> Medication:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}"
        data = self.request_handler("GET", url, _("Medications not found"))
        return Medication(data["id"], data["name"], data["dosage"], data["start_date"], data["treatment_duration"], data["patient_id"])


    def patch_medication(self, patient_id: int, medication_id: int, medication: Medication) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}"
        self.request_handler("PATCH", url, _("Failed to update medication"), medication.__dict__)


    def delete_medication(self, patient_id: int, medication_id: int) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}"
        self.request_handler("DELETE", url, _("Failed to delete medication"))


    def post_posology(self, patient_id: int, medication_id: int, posology: Posology) -> Posology:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}/posologies"
        data = self.request_handler("POST", url, _("Failed to add posology"), posology.__dict__)
        return Posology(data["id"], data["hour"], data["minute"], data["medication_id"])


    def get_posologies(self, patient_id: int, medication_id: int) -> list[Posology]:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}/posologies"
        data = self.request_handler("GET", url, _("Posologies not found"))
        return [Posology(item["id"], item["hour"], item["minute"], item["medication_id"]) for item in data]


    def patch_posology(self, patient_id: int, medication_id: int, posology_id: int, posology: Posology) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}/posologies/{posology_id}"
        self.request_handler("PATCH", url, _("Failed to update posology"), posology.__dict__)


    def delete_posology(self, patient_id: int, medication_id: int, posology_id: int) -> None:
        url = f"{SERVER_URL}/patients/{patient_id}/medications/{medication_id}/posologies/{posology_id}"
        self.request_handler("DELETE", url, _("Failed to delete posology"))
