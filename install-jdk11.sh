#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://monge.univ-mlv.fr/~forax/tmp/jdk/jdk-11-amber-raw-27-03-2018-linux_x64.tar.gz
tar -xzf jdk-11-amber-raw-27-03-2018-linux_x64.tar.gz
export JAVA_HOME=~/jdk/jdk-11
cd -

${JAVA_HOME}/bin/java -version
