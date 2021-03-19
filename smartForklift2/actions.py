from devices.display import *
from devices.led import *

def startUse(config):
    ledOn(config['ready_led_pin'])
    for placement in config['placements']:
        ledOff(placement['ready_led_pin'])
        ledOff(placement['catch_attention_led_pin'])
        dc = placement['display_channel']
        lcd_init(dc)
        lcd_string("READY-TO-CONNECT",LCD_LINE_1,dc)