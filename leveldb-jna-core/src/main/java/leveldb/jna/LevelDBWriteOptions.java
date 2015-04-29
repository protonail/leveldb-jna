package leveldb.jna;

public class LevelDBWriteOptions implements AutoCloseable {
    protected LevelDBNative.WriteOptions writeOptions;

    private boolean sync = false;

    public LevelDBWriteOptions() {
        writeOptions = LevelDBNative.leveldb_writeoptions_create();
        setSync(sync);
    }

    public void close() {
        if (writeOptions != null) {
            LevelDBNative.leveldb_writeoptions_destroy(writeOptions);
            writeOptions = null;
        }
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        if (writeOptions != null) {
            this.sync = sync;
            LevelDBNative.leveldb_writeoptions_set_sync(writeOptions, (byte) (sync ? 1 : 0));
        }
    }
}
