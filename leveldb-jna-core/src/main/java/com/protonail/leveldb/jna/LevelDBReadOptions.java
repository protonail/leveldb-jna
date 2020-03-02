package com.protonail.leveldb.jna;

public class LevelDBReadOptions implements AutoCloseable {
    protected LevelDBNative.ReadOptions readOptions;

    private boolean verifyChecksum = false;
    private boolean fillCache = true;
    private LevelDBSnapshot snapshot;

    public LevelDBReadOptions() {
        readOptions = LevelDBNative.leveldb_readoptions_create();
        setVerifyChecksum(verifyChecksum);
        setFillCache(fillCache);
    }

    public synchronized void close() {
        checkReadOptionsOpen();

        LevelDBNative.leveldb_readoptions_destroy(readOptions);
        readOptions = null;
    }

    public boolean isVerifyChecksum() {
        return verifyChecksum;
    }

    public void setVerifyChecksum(boolean verifyChecksum) {
        checkReadOptionsOpen();

        this.verifyChecksum = verifyChecksum;
        LevelDBNative.leveldb_readoptions_set_verify_checksums(readOptions, (byte) (verifyChecksum ? 1 : 0));
    }

    public boolean isFillCache() {
        return fillCache;
    }

    public void setFillCache(boolean fillCache) {
        checkReadOptionsOpen();

        this.fillCache = fillCache;
        LevelDBNative.leveldb_readoptions_set_fill_cache(readOptions, (byte) (fillCache ? 1 : 0));
    }

    public LevelDBSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(LevelDBSnapshot snapshot) {
        checkReadOptionsOpen();

        this.snapshot = snapshot;
        LevelDBNative.leveldb_readoptions_set_snapshot(readOptions, snapshot.getSnapshot());
    }

    protected void checkReadOptionsOpen() {
        if (readOptions == null) {
            throw new LevelDBException("LevelDB read options was closed.");
        }
    }
}
