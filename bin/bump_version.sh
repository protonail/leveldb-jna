#!/bin/sh

mvn versions:set -DnewVersion=$1
mvn versions:commit