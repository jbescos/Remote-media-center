#!/bin/bash

export PATH="C:/Program Files/VideoLAN/VLC":$PATH
cd ..
mvn clean install
java -jar -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8081 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.awt.headless=false target/Remote-media-center-1.0.jar