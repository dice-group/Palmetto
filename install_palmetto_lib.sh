#!/bin/bash

# usage ./install_palmetto_lib <version> [path_to_palmetto_project]

if [ $# -gt 0 ]
then
VERSION=$1
if [ $# -gt 1 ]
then
	PALMETTO_PROJECT_PATH=$2
else
	PALMETTO_PROJECT_PATH="../Palmetto"
fi

WEB_APP_LIB_DIR=$(pwd)"/lib"

if [ -d "$PALMETTO_PROJECT_PATH" ]; then
	cd $PALMETTO_PROJECT_PATH
	mvn clean package
	mvn install:install-file -Dfile=./target/palmetto-$VERSION.jar -Dpackaging=jar -Djavadoc=./target/palmetto-$VERSION-javadoc.jar -Dsources=./target/palmetto-$VERSION-sources.jar -DpomFile=./pom.xml -DlocalRepositoryPath=$WEB_APP_LIB_DIR
else
	echo "Couldn't find palmetto's directory $PALMETTO_PROJECT_PATH . Please set the path by using the parameter."
	echo "usage ./install_palmetto_lib <version> [path_to_palmetto_project]"
fi

else
	echo "usage ./install_palmetto_lib <version> [path_to_palmetto_project]"
fi

