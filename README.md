
# TicketGuru

Tiimi: Topi-Veikko Tuusa, Christoph Rehwald, Antton Selkänen, Nikolai Ylänne

## Johdanto

Projektin aiheena on lipunmyyntijärjestelmä lipputoimiston myyntipisteihin. Lipputoimisto tarvitsee järjestelmältä tavan lipun myyntiin, tulostamiseen sekä tarkastamiseen.
Projektin lopputuloksena pyritään tuottamaan järjestelmä, joka toteuttaa lipputoimiston tarpeet.

Päätelaitteina toimivat lipputoimiston tietokoneet.

Palvelinteknologiana käytetään Spring Bootia ja tietokantajärjestelmänä MariaDB:tä.

## Järjestelmän määrittely

### Käyttäjäroolit ja tarinat

####  Ylläpitäjä
- *kuvaus:* Vastuussa lipunmyyntijärjestelmän hallinnasta ja ylläpidosta.
- *Vaaditut toiminnot:*
    - Tapahtuman lisääminen
    - Käyttäjien hallinta
    - Järjestelmän päivitys

Ylläpitäjänä haluan:
- *Lisätä tapahtumia järjestelmään, määritellä lipputyypit ja asettaa ennakkomyynnin päivämäärän, jotta asiakkaat voivat ostaa lippuja tuleviin tapahtumiin.*
- *Hallinnoida käyttäjiä* ja lippuja
- *Päivittää lipunmyyntijärjestelmän varmistaakseni sen optimaalisen ja katkottoman toiminnan.*
<!-- Määrittelyssä järjestelmää tarkastellaan käyttäjän näkökulmasta. Järjestelmän
toiminnot hahmotellaan käyttötapausten tai käyttäjätarinoiden kautta, ja kuvataan järjestelmän
käyttäjäryhmät.-->

####  Lipunmyyjä
- *kuvaus:* Myy lippuja lipunmyyntipisteessä ja huolehtii tapahtumien sujuvasta etenemisestä.
- *Vaaditut toiminnot::*
    - Lipunmyynti myyntipisteessä
    - Ovella myytävien lippujen hallinta
    - Käytettyjen lippujen merkitseminen

Lipunmyyjänä haluan:
- *Myydä lippuja asiakkaille fyysisessä myyntipisteessä ja tulostaa ne, jotta asiakkaat saavat saumattoman kokemuksen.*
- *Seurata ennakkomyynnin tilannetta, tulostaa jäljellä olevat liput ovelle myytäviksi ja helposti merkitä käytetyt liput sisäänkäynnillä.*


####  Asiakas
- *kuvaus:* Ostaa lippuja myyntipisteistä sekä verkkokaupasta ja osallistuu tapahtumiin.
- *Vaaditut toiminnot:*
    - Lipun ostaminen verkkokaupasta
    <br/>

Asiakkaana haluan:
- *Ostaa lippuja verkkokaupasta, valita lipputyypin ja suorittaa maksutapahtuman.*

## Käyttöliittymä

Ohessa yksinkertainen kuvaus lipunmyyntijärjestelmän käyttöliittymäkaaviosta.

<img src="assets\images\Use_case_model.png" alt="Käyttöliittymäkaavio" style="height: 600px;"/>

#### Kuvia käyttöliittymästä

Kirjaudu sisään

<img src="assets\images\kirjautumisnäkymä.png" alt="Kirjautumisnäkymä"/>

Hallintanäkymä

<img src="assets\images\tapahtumienhallinta.png" alt="Hallintanäkymä"/>

Tapahtuman muokkaus

<img src="assets\images\tapahtumanmuokkaus.png" alt="Tapahtuman muokkaus"/>

Lipputyyppien muokkaus

<img src="assets\images\lipputyypit-muokkaus.png" alt="Lipputyyppien muokkaus"/>

Tapahtuman lisääminen

<img src="assets\images\uusitapahtuma.png" alt="Lisää uusi tapahtuma"/>

Lipun osto

<img src="assets\images\lipunostonäkymäesimerkillä.png" alt="Lipun osto"/>

Onnistunut lipun osto

<img src="assets\images\onnistunutosto.png" alt="Onnistunut lipun osto"/>

Lipun tarkastus

<img src="assets\images\tapahtumienhallinta.png" alt="Lipun tarkastus"/>
## Tietokanta

Järjestelmään säilöttävä ja siinä käsiteltävät tiedot ja niiden väliset suhteet
kuvataan käsitekaaviolla. Käsitemalliin sisältyy myös taulujen välisten viiteyhteyksien ja avainten
määritykset. Tietokanta kuvataan käyttäen jotain kuvausmenetelmää, joko ER-kaaviota ja UML-luokkakaaviota.

Lisäksi kukin järjestelmän tietoelementti ja sen attribuutit kuvataan
tietohakemistossa. Tietohakemisto tarkoittaa yksinkertaisesti vain jokaisen elementin (taulun) ja niiden
attribuuttien (kentät/sarakkeet) listausta ja lyhyttä kuvausta esim. tähän tyyliin:

<img src="assets\images\tietokantarelaatiot.png" alt="Tietokantarelaatiot" style="height: 600px;"/>

> ### app_user
> _app_user-taulu sisältää tiedon palvelun käyttäjätileistä._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> user_id | int PK | Käyttäjätilin id
> username | varchar(30) |  Käyttäjätilin nimimerkki
> password | varchar(128) | Tilin salasana
> role | varchar(255) | Käyttäjän rooli
>
> ### event
> _Event-taulu sisältää tiedot tämän hetkisistä, tulevista sekä menneistä tapahtumista._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | int PK | datan id
> place | varchar(255) | tapahtumapaikka
> date | varchar(255) | tapahtuman päivämäärä
> city | varchar(255) | tapahtuman kaupunki
> name | varchar(255) | tapahtuman nimi
> ticketCount | int | saatavilla olevien lippujen maksimi määrä
> tickets | List\<Ticket\> | Tapahtumaan liittyvien lippujen lista (Ei näytetä dokumentaatiossa)
> ticketTypes | List\<TicketType\> | Tapahtumaan liittyvien lipputyyppien lista (Ei näytetä dokumentaatiossa)
>
> ### tickets
> _Tickets-taulu sisältää tiedot lipuista, mihin tapahtumiin ne on tarkoitettu, mihin ostotapahtumaan ne liittyy, mikä niiden lipputyyppi ja hinta on, ja onko kyseinen lippu jo käytetty._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | Long PK | datan id¨
> ticketType | TicketType | Lipputyyppi, joka määrittää lipun hinnan ja tyypin
> event | Long | Tapahtuman ID, johon lippu liittyy
> purchase | Purchase | Ostotapahtuma, johon lippu liittyy
> isUsed | boolean | Tieto onko lippu käytetty vai ei
>
> ### TicketType
> _Lipputyypit-taulu sisältää tiedon tapahtumiin kuuluvista lipputyypeistä ja niiden hinnoista._
> 
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | Long PK | Lipputyypin yksilöllinen tunniste
> name | String | Lipputyypin nimi
> price | double | Lipputyypin hinta
> event | Event | Tapahtuma, johon lipputyyppi liittyy
>
> ### purchase
> _Purchase-taulu sisältää tiedot ostotapahtumasta, koska se on tehty ja kuka sen on tehnyt._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | Long PK | Ostotapahtuman yksilöllinen tunniste
> purchaseDate | Date | Ostotapahtuman päivämäärä ja aika
> tickets | List<Ticket> | Lista lipuista, jotka ostotapahtumaan liittyy
> appUser | AppUser | Käyttäjä, joka suoritti ostotapahtuman
> 

## Tekninen kuvaus

### Järjestelmän komponentit
Järjestelmä koostuu Frontend-, Backend- ja Tietokantakomponentista:

**Frontend-komponentti:** On toteutettu Vue.js:llä, joka on moderni JavaScript ohjelmistokehys. Vue.js soveltui projektiin
erinomaisesti helppokäyttöisyydensä ja tehokkuudensa ansiosta. Frontend kommunikoi backendin kanssa REST-rajapinnan 
kautta. Projektin käyttöliittymä on deployattu GitHub pagesiin ja ajetaan käyttäjän selaimen kautta.

**Backend-komponentti:** Backend on toteutettu käyttäen Javan Spring Boot ohjelmistokehystä. Komponentti ajetaan Rahdin 
pilvipalvelimella. Kommunikaatio tapahtuu REST-rajapinnan kautta.

**Tietokantakomponentti:** Projekti käyttää MariaDB:tä tietokantana ja tietokanta pyörii Rahdin pilvipalvelimella.

<img src="assets\images\järjestelmän tietovirta.png" alt="Tietovirtakaavio" style="height: 600px;"/>

### Rajapinta

Projektin API-rajapintana on käytetty projektia varten tehtyä RESTful API:a. Rajapinta mahdollistaa tietojen hallinnan
sovelluksessa. Rajapinta tarjoaa toiminnallisuuden muokata, hakea sekä poistaa tapahtumatietoja. Rajapinta palauttaa 
dataa JSON-muodossa. Ohessa esimerkkejä erilaisista pyyntötyypeistä ja -reiteistä joita rajapinta tarjoaa.



- **Hae kaikki tapahtumat**: `GET /api/events`
- **Hae tapahtuma ID:n perusteella**: `GET /api/events/1`
- **Hae tapahtumia nimen perusteella**: `GET /api/events/byName?name=Lordi`
- **Hae tapahtumia kaupungin perusteella**: `GET /api/events/byCity?city=Helsinki`
- **Hae kaikki liput**: `GET /api/tickets`
- **Hae lippu ID:n perusteella**: `GET /api/tickets/1`
- **Hae lippu UUID:n perusteella**: `GET /api/tickets/byUuid?uuid=a93af710-cb1f-4945-a108-2d3f5e7a4352`
- **Merkitse lippu käytetyksi**: `PATCH /api/tickets/markAsUsed?uuid=a93af710-cb1f-4945-a108-2d3f5e7a4352`
- **Hae kaikki ostotapahtumat**: `GET /api/purchases`
- **Lisää tapahtuma**: `POST /api/events`
- **Lisää lipputyypit tapahtumaan**: `POST /api/events/5/tickettypes`
- **Lisää ostotapahtuma ja luo liput**: `POST /api/purchases`
- **Päivitä tapahtumaa**: `PUT /api/events/5`
- **Päivitä tapahtuman lipputyypit**: `PUT /api/events/5/tickettypes`
- **Päivitä tapahtuma**: `DELETE /api/delete/5`

Tarkempi rajapintadokumentaatio esimerkkisyötteiden ja esimerkkivastausten kanssa löytyy [täältä](api_dokumentaatio.md).

### Turvallisuus
Projektin keskeinen turvallisuusratkaisu on tokenpohjainen autentikaatio. Autentikointiprosessissa käyttäjä lähettää
kirjautumispyynnön käyttäjätunnuksella ja salasanalla. Onnistuneessa sisäänkirjautumistilanteessa palvelin luo ja 
palauttaa käyttäjälle tokenin jota käytetään kaikissa autentikaatiota tarvitsevissa tilanteissa. Käyttäjä lähettää 
tokenin jokaisessa pyynnössä ja palvelin tarkastaa tokenin aitouiden ja validoi sisällön. Jos tiedot ovat ok, palvelin
antaa pääsyn pyydettyihin resursseihin. Vanhentuneen tokenin voi uusia kirjautumalla uudelleen onnistuneesti sisään.

Esimerkiksi tapahtumia voi muokkaa vain sisäänkirjautunut käyttäjä jolla on oikeudet ja validi token kutsujen
lähettämiseen.



## Testaus

Projektin aikana suoritettiin kattava testausprosessi varmistaakseen ohjelmiston oikea toiminta ja tulosten laatu. Testaus
jaettiin eri tasoihin ja vaiheisiin, joista jokainen keskittyy tiettyyn osa-alueeseen sovelluksessa.
Esimerkkejä:

**Yksikkötestit:**
        EventRepository-luokka: Testaa findById-metodin toiminnallisuutta.

**API-testit:**
        Käyttäjän authorisointi: Testaa käyttäjän autentikoinnin APIin ja virheidenkäsittelyn toimivuutta.
        EventApiTests-luokka: Testaa HTTP-pyyntöjen käsittelyä API:ssa.

**End-to-End -testit:**
        Koko sovelluksen toiminnan simulointi: Simuloi sovelluksen prosessin alusta loppuun.

**Testien tarkoitus:**

Yksikkötestit varmistavat yksittäisten komponenttien toiminnan.
API-testit testaavat API-rajapintojen toimintaa ja autentikointia.
End-to-End -testit tarkastavat sovelluksen kokonaisuuden toiminnan.

Tarkemmat testien selitykset löytyvät [täältä](testien_selitykset.md) ja testausraportti [täältä](testausraportti.md).



## Asennustiedot


Järjestelmän kehitysympäristön asentaminen toiseen koneeseen:

- Asenna haluamasi kehitysympäristö (esim. IntelliJ IDEA, VSCode jne.)
- Asenna MariaDB
- Kloonaa projektin Git-repositorio
- Avaa projekti kehitysympäristössä ja varmista konfiguraatiot

Spring Boot-projektin asentaminen:

- Käynnistä tietokantapalvelin ja varmista että tietokanta on käytettävissä
- Käynnistä Spring Boot-sovellus kehitysympäristöstä




Järjestelmän asentaminen tuotantoympäristöön:

- Luo tarvittavat palvelimet ja asenna tarvittavat ohjelmistot (JDK, tietokanta jne.)
- Siirrä ohjelmistokoodi tuotantoympäristöön Gitillä.
- Luo tietokantaan tarvittavat käyttäjät, salasanat ja oikeudet.
- Konfiguroi Spring Boot -projektiin tietokannan yhteysasetukset
- Aseta tarvittavat ympäristömuuttujat tai konfiguraatiot
   


## Käynnistys- ja käyttöohje

Käynnistys:
- Navigoi sivulle https://alttz.github.io/CopyPaste-ohjelmistoprojekti/
- Kirjaudu sisään default käyttäjällä (käyttäjätunnus: admin , salasana: adminpass)


