#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget http://igm.univ-mlv.fr/~forax/tmp/jdk/jdk-1_2_2_017-linux-i586.tar.gz
tar -xzf jdk-1_2_2_017-linux-i586.tar.gz
export JAVA_HOME=~/jdk/jdk1.2.2
cd -

#${JAVA_HOME}/bin/java -version
