#!/usr/bin/python
# -*- coding: utf-8 -*-

import flask
import json
import libs
import os
from devices.display import *
from devices.led import *
from . import smartForklift

def getConfig(placement_id=None):
    dirname = os.path.dirname(os.path.realpath(__file__))
    f = open(dirname + "/config.json")
    config = json.load(f)
    if(placement_id is not None):

        for e in config['placements']:
            if(e['id'] == placement_id):
                return e
    else:
        return config

@smartForklift.route('/', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def index():
    return "Smart forklift service active"

@smartForklift.route('/startUseSmartForklift', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def startUseSmartForklift():
    lcd_init()
    config = getConfig()
    lcd_string("READY-TO-CONNECT",LCD_LINE_1)
    ledOn(config['ready_led_pin'])
    for placement in config['placements']:
        ledOff(placement['ready_led_pin'])
        ledOff(placement['catch_attention_led_pin'])
    return "ok"

@smartForklift.route('/setPlacement', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def setPlacement():
    lcd_init()
    data = flask.request.get_json()
    placement_id = data['placement_id']
    order_id = data['order_id']
    config = getConfig(placement_id)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1)
    lcd_string("PICKING",LCD_LINE_2)
    ledOn(config['ready_led_pin'])
    return "ok"


@smartForklift.route('/placements/<int:id>/putItHere', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def putItHere(id):
    lcd_init()
    data = flask.request.get_json()
    placement_id = id
    product_code = data['product_code']
    qty = data['qty']
    config = getConfig(placement_id)
    lcd_string("{}".format(product_code),LCD_LINE_1)
    lcd_string("{}".format(qty),LCD_LINE_2)
    ledOn(config['catch_attention_led_pin'])
    return "ok"

@smartForklift.route('/placements/<int:id>/picked', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def picked(id):
    lcd_init()
    data = flask.request.get_json()
    placement_id = id
    order_id = data['order_id']
    config = getConfig(placement_id)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1)
    lcd_string("PICKING",LCD_LINE_2)
    ledOff(config['catch_attention_led_pin'])
    return "ok"
