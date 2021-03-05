#!/usr/bin/python
# -*- coding: utf-8 -*-

import decimal
import multiprocessing
import random


def roundDecimal(v):
    '''
    Sembra che l'arrotondamento di un decimal sia più complicato del previsto
    '''
    return v.quantize(decimal.Decimal('0.01'), rounding=decimal.ROUND_HALF_UP)


def maybeStart(startCb, debug):
    '''
    Ogni tanto esegue questa callback...
    Ad ogni restart di un worker in maniera casuale esegue la callback
    '''

    if debug:
        return
    workers = multiprocessing.cpu_count() * 2 + 1
    if random.randrange(workers) == 0:
        startCb()
