#!/usr/bin/python
# -*- coding: utf-8 -*-

import flask
import json
import libs
import os
from base64 import b64decode
import win32api
import win32print
import re
from subprocess import call


from . import label_writer_450


def printFiles(list_filename):
    for f in range(0,len(list_filename)):
        if os.path.isfile(list_filename[f]):
            print("Stampa")
            res=None
            """ print('--- DEAFAULT PRINTER ---')
            defaultPrinter = '"{}"'.format(win32print.GetDefaultPrinter())
            print(defaultPrinter)
            res = win32api.ShellExecute (0,'printto',list_filename[f], defaultPrinter,'.',0)
            print('Risultato operazione di stampa{}'.format(res))
            print('--- DEAFAULT PRINTER ---') """
            call([".\PDFtoPrinter.exe", list_filename[f]])

        else :
            return -1
    return 0

@label_writer_450.route('/', methods=['GET', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def index():
    return "warehouse labeling pinter service active!!!"

@label_writer_450.route('/printPDF', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def printPDF():
    ## save file 
    print('inizia procedura di stampa')
    data = flask.request.get_json()
    print(data)
    labels = data['label']
    orderId = data['orderId']
    startingBox = None
    if('startingBox' in data):
        startingBox = data['startingBox']
    list_filename = []
    ### save files
    for l in range(0,len(labels)):
        print('Salva etichetta')
        b64 = labels[l]['stream']
        binary = b64decode(b64, validate=True)
        boxToPrint = l
        if(startingBox is not None):
            boxToPrint = boxToPrint + startingBox
        filename = "{}parcel{}.pdf".format(orderId, boxToPrint)
        print('--- FILENAME ---')
        print(filename)
        list_filename.append(filename)
        f = open(filename, 'wb')
        f.write(binary)
        f.close()
    ### print files
    res = printFiles(list_filename)
    if(res == 0):
        return "ok"
    else:
        return 500

@label_writer_450.route('/reprintPDF', methods=['POST', 'OPTIONS'])
@libs.cors.crossdomain(origin='*')
def reprintPDF():
    data = flask.request.get_json()
    orderId = data['orderId']

    rootdir = "."
    regex = re.compile('{}parcel\d.pdf'.format(orderId))
    toPrint = []
    for root, dirs, files in os.walk(rootdir):
        for file in files:
            if regex.match(file):
                toPrint.append(file)
                print(file)

    if(len(toPrint) == 0):
        return "No label found", 404
    else:
        printFiles(toPrint)
        return "ok", 200

