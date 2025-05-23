# Backend for the _secret tresor application_

This is the backend is a Java Springboot application.

It can be used by API-calls.

The data's are stored in a database.

### information about database

prepare a database server and the database used by the application<br>
see [tresordb.sql](tresordb.sql) for an example database<br>
see [application.properties](src/main/resources/application.properties) about database access

## Requests examples

see [UserRequests.http](httprequest/UserRequests.http)<br>
see [SecretRequests.http](httprequest/SecretRequests.http)

## Environment variables

see [application.properties](src/main/resources/application.properties)

## Build image

see Dockerfile

```Bash
docker build -t tresorbackendimg .
```



## Start container local

```Bash
docker run -p 8080:8080 --name tresorbackend tresorbackendimg
```

(c) P.Rutschmann


# Doku Auftrag

## Anforderung HZ1

Ein statisches Salt schützt nicht gegen "Rainbow-Table-Angriffe"
oder gegen Identitätskorrelation bei mehreren Datensätzen, 
deshalb wird dieser bei mir zuffällig generiert.

## Anforderung E

Der Cypertext wird noch aus dem Salt und IV zusammen gesetzt.

## Anforderung HZ3 

Problem gelösst dadurch das der Schlüssel jetzt außerhalb des Codes liegt.
Wären diese im Code selber drinnen und jemand würde
auf den code kommen könnte man dies reverse engineeren. 

## Anforderung HZ4

ka

## Password Hashing

Hashing ist ein irreversibler Prozess: Ein Passwort wird in eine feste Zeichenkette umgewandelt – den Hash.
Man kann daraus das Originalpasswort nicht zurückrechnen.

Dadurch kann man wenn man Zugriff auf die Datenbank bekommt nicht die Passwörter der User heraus finden.
Um zu eine erfolgreichen Login auszuführen wird der momentan eingegebener Password gehäsht und mit dem in 
der Datenbank verglichen. 




