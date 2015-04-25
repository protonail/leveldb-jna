#!/bin/sh

mvn clean

rm -rf .idea
rm -f *.iml
rm -f */*.iml
rm -rf */target
