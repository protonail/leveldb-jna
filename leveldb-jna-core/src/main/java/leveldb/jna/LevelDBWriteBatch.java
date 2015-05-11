package leveldb.jna;

import com.sun.jna.Native;

public class LevelDBWriteBatch implements AutoCloseable {
    protected LevelDBNative.WriteBatch writeBatch;

    public LevelDBWriteBatch() {
        writeBatch = LevelDBNative.leveldb_writebatch_create();
    }

    @Override
    public void close() {
        checkWriteBatchOpen();

        LevelDBNative.leveldb_writebatch_destroy(writeBatch);
        writeBatch = null;
    }

    public void clear() {
        checkWriteBatchOpen();

        LevelDBNative.leveldb_writebatch_clear(writeBatch);
    }

    public void put(byte[] key, byte[] value) {
        checkWriteBatchOpen();

        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            long valueLength = value != null ? value.length : 0;
            LevelDBNative.leveldb_writebatch_put(writeBatch, key, keyLength, value, valueLength);
        } else {
            int keyLength = key != null ? key.length : 0;
            int valueLength = value != null ? value.length : 0;
            LevelDBNative.leveldb_writebatch_put(writeBatch, key, keyLength, value, valueLength);
        }
    }

    public void delete(byte[] key) {
        checkWriteBatchOpen();

        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_writebatch_delete(writeBatch, key, keyLength);
        } else {
            int keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_writebatch_delete(writeBatch, key, keyLength);
        }
    }

    protected void checkWriteBatchOpen() {
        if (writeBatch == null) {
            throw new LevelDBException("LevelDB write batch was closed.");
        }
    }
}
