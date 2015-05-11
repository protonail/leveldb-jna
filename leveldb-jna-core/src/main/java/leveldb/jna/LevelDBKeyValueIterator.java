package leveldb.jna;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class LevelDBKeyValueIterator extends LevelDBIteratorBase<KeyValuePair> {
    public LevelDBKeyValueIterator(LevelDB levelDB, LevelDBReadOptions readOptions) {
        super(levelDB, readOptions);
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
    }

    public KeyValuePair next() {
        levelDB.checkDatabaseOpen();
        checkIteratorOpen();

        IntByReference resultLength = new IntByReference();
        PointerByReference result;

        result = LevelDBNative.leveldb_iter_key(iterator, resultLength);
        byte[] key = result.getPointer().getByteArray(0, resultLength.getValue());

        result = LevelDBNative.leveldb_iter_value(iterator, resultLength);
        byte[] value = result.getPointer().getByteArray(0, resultLength.getValue());

        LevelDBNative.leveldb_iter_next(iterator);

        return new KeyValuePair(key, value);
    }
}
