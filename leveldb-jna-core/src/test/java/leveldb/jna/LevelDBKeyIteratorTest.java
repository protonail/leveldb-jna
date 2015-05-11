package leveldb.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Arrays;

public class LevelDBKeyIteratorTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void open_and_close() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    LevelDBKeyIterator keyIterator = new LevelDBKeyIterator(levelDB, readOptions);
                    keyIterator.close();
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
                    LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions);
                    iterator.close();
                    iterator.close();
                }
            }
        }
    }

    @Test
    public void iterate_over_empty_keys() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
                        while (iterator.hasNext()) {
                            Assert.fail("Empty database doesn't has keys.");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void iterate_over_keys() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                byte[] key = new byte[] {42};
                byte[] value = new byte[] {43};

                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    levelDB.put(key, value, writeOptions);
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
                        int loop = 0;
                        while (iterator.hasNext()) {
                            byte[] currentKey = iterator.next();
                            Assert.assertArrayEquals(key, currentKey);

                            loop += 1;
                        }

                        Assert.assertEquals(1, loop);
                    }
                }
            }
        }
    }

    @Test
    public void seek_to_first() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    for (int i = 0; i < 10; i++) {
                        byte[] bytes = {(byte) i};

                        levelDB.put(bytes, bytes, writeOptions);
                    }
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
                        while (iterator.hasNext()) {
                            byte[] currentKey = iterator.next();

                            if (Arrays.equals(new byte[]{5}, currentKey)) {
                                break;
                            }
                        }

                        iterator.seekToFirst();
                        byte[] currentKey = iterator.next();

                        Assert.assertArrayEquals(new byte[]{0}, currentKey);
                    }
                }
            }
        }
    }

    @Test
    public void seek_to_last() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    for (int i = 0; i < 10; i++) {
                        byte[] bytes = {(byte) i};

                        levelDB.put(bytes, bytes, writeOptions);
                    }
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
                        iterator.seekToLast();

                        // seekToLast changed direction of iterator to revers
                        // so, need to change it to forward
                        iterator.next();

                        Assert.assertFalse(iterator.hasNext());
                    }
                }
            }
        }
    }

    @Test
    public void seek_to_key() {
        try(LevelDBOptions options = new LevelDBOptions()) {
            options.setCreateIfMissing(true);

            try(LevelDB levelDB = new LevelDB(testFolder.getRoot().getAbsolutePath(), options)) {
                try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
                    for (int i = 0; i < 10; i++) {
                        byte[] bytes = {(byte) i};

                        levelDB.put(bytes, bytes, writeOptions);
                    }
                }

                try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
                    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
                        iterator.seekToKey(new byte[]{5});

                        Assert.assertTrue(iterator.hasNext());
                        Assert.assertArrayEquals(new byte[]{5}, iterator.next());
                    }
                }
            }
        }
    }
}
