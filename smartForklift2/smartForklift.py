from devices import *
import os
import json
import time

last_update_time = time.time() * 1000
update_interval = 2000
sleep_time = 0.5

def getConfig():
    dirname = os.path.dirname(os.path.realpath(__file__)) 
    file = "{}/config.json".format(dirname)
    f = open(file)
    config = json.load(f)
    return config

def application():
    print('--- FETCH DATA ---')

def loop():
    global last_update_time
    if((time.time() * 1000) - last_update_time >= update_interval):
        application()
        last_update_time = time.time() * 1000
        loop()
    else:
        time.sleep(sleep_time)
        loop()

print('--- SMART FORKLIFT RUN ---')
print('..........................')
print('-------- GET CONFIG ------')
config = getConfig()
print(config)

loop()



