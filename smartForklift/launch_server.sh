#!/bin/bash

# this launch script lauch in a single command the server but not the container

python3 -m venv ENV && source ENV/bin/activate&& pip3 install -r requirements-lock.txt && export FLASK_APP=web.py && export FLASK_DEBUG=1 && flask run --host=0.0.0.0
