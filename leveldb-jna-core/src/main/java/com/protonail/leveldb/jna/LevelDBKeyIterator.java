package com.protonail.leveldb.jna;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

public class LevelDBKeyIterator extends LevelDBIteratorBase<byte[]> {
    public LevelDBKeyIterator(LevelDB levelDB, LevelDBReadOptions readOptions) {
        super(levelDB, readOptions);
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
    }

    public byte[] next() {
        levelDB.checkDatabaseOpen();
        checkIteratorOpen();

        PointerByReference resultLengthPointer = new PointerByReference();
        PointerByReference resultPointer = LevelDBNative.leveldb_iter_key(iterator, resultLengthPointer);

        long resultLength;
        if (Native.POINTER_SIZE == 8) {
            resultLength = resultLengthPointer.getPointer().getLong(0);
        } else {
            resultLength = resultLengthPointer.getPointer().getInt(0);
        }

        byte[] key = resultPointer.getPointer().getByteArray(0, (int) resultLength);

        LevelDBNative.leveldb_iter_next(iterator);

        return key;
    }
}
