import redis
DEVICES_DESCRIPTIONS_DIR = '/descriptions'
REDIS_ADDRESS='redis/0'
REDIS_PORT=4000
REDIS_INSTANCE = redis.Redis(REDIS_ADDRESS, REDIS_PORT, db=0, decode_responses=True)