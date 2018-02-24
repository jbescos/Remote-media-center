#!/bin/bash

export XAUTHORITY=/home/pi/.Xauthority
export DISPLAY=:0
export PATH=/usr/lib/i386-linux-gnu/:$PATH

mvn clean install

java -jar -DVLCJ_INITX=no -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8081 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.awt.headless=false target/Remote-media-center-1.0.jar
