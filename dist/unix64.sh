#!/bin/bash

export PATH=/usr/lib/x86_64-linux-gnu/:$PATH

java -jar -DVLCJ_INITX=no -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8081 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.awt.headless=false Remote-media-center-1.0.jar