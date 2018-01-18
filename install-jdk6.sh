#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://igm.univ-mlv.fr/~forax/tmp/jdk/jdk-6u45-linux-x64.tar.gz
tar -xzf jdk-6u45-linux-x64.tar.gz
export JAVA_HOME=~/jdk/jdk1.6.0_45
cd -

${JAVA_HOME}/bin/java -version
