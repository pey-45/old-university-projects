#!/usr/bin/env python

from model import Model
from view import View
from presenter import Presenter

import gettext
import locale
from pathlib import Path

mytextdomain='app'
locale.setlocale(locale.LC_ALL, '')
# The i18n files should be copied to ./locale/LANGUAGE_CODE/LC_MESSAGES/
LOCALE_DIR = Path(__file__).resolve().parents[1] / "locale"
locale.bindtextdomain(mytextdomain, LOCALE_DIR)
gettext.bindtextdomain(mytextdomain, LOCALE_DIR)
gettext.textdomain(mytextdomain)


if __name__ == "__main__":
    presenter = Presenter(view=View(), model=Model())
    presenter.run(application_id="gal.udc.fic.ipm.PatientList")
