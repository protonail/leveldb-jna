package leveldb.jna;

import org.junit.Assert;
import org.junit.Test;

public class LevelDBReadOptionsTest {
    @Test
    public void create_and_close() {
        LevelDBReadOptions readOptions = new LevelDBReadOptions();
        readOptions.close();
    }

    @Test
    public void create_and_close_twice() {
        LevelDBReadOptions readOptions = new LevelDBReadOptions();
        readOptions.close();
        readOptions.close();
    }

    @Test
    public void set_verify_checksum() {
        try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
            Assert.assertEquals(false, readOptions.isVerifyChecksum());

            readOptions.setVerifyChecksum(true);

            Assert.assertEquals(true, readOptions.isVerifyChecksum());
        }
    }

    @Test
    public void set_fill_cache() {
        try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
            Assert.assertEquals(true, readOptions.isFillCache());

            readOptions.setFillCache(false);

            Assert.assertEquals(false, readOptions.isFillCache());
        }
    }
}
