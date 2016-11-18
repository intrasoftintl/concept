# CONCEPT
> Collaborative creative design platform

The platform has been developed as part of the ConCEPT FP7 EU funded project. The project aims to implement a semantically driven collaboration framework to be integrated into future synchronous/asynchronous collaborative design environments, to assist professional product designers during the early stages of the design process.

## Prerequisites

The main application requires *Maven* (3) and the latest *Java* version (1.8.0_latest). You will also need a *MySQL* server.

Besides the main application the platform requires the following third-party software: *OpenProject*, *Elasticsearch*, *WiseMapping* and *Etherpad*.

You can deploy the rest of the components using a *WildFly* server.

Before moving on, make sure that you have successfully installed the aforementioned components.

## Building and running

	$ git clone git@github.com:intrasoftintl/concept.git
	$ cd concept
	$ mvn clean install
	$ cd concept-app
	$ mvn spring-boot:run