#! /bin/bash

# This script is a way to run the java classes with the build class-path in CMD module.

script_directory=$(dirname $0)
CP=${script_directory}:$(cat ${script_directory}/target/class-path-fred.txt)

java -Dfile.encoding=UTF-8 -cp "${CP}" "$@"