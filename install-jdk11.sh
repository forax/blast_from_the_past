#!/usr/bin/bash
set -e

mkdir ~/jdk
cd ~/jdk
wget https://download.java.net/java/early_access/jdk11/5/GPL/openjdk-11-ea+5_linux-x64_bin.tar.gz
tar -xzf openjdk-11-ea+5_linux-x64_bin.tar.gz
export JAVA_HOME=~/jdk/jdk-11
cd -

${JAVA_HOME}/bin/java -version
