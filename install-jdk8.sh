#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://igm.univ-mlv.fr/~forax/tmp/jdk/jdk-8u152-linux-x64.tar.gz
tar -xzf jdk-8u152-linux-x64.tar.gz
export JAVA_HOME=~/jdk/jdk1.8.0_152
cd -

${JAVA_HOME}/bin/java -version
