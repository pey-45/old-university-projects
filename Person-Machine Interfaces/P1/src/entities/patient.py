from gi.repository import GObject


class Patient(GObject.GObject):
    def __init__(self, id: int, code: int, name: int, surname: int) -> None:
        super().__init__()
        self.id = id
        self.code = code
        self.name = name
        self.surname = surname