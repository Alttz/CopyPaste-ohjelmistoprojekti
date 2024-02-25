# TicketGuru Tapahtuma-API Dokumentaatio

TicketGuru Tapahtuma-API tarjoaa joukon päätepisteitä tapahtumainformaation hakemiseen ja selaamiseen. Tässä dokumentaatiossa kerrotaan, miten kutakin päätepistettä käytetään tapahtumien hakuun TicketGuru-järjestelmästä.

## Päätepisteet

### Hae Kaikki Tapahtumat

- **URL**: `api/events`
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
        "tickets": []
    },
    {
        "id": 2,
        "date": "1.4.2024",
        "place": "PubiTarmo",
        "city": "Turku",
        "name": "Apulanta",
        "ticketCount": 1000,
        "tickets": []
    },
    {
        "id": 3,
        "date": "18.7.2024",
        "place": "Kansallisteatteri",
        "city": "Pasila",
        "name": "Käärijä",
        "ticketCount": 1000,
        "tickets": []
    },
    {
        "id": 4,
        "date": "5.5.2024",
        "place": "Koulun musaluokka",
        "city": "Luhanka",
        "name": "Antti Tuisku",
        "ticketCount": 1000,
        "tickets": []
    }
]
```

### Hae Tapahtuma ID:n Perusteella

- **URL**: `api/event/{id}`
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
    "tickets": []
}
```

### Etsi Tapahtumia Nimen Perusteella

- **URL**: `api/events/search/byName`
- **Metodi**: `GET`
- **Kuvaus**: Etsii tapahtumia, jotka sisältävät määritetyn nimen.
- **Kyselyparametrit**: `name=[String]`, missä `name` on tapahtuman nimi (tai osa nimestä), jota haetaan.
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
        "tickets": []
    }
]
```

### Etsi Tapahtumia Kaupungin Perusteella

- **URL**: `api/events/search/byCity`
- **Metodi**: `GET`
- **Kuvaus**: Etsii tapahtumia kaupungin perusteella, jossa ne sijaitsevat.
- **Kyselyparametrit**: `city=[String]`, missä `city` on kaupunki, jossa tapahtumaa etsitään.
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
        "tickets": []
    }
]
```

### Päivitä tapahtumaa
- **URL**: `api/event/{id}`
- **Metodi**: `PUT`
- **Kuvaus**: Päivittää tiettyä tapahtumaa pyynnon bodyssä sisältyvän tapahtuman IDn perusteella, jos tapahtuma on olemassa tietokannassa.
- **Kyselyparametrit**: `id=[Long]`, missä id on tarvittavan tapahtuman ID, sekä pyynnön bodyssä oleva uusi tapahtumaobjekti JSON-muodossa.
- **Vastaus**: Jos id:n mukaista tapahtumaa ei ole olemassa, palauttaa 404 Not Found. Jos tapahtuman muokkaus onnistuu, palauttaa 200 OK sekä muokatun objektin.

**Käytön esimerkki**
```
curl --location --request PUT 'http://localhost:8080/api/event/1' \
--header 'Content-Type: application/json' \
--data '{
    "id": 0,
    "date": "1.8.2024",
    "place": "Messukeskus",
    "city": "Helsinki",
    "name": "Assembly Summer 2024",
    "ticketCount": 0,
    "tickets": []
}'
```

```
200 OK
{
    "id": 1,
    "date": "1.8.2024",
    "place": "Messukeskus",
    "city": "Helsinki",
    "name": "Assembly Summer 2024",
    "ticketCount": 0,
    "tickets": []
}
```

## Käyttö

Näiden päätepisteiden käyttämiseksi lähetä dokumentaation mukaisia kysyttyjä HTTP-pyyntöjä määriteltyihin URL-osoitteisiin asianmukaisilla parametreilla.

## Esimerkit

- **Hae Kaikki Tapahtumat**: `GET api/events`
- **Hae Tapahtuma ID:n Perusteella**: `GET api/event/1`
- **Etsi Tapahtumia Nimen Perusteella**: `GET api/events/search/byName?name=Konsertti`
- **Etsi Tapahtumia Kaupungin Perusteella**: `GET api/events/search/byCity?city=Helsinki`

Huomioithan, että haku päätepisteissä ei ole kirjainkoosta riippuvainen ja se löytää kaikki hakutermejä vastaavat tapahtumat.
