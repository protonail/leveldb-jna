package leveldb.jna;

import org.junit.Assert;
import org.junit.Test;

public class LevelDBWriteOptionsTest {
    @Test
    public void create_and_close() {
        LevelDBWriteOptions writeOptions = new LevelDBWriteOptions();
        writeOptions.close();
    }

    @Test(expected = LevelDBException.class)
    public void create_and_close_twice() {
        LevelDBWriteOptions writeOptions = new LevelDBWriteOptions();
        writeOptions.close();
        writeOptions.close();
    }

    @Test
    public void set_sync() {
        try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
            Assert.assertEquals(false, writeOptions.isSync());

            writeOptions.setSync(true);

            Assert.assertEquals(true, writeOptions.isSync());
        }
    }

}
