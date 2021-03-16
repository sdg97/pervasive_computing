#!/usr/bin/python
# -*- coding: utf-8 -*-

import flask
import json
import libs
import os
from . import smartForklift
from settings import *
import redis
import time

def getConfig(smartForklift_id=None):
    dirname = os.path.dirname(os.path.realpath(__file__)) + DEVICES_DESCRIPTIONS_DIR
    file = "{}/config_{}.json".format(dirname, smartForklift_id)
    f = open(file)
    config = json.load(f)
    return config

def saveConfig(config):
    dirname = os.path.dirname(os.path.realpath(__file__)) + DEVICES_DESCRIPTIONS_DIR
    file = "{}/config_{}.json".format(dirname, config['id'])
    if not os.path.exists(os.path.dirname(file)):
        try:
            os.makedirs(os.path.dirname(file))
        except OSError as exc: 
            if exc.errno != errno.EEXIST:
                raise

    with open(file, 'w') as f:
        json.dump(config, f)

def saveAction(id, action_data):
    r = REDIS_INSTANCE
    key = "smartForklift_{}_{}".format(id, time.time() * 1000)
    value = json.dumps(action_data)
    r.set(key, value)



@smartForklift.route('/', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def index():
    return "IoT Server: smart forklift service active"

@smartForklift.route('/<int:id>', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def getConfigRoute(id):
    return getConfig(id)

@smartForklift.route('/publicConfig', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def publicConfig():
    data = flask.request.get_json()
    saveConfig(data)
    return "Config Saved"
    
@smartForklift.route('/<int:id>/startUse', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def startUseSmartForklift(id):
    getConfig(id)
    action_data = {}
    action_data['action'] = "startUse"
    saveAction(id, action_data)
    return "Action saved"

@smartForklift.route('/setPlacement', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def setPlacement():
    data = flask.request.get_json()
    placement_id = data['placement_id']
    order_id = data['order_id']
    config = getConfig(placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1, dc)
    lcd_string("PICKING",LCD_LINE_2, dc)
    ledOn(config['ready_led_pin'])
    return "ok"


@smartForklift.route('/placements/<int:id>/putItHere', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def putItHere(id):
    data = flask.request.get_json()
    placement_id = id
    product_code = data['product_code']
    qty = data['qty']
    config = getConfig(placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("{}".format(product_code),LCD_LINE_1, dc)
    lcd_string("{}".format(qty),LCD_LINE_2, dc)
    ledOn(config['catch_attention_led_pin'])
    return "ok"

@smartForklift.route('/placements/<int:id>/picked', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def picked(id):
    data = flask.request.get_json()
    placement_id = id
    order_id = data['order_id']
    config = getConfig(placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1, dc)
    lcd_string("PICKING",LCD_LINE_2, dc)
    ledOff(config['catch_attention_led_pin'])
    return "ok"

@smartForklift.route('/placements/<int:id>/orderDone', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def orderDone(id):
    placement_id = id
    placement = getConfig(placement_id)
    dc = placement['display_channel']
    lcd_init(dc)
    lcd_string("READY-TO-CONNECT",LCD_LINE_1,dc)
    ledOff(placement['ready_led_pin'])
    ledOff(placement['catch_attention_led_pin'])
    return "ok"
