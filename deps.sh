#!/bin/bash

set -e
set -x

apt-get update
apt install -y wget maven libglu1 git unzip

# Download JDK 12
wget https://download.java.net/java/GA/jdk12.0.2/e482c34c86bd4bf8b56c0b35558996b9/10/GPL/openjdk-12.0.2_linux-x64_bin.tar.gz
tar -xzvf openjdk-12.0.2_linux-x64_bin.tar.gz
rm -rf openjdk-12.0.2_linux-x64_bin.tar.gz

# Install Gradle
wget https://services.gradle.org/distributions/gradle-6.5-bin.zip 
mkdir /opt/gradle 
unzip -d /opt/gradle gradle-6.5-bin.zip 
rm gradle-6.5-bin.zip

# Install MiniZinc
wget https://github.com/MiniZinc/MiniZincIDE/releases/download/2.3.2/MiniZincIDE-2.3.2-bundle-linux-x86_64.tgz 
mv MiniZincIDE-2.3.2-bundle-linux-x86_64.tgz minizinc.tgz 
tar -xzvf minizinc.tgz 
mv MiniZincIDE-2.3.2-bundle-linux minizinc 
rm minizinc.tgz

# Install or-tools
wget https://github.com/google/or-tools/releases/download/v7.7/or-tools_ubuntu-18.04_v7.7.7810.tar.gz 
mv or-tools_ubuntu-18.04_v7.7.7810.tar.gz ortools.tgz 
tar -xzvf ortools.tgz 
mv or-tools_Ubuntu-18.04-64bit_v7.7.7810 ortools 
cd ortools/lib/ 
mvn install:install-file -Dfile=com.google.ortools.jar -DgroupId=com.google -DartifactId=ortools -Dversion=7.7 -Dpackaging=jar 
cd ~/
rm ortools.tgz

cd ~/

