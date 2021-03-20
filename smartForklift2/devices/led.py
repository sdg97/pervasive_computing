import RPi.GPIO as GPIO
import time

def ledOn(*gpio):
    for g in gpio:
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(g,GPIO.OUT)
        print("LED on")
        GPIO.output(g,GPIO.HIGH)

def ledOff(*gpio):
    for g in gpio:
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(g,GPIO.OUT)
        print("LED off")
        GPIO.output(g,GPIO.LOW)

