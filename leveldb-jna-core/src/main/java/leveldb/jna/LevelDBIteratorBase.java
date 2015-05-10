package leveldb.jna;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

import java.util.Iterator;

public abstract class LevelDBIteratorBase<TElement> implements AutoCloseable, Iterator<TElement> {
    protected LevelDBNative.Iterator iterator;

    public LevelDBIteratorBase(LevelDB levelDB, LevelDBReadOptions readOptions) {
        iterator = LevelDBNative.leveldb_create_iterator(levelDB.levelDB, readOptions.readOptions);
    }

    public void close() {
        if (iterator != null) {
            LevelDBNative.leveldb_iter_destroy(iterator);
            iterator = null;
        }
    }

    private void checkError() {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_iter_get_error(iterator, error);
        LevelDBNative.checkError(error);
    }

    public boolean hasNext() {
        boolean hasNext = LevelDBNative.leveldb_iter_valid(iterator) != 0;
        checkError();
        return hasNext;
    }

    public void seekToFirst() {
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
        checkError();
    }

    public void seekToLast() {
        LevelDBNative.leveldb_iter_seek_to_last(iterator);
        checkError();
    }

    public void seekToKey(byte[] key) {
        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_iter_seek(iterator, key, keyLength);
        } else {
            int keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_iter_seek(iterator, key, keyLength);
        }
        checkError();
    }
}
