#!/bin/bash

set -e

export ROOT_HOME=$(cd `dirname "$0"` && cd .. && pwd)
export SNAPPY_HOME=$(cd $ROOT_HOME && cd vendor/snappy && pwd)
export LEVELDB_HOME=$(cd $ROOT_HOME && cd vendor/leveldb && pwd)

if [[ "$1" == "clean" ]]; then
  echo --------------------
  echo Clean
  echo --------------------

  cd $SNAPPY_HOME
  git clean -fdx
  git reset --hard

  cd $LEVELDB_HOME
  git clean -fdx
  git reset --hard
fi

echo --------------------
echo Build Snappy
echo --------------------

cd $SNAPPY_HOME
mkdir -p build && cd build
if [[ "$OSTYPE" == "msys" ]]
then
  /mingw64/bin/cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_POSITION_INDEPENDENT_CODE=on -DBUILD_SHARED_LIBS=off -G "MSYS Makefiles" ..
else
  cmake -DCMAKE_POSITION_INDEPENDENT_CODE=on -DBUILD_SHARED_LIBS=off ..
fi
cmake --build .
cp ../*.h .

echo --------------------
echo Build LevelDB
echo --------------------

cd $LEVELDB_HOME
export LIBRARY_PATH=$SNAPPY_HOME/lib
export C_INCLUDE_PATH=$SNAPPY_HOME/include
export CPLUS_INCLUDE_PATH=$SNAPPY_HOME/include
mkdir -p build && cd build

if [[ "$OSTYPE" == "msys" ]]
then
  /mingw64/bin/cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=on -DLEVELDB_INSTALL=off "-DSNAPPY_HOME=$SNAPPY_HOME" -G "MSYS Makefiles" ..
else
  cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=on -DLEVELDB_INSTALL=off "-DSNAPPY_HOME=$SNAPPY_HOME" ..
fi
cmake --build .

echo --------------------
echo Copy LevelDB library
echo --------------------

cd $LEVELDB_HOME

if [[ "$OSTYPE" == "darwin"* ]]; then
  LEVELDB_FILE=libleveldb.dylib
  LEVELDB_ARCH=darwin
  OUTPUT_LEVELDB_FILE=
elif [[ "$OSTYPE" == "linux"* ]]; then
  LEVELDB_FILE=libleveldb.so
  if [[ $(uname -m) == "x86_64" ]]; then
    LEVELDB_ARCH=linux-x86-64
  else
    LEVELDB_ARCH=linux-x86
  fi
  OUTPUT_LEVELDB_FILE=
elif [[ "$OSTYPE" == "msys" ]]; then
  LEVELDB_FILE=libleveldb.dll
  if [[ "$MSYSTEM" == "MINGW64" ]]; then
    LEVELDB_ARCH=win32-x86-64
  else
    LEVELDB_ARCH=win32-x86
  fi
  OUTPUT_LEVELDB_FILE=leveldb.dll
fi

mkdir -p $ROOT_HOME/leveldb-jna-native/src/main/resources/$LEVELDB_ARCH/
cp $LEVELDB_HOME/build/$LEVELDB_FILE $ROOT_HOME/leveldb-jna-native/src/main/resources/$LEVELDB_ARCH/$OUTPUT_LEVELDB_FILE
