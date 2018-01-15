#!/bin/bash
cd ..
mvn clean install
java -jar -Djava.awt.headless=false -Dvlc.lib=/usr/lib/x86_64-linux-gnu/ target/Remote-media-center-0.0.1-SNAPSHOT.jar