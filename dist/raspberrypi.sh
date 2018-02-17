#!/bin/bash

# Add next in '$ corntab -e' if you want to execute the script automatically after each restart 
#@reboot sleep 5; cd /home/pi/workspace/Remote-media-center/dist; sudo ./raspberrypi.sh > /home/pi/remoteMediaCenter.log 2>&1

export XAUTHORITY=/home/pi/.Xauthority
export DISPLAY=:0
export PATH=/usr/lib/i386-linux-gnu/:$PATH

java -jar -DVLCJ_INITX=no -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8081 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.awt.headless=false Remote-media-center-1.0.jar
