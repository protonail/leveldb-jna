package leveldb.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LevelDBKeyValueIteratorTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void open_and_close() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    LevelDBKeyValueIterator iterator = new LevelDBKeyValueIterator(levelDB, readOptions);
                    iterator.close();
                }
            }
        }
    }

    @Test(expected = LevelDBException.class)
    public void open_and_close_twice() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    LevelDBKeyValueIterator iterator = new LevelDBKeyValueIterator(levelDB, readOptions);
                    iterator.close();
                    iterator.close();
                }
            }
        }
    }

    @Test
    public void iterate_over_empty_keys_and_values() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyValueIterator iterator = new LevelDBKeyValueIterator(levelDB, readOptions)) {
                        while (iterator.hasNext()) {
                            Assert.fail("Empty database doesn't has keys.");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void iterate_over_keys_and_values() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                byte[] key = new byte[] {42};
                byte[] value = new byte[] {43};

                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(key, value, writeOptions);
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyValueIterator iterator = new LevelDBKeyValueIterator(levelDB, readOptions)) {
                        int loop = 0;
                        while (iterator.hasNext()) {
                            KeyValuePair pair = iterator.next();
                            Assert.assertArrayEquals(key, pair.getKey());
                            Assert.assertArrayEquals(value, pair.getValue());

                            loop += 1;
                        }

                        Assert.assertEquals(1, loop);
                    }
                }
            }
        }
    }
}
