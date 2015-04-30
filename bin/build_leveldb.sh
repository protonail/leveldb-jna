#!/bin/bash

set -e

export ROOT_HOME=$(cd `dirname "{0}"` && pwd)
export SNAPPY_HOME=$(cd `dirname "{0}"` && cd vendor/snappy && pwd)
export LEVELDB_HOME=$(cd `dirname "{0}"` && cd vendor/leveldb && pwd)

echo --------------------
echo Build Snappy
echo --------------------

cd $SNAPPY_HOME
git clean -fdx
git reset --hard
[[ "$OSTYPE" == "darwin"* ]] && patch -N $SNAPPY_HOME/autogen.sh $ROOT_HOME/patches/autogen.sh.osx.patch
patch -N $SNAPPY_HOME/configure.ac $ROOT_HOME/patches/configure.ac.noarch.patch
./autogen.sh
./configure --disable-shared --with-pic --prefix=$SNAPPY_HOME
make install

echo --------------------
echo Build LevelDB
echo --------------------

cd $LEVELDB_HOME
export LIBRARY_PATH=$SNAPPY_HOME/lib
export C_INCLUDE_PATH=$SNAPPY_HOME/include
export CPLUS_INCLUDE_PATH=$SNAPPY_HOME/include
git clean -fdx
git reset --hard
make

echo --------------------
echo Copy LevelDB library
echo --------------------

cd $LEVELDB_HOME

if [[ "$OSTYPE" == "darwin"* ]]; then
  LEVELDB_FILE=libleveldb.dylib
  LEVELDB_ARCH=darwin
elif [[ "$OSTYPE" == "linux"* ]]; then
  LEVELDB_FILE=libleveldb.so
  if [[ $(uname -m) == "x86_64" ]]; then
    LEVELDB_ARCH=linux-x86-64
  else
    LEVELDB_ARCH=linux-x86
  fi
fi

mkdir -p $ROOT_HOME/leveldb-jna-native/src/main/resources/$LEVELDB_ARCH/
cp $LEVELDB_FILE $ROOT_HOME/leveldb-jna-native/src/main/resources/$LEVELDB_ARCH/
