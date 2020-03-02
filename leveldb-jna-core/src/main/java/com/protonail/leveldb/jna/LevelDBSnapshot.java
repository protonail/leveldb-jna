package com.protonail.leveldb.jna;

public class LevelDBSnapshot implements AutoCloseable {
    private boolean closed = false;
    private final transient LevelDBNative.LevelDB levelDB;
    private final transient LevelDBNative.Snapshot snapshot;

    LevelDBSnapshot(LevelDBNative.LevelDB levelDB, LevelDBNative.Snapshot snapshot) {
        this.levelDB = levelDB;
        this.snapshot = snapshot;
    }

    LevelDBNative.Snapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public synchronized void close() throws Exception {
        if (!closed) LevelDBNative.leveldb_release_snapshot(levelDB, snapshot);
        closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
    }
}
