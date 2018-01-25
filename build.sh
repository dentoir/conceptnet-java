#!/bin/sh -e
#
# This Unix script builds the ConceptNet Java experimentation library from source.
#
# It has been tested on Debian GNU/Linux, but should work on any Unix with minimal
# adjustments.
#
# Dependencies: 
#
# 1) You will need to install the jar for the Apache Commons Lang library:
#    https://commons.apache.org/proper/commons-lang/
#
# 2) You will need to install the jar for the Apache Commons Math library:
#    http://commons.apache.org/proper/commons-math/
#
# Debian package dependencies:
# libcommons-lang3-java
# libcommons-math3-java
#

# Configurable classpath (this should contain pointers to commons-lang3.jar and commons-math3.jar)

export classes="/usr/share/java/commons-lang3.jar:/usr/share/java/commons-math3.jar:."

mkdir -p target
cd src/main/java
javac -classpath "$classes" net/intertextueel/conceptnet/ConceptNet.java
jar cvf ../../../target/conceptnet.jar net/intertextueel/conceptnet/*.class
cd ../../../
