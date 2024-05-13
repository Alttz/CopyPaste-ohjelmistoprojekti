# Sovelluksen testit


Yksikkötesti EventRepository-luokalle:

Tämä yksikkötesti keskittyy EventRepository-luokan findById-metodin testaamiseen. Testi käyttää väärennettyä Event-oliota ja määrittelee odotetun toiminnallisuuden eventRepository.findById-metodia varten. Tämän jälkeen testi kutsuu findById-metodia ja tarkistaa, että palautettu Event-olio vastaa odotuksia.

Tämän testin tarkoitus on varmistaa, että EventRepository-luokan findById-metodi palauttaa odotetun Event-olion oikealla id-parametrillä.



API-testit käyttäjän authorisoinnille:

Nämä testit kohdistuvat käyttäjän autentikoinnin APIin sekä sen virheidenkäsittelyyn. Testit varmistavat JWT-token-peräisen autentikoinnin toimivan sekä antavan oikeat virhevastaukset väärällä käyttäjätunnuksella tai salasanalla. Vaikka muutkin integraatiotestit käyttävät autentikointimetodien antamaa JWT-tokenia, muut testit eivät testaa autentikointia virheellisillä tiedoilla.



API/Integraatiotestit EventApiTests-luokalle:

Tässä testiluokassa on kaksi testiä, jotka kohdistuvat HTTP-pyyntöjen käsittelyyn API:ssa. Testit ovat integraatiotestejä, koska ne arvioivat useiden osien, kuten HTTP-pyynnön lähettämisen, reitin käsittelyn, autentikoinnin JWT-tokenin avulla ja vastauksen tarkistamisen, toimintaa yhdessä.

testGetEvents: Lähettää GET-pyynnön /api/events-endpointille ja odottaa HTTP 200 -tilakoodia. Testi tarkistaa JSON-vastauksen rakenteen ja sisällön, erityisesti ensimmäisen tapahtuman nimen "Lordi".



testCreateEvent: Lähettää POST-pyynnön /api/events-endpointille uuden tapahtuman luomiseksi. Testi odottaa HTTP 201 -tilakoodia ja tarkistaa vastauksen JSON-muodon, varmistaen, että luotu tapahtuma vastaa odotuksia.

Näiden testien integraatioluonne korostuu, koska ne arvioivat eri osien toimintaa yhdessä. Lisäksi ne käyttävät MockMvc-kehystä testataan Spring MVC -sovelluksia ilman todellista HTTP-verkkoa, mikä tekee niistä integraatiotestejä sen sijaan, että ne testaisivat erillisiä yksiköitä.



End-to-End -testiluokka:



Tämä testiluokka käsittää koko sovelluksen end-to-end -testin, joka simuloi koko palvelun prosessin alusta loppuun: tapahtuman luomisesta, lipputyypin lisäämisestä, lippujen ostamisesta, tarkastamisesta ja käytetyksi merkitsemisestä.

obtainAccessToken-metodi: Luo kirjautumistokenin testin tarpeisiin.

createEventAndReturnId-metodi: Luo uuden tapahtuman ja palauttaa sen ID:n.

testEventLifecycleIncludingTickets: Tämä testi kattaa koko tapahtuman elinkaaren. Se luo uuden tapahtuman, lisää siihen lipputyypit, ostaa lippuja, merkitsee yhden lipuista käytetyksi ja tarkistaa sen tilan.

Tämä testi on end-to-end -testi, koska se simuloi koko sovelluksen toimintaa alusta loppuun. Se kutsuu useita REST-palvelun reittejä ja varmistaa, että kaikki osat toimivat odotetulla tavalla yhdessä. 