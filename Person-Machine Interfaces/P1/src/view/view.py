from datetime import date
from typing import Callable

from gi.overrides import GLib

from .view_handler import ViewHandler
from entities import Patient, Medication, Posology
from constants import Constants
constants = Constants()

import gi, re

gi.require_version('Gtk', '4.0')
gi.require_version('Adw', '1')

from gi.repository import Gtk, Gio, Adw

import gettext
_=gettext.gettext

run_on_main_thread = GLib.idle_add

MED_NAME = 0
DOSAGE = 1
STARTDATE = 2
DURATION = 3
POSOLOGIES = 4
MEDICATION_ENTRIES = 5

CODE_REGEX = r"^\s*\d{3}-\d{2}-\d{4}\s*$"
STARTDATE_REGEX = r"^\s*(\d{2})-(\d{2})-(\d{4})\s*$"
POSOLOGIES_REGEX = "^\s*((([01]?\d|2[0-3]):[0-5]\d)(,\s*([01]?\d|2[0-3]):[0-5]\d)*)?\s*$"

def run(application_id: str, on_activate: Callable) -> None:
    app = Adw.Application(application_id=application_id)
    app.connect("activate", on_activate)
    app.run()


class View:
    def __init__(self) -> None:
        pass

    def on_activate(self, app: Adw.Application) -> None:
        self.build_ui(app)
        self.build_edit_medication()
        self.build_about()

    def set_handler(self, handler: ViewHandler) -> None:
        self.handler = handler


    ### BUILD MAIN PAGE ###

    def build_ui(self, app: Adw.Application) -> None:
        self.window = Adw.ApplicationWindow(title=_("Medical Center"))
        self.window.set_default_size(1280, 720)

        self.set_current_window(self.window)
        app.add_window(self.window)
        self.window.connect("destroy", lambda win: win.close())

        # PÁGINA PRINCIPAL

        # HEADER

        search_bar = Gtk.Entry()
        search_bar.set_placeholder_text("XXX-XX-XXXX")

        self.search_button = Gtk.Button.new_with_label(_("Search"))
        self.search_button.connect("clicked", lambda _: self.handler.on_search_patient(search_bar.get_text()))

        self.window_spinner = Gtk.Spinner(margin_start=8)

        about_action = Gio.SimpleAction.new("about", None)
        about_action.connect("activate", lambda a, b: self.show_about())
        self.window.add_action(about_action)

        menu = Gio.Menu.new()
        menu.append(_("About"), "win.about")

        popover = Gtk.PopoverMenu()
        popover.set_menu_model(menu)

        hamburger = Gtk.MenuButton()
        hamburger.set_popover(popover)
        hamburger.set_icon_name("open-menu-symbolic")

        header = Adw.HeaderBar(hexpand=True)
        header.pack_start(search_bar)
        header.pack_start(self.search_button)
        header.pack_start(self.window_spinner)
        header.pack_end(hamburger)

        # CONTENIDO

        self.patient_title_label = Gtk.Label(label=_("Search a patient"))
        self.patient_title_label.add_css_class("title-1")
        title_box = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, homogeneous=False, spacing=16, halign=Gtk.Align.CENTER, margin_top=16)
        title_box.append(self.patient_title_label)

        self.patients_store = Gtk.ListStore(str, str, str, str, str)
        self.patients_tree_view = Gtk.TreeView(model=self.patients_store)
        self.patients_tree_view.connect("row-activated", lambda a, b, c: self.edit_medication())

        selection = self.patients_tree_view.get_selection()
        selection.connect("changed", self.on_medication_selection_changed)

        renderer = Gtk.CellRendererText()
        renderer.set_property("xpad", 25)
        renderer.set_property("xalign", 0.0)

        columns = [Gtk.TreeViewColumn(_("\tMedication"), renderer, text=0), Gtk.TreeViewColumn(_("\tDosage"), renderer, text=1), Gtk.TreeViewColumn(_("\tStart date"), renderer, text=2),
                        Gtk.TreeViewColumn(_("\tDuration"), renderer, text=3), Gtk.TreeViewColumn(_("\tPosology\t"), renderer, text=4)]
        for column in columns:
            column.set_resizable(True)
            column.set_min_width(10)
            self.patients_tree_view.append_column(column)

        self.patients_tree_view.set_visible(False)

        self.add_medication_button = Gtk.Button.new_with_label(_("Add medication"))
        self.add_medication_button.set_visible(False)
        self.add_medication_button.connect("clicked", lambda _: self.add_medication())
        self.add_medication_button.set_visible(False)

        self.delete_medication_button = Gtk.Button.new_with_label(_("Delete medication"))
        self.delete_medication_button.set_visible(False)
        self.delete_medication_button.connect("clicked", lambda _: self.show_delete_medication_confirmation())
        self.delete_medication_button.set_visible(False)
        self.delete_medication_button.set_sensitive(False)  # Initially disabled

        medication_list_box = Gtk.Box(margin_top=16)
        medication_list_box.append(self.patients_tree_view)

        medication_buttons_box = Gtk.Box(spacing=16, margin_top=8)
        medication_buttons_box.append(self.add_medication_button)
        medication_buttons_box.append(self.delete_medication_button)

        medications_box = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, homogeneous=False, vexpand=False, hexpand=False, spacing=16, margin_top=16, margin_start=16, margin_bottom=16, margin_end=16, halign=Gtk.Align.CENTER)
        medications_box.append(title_box)
        medications_box.append(medication_list_box)
        medications_box.append(medication_buttons_box)

        medications_box.set_size_request(300, -1)

        home_page = Gtk.Box(orientation=Gtk.Orientation.VERTICAL)
        home_page.append(header)
        home_page.append(medications_box)

        self.window.set_content(home_page)
        self.window.present()

        ### DEBUG ###
        #self.handler.on_search_patient("871-48-7999")




    ### BUILD EDIT MEDICATION PAGE ###

    def build_edit_medication(self) -> None:
        self.edit_medication_window = Adw.Window(title=_("Medical Center"))
        self.edit_medication_window.connect("close-request", lambda _: self.close_edit_medication_window())
        self.edit_medication_window.set_transient_for(self.window)
        self.edit_medication_window.set_modal(True)
        self.edit_medication_window.set_default_size(480, 560)
        self.edit_medication_window.set_resizable(False)

        header_edit_medication = Adw.HeaderBar()

        self.edit_medication_spinner = Gtk.Spinner(margin_start=8)
        header_edit_medication.pack_start(self.edit_medication_spinner)

        self.edit_medication_title_label = Gtk.Label(halign=Gtk.Align.CENTER)
        self.edit_medication_title_label.add_css_class("title-1")
        self.edit_medication_title_label.set_margin_top(16)

        # 0: name, 1: dosage, 2: start_date, 3: duration, 4: posologies
        self.medication_entries = [Gtk.Entry() for _ in range(5)]

        entry_titles = [Gtk.Label(label=_("Name:")), Gtk.Label(label=_("Dosage:")), Gtk.Label(label=_("Start date:")), Gtk.Label(label=_("Duration:")), Gtk.Label(label=_("Posology:"))]

        self.medication_entries[DOSAGE].set_placeholder_text("mg/ml")
        self.medication_entries[STARTDATE].set_placeholder_text(_("MM-DD-YYYY"))
        self.medication_entries[DURATION].set_placeholder_text(_("days"))
        self.medication_entries[POSOLOGIES].set_placeholder_text("hh:mm, hh:mm, ...")

        entry_boxes = []
        for i in range(MEDICATION_ENTRIES):
            entry_boxes.append(Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=8, margin_top=32, hexpand=True))
            entry_boxes[i].append(entry_titles[i])
            entry_boxes[i].append(self.spacer())
            entry_boxes[i].append(self.medication_entries[i])

        self.medication_save_button = Gtk.Button(label=_("   Save   "))
        discard_button = Gtk.Button(label=_("Discard"))

        self.medication_save_button.connect("clicked", lambda _: self.validate_medication_form())
                                            #Medication(self.medication_id_for_edit, self.medication_entries[MED_NAME].get_text(), float(self.medication_entries[DOSAGE].get_text()),
                                            #           self.medication_entries[STARTDATE].get_text(), int(self.medication_entries[DURATION].get_text()), int(self.selected_patient_id)),
                                            #self.medication_entries[POSOLOGIES].get_text().split(",")))
        discard_button.connect("clicked", lambda _: self.close_edit_medication_window())

        self.medication_save_button.set_hexpand(True)
        discard_button.set_hexpand(True)

        save_button_box = Gtk.Box()
        discard_button_box = Gtk.Box()

        save_button_box.append(self.medication_save_button)
        discard_button_box.append(discard_button)

        save_discard_box = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, margin_top=32, spacing=0)
        save_discard_box.append(discard_button_box)
        save_discard_box.append(self.spacer())
        save_discard_box.append(save_button_box)

        form = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, margin_top=16, margin_start=16, margin_bottom=16, margin_end=16, hexpand=True)
        form.append(self.edit_medication_title_label)
        for i in range(MEDICATION_ENTRIES):
            form.append(entry_boxes[i])
        form.append(save_discard_box)
        form.set_halign(Gtk.Align.CENTER)

        main_page = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, hexpand=True)
        main_page.append(header_edit_medication)
        main_page.append(form)

        self.edit_medication_window.set_content(main_page)


    ### BUILD AND SHOW ABOUT PAGE ###

    def build_about(self) -> None:
        self.about = Adw.AboutWindow()
        self.about.set_title(_("About"))
        self.about.set_modal(False)
        self.about.connect("close-request", lambda win: win.hide())

        self.about.set_application_name(_("Medication Center App"))
        self.about.set_developers(["Pablo Manzanares López", "Hugo Duarte García Liñares", "Hugo Yoel López Rodeiro", "Alexandre Fernández Fernández"])
        self.about.set_designers(["Pablo Manzanares López", "Hugo Duarte García Liñares", "Hugo Yoel López Rodeiro", "Alexandre Fernández Fernández"])
        self.about.set_license_type(Gtk.License.GPL_3_0)
        self.about.set_website("https://github.com/nbarreira")
        self.about.set_version("1.0.0")
        self.about.set_copyright("© 2024 Noelia Barreira")


    ### ACTIONS WHEN A PATIENT IS (SUCCESSFULLY) SEARCHED ###

    def set_patient(self, patient: Patient, medications: list[Medication], posologies: list[list[Posology]]) -> None:
        self.set_selected_patient_id(patient.id)
        self.patient_title_label.set_text(f"{patient.name} {patient.surname} ({patient.code})")

        current_medications = []
        current_posologies = []

        self.patients_store.clear()
        for i, medication in enumerate(medications):
            posologies_string = self.posologies_to_string(posologies[i])
            self.patients_store.append([medication.name, str(medication.dosage), medication.start_date, str(medication.treatment_duration), posologies_string])

            current_medications.append(medication)
            current_posologies.append(posologies[i])

        self.set_current_medications(current_medications)
        self.set_current_posologies(current_posologies)

        self.patients_tree_view.set_visible(True)
        self.add_medication_button.set_visible(True)
        self.delete_medication_button.set_visible(True)
        self.delete_medication_button.set_sensitive(False)



    ### UPDATES A ROW OF THE TREE VIEW ###

    def update_row(self, medication, posologies) -> None:
        posologies_string = self.posologies_to_string(posologies)

        model, treeiter = self.get_selection().get_selected()
        model.set(treeiter, 0, medication.name, 1, str(medication.dosage), 2, medication.start_date, 3, str(medication.treatment_duration), 4, posologies_string)

        index = self.get_selection_index()

        current_medications = self.get_current_medications()
        current_posologies = self.get_current_posologies()

        current_medications[index] = medication
        current_posologies[index] = posologies

        self.set_current_medications(current_medications)
        self.set_current_posologies(current_posologies)


    ### ADDS A ROW TO THE TREE VIEW ###

    def add_row(self, medication: Medication, posologies: list[Posology]) -> None:
        posologies_string = self.posologies_to_string(posologies)

        self.patients_store.append([medication.name, str(medication.dosage), medication.start_date, str(medication.treatment_duration), posologies_string])

        current_medications = self.get_current_medications()
        current_posologies = self.get_current_posologies()

        current_medications.append(medication)
        current_posologies.append(posologies)

        self.set_current_medications(current_medications)
        self.set_current_posologies(current_posologies)


    ### ACTIONS WHEN A MEDICATION IS SINGLE CLICKED ###

    def on_medication_selection_changed(self, selection):
        _, treeiter = selection.get_selected()
        self.delete_medication_button.set_sensitive(False if treeiter is None else True)


    ### ACTIONS WHEN A MEDICATION IS DOUBLE CLICKED ###

    def edit_medication(self) -> None:
        index = self.get_selection_index()
        medication = self.get_current_medications()[index]
        posologies = self.get_current_posologies()[index]

        self.medication_id_for_edit = medication.id
        self.edit_medication_title_label.set_text(_("Edit medication"))
        self.medication_entries[MED_NAME].set_text(medication.name)
        self.medication_entries[DOSAGE].set_text(str(medication.dosage))
        self.medication_entries[STARTDATE].set_text(medication.start_date)
        self.medication_entries[DURATION].set_text(str(medication.treatment_duration))
        self.medication_entries[POSOLOGIES].set_text(self.posologies_to_string(posologies))
        self.medication_entries[MED_NAME].grab_focus()
        self.medication_entries[MED_NAME].set_position(-1)

        self.edit_medication_window.present()
        self.set_current_window(self.edit_medication_window)


    ### ACTIONS WHEN ADD MEDICATION BUTTON IS CLICKED ###

    def add_medication(self):
        self.add_medication_button.set_sensitive(False)
        self.medication_id_for_edit = None
        self.edit_medication_title_label.set_text(_("Add medication"))
        for entry in self.medication_entries:
            entry.set_text("")
        self.medication_entries[MED_NAME].grab_focus()
        self.medication_entries[MED_NAME].set_position(-1)

        self.edit_medication_window.present()
        self.set_current_window(self.edit_medication_window)
        self.add_medication_button.set_sensitive(True)


    ### ACTIONS WHEN DELETE MEDICATION BUTTON IS CLICKED ###

    def show_delete_medication_confirmation(self) -> None:
        model, treeiter = self.get_selection().get_selected()
        index = self.get_selection_index()

        selected_medication = self.get_current_medications()[index]

        dialog = Adw.MessageDialog(
            heading=_("Confirm Deletion"),
            body=_("Are you sure you want to delete the medication ")+ f'{selected_medication.name}'+"?",
            transient_for=self.window
        )
        dialog.add_response("cancel", _("Cancel"))
        dialog.add_response("delete", _("Delete"))
        dialog.set_default_response("cancel")
        dialog.set_close_response("cancel")
        def on_delete_medication_response(d, response):
            if response == "delete":
                self.handler.on_delete_medication(selected_medication.id)
                model.remove(treeiter)
                self.get_selection().unselect_all()
                self.delete_medication_button.set_sensitive(False)
            d.destroy()
        dialog.connect("response", on_delete_medication_response)
        dialog.present()




    ### MEDICATION FORM VALIDATION ###

    def validate_medication_form(self) -> None:
        def is_empty_medication_name(medication_name: str) -> bool:
            return medication_name.strip() == ""

        def is_not_valid_medication_dosage(medication_dosage: str) -> bool:
            if medication_dosage.strip() == "":
                return False
            try:
                return float(medication_dosage) < 0
            except ValueError:
                return True

        def is_empty_medication_startdate(medication_startdate: str) -> bool:
            return medication_startdate.strip() == ""

        def is_not_valid_medication_startdate(medication_startdate: str) -> bool:
            if re.fullmatch(STARTDATE_REGEX, medication_startdate) is None:
                return True
            try:
                if (_("__lang__") in constants.MM_DD_DATES):
                    m, d, y = map(int, medication_startdate.strip().split('-'))
                elif (_("__lang__") in constants.DD_MM_DATES):
                    d, m, y = map(int, medication_startdate.strip().split('-'))
                else:
                    y, m, d = map(int, medication_startdate.strip().split('-'))

                date(y, m, d)
                return False
            except ValueError:
                return True

        def is_not_valid_medication_treatment_duration(medication_treatment_duration: str) -> bool:
            if medication_treatment_duration.strip() == "":
                return False
            return not medication_treatment_duration.isdigit() or int(medication_treatment_duration) < 0

        def is_not_valid_medication_posologies(posologies: str) -> bool:
            return re.fullmatch(POSOLOGIES_REGEX, posologies) is None

        if is_empty_medication_name(self.medication_entries[MED_NAME].get_text()):
            self.show_error_message(_("Medication name is mandatory"));return
        elif is_not_valid_medication_dosage(self.medication_entries[DOSAGE].get_text()):
            self.show_error_message(_("Medication dosage must be a positive integer or decimal"));return
        elif is_empty_medication_startdate(self.medication_entries[STARTDATE].get_text()):
            self.show_error_message(_("Medication start date is mandatory"));return
        elif is_not_valid_medication_startdate(self.medication_entries[STARTDATE].get_text()):
            self.show_error_message(_("Medication start date has an invalid format:\nexpected 'MM-DD-YYYY'"));return
        elif is_not_valid_medication_treatment_duration(self.medication_entries[DURATION].get_text()):
            self.show_error_message(_("Medication dosage must be a positive integer"));return
        elif is_not_valid_medication_posologies(self.medication_entries[POSOLOGIES].get_text()):
            self.show_error_message(_("Medication posologies has an invalid format:\nexpected 'hh:mm, hh:mm, ...'"));return

        name = self.medication_entries[MED_NAME].get_text().strip()
        dosage = float(self.medication_entries[DOSAGE].get_text()) if not self.medication_entries[DOSAGE].get_text().strip() == "" else 0
        start_date = self.medication_entries[STARTDATE].get_text().strip()
        duration = int(self.medication_entries[DURATION].get_text()) if not self.medication_entries[DURATION].get_text().strip() == "" else 0

        self.handler.on_save_medication(Medication(self.medication_id_for_edit, name, dosage, start_date, duration, self.get_selected_patient_id()),
                                                    self.medication_entries[POSOLOGIES].get_text().split(","))


    ### AUXILIAR VIEW FUNCTIONS ###

    def show_window_spinner(self, show: bool) -> None:
        if show:
            self.window_spinner.start()
        else:
            self.window_spinner.stop()

    def show_edit_medication_spinner(self, show: bool) -> None:
        if show:
            self.edit_medication_spinner.start()
        else:
            self.edit_medication_spinner.stop()

    def set_sensitive_save_medication_button(self, show: bool) -> None:
        self.medication_save_button.set_sensitive(show)

    def set_sensitive_search_patient_button(self, show: bool) -> None:
        self.search_button.set_sensitive(show)

    def set_current_window(self, window: Adw.Window) -> None:
        self.current_window = window

    def close_edit_medication_window(self) -> None:
        self.edit_medication_window.hide()
        self.edit_medication_window.destroy()
        self.window.present()
        self.set_current_window(self.window)

    def show_about(self) -> None:
        self.about.present()

    def show_message(self, heading: str, msg: str, button_msg: str) -> None:
        dialog = Adw.MessageDialog(heading=heading, body=msg, transient_for=self.current_window)
        dialog.add_response("id-response-1", button_msg)
        dialog.set_default_response(_("Close"))
        dialog.show()

    def show_error_message(self, msg: str) -> None:
        self.show_message(_("Error"), msg, _("Ok"))

    def get_selection(self) -> Gtk.TreeSelection:
        return self.patients_tree_view.get_selection()

    def get_selection_index(self) -> int:
        model, treeiter = self.get_selection().get_selected()
        if treeiter is None: return -1
        path = model.get_path(treeiter)
        return path.get_indices()[0]

    def get_selected_patient_id(self) -> int:
        return self.selected_patient_id
    def get_current_medications(self) -> list[Medication]:
        return self.selected_medications if self.selected_medications else []
    def get_current_posologies(self) -> list[list[Posology]]:
        return self.selected_posologies if self.selected_posologies else []

    def set_selected_patient_id(self, patient_id: int) -> None:
        self.selected_patient_id = patient_id
    def set_current_medications(self, medications: list[Medication]) -> None:
        self.selected_medications = medications
    def set_current_posologies(self, posologies: list[list[Posology]]) -> None:
        self.selected_posologies = posologies

    def spacer(self) -> Gtk.Box:
        return Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, hexpand=True)

    def posologies_to_string(self, posologies: list[Posology]) -> str:
        posologies_formatted = [f"{item.hour:02}:{item.minute:02}" for item in posologies]
        posologies_string = ""
        for j, posology in enumerate(posologies_formatted):
            posologies_string += posology
            if j < len(posologies_formatted) - 1:
                posologies_string += ", "

        return posologies_string
