package leveldb.jna;

import com.sun.jna.Native;

public class LevelDBOptions implements AutoCloseable {
    protected LevelDBNative.Options options;

    private boolean createIfMissing = false;
    private boolean errorIfExists = false;
    private boolean paranoidChecks = false;
    private LevelDBCompressionType compressionType = LevelDBCompressionType.SnappyCompression;
    private long writeBufferSize = 4 * 1024 * 1204;
    private int maxOpenFiles = 1000;
    private long blockSize = 4096;
    private int blockRestartInterval = 16;

    public LevelDBOptions() {
        options = LevelDBNative.leveldb_options_create();
        setCreateIfMissing(createIfMissing);
        setErrorIfExists(errorIfExists);
        setParanoidChecks(paranoidChecks);
        setCompressionType(compressionType);
        setWriteBufferSize(writeBufferSize);
        setMaxOpenFiles(maxOpenFiles);
        setBlockSize(blockSize);
        setBlockRestartInterval(blockRestartInterval);
    }

    public void close() {
        if (options != null) {
            LevelDBNative.leveldb_options_destroy(options);
            options = null;
        }
    }

    public boolean isCreateIfMissing() {
        return createIfMissing;
    }

    public void setCreateIfMissing(boolean createIfMissing) {
        if (options != null) {
            this.createIfMissing = createIfMissing;
            LevelDBNative.leveldb_options_set_create_if_missing(options, (byte) (createIfMissing ? 1 : 0));
        }
    }

    public boolean isErrorIfExists() {
        return errorIfExists;
    }

    public void setErrorIfExists(boolean errorIfExists) {
        if (options != null) {
            this.errorIfExists = errorIfExists;
            LevelDBNative.leveldb_options_set_error_if_exists(options, (byte) (errorIfExists ? 1 : 0));
        }
    }

    public boolean isParanoidChecks() {
        return paranoidChecks;
    }

    public void setParanoidChecks(boolean paranoidChecks) {
        if (options != null) {
            this.paranoidChecks = paranoidChecks;
            LevelDBNative.leveldb_options_set_paranoid_checks(options, (byte) (paranoidChecks ? 1 : 0));
        }
    }

    public LevelDBCompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(LevelDBCompressionType compressionType) {
        if (options != null) {
            this.compressionType = compressionType;
            LevelDBNative.leveldb_options_set_compression(options, compressionType.getCompressionType());
        }
    }

    public long getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(long writeBufferSize) {
        if (options != null) {
            this.writeBufferSize = writeBufferSize;
            if (Native.POINTER_SIZE == 8) {
                LevelDBNative.leveldb_options_set_write_buffer_size(options, writeBufferSize);
            } else {
                LevelDBNative.leveldb_options_set_write_buffer_size(options, (int) writeBufferSize);
            }
        }
    }

    public int getMaxOpenFiles() {
        return maxOpenFiles;
    }

    public void setMaxOpenFiles(int maxOpenFiles) {
        if (options != null) {
            this.maxOpenFiles = maxOpenFiles;
            LevelDBNative.leveldb_options_set_max_open_files(options, maxOpenFiles);
        }
    }

    public long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(long blockSize) {
        if (options != null) {
            this.blockSize = blockSize;
            if (Native.POINTER_SIZE == 8) {
                LevelDBNative.leveldb_options_set_block_size(options, blockSize);
            } else {
                LevelDBNative.leveldb_options_set_block_size(options, (int) blockSize);
            }
        }
    }

    public int getBlockRestartInterval() {
        return blockRestartInterval;
    }

    public void setBlockRestartInterval(int blockRestartInterval) {
        if (options != null) {
            this.blockRestartInterval = blockRestartInterval;
            LevelDBNative.leveldb_options_set_block_restart_interval(options, blockRestartInterval);
        }
    }
}
