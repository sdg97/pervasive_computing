### GET /config
    Prende le configurazioni dello smart forklift

### PUT /config
    Setta le configurazioni dello smart forklift

```
ready_led_pin: int,
error_pin: int,
placements:[
    {
        id: string,
        ready_led_pin: int,
        catch_attention_led_pin: int,
        display_address: exadecimal address, 
    }
]
```

### POST /startUseSmartForklift
    Spegni tutti i led
    Stampa sugli schermi "Ready to handle an order"

### POST /setPlacement
    Associa un ordine a un placement
    
    Accendi led verde per far capire che è utilizzato
    Stampa scritta "Ready"

### POST /placements/{placement_id}/putItHere
    Attira l'attenzione del magazziniere per far capire che deve mettere li il prodotto

    Accende led giallo lampeggiante
    Scrive sulla prima linea il codice prodotto,
    sulla seconda la quantità

### POST /placements/{placement_id}/orderDone
    Dice che l'ordine in quella posizione è finito e invita il magazziniere ad associargliene un altro

    Spegni tutti i led
    Stampa sugli schermi "Ready to handle an order"

