package leveldb.jna;

public class LevelDBWriteBatch implements AutoCloseable {
    protected LevelDBNative.WriteBatch writeBatch;

    public LevelDBWriteBatch() {
        writeBatch = LevelDBNative.leveldb_writebatch_create();
    }

    @Override
    public void close() {
        if (writeBatch != null) {
            LevelDBNative.leveldb_writebatch_destroy(writeBatch);
            writeBatch = null;
        }
    }

    public void clear() {
        LevelDBNative.leveldb_writebatch_clear(writeBatch);
    }

    public void put(byte[] key, byte[] value) {
        LevelDBNative.leveldb_writebatch_put(writeBatch, key, key.length, value, value.length);
    }

    public void delete(byte[] key) {
        LevelDBNative.leveldb_writebatch_delete(writeBatch, key, key.length);
    }
}
