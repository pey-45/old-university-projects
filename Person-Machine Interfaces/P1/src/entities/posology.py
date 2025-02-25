from typing import Optional

from gi.repository import GObject


class Posology(GObject.GObject):
    def __init__(self, id: Optional[int], hour: int, minute: int, medication_id: int) -> None:
        super().__init__()
        self.id = id
        self.hour = hour
        self.minute = minute
        self.medication_id = medication_id