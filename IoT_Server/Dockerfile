FROM python:3

COPY ./requirements.txt ./requirements.txt

RUN pip install -r requirements.txt
VOLUME /IoTServer 
WORKDIR /IoTServer
ENTRYPOINT export FLASK_APP=web.py && export FLASK_DEBUG=1 && flask run --host=0.0.0.0



