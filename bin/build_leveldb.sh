set -e

export ROOT_HOME=$(cd `dirname "{0}"` && pwd)
export SNAPPY_HOME=$(cd `dirname "{0}"` && cd vendor/snappy && pwd)
export LEVELDB_HOME=$(cd `dirname "{0}"` && cd vendor/leveldb && pwd)

# Build Snappy
echo --------------------
echo Snappy
echo --------------------

cd $SNAPPY_HOME
git clean -fdx
git reset --hard
patch -N $SNAPPY_HOME/autogen.sh $ROOT_HOME/patches/autogen.sh.osx.patch
patch -N $SNAPPY_HOME/configure.ac $ROOT_HOME/patches/configure.ac.osx.patch
./autogen.sh
./configure --disable-shared --with-pic --prefix=$SNAPPY_HOME
make install

# Build LevelDB
echo --------------------
echo LevelDB
echo --------------------

cd $LEVELDB_HOME
export LIBRARY_PATH=$SNAPPY_HOME/lib
export C_INCLUDE_PATH=$SNAPPY_HOME/include
export CPLUS_INCLUDE_PATH=$SNAPPY_HOME/include
git clean -fdx
git reset --hard
make
