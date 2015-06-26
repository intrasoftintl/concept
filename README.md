# COnCEPT platform

## Prerequisites

* JDK 1.7.0_latest
* Maven 3.x

Before moving on, make sure you have the required JDK and Maven version.
 
	$ mvn -version
	$ java -version
	$ javac -version
	
### Install Maven on Ubuntu 
	sudo apt-get install maven
### Install Maven on OS X
	brew install maven	

## Run the Application

	$ git clone git@github.com:IntrasoftInternational/concept.git
	$ cd concept
	$ mvn clean install
	$ cd COnCEPT-PLATFORM-APP
	$ mvn spring-boot:run
