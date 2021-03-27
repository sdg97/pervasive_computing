### INSTALLATION 

1. clone the repo 
2. cd to smartForklift2
3. pwd and get the complete path of dir
4. chmod 777 ./launch.sh
5. ``` sudo  nano /etc/init/smartForklift.conf ```
6. ``` 
    description "smartForklift app"
    start on startup
    task
    exec `cd /home/pi/pervasive_computing/smartForklift2 && ./launch.sh` 
    ```