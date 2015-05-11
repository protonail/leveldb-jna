package com.protonail.leveldb.jna;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class LevelDBKeyIterator extends LevelDBIteratorBase<byte[]> {
    public LevelDBKeyIterator(LevelDB levelDB, LevelDBReadOptions readOptions) {
        super(levelDB, readOptions);
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
    }

    public byte[] next() {
        levelDB.checkDatabaseOpen();
        checkIteratorOpen();

        IntByReference resultLength = new IntByReference();
        PointerByReference pointerToKey = LevelDBNative.leveldb_iter_key(iterator, resultLength);
        byte[] key = pointerToKey.getPointer().getByteArray(0, resultLength.getValue());

        LevelDBNative.leveldb_iter_next(iterator);

        return key;
    }
}
