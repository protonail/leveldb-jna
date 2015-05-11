package leveldb.jna;

import org.junit.Test;

public class LevelDBWriteBatchTest {
    @Test
    public void create_and_close() {
        LevelDBWriteBatch writeBatch = new LevelDBWriteBatch();
        writeBatch.close();
    }

    @Test(expected = LevelDBException.class)
    public void create_and_close_twice() {
        LevelDBWriteBatch writeBatch = new LevelDBWriteBatch();
        writeBatch.close();
        writeBatch.close();
    }

    @Test
    public void put() {
        try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
            writeBatch.put(new byte[]{42}, new byte[]{43});
        }
    }

    @Test
    public void delete() {
        try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
            writeBatch.delete(new byte[]{42});
        }
    }

    @Test
    public void clear() {
        try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
            writeBatch.clear();
        }
    }
}
