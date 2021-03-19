### Note

- Chiamata per ottenere gli ordini
 ``` GET http://it2.life365.eu/api/order/{order_id}?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTYwMDc2MDAsImRhdGEiOnsiaWQiOjEwOCwibG9naW4iOiIwMDgiLCJzaWdsYSI6IjAwIiwibmFtZSI6IjAwOCIsImxldmVsIjo1LCJyb2xlIjoid2FyZWhvdXNlIiwiY291bnRyeSI6IklUIn19.YCihT6KORhPUppHkpYqBIxVQvgWMZd8Wwc6qeyHoRFE```

- Risposta del server
``` 
{
    "addr": {
        "fatturazione": {
            "cap": "47121",
            "citta": "Forlì",
            "codice_fiscale": "SPDGCR75C01A794U",
            "indirizzo": "via Carlo Forlanini 19",
            "nazione": 168,
            "partita_iva": null,
            "provincia": 118,
            "ragione_sociale": "Giancarlo Spadini"
        },
        "spedizione": {
            "cap": "86021",
            "citta": "Bojano",
            "indirizzo": "Piazza roma,91 C/O Mastrobuono Pizzardi",
            "nazione": 102,
            "nome": "Alfonso Morgillo",
            "provincia": 21,
            "ragione_sociale": "Consegnare a Gianca",
            "telefono": "0"
        }
    },
    "box_n": 5,
    "courier": 1,
    "customer_parameters": "{\"not_buyer\": true, \"shipment_BRT_particularDelivery\": \"A\"}",
    "date": "2020-11-06T13:04:00+01:00",
    "dropshipping": false,
    "has_invoice": false,
    "id": 447499,
    "items": [
        {
            "Codicesenza": "LBAC4000B-4400",
            "barcode": "0000000000000",
            "description": "ACER TravelMate 2300 4000 Aspire 1410 1600 1640 - 4400 mAh",
            "id": 6045,
            "perbox_discount": 10.0,
            "peso": 530,
            "photos": [
                "https://static.life365.eu/IT/p/6045/img/6045_LBAC4000B.jpg"
            ],
            "prezzo": 17.72,
            "qta": 1,
            "qta_scatola": 3,
            "qty_delivery": 1,
            "rma_warranty": 6,
            "stock": 8,
            "tax_type": 1,
            "tax_value": 22.0,
            "virtual": false,
            "warehouse_place": "D.14.02.10"
        }
    ],
    "logistic_state": "PICKED",
    "payment_type": "POSTEPAY",
    "shipment_data": "{\"BRT\": [{\"nSR\": 4474990, \"date\": 1615978149982, \"box_n\": 1, \"weight\": 0.53}]}",
    "shippingCostByCourier": {
        "1": 4.5,
        "4": 4.5
    },
    "shipping_cost": 4.5,
    "stato": 100,
    "tax2": {
        "actualValue": 0.0,
        "potentialValue": 0.0
    },
    "tax_type": 1,
    "tax_value": 22.0,
    "tipopagamento": "postepay",
    "total": 27.11,
    "tracking_url": "https://vas.brt.it/vas/sped_ricDocMit_load.hsm?referer=sped_numspe_par.htm&KSU=1312829&DocMit=4884660&rma=&RicercaDocMit=Search",
    "transport_doc_n": 79251
} 
```

La parte degli items alla fine è quella che ci interessa a noi

- chiamata per la editare l'ordine (ci serve per dire che è stato preso)
``` PUT localhost:5000/order/edit/447499?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTU5ODMwMDAsImRhdGEiOnsiaWQiOjMsImxvZ2luIjoid2giLCJzaWdsYSI6IldIIiwibmFtZSI6IndoIiwibGV2ZWwiOjEwLCJyb2xlIjoid2FyZWhvdXNlIiwiY291bnRyeSI6IklUIn19.l7cYVMGqjyeP6QylpxqCE8Z1avGc5pdot4GB6lym5s4 ```

Il body è il seguente:

``` 
{
    logistic_state: 'PICKED'
} 
```

- Le chiamate all'IoT server sono quelle che si trovano nel file ```\pervasive_computing\IoT_Server\app\smartForklift\views.py```. Per fare un esempio:

``` @smartForklift.route('/<int:id>/action/<string:action_name>', methods=['POST', 'OPTIONS']) ``` questo API qui corrisponde alla chimata http ``` POST http://indirizzoIoTServer/smartForklift/1 ```

- Il jwt è un token che permette l'autenticazione. Come si ottiene?

1- Vai al'indirizzo http://it2.life365.eu/signin


2- ti logghi come W1 password wh365

3- premi f12 e vai in application 

4- chiama simo che si fa prima

