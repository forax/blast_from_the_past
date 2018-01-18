#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://igm.univ-mlv.fr/~forax/tmp/jdk/jdk-7u80-linux-x64.tar.gz
tar -xzf jdk-7u80-linux-x64.tar.gz
export JAVA_HOME=~/jdk/jdk1.7.0_80
cd -

${JAVA_HOME}/bin/java -version
