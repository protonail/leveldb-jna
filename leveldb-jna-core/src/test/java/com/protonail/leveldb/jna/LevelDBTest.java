package com.protonail.leveldb.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public class LevelDBTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void open_and_close() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options);
            levelDB.close();
        }
    }

    @Test
    public void open_and_close_twice() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options);
            levelDB.close();
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void open_database_twice() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB1 = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                boolean hasException = false;
                try(LevelDB levelDB2 = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    Assert.fail("Expected LevelDBException about already used database");
                } catch (LevelDBException e) {
                    hasException = true;
                }
                Assert.assertTrue(hasException);
            }
        }
    }

    @Test
    public void get_null_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    byte[] result = levelDB.get(null, readOptions);
                    Assert.assertEquals(null, result);
                }
            }
        }
    }

    @Test
    public void get_not_exists_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    byte[] result = levelDB.get(new byte[]{}, readOptions);
                    Assert.assertEquals(null, result);
                }
            }
        }
    }

    @Test
    public void put_null_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    levelDB.put(null, new byte[]{}, writeOptions);
                }
            }
        }
    }

    @Test
    public void put_null_key_and_null_value() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    levelDB.put(null, null, writeOptions);
                }
            }
        }
    }

    @Test
    public void put_and_get() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            byte[] key = new byte[] {42};
            byte[] value = new byte[] {43};

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(key, value, writeOptions);
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    byte[] result = levelDB.get(key, readOptions);

                    Assert.assertArrayEquals(value, result);
                }
            }
        }
    }

    @Test
    public void write_empty_write_batch() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
                        levelDB.write(writeBatch, writeOptions);
                    }
                }
            }
        }
    }

    @Test
    public void write_write_batch() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    byte[] keyForDelete = new byte[] {42};
                    byte[] valueForDelete = new byte[] {43};

                    byte[] keyForPut = new byte[] {44};
                    byte[] valueForPut = new byte[] {45};

                    levelDB.put(keyForDelete, valueForDelete, writeOptions);

                    try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
                        writeBatch.delete(keyForDelete);
                        writeBatch.put(keyForPut, valueForPut);

                        levelDB.write(writeBatch, writeOptions);
                    }

                    try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                        Assert.assertNull(levelDB.get(keyForDelete, readOptions));
                        Assert.assertArrayEquals(valueForPut, levelDB.get(keyForPut, readOptions));
                    }
                }
            }
        }
    }

    @Test
    public void write_cleared_write_batch() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    byte[] keyForDelete = new byte[] {42};
                    byte[] valueForDelete = new byte[] {43};

                    byte[] keyForPut = new byte[] {44};
                    byte[] valueForPut = new byte[] {45};

                    levelDB.put(keyForDelete, valueForDelete, writeOptions);

                    try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
                        writeBatch.delete(keyForDelete);
                        writeBatch.put(keyForPut, valueForPut);
                        writeBatch.clear();

                        levelDB.write(writeBatch, writeOptions);
                    }

                    try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                        Assert.assertArrayEquals(valueForDelete, levelDB.get(keyForDelete, readOptions));
                        Assert.assertNull(levelDB.get(keyForPut, readOptions));
                    }
                }
            }
        }
    }

    @Test
    public void delete_null_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    levelDB.delete(null, writeOptions);
                }
            }
        }
    }

    @Test
    public void delete_not_esists_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                    levelDB.delete(new byte[]{42}, writeOptions);
                }
            }
        }
    }

    @Test
    public void delete_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            byte[] key = new byte[] {42};
            byte[] value = new byte[] {43};


            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(key, value, writeOptions);
                }

                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.delete(key, writeOptions);
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    byte[] result = levelDB.get(key, readOptions);

                    Assert.assertEquals(null, result);
                }
            }
        }
    }

    @Test
    public void get_unknown_property() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                String result = levelDB.property("unknown-property");

                Assert.assertEquals(null, result);
            }
        }
    }

    @Test
    public void get_known_property() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                String result = levelDB.property("leveldb.stats");

                Assert.assertNotEquals(null, result);
            }
        }
    }

    @Test
    public void approximate_sizes_for_empty_ranges() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                long[] sizes = levelDB.approximateSizes();

                Assert.assertArrayEquals(new long[0], sizes);
            }
        }
    }

    @Test
    public void approximate_sizes_for_ranges() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    for (int i = 0; i < 20000; i++) {
                        levelDB.put(String.format("k%020d", i).getBytes(), String.format("v%020d", i).getBytes(), writeOptions);
                    }
                }
            }
        }

        try(LevelDBOptions options = new LevelDBOptions()) {
            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                Range range1 = new Range("a".getBytes(), "k00000000000000010000".getBytes());
                Range range2 = new Range("k00000000000000010000".getBytes(), "z".getBytes());

                long[] sizes = levelDB.approximateSizes(range1, range2);

                Assert.assertTrue(sizes[0] > 0);
                Assert.assertTrue(sizes[1] > 0);
            }
        }
    }

    @Test
    public void compact_all_range() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                levelDB.compactRange(null, null);
            }
        }
    }

    @Test
    public void compact_range() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                levelDB.compactRange(new byte[] {'a'}, new byte[] {'z'});
            }
        }
    }

    @Test
    public void repair_not_esists_database() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            LevelDB.repair(testFolder.getRoot().getAbsolutePath(), options);
        }
    }

    @Test
    public void repair_database() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(new byte[] {42}, new byte[] {43}, writeOptions);
                }
            }
        }

        try(LevelDBOptions options = new LevelDBOptions()) {
            LevelDB.repair(testFolder.getRoot().getAbsolutePath(), options);
        }
    }

    @Test
    public void destroy_not_esists_database() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            LevelDB.destroy(testFolder.getRoot().getAbsolutePath(), options);
        }
    }

    @Test
    public void destroy_database() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try (LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(new byte[] {42}, new byte[] {43}, writeOptions);
                }
            }
        }

        try(LevelDBOptions options = new LevelDBOptions()) {
            LevelDB.destroy(testFolder.getRoot().getAbsolutePath(), options);
        }

        Assert.assertFalse(new File(testFolder.getRoot(), "CURRENT").exists());
    }

    @Test
    public void get_version() {
        Assert.assertTrue(LevelDB.majorVersion() >= 1);
        Assert.assertTrue(LevelDB.minorVersion() >= 0);
    }
}
