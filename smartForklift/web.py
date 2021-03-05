#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Per usare il log all'interno di un route devo usare:

    from web import app

    app.logger.debug('A value for debugging')
'''

from flask import Flask

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

from app.smartForklift import smartForklift
app.register_blueprint(smartForklift, url_prefix='/smartForklift')








