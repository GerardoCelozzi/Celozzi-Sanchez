# Progetto OOP Celozzi - Sanchez

![ticket_master_official](https://user-images.githubusercontent.com/95294283/150121832-d316b42a-4142-4ee5-bcb7-aa17ec900ea4.jpg)

# SOMMARIO
* [Introduzione](#introduzione)
* [Specifiche Funzionali](#spec_funzionali)
   * [Extra](#spec_extra)
* [Rotte](#rotte)
* [Esempi Request](#esempi_request)
   * [Esempi Stats](#esempi_stats)
   * [Altri Esempi](#esempi_altri)
   * [Casi di Errore](#errori)


<div id = introduzione />

## INTRODUZIONE
L'applicazione consente all'utente di analizzare gli eventi che avranno luogo in un determinato paese, in particolare in Canada, utilizzando l'API di Ticket Master. Ticket Master rende un set di informazioni per gli eventi organizzati nel mondo, la nostra applicazione attraverso le proprie API (di seguito elencate) ne riproduce lo stesso comportamento senza limiti informativi (in funzione dei parametri immessi dall'utente).

<div id = spec_funzionali />

## SPECIFICHE FUNZIONALI
Statistiche per ogni stato: il numero totale di eventi, numero di eventi raggruppati per genere, numero minimo/massimo/medio di eventi mensili. 
Filtraggio in base a: uno o più stati, uno o più generi, periodo personalizzato per calcolare valori di minimo/massimo/media eventi (invece di calcolarli mensilmente).

<div id = spec_extra />

## SPECIFICHE EXTRA

#### GENERATORE REQUEST URL PER ENDPOINT CON CONNESIONE AD ENDPOINT <br>
In funzione delle specifiche di progetto, con l'esigenza di utilizzare un endpoint da cui recuperare informazioni, abbiamo deciso di generalizzare questa esigenza e di
incapsularne la responsabilità, immaginandone il riuso per altri progetti, per altri contesti. <br>
Abbiamo quindi implementato classi che instaziono oggetti in grado di gestire tutti i possibili vincoli funzionali che tale obbietivo avrebbe implicato. 

**Vincoli Funzionali**
* Endpoint diversi implicano pattern api o pattern url diversi, quindi possiamo comporre url/api in ogni punto del codice dove c'è ne sia bisogno settando dominio, path (opzionale),	queryString(opzionale), e parametri di queryString definendone chiavi e valori in maniera e numero arbitrario.
* Creazione di un file di testo per eventuali configurazioni rispetto a un endpoint (nel nostro progetto utilizzato per valore apikey).
* Definizione di regole di scrittura nel file in funzione del dato di configurazione da memorizzare (cosi facendo nel nostro caso è possibile memorizzare piu apikey nel file ma il nostro oggetto responsabile della lettura in funzione di queste regole prestabilite e standardizzate dal team di sviluppo è in grado di selezionare la giusta apikey relativa  all'endpoint cui riferisce).

**Le Classi** (*package com.univpm.demo.utils*)
* __ApiKeyScanner.java__ - L'oggetto di tipo ApiKeyScanner va a leggere in un file di testo recuperando l'apikey di un dato endpoint.
* __APICall.java__ - Instanziando un oggetto di tipo APICall effetuiamo una chiamata all'url che passiamo come parametro al suo metodo getData(url), lo stesso metodo ritorna il json di risposta. 
* __EndPoint.java__ - L'oggetto instanziato di questa classe è l'oggetto con cui costruiamo tutti i tipi di url da passare al metodo getdata di APICall. La classe contiene metodi che permettono la realzzazione di qualsiasi tipo di url ovvero attraverso l'oggetto possiamo definire un path per l'url e definire tutti i parametri di una queryString.
* __EndPointApiKey.java__ - (estenzione di EndPoint) Avendo l'esigenza di inserire per ogni chiamata il parametro apikey alla fine di una queryString, per evitare ogni volta di settare questo parametro per Endpoint sul oggetto di tipo EndPoint abbiamo creato questa classe che estende Endpoint ed ogni chiamata aggancia come ultimo parametro della queryString l'apikey. 

In effetti nel codice istanziamo un oggetto di tipo EndPointApikey con il 	quale generare l'url da passare a APICall.
* __Parametro.java__ - Con questa classe possiamo realizzare parametri di ogni tipo e anche questa è una classe che può essere riusata in altri contesti o per altre esigenze funzionali. Possiamo associare una chiave a ogni tipo di dato che sia un oggetto "x" o un tipo di dato primitivo. In molti casi nel nostro codice abbiamo utilizzato oggetti di questo tipo	ma con informazione incapsulata diversa, un esempio è quando andiamo a settare i parametri di chiamata con l'oggetto EndPointAiKey.


<div id = rotte />

## ROTTE
L'applicazione consente di utilizzare 2 diversi tipi di rotte:
* Rotte per le Statistiche (/stats/country/)
* Rotte Varie (/country)
 
Per l'utilizzo delle rotte di seguito troviamo una legenda dei parametri che si possono utilizzare nelle richieste:
- `country` indica la nazione 
- `stateCode` indica lo stato o gli stati (:heavy_exclamation_mark:)
- `nameCat` indica la classificazione o le classificazioni (:heavy_exclamation_mark:)
- `startdate` inidica la data di inizio periodo  (:grey_exclamation:)
- `enddate` indica la data fine periodo  (:grey_exclamation:)

(:heavy_exclamation_mark:)  E' possibile inserire più di un valore (basta dividere i valori desiderati con `-`) <br>
(:grey_exclamation:)  La data deve essere inserita in formato data italiano __gg/mm/aaaa__

<br>

### Stats

|**Tipo**| **Rotta** (/stats/country)                                                                   |**Parametri**                                            |
|--------|----------------------------------------------------------------------------------------------|---------------------------------------------------------|
|` GET ` | `/{countryCode}`                                                                             | `countryCode`                                           |
|` GET ` | `/{countryCode}/classification/{nameCat}`                                                    | `countryCode`,`nameCat`                                 |
|` GET ` | `/{countryCode}/state/{stateCode}/classification/{nameClass}/startdate/{start}/enddate/{end}`| `countryCode`,`stateCode`,`nameClass`,`start`,`end`     |
|` GET ` | `/{countryCode}/state/{stateCode}/statsmensili/{anno}`                                       | `countryCode`,`stateCode`,`anno`                        |
  
### Altre

|__Tipo__| __Rotta__                                                             |__Parametri__                                            |
|--------|-----------------------------------------------------------------------|---------------------------------------------------------|
|` GET ` | `/country/{countryCode}`                                              | `countryCode`                                           |
|` GET ` | `/country/{countryCode}/state/{stateCode}`                            | `countryCode`,`stateCode`                               |
|` GET ` | `/country/{countryCode}/state/{stateCode}/classification/{nameClass}` | `countryCode`,`stateCode`,`nameClass`                   |
|` GET ` | `/events/classification`                                              |                                                         | 

<br>
<div id = esempi_request />
<div id = esempi_stats />

## ESEMPI STATS REQUEST

### /stats/country/{countryCode}
Request URL: __/stats/country/ca__
```json
{
    "Stats": {
        "media eventi nazione ": 540,
        "totale eventi organizzati di ": 6487,
        "statistiche": [
            {
                "statistiche stati con numero maggiore di eventi": [
                    {
                        "stato  ": "ON",
                        "num eventi": 3695
                    }
                ]
            },
            {
                "statistiche stati con numero minore  di eventi": [
                    {
                        "stato  ": "NU",
                        "num eventi": 0
                    },
                    {
                        "stato  ": "YT",
                        "num eventi": 0
                    }
                ]
            }
        ],
        "nazione": "ca"
    }
}
```

### stats/country/{countryCode}/classification/{nameCat}
Request URL: __/stats/country/ca/classification/music__
```json
{
    "Stats": [
        {
            "totale eventi organizzati di music": 2326,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 885
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "categoria": "music",
            "media eventi per stato  music": 193
        }
    ],
    "nazione": "ca"
}
```
Request URL: __/stats/country/ca/classification/music-sports__
```json
{
    "Stats": [
        {
            "totale eventi organizzati di music": 2326,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 885
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "categoria": "music",
            "media eventi per stato  music": 193
        },
        {
            "totale eventi organizzati di sports": 783,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 309
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "media eventi per stato  sports": 65,
            "categoria": "sports"
        }
    ],
    "nazione": "ca"
}
```
Request URL: __/stats/country/ca/classification/music-sports-film__
```json
{
    "Stats": [
        {
            "totale eventi organizzati di music": 2326,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 885
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "categoria": "music",
            "media eventi per stato  music": 193
        },
        {
            "totale eventi organizzati di sports": 783,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 309
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "media eventi per stato  sports": 65,
            "categoria": "sports"
        },
        {
            "media eventi per stato  film": 0,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "BC",
                            "num eventi": 6
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "AB",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "MB",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NB",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NL",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NS",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "PE",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "QC",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "SK",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "categoria": "film",
            "totale eventi organizzati di film": 10
        }
    ],
    "nazione": "ca"
}
```

### stats/country/{countryCode}/state/{stateCode}/classification/{nameClass}/startdate/{start}/enddate/{end}
Request URL: __/stats/country/CA/state/QC-ON/classification/music-sports/startdate/22-2-2022/enddate/14-5-2022__
```json
{
    "categorie": [
        {
            "categoria": "music",
            "stati": [
                {
                    "numero eventi": 289,
                    "media mensile eventi": "144,5",
                    "stateCode": "qc"
                },
                {
                    "numero eventi": 508,
                    "media mensile eventi": "254",
                    "stateCode": "on"
                }
            ]
        },
        {
            "categoria": "sports",
            "stati": [
                {
                    "numero eventi": 80,
                    "media mensile eventi": "40",
                    "stateCode": "qc"
                },
                {
                    "numero eventi": 138,
                    "media mensile eventi": "69",
                    "stateCode": "on"
                }
            ]
        }
    ],
    "Periodo": [
        {
            "Start Date": "22-2-2022",
            "End Date": "14-5-2022"
        }
    ],
    "contryCode": "ca"
}
```

### stats/country/{countryCode}/state/{stateCode}/statsmensili/{anno}
Request URL: __/stats/country/ca/state/on/statsmensili/2022__
```json
{
    "Anno": "2022",
    "Stats Mensili": [
        {
            "Gennaio": 523
        },
        {
            "Febbraio": 1540
        },
        {
            "Marzo": 499
        },
        {
            "Aprile": 456
        },
        {
            "Maggio": 343
        },
        {
            "Giugno": 114
        },
        {
            "Luglio": 82
        },
        {
            "Agosto": 63
        },
        {
            "Settembre": 42
        },
        {
            "Ottobre": 23
        },
        {
            "Novembre": 14
        },
        {
            "Dicembre": 7
        }
    ],
    "MIN,MAX,MEDIA": [
        {
            "Mese con numero minimo di eventi": "Dicembre(7)",
            "Mese con numero massimo di eventi": "Febbraio(1540)",
            "Media Mensile": 308
        }
    ],
    "State": "on",
    "Country": "ca",
    "TOT EVENTI": 3706,
    "Stats Classificazioni": [
        {
            "Numero Eventi": 419,
            "Nome": "Miscellaneous"
        },
        {
            "Numero Eventi": 309,
            "Nome": "Sports"
        },
        {
            "Numero Eventi": 880,
            "Nome": "Music"
        },
        {
            "Numero Eventi": 2067,
            "Nome": "Arts % Theatre"
        },
        {
            "Numero Eventi": 8,
            "Nome": "Undefined"
        },
        {
            "Numero Eventi": 4,
            "Nome": "Film"
        }
    ]
}
```
<div id = esempi_altri />

## ESEMPI ALTRE REQUEST

### /country/{countryCode}
Request URL: __/country/ca__
```json
{
    "numero eventi": 6414,
    "contryCode": "ca"
}
```
### /country/{countryCode}/state/{stateCode}
Request URL: __/country/ca/state/on__
```json
{
    "numero eventi": 3696,
    "contryCode": "CA",
    "stati": [
        {
            "numero eventi": 3696,
            "stateCode": "ON"
        }
    ]
}
```
Request URL: __/country/ca/state/on-qc-pe-nu__
```json
{
    "numero eventi": 4868,
    "contryCode": "ca",
    "stati": [
        {
            "numero eventi": 3696,
            "stateCode": "on"
        },
        {
            "numero eventi": 1159,
            "stateCode": "qc"
        },
        {
            "numero eventi": 13,
            "stateCode": "pe"
        },
        {
            "numero eventi": "0",
            "stateCode": "nu"
        }
    ]
}
```
### /country/{countryCode}/state/{stateCode}/classification/{nameClass}
Request URL: __/country/CA/state/QC/classification/music__
```json
{
    "categorie": [
        {
            "categoria": "music",
            "stati": [
                {
                    "numero eventi": 556,
                    "stateCode": "QC"
                }
            ]
        }
    ],
    "contryCode": "CA"
}
```
Request URL: __/country/ca/state/QC-ON-NU/classification/music-sports__
```json
{
    "categorie": [
        {
            "categoria": "music",
            "stati": [
                {
                    "numero eventi": 556,
                    "stateCode": "QC"
                },
                {
                    "numero eventi": 886,
                    "stateCode": "ON"
                },
                {
                    "numero eventi": "0",
                    "stateCode": "NU"
                }
            ]
        },
        {
            "categoria": "sports",
            "stati": [
                {
                    "numero eventi": 143,
                    "stateCode": "QC"
                },
                {
                    "numero eventi": 309,
                    "stateCode": "ON"
                },
                {
                    "numero eventi": "0",
                    "stateCode": "NU"
                }
            ]
        }
    ],
    "contryCode": "CA"
}
```
<div id = errori />

## ESEMPI CASI DI ERRORE

Di seguito troviamo delle risposte generate da errori inseriti dall'utente in fase di scrittura delle rotte delle **Statistiche**.

### stats/country/{countryCode}
Request URL: __stats/country/us__
```json
{
    "messagio": "statistiche disponibili solo per il Canada",
    "codice ammesso": "countryCode: ca/CA"
}
```

### stats/country/{countryCode}/classification/{nameCat}
Request URL: stats/country/ca/classification/music-phone
```json
{
    "Stats": [
        {
            "totale eventi organizzati di music": 2337,
            "statistiche": [
                {
                    "statistiche stati con numero maggiore di eventi": [
                        {
                            "stato  ": "ON",
                            "num eventi": 894
                        }
                    ]
                },
                {
                    "statistiche stati con numero minore  di eventi": [
                        {
                            "stato  ": "NT",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "NU",
                            "num eventi": 0
                        },
                        {
                            "stato  ": "YT",
                            "num eventi": 0
                        }
                    ]
                }
            ],
            "categoria": "music",
            "media eventi per stato  music": 194
        },
        {
            "totale eventi organizzati di phone": 0,
            "statistiche": [],
            "categoria": "phone parametro non valido",
            "media eventi per stato  phone": 0
        }
    ],
    "nazione": "ca"
}
```

### stats/country/{countryCode}/state/{stateCode}/classification/{nameClass}/startdate/{start}/enddate/{end}
Request URL: __stats/country/ca/state/qc/classification/music-sports/startdate/16-2-2022/enddate/1-1-2022__
```json
{
    "Messaggio": "Periodo non valido!",
    "errore": "Pattern di chiamata compromesso"
}
```
### stats/country/{countryCode}/state/{stateCode}/statsmensili/{anno}
Request URL: __stats/country/ca/state/qc/statsmensili/2010__
```json
{
    "Messaggio": "anno non valido",
    "errore": "pattern di chiamata compromesso"
}
```


