# Worker-Service

[![Build Status](https://travis-ci.org/coolcrowd/worker-service.svg?branch=master)](https://travis-ci.org/coolcrowd/worker-service)
[![Coverage Status](https://coveralls.io/repos/github/coolcrowd/worker-service/badge.svg?branch=master)](https://coveralls.io/github/coolcrowd/worker-service?branch=master)

The documentation of the Worker-Service API can be found [here](http://coolcrowd.github.io/worker-service/)

## Requirements

 * Java 8
 * MySQL 5.6
 * Gradle (optional, but recommended)
 
## Running the Worker-Service

you easily can run the Worker-Service via docker:

```bash
docker pull coolcrowd/worker-service
docker run -p -p 127.0.0.1:4567:4567 --link mysqldb:db -d coolcrowd/worker-service:latest -Ddatabase.url=jdbc:mysql:url -Ddatabase.username=user -Ddatabase.password=password -Dos.url=http://www.example.org
```

where:
* `database.url` is the jdbc-url
* `database.username` username of the database account
* `database.password` the password of the database account
* `os.url` is the url of the object-service

this will bind the Worker-Service on post 4567 on 127.0.0.1 of the hosts machine.

The configuration files are located under `/conf` on the image and named `configuration.properties` and `logging.xml`.
Please see below for more details.
 
## Installation

```bash
# Clone repository and change into its directory.
git clone https://github.com/coolcrowd/worker-service && cd worker-service

# Import database schema from ./src/main/resources/db.sql

# Install all dependencies and compile sources.
# Use gradle instead of ./gradlew if you have Gradle installed.
./gradlew jar
```

## Configuration
 
In the git, the configuration is detailed in `./conf/configuration.properties`. You can alter the 
configuration-file to permanently change properties. Every property can be overridden by setting a global-property via
`-D{key}={value}`.

You can also set the config-file location with the system-property `workerservice.config`, e.g. `-Dworkerservice.config=location`.
If none passed the app will always look for the configuration file in `./conf/`.

The logging is specified in the logging-file `./conf/logging.xml`. You can alter the logging-file to permanently change properties.

You can also set the config-file location with the system-property `logback.configurationFile`, e.g. `-Dlogback.configurationFile=location`.
If none passed the app will always look for the logging file in `./conf/`.

## Database

To initialise the Database it is recommended to use the `db.sql` script located in `src/main/resources`. 
 


