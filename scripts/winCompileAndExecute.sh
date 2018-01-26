#!/bin/bash

export PATH="C:/Program Files/VideoLAN/VLC":$PATH
cd ..
mvn clean install
java -jar -Djava.awt.headless=false target/Remote-media-center-0.0.1-SNAPSHOT.jar