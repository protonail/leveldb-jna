package leveldb.jna;

import org.junit.Assert;
import org.junit.Test;

public class LevelDBOptionsTest {
    @Test
    public void create_and_close() {
        LevelDBOptions options = new LevelDBOptions();
        options.close();
    }

    @Test
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
}
