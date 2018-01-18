#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://igm.univ-mlv.fr/~forax/tmp/jdk/jdk1.5.0_22.tar.gz
tar -xzf jdk1.5.0_22.tar.gz
export JAVA_HOME=~/jdk/jdk1.5.0_22
cd -

${JAVA_HOME}/bin/java -version
