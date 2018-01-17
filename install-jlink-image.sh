#!/usr/bin/bash
set -e

# create a small jdk using jlink
${JAVA_HOME}/bin/jlink --add-modules java.base --output jlink_jdk

export JAVA_HOME=jlink_jdk
export PATH=${JAVA_HOME}/bin:$PATH
cd -

java --version

