package leveldb.jna;

public class LevelDBReadOptions implements AutoCloseable {
    protected LevelDBNative.ReadOptions readOptions;

    private boolean verifyChecksum = false;
    private boolean fillCache = true;

    public LevelDBReadOptions() {
        readOptions = LevelDBNative.leveldb_readoptions_create();
        setVerifyChecksum(verifyChecksum);
        setFillCache(fillCache);
    }

    public void close() {
        if (readOptions != null) {
            LevelDBNative.leveldb_readoptions_destroy(readOptions);
            readOptions = null;
        }
    }

    public boolean isVerifyChecksum() {
        return verifyChecksum;
    }

    public void setVerifyChecksum(boolean verifyChecksum) {
        if (readOptions != null) {
            this.verifyChecksum = verifyChecksum;
            LevelDBNative.leveldb_readoptions_set_verify_checksums(readOptions, (byte) (verifyChecksum ? 1 : 0));
        }
    }

    public boolean isFillCache() {
        return fillCache;
    }

    public void setFillCache(boolean fillCache) {
        if (readOptions != null) {
            this.fillCache = fillCache;
            LevelDBNative.leveldb_readoptions_set_fill_cache(readOptions, (byte) (fillCache ? 1 : 0));
        }
    }
}
