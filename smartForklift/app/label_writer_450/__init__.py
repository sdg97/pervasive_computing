#!/usr/bin/python
# -*- coding: utf-8 -*-

from flask import Blueprint


label_writer_450 = Blueprint(
    'label_writer_450', __name__, template_folder='templates', static_folder='static'
)

from . import views
