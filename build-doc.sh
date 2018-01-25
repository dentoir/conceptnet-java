#!/bin/sh -e
#
# This Unix build script builds the documentation for the ConceptNet java experimentation library.
#
# It has been tested on Debian GNU/Linux, but should work with any Unix with only minimal adjustments.
#

# Configurable classpath (this should contain pointers to commons-lang3.jar and commons-math3.jar)

export classes="/usr/share/java/commons-lang3.jar:/usr/share/java/commons-math3.jar:."

# Relative paths for the local application

export docpath="./doc"
export sourcepath="./src/main/java/net/intertextueel/conceptnet"

echo "Building documentation.."
mkdir -p $docpath
javadoc -classpath "$classes" -d "$docpath" -version net.intertextueel.conceptnet $sourcepath/*.java
