# Event Manager Setup Anleitung

## 💻Technische Voraussetzungen

- **Maven-Version** >= 3.6.3
  
- **Java-Version** >= 21.0.3
  
- **Datenbanksystem**: Das Projekt verwendet eine SQLite Datenbank integriert mittels JDBC

## ⌨️Nutzung des Event Managers
**🏎️Quick Start: 💿Installation mittels .jar-Datei**

- Die an den Release angehängte .jar-Datei herunterladen und mittels des 		nachfolgenden Kommandos im jeweiligen eigenen Downloadordner ausführen:
  ```bash
  java -jar EventManagementSystem-1.0.0-jar-with-dependencies.jar
- **‼️Beachte**
	Beim ersten Start des Programms wird im Homeverzeichnis des jeweiligen 		Nutzers ein Ordner namens EventManagerFiles angelegt.

	In diesem Ordner werden sodann folgende Dateien erstellt:
	- application.log: Die Log-Datei der Anwendung
 	- eventmanager.sqlite initialisiert.

**👨‍💻Installation nach selbst ausgeführter Kompilierung aus dem geklonten Repository**

- Das Projekt mittels des nachstehenden Kommandos klonen:
	- mittels ssh:
		```bash
   		git clone git@github.com:dele1907/EventManager.git


   	- mittels https:
  		```bash
   		git clone https://github.com/dele1907/EventManager.git
   	
- In das verzeichnis des Projekts navigieren
  ```bash
  cd EventManager

- Code kompilieren
  ```bash
  mvn clean install

- Die erstellte .jar Datei ausführen
	-‼️ Es werden zwei .jar Dateien erstellt, die auszuführendene ist 		folgendermaßen benannt:
  	
	**EventManagementSystem-1.0.0-jar-with-dependencies.jar**

	```bash
 	java -jar target/EventManagementSystem-1.0.0-jar-with-dependencies.jar
 
 - **‼️Beachte**
	Beim ersten Start des Programms wird im Homeverzeichnis des jeweiligen 		Nutzers ein Ordner namens EventManagerFiles angelegt.

	In diesem Ordner werden sodann folgende Dateien erstellt:
	- application.log: Die Log-Datei der Anwendung
 	- eventmanager.sqlite initialisiert.


## Anwendungsdemonstration
Eine Demonstration in Videofrom wird hier zu finden sein:
https://dele1907.github.io/EventManager/
