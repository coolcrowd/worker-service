#!/usr/bin/env bash
set -e

# if `docker run` first argument start with `--` the user is passing jenkins launcher arguments
if [[ $# -lt 1 ]] || [[ "$1" == "--"* ]]; then
  exec /bin/bash -c "java $JAVA_OPTS -jar workerservice-all-0.5.jar $JENKINS_OPTS $@"
fi