# Event Manager Setup Anleitung

## Voraussetzungen

- Maven-Version >= 3.6.3
- Java-Version >= 21.0.3

## Nutzung des Event Managers

1. **Kommando:**
	```bash
 	git clone *ssh-key des Repositories*

Navigation an den Speicherort des Projekts.

In den Projektordner "EventManager" navigieren.

2. **Kommando:**
	```bash
	mvn clean install

3. **Kommando:**
	```bash
	java -jar target/EventManagementSystem-1.0.0-jar-with-dependencies.jar ausfuehren

EventManager startet.

Die Initialisierung der Datenbank erfolgt automatisch im Verzeichnis homeVerzeichnisUser/EventManagerFiles als eventmanagerdata.sqlite.

Eine BenutzerAnleitung in Videofrom ist hier zu finden:
https://dele1907.github.io/EventManager/
