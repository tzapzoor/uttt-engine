#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
javac -d $DIR/bin/ $DIR/*.java
