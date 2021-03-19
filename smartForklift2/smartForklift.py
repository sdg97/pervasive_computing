import os
import json
import time
import requests
from actions import *

last_update_time = time.time() * 1000
update_interval = 2000
sleep_time = 0.5
restart_time_sleep = 2
config = None

def getConfig():
    dirname = os.path.dirname(os.path.realpath(__file__)) 
    file = "{}/config.json".format(dirname)
    f = open(file)
    config = json.load(f)
    return config

def publicMyself():
    global config
    print('------ PUBLIC THING DESCRIPTION ------')
    IoTServerAddr = config['SERVER_ADDRESS']
    print('DEBUG')
    payload = {"placements": []}
    for p in config['HARDWARE_SETTINGS']['placements']:
        payload['placements'].append(p['id'])
    payload['action'] = config['ACTIONS']
    payload['id'] = config['ID']
    headers = {'content-type': 'application/json'}
    r = requests.post(IoTServerAddr+'/smartForklift/publicConfig', data=json.dumps(payload), headers = headers)
    print(r)
    print(r.status_code)
    if(r.status_code != 200):
        time.sleep(sleep_time)
        publicMyself()


def application():
    global config
    print('--- FETCH DATA ---')
    r = requests.get('{}/smartForklift/{}/actions'.format(config['SERVER_ADDRESS'], config['ID']))
    if(r.status_code == 200):
        actions = r.json()
        hw_s = config['HARDWARE_SETTINGS']
        for a in actions:
            print('---- ACTION TO DO -----')
            print(a)
            if(a['action_name'] == 'startUse'):
                startUse(hw_s) 
            elif(a['action_name'] == 'setPlacement'):
                setPlacement(hw_s, a)
            elif(a['action_name'] == 'putItHere'):
                putItHere(hw_s, a)
            elif(a['action_name'] == 'picked'):
                picked(hw_s, a)
            elif(a['action_name'] == 'orderDone'):
                orderDone(hw_s, a)

        


def loop():
    global last_update_time
    if((time.time() * 1000) - last_update_time >= update_interval):
        application()
        last_update_time = time.time() * 1000
        loop()
    else:
        time.sleep(sleep_time)
        loop()

def init():
    global config
    try:
        print('--- SMART FORKLIFT RUN ---')
        print('..........................')
        print('-------- GET CONFIG ------')
        config = getConfig()
        print(config)
        publicMyself()
        loop()
    except Exception as e:
        print('Some error occurred')
        print(e)
        time.sleep(restart_time_sleep)
        init()

init()
    



