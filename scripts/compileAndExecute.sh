#!/bin/bash

export PATH=/usr/lib/x86_64-linux-gnu:$PATH
cd ..
mvn clean install
java -jar -Djava.awt.headless=false target/Remote-media-center-0.0.1-SNAPSHOT.jar