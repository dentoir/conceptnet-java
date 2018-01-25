#!/bin/sh -e
#
# This Unix script will build the ConceptNet Java library examples.
#
# It has been tested on Debian GNU/Linux, but should work with on any Unix with minimal
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

# Where is the jar of your ConceptNet library?

export conceptnetjar="../target/conceptnet.jar"

# Configurable classpath (this should contain pointers to commons-lang3.jar and commons-math3.jar)

export classes="/usr/share/java/commons-lang3.jar:/usr/share/java/commons-math3.jar:."

echo "Compiling.."
javac -classpath "$classes:$conceptnetjar" Lookup.java
javac -classpath "$classes:$conceptnetjar" Explorer.java
javac -classpath "$classes:$conceptnetjar" NearestNeighbors.java
javac -classpath "$classes:$conceptnetjar" FindPaths.java

echo ""
echo "Run example: java -classpath \"$classes:$conceptnetjar\" -Xmx14336m SimpleLookup"

