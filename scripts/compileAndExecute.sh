#!/bin/bash
cd ..
mvn clean install
java -jar -Djava.awt.headless=false target/Remote-media-center-0.0.1-SNAPSHOT.jar