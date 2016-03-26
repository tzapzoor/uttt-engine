#!/bin/bash
javac `find ./ -name '*.java' -regex '^[./A-Za-z0-9]*$'`
