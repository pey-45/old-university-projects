from typing import Protocol, Any

class ViewHandler(Protocol):
    def on_search_patient(self, code) -> Any: pass
    def on_save_medication(self, medication, posologies_formatted) -> Any: pass
    def on_delete_medication(self, medication_id) ->  Any: pass
