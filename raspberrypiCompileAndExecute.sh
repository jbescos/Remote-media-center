#!/bin/bash

export XAUTHORITY=/home/pi/.Xauthority
export DISPLAY=:0
export PATH=/usr/lib/i386-linux-gnu/:$PATH

mvn clean install

java -jar -DVLCJ_INITX=no -Djava.awt.headless=false target/Remote-media-center-1.0.jar
