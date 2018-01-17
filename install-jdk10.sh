#!/usr/bin/bash
set -e

# From https://github.com/sormuras/bach/blob/75a6719e5a42b3cde9b84f82e89cfb1c53abbe96/install-jdk.sh

JDK_FEATURE=10

TMP=$(curl -L jdk.java.net/${JDK_FEATURE})
TMP="${TMP#*Most recent build: jdk-${JDK_FEATURE}-ea+}" # remove everything before the number
TMP="${TMP%%<*}"                                        # remove everything after the number
JDK_BUILD="$(echo -e "${TMP}" | tr -d '[:space:]')"     # remove all whitespace

JDK_ARCHIVE=openjdk-${JDK_FEATURE}-ea+${JDK_BUILD}_linux-x64_bin.tar.gz

mkdir jdk
cd jdk
wget http://download.java.net/java/jdk${JDK_FEATURE}/archive/${JDK_BUILD}/GPL/${JDK_ARCHIVE}
tar -xzf ${JDK_ARCHIVE}
export JAVA_HOME=./jdk/jdk-${JDK_FEATURE}
export PATH=${JAVA_HOME}/bin:$PATH
cd ..

java --version

