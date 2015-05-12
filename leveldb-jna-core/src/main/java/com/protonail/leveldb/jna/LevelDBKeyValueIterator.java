package com.protonail.leveldb.jna;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

public class LevelDBKeyValueIterator extends LevelDBIteratorBase<KeyValuePair> {
    public LevelDBKeyValueIterator(LevelDB levelDB, LevelDBReadOptions readOptions) {
        super(levelDB, readOptions);
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
    }

    public KeyValuePair next() {
        levelDB.checkDatabaseOpen();
        checkIteratorOpen();

        PointerByReference resultPointer;
        PointerByReference resultLengthPointer = new PointerByReference();
        long resultLength;

        resultPointer = LevelDBNative.leveldb_iter_key(iterator, resultLengthPointer);
        if (Native.POINTER_SIZE == 8) {
            resultLength = resultLengthPointer.getPointer().getLong(0);
        } else {
            resultLength = resultLengthPointer.getPointer().getInt(0);
        }
        byte[] key = resultPointer.getPointer().getByteArray(0, (int) resultLength);

        resultPointer = LevelDBNative.leveldb_iter_value(iterator, resultLengthPointer);
        if (Native.POINTER_SIZE == 8) {
            resultLength = resultLengthPointer.getPointer().getLong(0);
        } else {
            resultLength = resultLengthPointer.getPointer().getInt(0);
        }
        byte[] value = resultPointer.getPointer().getByteArray(0, (int) resultLength);

        LevelDBNative.leveldb_iter_next(iterator);

        return new KeyValuePair(key, value);
    }
}
