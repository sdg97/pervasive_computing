Smart forklift prototype

### Deployment
- launc a redis container ``` sudo docker run --name=redis --net=host -v "`pwd`/redis":"/data"  redis redis-server --appendonly yes``` 
- build image ``` sudo docker build -t iot_server:latest .  ```
- active venv and install requirement
- run container ``` sudo docker run  --net=host -v `pwd`:"/IoTServer" --name iot_server iot_server:latest ```
