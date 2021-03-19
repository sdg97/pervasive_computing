from devices.display import *
from devices.led import *

def getPlacementConfig(hw_s, p_id):
    for p in hw_s['placements']:
        if(p['id'] == p_id):
            return p

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
    config = getPlacementConfig(hw_s, placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1, dc)
    lcd_string("PICKING",LCD_LINE_2, dc)
    ledOn(config['ready_led_pin'])

def putItHere(hw_s, data):
    placement_id = data['placement_id']
    product_code = data['product_code']
    qty = data['qty']
    config = getPlacementConfig(hw_s, placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("{}".format(product_code),LCD_LINE_1, dc)
    lcd_string("{}".format(qty),LCD_LINE_2, dc)
    ledOn(config['catch_attention_led_pin'])

def picked(hw_s, data):
    placement_id = data['placement_id']
    order_id = data['order_id']
    config = getPlacementConfig(hw_s, placement_id)
    dc = config['display_channel']
    lcd_init(dc)
    lcd_string("ORDER {}".format(order_id),LCD_LINE_1, dc)
    lcd_string("PICKING",LCD_LINE_2, dc)
    ledOff(config['catch_attention_led_pin'])

def orderDone(hw_s, data):
    placement_id = data['placement_id']
    placement = getPlacementConfig(hw_s, placement_id)
    dc = placement['display_channel']
    lcd_init(dc)
    lcd_string("READY-TO-CONNECT",LCD_LINE_1,dc)
    ledOff(placement['ready_led_pin'])
    ledOff(placement['catch_attention_led_pin'])