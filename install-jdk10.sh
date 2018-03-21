#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget https://download.java.net/java/GA/jdk10/10/binaries/openjdk-10_linux-x64_bin.tar.gz
tar -xzf openjdk-10_linux-x64_bin.tar.gz
export JAVA_HOME=~/jdk/jdk-10
cd -

${JAVA_HOME}/bin/java -version
