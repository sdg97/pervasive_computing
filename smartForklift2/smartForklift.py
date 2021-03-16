from devices import *
import os
import json
import time

last_update_time = time.time() * 1000

def getConfig():
    dirname = os.path.dirname(os.path.realpath(__file__)) 
    file = "{}/config.json".format(dirname)
    f = open(file)
    config = json.load(f)
    return config

def application():
    print('--- FETCH DATA ---')
    loop()

def loop():
    global last_update_time
    if(last_update_time - (time.time() * 1000)):
        application()
        last_update_time = time.time() * 1000
    else:
        loop()

print('--- SMART FORKLIFT RUN ---')
print('..........................')
print('-------- GET CONFIG ------')
config = getConfig()
print(config)

loop()



