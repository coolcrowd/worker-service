#!/bin/sh
echo The options are: $@
java $@ -Drouter.port=4567 -jar bin/workerservice.jar