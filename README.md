# 🗓️**Event-Manager**

## 📌Kurzbeschreibung

Die Anwendung bietet eine intuitive und benutzerfreundliche Plattform zur Organisation und Verwaltung von Events.
Neben der zentralen Eventplanung erleichtert sie den Überblick über öffentliche und private Events und benachrichtigt die Teilnehmer bei Änderungen.

## 🛠️Features

- **🔐Registrierungssystem mit Login**
- **📝Eventverwaltung**: Events erstellen/bearbeiten/löschen
- **👥Teilnehmermanagement**: Teilnehmer einladen/entfernen
- **🔍Such- und Filterfunktionen** von öffentlichen Events
- **🔔Benachrichtung** zu den Events der Teilnehmer
- **🗂️Exportieren der Events** in universelles Kalenderdateiformat

## 💻Systemanforderungen

- **Maven-Version** >= 3.6.3

- **Java-Version** >= 21.0.3

- **Datenbanksystem**: Das Projekt verwendet eine SQLite-Datenbank integriert mittels JDBC

## ⌨️Installation & Schnellstart

### Variante 1: 🏎️Quick Start - 💿Installation mittels .jar-Datei

- Die an den Release angehängte .jar-Datei herunterladen und mittels des nachfolgenden Kommandos im jeweiligen eigenen Downloadordner ausführen:
  ```bash
  java -jar EventManagementSystem-1.0.0-jar-with-dependencies.jar
  ```

  - **‼️ Beachte:**
    Beim ersten Start des Programms wird im Homeverzeichnis des jeweiligen Nutzers ein Ordner namens EventManagerFiles angelegt
    und man wird aufgefordert einen Admin-Account anzulegen. Anschließend gelangt zum Login. 
  
    In diesem Ordner werden sodann folgende Dateien erstellt:
      - application.log: Die Log-Datei der Anwendung
      - eventmanager.sqlite: Die Datenbank-Datei


### Variante 2: 👨‍💻Installation nach selbst ausgeführter Kompilierung aus dem geklonten Repository

- Das Projekt mittels eines der nachstehenden Kommandos klonen:

	- Entweder mittels ssh:
	  ```bash
      git clone git@github.com:dele1907/EventManager.git
      ```

	- Oder mittels https:
	  ```bash
      git clone https://github.com/dele1907/EventManager.git 
      ```

- In das Verzeichnis des Projekts navigieren:
  ```bash
  cd EventManager
  ```

- Code kompilieren:
  ```bash
  mvn clean install
  ```

- Die erstellte .jar-Datei ausführen:

	- Es werden zwei .jar-Dateien erstellt, die auszuführendene ist folgendermaßen benannt: **EventManagementSystem-1.0.0-jar-with-dependencies.jar**
	  ```bash
      java -jar target/EventManagementSystem-1.0.0-jar-with-dependencies.jar
      ```

- **‼️ Beachte:**
  Beim ersten Start des Programms wird im Homeverzeichnis des jeweiligen Nutzers ein Ordner namens EventManagerFiles angelegt
  und man wird aufgefordert einen Admin-Account anzulegen. Anschließend gelangt zum  
  Login.

  In diesem Ordner werden sodann folgende Dateien erstellt:
  - application.log: Die Log-Datei der Anwendung
  - eventmanager.sqlite: Die Datenbank-Datei

## 📽️Anwendungsbeispiele

Eine Demonstration unseres Event-Managers mit Beispielen findet man im hier angehängten Video:  
https://dele1907.github.io/EventManager/

## ⛔Bekannte Einschränkungen

- Exportfunktion für mehrere Events gleichzeitig ist deaktiviert aufgrund von Duplikaten beim Apple-Kalendar
- Event-Mindestalter kann nach Erstellen des Events nicht mehr geändert werden
- Adminstratoren können keine Events von anderen Benutzern bearbeiten/löschen 