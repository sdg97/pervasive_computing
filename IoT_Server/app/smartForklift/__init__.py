#!/usr/bin/python
# -*- coding: utf-8 -*-

from flask import Blueprint


smartForklift = Blueprint(
    'smartForklift', __name__, template_folder='templates', static_folder='static'
)

from . import views
