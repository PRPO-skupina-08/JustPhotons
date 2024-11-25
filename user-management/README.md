# User management microservice

Ta microservice bo upravljal s uporabniki naše spletne platforme. 
Implementira sledeče API dostopne točke (actually you better look into OpenAPI :):
1. `/users`: 
   - `GET` - vrne vse podatke o vseh uporabnikih (200)
   - `POST` - doda uporabnika. V polju morajo biti prisotni sledeči podatki:
     ```json
     {
      "username": string,
      "email": string (valid email),
     }
     ```
     Response codes: `201` Created ali `400` Bad Request, če so podatki v bodyju nepravilni
2. `/users/{id}`
   - `GET` - vrne uporabnika (`200`) s podanim `id` ali vrne kodo `404`
   - `DELETE` - izbriše uporabnika (`204`) s podanim `id` ali vrne kodo `404`
   - `PUT` - posodobi uporabnika s podanim id in vrne `200`, če je vse kul, `304`, če se uporabnik ni spremenil, `404`, če podani uporabnik ne obstaja
3. `/users/filtered`
   - na tem endpointu se lahko pridobi uporabnike filtrirane/sortirane po kriterijih, ki so objavljeni tukaj: https://github.com/kumuluz/kumuluzee-rest
   - metoda vrača `200` in upornike, ki ustrezajo kriterijem

# Entitete

Entity users ima naslednje atribute:
- id
- email
- username

## Zagon baze

Za delovanje aplikacije potrebujete podatkovno bazo, ki jo lahko enostavno zaženete s tehnologijo Docker na naslednji način:

```bash
docker run -d --rm -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=justphotons -p 5432:5432 postgres:13
```
`-rm` imamo samo v testnem okolju, da se nam container zbriše, ko ga ugasnemo. Drugače se jih nabere za celo gomilo.

## Gradnja in zagon projekta

Najprej zgradite projekt:

```
mvn clean package
```

Nato ga lahko zaženete z naslednjim ukazom:

```
java -jar api/target/api-1.0.0-SNAPSHOT.jar
```

Za preverjanje delovanja aplikacije lahko v brskalniku odprete naslednji URL: http://localhost:8080/servlet