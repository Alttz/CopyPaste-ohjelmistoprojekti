
# TicketGuru

Tiimi: Topi-Veikko Tuusa, Christoph Rehwald, Antton Selkänen, Nikolai Ylänne

## Johdanto

Projektin aiheena on lipunmyyntijärjestelmä lipputoimiston myyntipisteihin. Lipputoimisto tarvitsee järjestelmältä tavan lipun myyntiin, tulostamiseen sekä tarkastamiseen.
Projektin lopputuloksena pyritään tuottamaan järjestelmä, joka toteuttaa lipputoimiston tarpeet.

Päätelaitteina toimivat lipputoimiston tietokoneet.

Palvelinteknologiana käytetään Spring Bootia ja tietokantajärjestelmänä MySQLia.

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
    - Tietojen tallentaminen profiiliin
    - Aiempien ostojen tarkastelu <br/>

Asiakkaana haluan:
- *Ostaa lippuja verkkokaupasta, valita lipputyypin ja suorittaa maksutapahtuman.*
- *Tallentaa tietoni profiiliin, jotta voin tehdä tulevia ostoksia kätevämmin.*
- *Tarkastella aiempia ostoksiani ja saada tietoa eri tapahtumista.*

## Käyttöliittymä

Ohessa yksinkertainen kuvaus lipunmyyntijärjestelmän käyttöliittymäkaaviosta.

<img src="assets\images\Use_case_model.png" alt="Käyttöliittymäkaavio" style="height: 600px;"/>

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
> passwordhash | varchar(128) | Tilin salasana muunnettuna
>
> ### role
> _Role-taulu sisältää eri roolit, joita käyttäjillä voi olla._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> role_id | int PK | Roolin id
> role_name | varchar(50) |  Roolin nimi
>
> ### permission
> _Permission-taulu sisältää tiedot eri oikeuksista, joita eri rooleilla voi olla._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> permission_id | int PK | Oikeuden id
> permission_name | varchar(50) |  Oikeuden nimi
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
> ticketCount | int | saatavilla olevien lippujen maksimi määrä
>
> ### tickets
> _Tickets-taulu sisältää tiedot lipuista, mihin tapahtumiin ne on tarkoitettu, mikä niiden lipputyyppi on, hinta ja onko kyseinen lippu jo käytetty._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | int PK | datan id
> event_id | int FK | referenssi ostotapahtumaan
> type | varchar(255) | lipputyyppi
> price | double | lipun hinta
> isUsed | boolean | Tieto onko lippu käytetty vai ei
>
> ### purchase
> _Purchase-taulu sisältää tiedot ostotapahtumasta, koska se on tehty ja kuka sen on tehnyt._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | int PK | datan id
> user_id | int FK | Käyttäjätilin id
> purchase_date | Date | ostotapahtuman päivämäärä
>
> ### purchase_row
> _Tämä taulu sisältää kaikki ostotapahtumaan liitetyt liput. Tämä johtuu siitä, että yhdessä ostotapahtumassa voidaan ostaa monta lippua kerrallaan._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | int PK | datan id
> purchase_id | int FK | referenssi ostotapahtumaan
> ticket_id | int FK | referenssi lippuun

## Tekninen kuvaus

Teknisessä kuvauksessa esitetään järjestelmän toteutuksen suunnittelussa tehdyt tekniset
ratkaisut, esim.

-   Missä mikäkin järjestelmän komponentti ajetaan (tietokone, palvelinohjelma)
    ja komponenttien väliset yhteydet (vaikkapa tähän tyyliin:
    https://security.ufl.edu/it-workers/risk-assessment/creating-an-information-systemdata-flow-diagram/)
-   Palvelintoteutuksen yleiskuvaus: teknologiat, deployment-ratkaisut yms.
-   Keskeisten rajapintojen kuvaukset, esimerkit REST-rajapinta. Tarvittaessa voidaan rajapinnan käyttöä täsmentää
    UML-sekvenssikaavioilla.
-   Toteutuksen yleisiä ratkaisuja, esim. turvallisuus.

Tämän lisäksi

-   ohjelmakoodin tulee olla kommentoitua
-   luokkien, metodien ja muuttujien tulee olla kuvaavasti nimettyjä ja noudattaa
    johdonmukaisia nimeämiskäytäntöjä
-   ohjelmiston pitää olla organisoitu komponentteihin niin, että turhalta toistolta
    vältytään

## Testaus

Tässä kohdin selvitetään, miten ohjelmiston oikea toiminta varmistetaan
testaamalla projektin aikana: millaisia testauksia tehdään ja missä vaiheessa.
Testauksen tarkemmat sisällöt ja testisuoritusten tulosten raportit kirjataan
erillisiin dokumentteihin.

Tänne kirjataan myös lopuksi järjestelmän tunnetut ongelmat, joita ei ole korjattu.

## Asennustiedot

Järjestelmän asennus on syytä dokumentoida kahdesta näkökulmasta:

-   järjestelmän kehitysympäristö: miten järjestelmän kehitysympäristön saisi
    rakennettua johonkin toiseen koneeseen

-   järjestelmän asentaminen tuotantoympäristöön: miten järjestelmän saisi
    asennettua johonkin uuteen ympäristöön.

Asennusohjeesta tulisi ainakin käydä ilmi, miten käytettävä tietokanta ja
käyttäjät tulee ohjelmistoa asentaessa määritellä (käytettävä tietokanta,
käyttäjätunnus, salasana, tietokannan luonti yms.).

## Käynnistys- ja käyttöohje

Tyypillisesti tässä riittää kertoa ohjelman käynnistykseen tarvittava URL sekä
mahdolliset kirjautumiseen tarvittavat tunnukset. Jos järjestelmän
käynnistämiseen tai käyttöön liittyy joitain muita toimenpiteitä tai toimintajärjestykseen liittyviä asioita, nekin kerrotaan tässä yhteydessä.

Usko tai älä, tulet tarvitsemaan tätä itsekin, kun tauon jälkeen palaat
järjestelmän pariin !
