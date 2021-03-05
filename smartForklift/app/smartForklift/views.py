#!/usr/bin/python
# -*- coding: utf-8 -*-

import flask
import json
import libs
import devices.display



from . import smartForklift

@smartForklift.route('/', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def index():
    return "smartForklift"

@smartForklift.route('/startUseSmartForklift', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def startUseSmartForklift():
    print("start to use smart config")
    ##read the config
    lcd_string("SMARTFORKLIFT READY",LCD_LINE_1)
    lcd_string("-***  **  ***-",LCD_LINE_2)
    return 200
