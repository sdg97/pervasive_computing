#!/usr/bin/python
# -*- coding: utf-8 -*-

import flask
import json
import libs



from . import smartForklift

@smartForklift.route('/', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def index():
    return "smartForklift"

