# Worker-Service

[![Build Status](https://travis-ci.org/coolcrowd/worker-service.svg?branch=master)](https://travis-ci.org/coolcrowd/worker-service)
[![Coverage Status](https://coveralls.io/repos/github/coolcrowd/worker-service/badge.svg?branch=master)](https://coveralls.io/github/coolcrowd/worker-service?branch=master)

the documentation of the API can be found [here](http://coolcrowd.github.io/worker-service/)

## Requirements

 * Java 8
 * MySQL 5.6
 * Gradle (optional, but recommended)

## Installation

```bash
# Clone repository and change into its directory.
git clone https://github.com/coolcrowd/worker-service && cd worker-service

# Import database schema from ./src/main/resources/db.sql
# Create an appropriate MySQL user.
# Copy ./src/main/resources/worker-service.properties and adjust all settings to your needs.

# Install all dependencies and compile sources.
# Use gradle instead of ./gradlew if you have Gradle installed.
./gradlew assemble

# Run it.
./gradlew run PATH_TO_PROPERTIES
```