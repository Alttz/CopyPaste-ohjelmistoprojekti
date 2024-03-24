

# TicketGuru Tapahtuma-API Dokumentaatio

TicketGuru Tapahtuma-API tarjoaa joukon päätepisteitä tapahtumainformaation hakemiseen ja selaamiseen. Tässä dokumentaatiossa kerrotaan, miten kutakin päätepistettä käytetään tapahtumien hakuun TicketGuru-järjestelmästä.

## Autentikointi

Päätepisteiden käyttämiseksi sinun on hankittava JWT-tokeni tunnistautumalla `/api/login` routeen. Tämän jälkeen kun haluat käyttää muita routeja se pitää antaa `Authorization` headerissa.

###  Kirjaudu & Hanki JWT-tokeni

- **URL**: `/api/login`
- **Metodi**: `POST`
- **Kuvaus**: Tunnistaudu käyttäjänimen ja salasanan avulla. Onnistuneet tunnistautumisen yhteydessä saat tässä sessiossa käytettävän JWT-tokenin
- **Esimerkkipyyntö**:
  ```json
  {
    "username": "admin",
    "password": "adminpass"
  }
  ```
- **Vastaus**: 
  - **200 OK**: Kyseinen responssi sisältää tarvitun JWT-tokenin.
    ```json
    {
      "token": "[TOKEN]"
    }
    ```
Muista varmistaa, että antaessasi tokenin pyynnön ohessa se on varmasti asetettu Bearer tokeniksi.


## Päätepisteet

### Lisää tapahtuma
- **URL**: `/api/events/{id}/tickettypes`
- **Metodi**: `POST`
- **Kuvaus**: Lisää tapahtumalle lipputyypit. Lipputyyppien nimet on standardisoitu ja rajattu seuraaviin: "Aikuinen", "Lapsi", "Eläkeläinen", "Opiskelija", "Varusmies", "VIP". Kunkin lipputyypin hinnan voi tapahtumakohtaisesti määrittää, kun ne luodaan.
- **Vastaus**: 201 Created.

**esimerkkisyöte**
```json
[
  {
    "name": "Aikuinen",
    "price": 25.00
  },
  {
    "name": "VIP",
    "price": 50.00
  },
  {
    "name": "Lapsi",
    "price": 10.00
  }
]
```
### Lisää lipputyypit tapahtumaan

- **URL**: `/api/events/{id}/tickettypes`
- **Metodi**: `POST`
- **Kuvaus**: Lisää tapahtumalle lipputyypit. Lipputyyppien nimet on standardisoitu ja rajattu seuraaviin: "Aikuinen", "Lapsi", "Eläkeläinen", "Opiskelija", "Varusmies", "VIP". Kunkin lipputyypin hinnan voi tapahtumakohtaisesti määrittää, kun ne luodaan.
- **Vastaus**: 201 Created.

**esimerkkisyöte**
```json
[
  {
    "name": "Aikuinen",
    "price": 25.00
  },
  {
    "name": "VIP",
    "price": 50.00
  },
  {
    "name": "Lapsi",
    "price": 10.00
  }
]
```

**esimerkki vastaus**
```json
[
    {
        "price": 25.0,
        "name": "Aikuinen",
        "id": 13,
        "event": 5
    },
    {
        "price": 50.0,
        "name": "VIP",
        "id": 14,
        "event": 5
    },
    {
        "price": 10.0,
        "name": "Lapsi",
        "id": 15,
        "event": 5
    }
]
```

### Lisää ostotapahtuma ja liput

- **URL**: `/api/purchases`
- **Metodi**: `POST`
- **Kuvaus**: Lisää ostotapahtuman ja luo samalla liput määritettyihin tapahtumiin. Metodi tarkastaa tapahtuman lippumäärän ja päivittää sen ostotapahtuman päätteeksi. Ostotapahtuman yhteydessä määritetään myös, mitä lipputyyppejä ollaan myymässä. Lisäksi ostotapahtumaan määritettään kuka käyttäjä on sen suorittanut.
- **Vastaus**: Metodi palauttaa taulukot "successfulPurchases" onnistuineista ostotapahtumista ja "failedPurchases" epäonnistuneista. Onnistuneet ostotapahtumat listataan ja epäonnistuneista tulee seuraava viesti: "Ostotapahtumaa ei pystytty suorittamaan tapahtumaan ID:llä: 4. Tarkasta tapahtuman ID, lipputyypit ja saatavuus.".

**Esimerkkisyöte**
```json
{
    "userId": 1,
    "purchaseRequestRows": [
        {
            "eventId": 5,
            "ticketTypeNames": ["Aikuinen", "Aikuinen", "VIP", "VIP", "Lapsi"]
        },
        {
            "eventId": 4,
            "ticketTypeNames": ["Aikuinen", "Aikuinen", "Opiskelija"]
        }
    ]
}
```

**Esimerkkivastaus**
```json
{
    "successfulPurchases": [
        {
            "id": 3,
            "purchaseDate": "2024-03-10T15:06:15.196+00:00",
            "tickets": [
                {
                    "id": 5,
                    "ticketType": {
                        "id": 13,
                        "name": "Aikuinen",
                        "price": 25.0,
                        "event": 5
                    },
                    "used": false,
                    "event": 5
                },
                {
                    "id": 6,
                    "ticketType": {
                        "id": 13,
                        "name": "Aikuinen",
                        "price": 25.0,
                        "event": 5
                    },
                    "used": false,
                    "event": 5
                },
                {
                    "id": 7,
                    "ticketType": {
                        "id": 14,
                        "name": "VIP",
                        "price": 50.0,
                        "event": 5
                    },
                    "used": false,
                    "event": 5
                },
                {
                    "id": 8,
                    "ticketType": {
                        "id": 14,
                        "name": "VIP",
                        "price": 50.0,
                        "event": 5
                    },
                    "used": false,
                    "event": 5
                },
                {
                    "id": 9,
                    "ticketType": {
                        "id": 15,
                        "name": "Lapsi",
                        "price": 10.0,
                        "event": 5
                    },
                    "used": false,
                    "event": 5
                }
            ],
            "appUser": {
                "user_id": 1,
                "username": "TeppoTestaaja",
                "role": null
            }
        },
        {
            "id": 4,
            "purchaseDate": "2024-03-10T15:06:15.203+00:00",
            "tickets": [
                {
                    "id": 10,
                    "ticketType": {
                        "id": 11,
                        "name": "Opiskelija",
                        "price": 20.0,
                        "event": 4
                    },
                    "used": false,
                    "event": 4
                },
                {
                    "id": 11,
                    "ticketType": {
                        "id": 12,
                        "name": "Aikuinen",
                        "price": 35.0,
                        "event": 4
                    },
                    "used": false,
                    "event": 4
                },
                {
                    "id": 12,
                    "ticketType": {
                        "id": 12,
                        "name": "Aikuinen",
                        "price": 35.0,
                        "event": 4
                    },
                    "used": false,
                    "event": 4
                }
            ],
            "appUser": 1
        }
    ],
    "failedPurchases": []
}
```

### Hae Kaikki Tapahtumat

- **URL**: `/api/events`
- **Metodi**: `GET`
- **Kuvaus**: Palauttaa listan kaikista tapahtumista.
- **Vastaus**: Lista tapahtumista JSON-muodossa.

**Esimerkki vastaus**

```json
[
    {
        "id": 1,
        "date": "28.9.2023",
        "place": "Hartwallareena",
        "city": "Helsinki",
        "name": "Lordi",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 1,
                "name": "Eläkeläinen",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 2,
                "name": "Opiskelija",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 3,
                "name": "Aikuinen",
                "price": 25.0,
                "event": 1
            }
        ]
    },
    {
        "id": 2,
        "date": "1.4.2024",
        "place": "PubiTarmo",
        "city": "Turku",
        "name": "Apulanta",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 4,
                "name": "Eläkeläinen",
                "price": 15.0,
                "event": 2
            },
            {
                "id": 5,
                "name": "Opiskelija",
                "price": 15.0,
                "event": 2
            },
            {
                "id": 6,
                "name": "Aikuinen",
                "price": 25.0,
                "event": 2
            }
        ]
    },
    {
        "id": 3,
        "date": "18.7.2024",
        "place": "Kansallisteatteri",
        "city": "Helsinki",
        "name": "Käärijä",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 7,
                "name": "Eläkeläinen",
                "price": 40.0,
                "event": 3
            },
            {
                "id": 8,
                "name": "Opiskelija",
                "price": 40.0,
                "event": 3
            },
            {
                "id": 9,
                "name": "Aikuinen",
                "price": 60.0,
                "event": 3
            }
        ]
    }
]
```

### Hae Tapahtuma ID:n Perusteella

- **URL**: `/api/events/{id}`
- **Metodi**: `GET`
- **Kuvaus**: Hakee tietyn tapahtuman sen ID:n perusteella.
- **URL-parametrit**: `id=[Long]`, missä `id` on haettavan tapahtuman ID.
- **Vastaus**: Yksittäinen tapahtuma JSON-muodossa. Palauttaa 404 Not Found, jos annetulla ID:llä ei löydy tapahtumaa.

**Esimerkki vastaus**

```json
{
    "id": 1,
    "date": "28.9.2023",
    "place": "Hartwallareena",
    "city": "Helsinki",
    "name": "Lordi",
    "ticketCount": 1000,
    "ticketTypes": [
        {
            "id": 1,
            "name": "Eläkeläinen",
            "price": 15.0,
            "event": 1
        },
        {
            "id": 2,
            "name": "Opiskelija",
            "price": 15.0,
            "event": 1
        },
        {
            "id": 3,
            "name": "Aikuinen",
            "price": 25.0,
            "event": 1
        }
    ]
}
```

### Hae Tapahtumia Nimen Perusteella

- **URL**: `/api/events/byName`
- **Metodi**: `GET`
- **Kuvaus**: Etsii tapahtumia, jotka sisältävät määritetyn nimen.
- **Kyselyparametrit**: `name=[String]`, missä `name` on tapahtuman nimi (tai osa nimestä), jota haetaan. Esim. `/api/events/byName?name=Lordi`.
- **Vastaus**: Lista hakuehtoja vastaavista tapahtumista JSON-muodossa. Palauttaa 204 No Content, jos hakuehtoja vastaavia tapahtumia ei löydy.

**Esimerkki vastaus**

```json
[
    {
        "id": 1,
        "date": "28.9.2023",
        "place": "Hartwallareena",
        "city": "Helsinki",
        "name": "Lordi",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 1,
                "name": "Eläkeläinen",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 2,
                "name": "Opiskelija",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 3,
                "name": "Aikuinen",
                "price": 25.0,
                "event": 1
            }
        ]
    }
]
```

### Hae Tapahtumia Kaupungin Perusteella

- **URL**: `/api/events/byCity`
- **Metodi**: `GET`
- **Kuvaus**: Etsii tapahtumia kaupungin perusteella, jossa ne sijaitsevat.
- **Kyselyparametrit**: `city=[String]`, missä `city` on kaupunki, jossa tapahtumaa etsitään. Esim. `/api/events/byCity?city=Helsinki`.
- **Vastaus**: Lista hakuehtoja vastaavista tapahtumista JSON-muodossa. Palauttaa 204 No Content, jos hakuehtoja vastaavia tapahtumia ei löydy.

**Esimerkki vastaus**

```json
[
    {
        "id": 1,
        "date": "28.9.2023",
        "place": "Hartwallareena",
        "city": "Helsinki",
        "name": "Lordi",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 1,
                "name": "Eläkeläinen",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 2,
                "name": "Opiskelija",
                "price": 15.0,
                "event": 1
            },
            {
                "id": 3,
                "name": "Aikuinen",
                "price": 25.0,
                "event": 1
            }
        ]
    },
    {
        "id": 3,
        "date": "18.7.2024",
        "place": "Kansallisteatteri",
        "city": "Helsinki",
        "name": "Käärijä",
        "ticketCount": 1000,
        "ticketTypes": [
            {
                "id": 7,
                "name": "Eläkeläinen",
                "price": 40.0,
                "event": 3
            },
            {
                "id": 8,
                "name": "Opiskelija",
                "price": 40.0,
                "event": 3
            },
            {
                "id": 9,
                "name": "Aikuinen",
                "price": 60.0,
                "event": 3
            }
        ]
    }
]
```

### Hae kaikki liput

- **URL**: `/api/tickets`
- **Metodi**: `GET`
- **Kuvaus**: Palauttaa listan kaikista lipuista.
- **Vastaus**: Lista lipusta JSON-muodossa.

**Esimerkki vastaus**

```json
[
    {
        "id": 1,
        "ticketType": {
            "id": 1,
            "name": "Eläkeläinen",
            "price": 15.0,
            "event": 1
        },
        "used": false,
        "event": 1
    },
    {
        "id": 2,
        "ticketType": {
            "id": 2,
            "name": "Opiskelija",
            "price": 15.0,
            "event": 1
        },
        "used": false,
        "event": 1
    },
    {
        "id": 3,
        "ticketType": {
            "id": 3,
            "name": "Aikuinen",
            "price": 25.0,
            "event": 1
        },
        "used": false,
        "event": 1
    },
    {
        "id": 4,
        "ticketType": {
            "id": 3,
            "name": "Aikuinen",
            "price": 25.0,
            "event": 1
        },
        "used": false,
        "event": 1
    }
]
```

### Hae kaikki ostotapahtumat

- **URL**: `/api/purchases`
- **Metodi**: `GET`
- **Kuvaus**: Palauttaa listan kaikista ostotapahtumista.
- **Vastaus**: Lista ostotapahtumista JSON-muodossa.

**Esimerkki vastaus**

```json
[
    {
        "id": 1,
        "purchaseDate": "2024-03-03T10:12:23.745+00:00",
        "tickets": [
            {
                "id": 1,
                "ticketType": {
                    "id": 1,
                    "name": "Eläkeläinen",
                    "price": 15.0,
                    "event": 1
                },
                "used": false,
                "event": 1
            },
            {
                "id": 2,
                "ticketType": {
                    "id": 2,
                    "name": "Opiskelija",
                    "price": 15.0,
                    "event": 1
                },
                "used": false,
                "event": 1
            }
        ],
        "appUser": {
            "user_id": 1,
            "username": "TeppoTestaaja",
            "role": null
        }
    },
    {
        "id": 2,
        "purchaseDate": "2024-03-03T10:12:23.830+00:00",
        "tickets": [
            {
                "id": 3,
                "ticketType": {
                    "id": 3,
                    "name": "Aikuinen",
                    "price": 25.0,
                    "event": 1
                },
                "used": false,
                "event": 1
            },
            {
                "id": 4,
                "ticketType": {
                    "id": 3,
                    "name": "Aikuinen",
                    "price": 25.0,
                    "event": 1
                },
                "used": false,
                "event": 1
            }
        ],
        "appUser": {
            "user_id": 2,
            "username": "Masa",
            "role": null
        }
    }
]
```

### Päivitä tapahtumaa
- **URL**: `/api/events/{id}`
- **Metodi**: `PUT`
- **Kuvaus**: Päivittää tiettyä tapahtumaa pyynnon bodyssä sisältyvän tapahtuman IDn perusteella, jos tapahtuma on olemassa tietokannassa.
- **Kyselyparametrit**: `id=[Long]`, missä id on tarvittavan tapahtuman ID, sekä pyynnön bodyssä oleva uusi tapahtumaobjekti JSON-muodossa.
- **Vastaus**: Jos id:n mukaista tapahtumaa ei ole olemassa, palauttaa 404 Not Found. Jos tapahtuman muokkaus onnistuu, palauttaa 200 OK sekä muokatun objektin.

**esimerkkisyöte**
```json
{
    "name": "Assembly Summer 2024",
    "date": "1.8.2024",
    "place": "Messukeskus",
    "city": "Helsinki",
    "ticketCount": 500
}
```

**esimerkkivastaus**

```json
200 OK
{
    "id": 1,
    "date": "1.8.2024",
    "place": "Messukeskus",
    "city": "Helsinki",
    "name": "Assembly Summer 2024",
    "ticketCount": 500,
    "ticketTypes": []
}
```

### Päivitä tapahtuman lipputyypit
- **URL**: `/api/events/{id}/tickettypes`
- **Metodi**: `PUT`
- **Kuvaus**: Päivittää URL:ssa määritetyn tapahtuman lipputyypit. Lipputyyppien nimet on standardisoitu ja rajattu seuraaviin: "Aikuinen", "Lapsi", "Eläkeläinen", "Opiskelija", "Varusmies", "VIP". Kunkin lipputyypin hinnan voi tapahtumakohtaisesti määrittää, kun tapahtuman lipputyypit päivitetään.
- **Kyselyparametrit**: `id=[Long]`, missä id on päivitettävän tapahtuman ID, sekä pyynnön bodyssä oleva uusi lipputyyppi-objekti JSON-muodossa.
- **Vastaus**: Jos id:n mukaista tapahtumaa ei ole olemassa, palauttaa 404 Not Found. Jos tapahtuman muokkaus onnistuu, palauttaa 200 OK sekä muokatun objektin.

**esimerkkisyöte**
```json
[
  {
    "name": "Aikuinen",
    "price": 25.00
  },
  {
    "name": "Lapsi",
    "price": 10.00
  },
  {
    "name": "Eläkeläinen",
    "price": 10.00
  }
]
```

**esimerkkivastaus**

```json
200 OK
[
    {
        "price": 25.0,
        "name": "Aikuinen",
        "id": 47,
        "event": 5
    },
    {
        "price": 10.0,
        "name": "Lapsi",
        "id": 48,
        "event": 5
    },
    {
        "price": 10.0,
        "name": "Eläkeläinen",
        "id": 49,
        "event": 5
    }
]
```

### Poista tapahtumia
- **URL**: `api/event/{id}`
- **Metodi**: `DELETE`
- **Kuvaus**: Poista annettu resurssi palvelimelta.
- **Vastaus**: Jos id:n mukaista tapahtumaa ei ole olemassa, palauttaa 404 Not Found. Jos tapahtuman poistaminen onnistuu, palauttaa 204 No Content.

## Käyttö

Näiden päätepisteiden käyttämiseksi lähetä dokumentaation mukaisia kysyttyjä HTTP-pyyntöjä määriteltyihin URL-osoitteisiin asianmukaisilla parametreilla.

## Esimerkit

- **Hae kaikki tapahtumat**: `GET /api/events`
- **Hae tapahtuma ID:n perusteella**: `GET /api/events/1`
- **Hae tapahtumia nimen perusteella**: `GET /api/events/byName?name=Lordi`
- **Hae tapahtumia kaupungin perusteella**: `GET /api/events/byCity?city=Helsinki`
- **Hae kaikki liput**: `GET /api/tickets`
- **Hae kaikki ostotapahtumat**: `GET /api/purchases`
- **Lisää tapahtuma**: `POST /api/events`
- **Lisää lipputyypit tapahtumaan**: `POST /api/events/5/tickettypes`
- **Lisää ostotapahtuma ja luo liput**: `POST /api/purchases`
- **Päivitä tapahtumaa**: `PUT /api/events/5`
- **Päivitä tapahtuman lipputyypit**: `PUT /api/events/5/tickettypes`
- **Päivitä tapahtuma**: `DELETE /api/delete/5`

Huomioithan, että haku päätepisteissä ei ole kirjainkoosta riippuvainen ja se löytää kaikki hakutermejä vastaavat tapahtumat.
