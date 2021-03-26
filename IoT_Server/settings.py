import redis
DEVICES_DESCRIPTIONS_DIR = '/descriptions'
REDIS_ADDRESS='localhost'
REDIS_PORT=6379
REDIS_INSTANCE = redis.Redis(REDIS_ADDRESS, REDIS_PORT, db=0, decode_responses=True)