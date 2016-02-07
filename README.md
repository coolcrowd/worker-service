# Worker-Service

[![Build Status](https://travis-ci.org/coolcrowd/worker-service.svg?branch=master)](https://travis-ci.org/coolcrowd/worker-service)
[![Coverage Status](https://coveralls.io/repos/github/coolcrowd/worker-service/badge.svg?branch=master)](https://coveralls.io/github/coolcrowd/worker-service?branch=master)

The documentation of the Worker-Service API can be found [here](http://coolcrowd.github.io/worker-service/)

## Requirements

 * Java 8
 * MySQL 5.6
 * Gradle (optional, but recommended)
 
## Installation

```bash
# Clone repository and change into its directory.
git clone https://github.com/coolcrowd/worker-service && cd worker-service

# Import database schema from ./src/main/resources/db.sql

# Install all dependencies and compile sources.
# Use gradle instead of ./gradlew if you have Gradle installed.
./gradlew jar
`
 
## How to start the Worker-Service

you can run the worker-service with :
`java -jar workerservice.jar -Ddatabase.url=jdbc:mysql:url -Ddatabase.username=user -Ddatabase.password=password -Dos.url=http://www.example.org`
where
 * `database.url` is the jdbc-url
 * `database.username` username of the database account
 * `database.password` the password of the database account
 * `os.url` is the url of the object-service
 
The configuration is detailed in the configuration-file conf/configuration.properties. You can alter the 
configuration-file to permanently change properties. Every property can be overridden by setting a global-property via
`-D{key}={value}`.