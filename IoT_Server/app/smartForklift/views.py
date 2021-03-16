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

def getActions(id):
    r = REDIS_INSTANCE
    keys = r.keys(pattern='smartForklift_{}_*'.format(id))
    res = []
    for k in keys:
        res.append(r.get(k))
    return res

def saveAction(id, action_name, action_data={}, placement_id=None):
    action_data['action_name'] = action_name 
    if(placement_id is not None):
        action_data['placement_id'] = placement_id  
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

@smartForklift.route('/<int:id>/actions', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def getActionsRoute(id):
    return flask.jsonify(getActions(id))

@smartForklift.route('/publicConfig', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def publicConfig():
    data = flask.request.get_json()
    saveConfig(data)
    return "Config Saved"
    
@smartForklift.route('/<int:id>/action/<string:action_name>', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def smartForkliftAction(id, action_name):
    getConfig(id)
    ## Controllo se l'azione è concessa
    if flask.request.get_json() is not None:
        action_data = flask.request.get_json()
        saveAction(id, action_name, action_data)
    else:
        saveAction(id, action_name)
    return "Action saved"


@smartForklift.route('/<int:s_id>/placements/<int:p_id>/<string:action_name>', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def placementsAction(s_id, p_id, action_name):
    getConfig(s_id)
    ## Controllo se l'azione è concessa
    if flask.request.get_json() is not None:
        action_data = flask.request.get_json()
        saveAction(s_id, action_name, action_data, p_id)
    else:
        saveAction(s_id, action_name, placement_id=p_id)
    return "Action saved"

