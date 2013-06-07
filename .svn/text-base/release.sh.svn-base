#!/bin/sh
./stop.sh
rm -rf lib
mkdir lib
mvn clean install
mvn dependency:copy-dependencies -DoutputDirectory=lib -U
mvn compile
./start.sh