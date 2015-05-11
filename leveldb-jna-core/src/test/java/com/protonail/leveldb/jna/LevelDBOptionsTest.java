package com.protonail.leveldb.jna;

import org.junit.Assert;
import org.junit.Test;

public class LevelDBOptionsTest {
    @Test
    public void create_and_close() {
        LevelDBOptions options = new LevelDBOptions();
        options.close();
    }

    @Test(expected = LevelDBException.class)
    public void create_and_close_twice() {
        LevelDBOptions options = new LevelDBOptions();
        options.close();
        options.close();
    }

    @Test
    public void set_create_if_missing() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(false, options.isCreateIfMissing());

            options.setCreateIfMissing(true);

            Assert.assertEquals(true, options.isCreateIfMissing());
        }
    }

    @Test
    public void set_error_if_exists() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(false, options.isErrorIfExists());

            options.setErrorIfExists(true);

            Assert.assertEquals(true, options.isErrorIfExists());
        }
    }

    @Test
    public void set_paranoid_checks() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(false, options.isParanoidChecks());

            options.setParanoidChecks(true);

            Assert.assertEquals(true, options.isParanoidChecks());
        }
    }

    @Test
    public void set_compression_type() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(LevelDBCompressionType.SnappyCompression, options.getCompressionType());

            options.setCompressionType(LevelDBCompressionType.NoCompression);

            Assert.assertEquals(LevelDBCompressionType.NoCompression, options.getCompressionType());
        }
    }

    @Test
    public void set_write_buffer_size() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(4 * 1024 * 1204, options.getWriteBufferSize());

            options.setWriteBufferSize(10 * 1024 * 1204);

            Assert.assertEquals(10 * 1024 * 1204, options.getWriteBufferSize());
        }
    }

    @Test
    public void set_max_open_files() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(1000, options.getMaxOpenFiles());

            options.setMaxOpenFiles(2000);

            Assert.assertEquals(2000, options.getMaxOpenFiles());
        }
    }

    @Test
    public void set_block_size() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(4096, options.getBlockSize());

            options.setBlockSize(8192);

            Assert.assertEquals(8192, options.getBlockSize());
        }
    }

    @Test
    public void set_block_restart_interval() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            Assert.assertEquals(16, options.getBlockRestartInterval());

            options.setBlockRestartInterval(32);

            Assert.assertEquals(32, options.getBlockRestartInterval());
        }
    }
}
