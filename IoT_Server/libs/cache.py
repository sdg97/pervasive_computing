#!/usr/bin/python
# -*- coding: utf-8 -*-

from flask_caching import Cache

from web import app


cache = Cache(config={'CACHE_TYPE': 'simple', 'CACHE_DEFAULT_TIMEOUT': 60 * 5})
cache.init_app(app)

fileSystemCache = Cache(
    config={
        'CACHE_TYPE': 'filesystem', 'CACHE_DIR': '/tmp/cache',
        'CACHE_DEFAULT_TIMEOUT': 60 * 60 * 24
    }
)
fileSystemCache.init_app(app)


def caching(key, fn, inMemory=True):
    c = cache if inMemory else fileSystemCache
    def f(*args, **argkw):
        res = c.get(key)
        if res is not None:
            return res
        res = fn(*args, **argkw)
        c.set(key, res)
        return res
    return f
