#!/bin/bash
mvn clean install
docker build -t flight-scrapper -f dockerfile .
docker run --rm -p 7801:7801 --name flight-scrapper flight-scrapper
