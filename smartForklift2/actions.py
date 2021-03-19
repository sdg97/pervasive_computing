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

def setPlacement(hw_s, data):
    placement_id = data['placement_id']
    order_id = data['order_id']
    config = hw_s['placements'][placement_id]
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1, dc)
    lcd_string("PICKING",LCD_LINE_2, dc)
    ledOn(config['ready_led_pin'])