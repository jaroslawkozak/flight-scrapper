#!/bin/bash
mvn clean install
docker build -t flight-data-manager -f dockerfile .
docker run --rm -p 7701:7701 --name flight-data-manager flight-data-manager
