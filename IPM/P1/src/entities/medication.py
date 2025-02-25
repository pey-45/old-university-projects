from typing import Optional

from gi.repository import GObject


class Medication(GObject.GObject):
    def __init__(self, id: Optional[int], name: str, dosage: float, start_date: str, treatment_duration: int, patient_id: int) -> None:
        super().__init__()
        self.id = id
        self.name = name
        self.dosage = dosage
        self.start_date = start_date
        self.treatment_duration = treatment_duration
        self.patient_id = patient_id