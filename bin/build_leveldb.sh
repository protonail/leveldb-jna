#!/bin/bash

set -e

export ROOT_HOME=$(cd "$(dirname "$0")" && cd .. && pwd)
export SNAPPY_HOME=$(cd $ROOT_HOME && cd vendor/snappy && pwd)
export LEVELDB_HOME=$(cd $ROOT_HOME && cd vendor/leveldb && pwd)
export RES_DIR="$ROOT_HOME/leveldb-jna-native/src/main/resources"


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

  rm -rf "$RES_DIR"
fi

echo --------------------
echo Build Snappy
echo --------------------

cd $SNAPPY_HOME
mkdir -p build && cd build
if [[ "$OSTYPE" == "msys" ]]; then
  /mingw64/bin/cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_POSITION_INDEPENDENT_CODE=on -DBUILD_SHARED_LIBS=off -DSNAPPY_BUILD_TESTS=off -G "MSYS Makefiles" ..
else
  if [[ -n $LEVELDB_TOOLCHAIN ]]; then
    cp "$LEVELDB_TOOLCHAIN" current.toolchain
    echo "Using custom toolchain"
    cat current.toolchain
  else
    echo "Using system compiler"
    touch current.toolchain
  fi

  cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_POSITION_INDEPENDENT_CODE=on -DBUILD_SHARED_LIBS=off -DSNAPPY_BUILD_TESTS=off -DCMAKE_TOOLCHAIN_FILE=current.toolchain ..
fi
cmake --build .
cp ../*.h .

echo --------------------
echo Build LevelDB
echo --------------------

cd $LEVELDB_HOME
export LIBRARY_PATH=$SNAPPY_HOME/build
export C_INCLUDE_PATH=$SNAPPY_HOME/include
export CPLUS_INCLUDE_PATH=$SNAPPY_HOME/include
mkdir -p build && cd build

if [[ "$OSTYPE" == "msys" ]]; then
  /mingw64/bin/cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=on -DLEVELDB_INSTALL=off "-DSNAPPY_HOME=$SNAPPY_HOME" -DLEVELDB_BUILD_TESTS=off -DLEVELDB_BUILD_BENCHMARKS=off -G "MSYS Makefiles" ..
else
  if [[ -n $LEVELDB_TOOLCHAIN ]]; then
    cp "$LEVELDB_TOOLCHAIN" current.toolchain
    echo "Using custom toolchain"
    cat current.toolchain
  else
    echo "Using system compiler"
    touch current.toolchain
  fi

  cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=on -DLEVELDB_INSTALL=off "-DSNAPPY_HOME=$SNAPPY_HOME" -DLEVELDB_BUILD_TESTS=off -DLEVELDB_BUILD_BENCHMARKS=off -DCMAKE_TOOLCHAIN_FILE=current.toolchain ..
fi
cmake --build .

echo --------------------
echo Copy LevelDB library
echo --------------------

cd $LEVELDB_HOME

if [[ -z $LEVELDB_ARCH ]]; then
  if [[ "$OSTYPE" == "darwin"* ]]; then
    LEVELDB_ARCH=darwin
  elif [[ "$OSTYPE" == "linux"* ]]; then
    if [[ $(uname -m) == "x86_64" ]]; then
      LEVELDB_ARCH=linux-x86-64
    else
      LEVELDB_ARCH=linux-x86
    fi
  elif [[ "$OSTYPE" == "msys" ]]; then
    LEVELDB_FILE=libleveldb.dll
    if [[ "$MSYSTEM" == "MINGW64" ]]; then
      LEVELDB_ARCH=win32-x86-64
    else
      LEVELDB_ARCH=win32-x86
    fi
  fi
fi

mkdir -p "$RES_DIR/$LEVELDB_ARCH"

cp "$LEVELDB_HOME/build/libleveldb.dll" "$RES_DIR/$LEVELDB_ARCH/leveldb.dll" ||
  cp "$LEVELDB_HOME/build/libleveldb.so" "$RES_DIR/$LEVELDB_ARCH/" ||
  cp "$LEVELDB_HOME/build/libleveldb.dylib" "$RES_DIR/$LEVELDB_ARCH/"
