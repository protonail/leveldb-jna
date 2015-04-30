package leveldb.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class LevelDB implements AutoCloseable {
    protected LevelDBNative.LevelDB levelDB;

    public LevelDB(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        levelDB = LevelDBNative.leveldb_open(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public void close() {
        if (levelDB != null) {
            LevelDBNative.leveldb_close(levelDB);
            levelDB = null;
        }
    }

    public byte[] get(byte[] key, LevelDBReadOptions readOptions) {
        IntByReference resultLength = new IntByReference();

        long keyLength = key != null ? key.length : 0;
        PointerByReference error = new PointerByReference();
        PointerByReference result = LevelDBNative.leveldb_get(levelDB, readOptions.readOptions, key, keyLength, resultLength, error);
        LevelDBNative.checkError(error);

        return result != null ? result.getPointer().getByteArray(0, resultLength.getValue()) : null;
    }

    public void put(byte[] key, byte[] value, LevelDBWriteOptions writeOptions) {
        long keyLength = key != null ? key.length : 0;
        long valueLength = value != null ? value.length : 0;
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_put(levelDB, writeOptions.writeOptions, key, keyLength, value, valueLength, error);
        LevelDBNative.checkError(error);
    }

    public void write(LevelDBWriteBatch writeBatch, LevelDBWriteOptions writeOptions) {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_write(levelDB, writeOptions.writeOptions, writeBatch.writeBatch, error);
        LevelDBNative.checkError(error);
    }

    public void delete(byte[] key, LevelDBWriteOptions writeOptions) {
        long keyLength = key != null ? key.length : 0;
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_delete(levelDB, writeOptions.writeOptions, key, keyLength, error);
        LevelDBNative.checkError(error);
    }

    public String property(String property) {
        return LevelDBNative.leveldb_property_value(levelDB, property);
    }

    public long[] approximateSizes(Range ...ranges) {
        if (ranges.length == 0)
            return new long[0];

        Memory startKeys = new Memory(ranges.length * Pointer.SIZE);
        Memory limitKeys = new Memory(ranges.length * Pointer.SIZE);

        long[] startLengths = new long[ranges.length];
        long[] limitLengths = new long[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            int startKeyLength = ranges[i].getStartKey().length;
            Memory startKeyMemory = new Memory(startKeyLength);
            startKeyMemory.write(0, ranges[i].getStartKey(), 0, startKeyLength);

            startKeys.setPointer(i * Pointer.SIZE, startKeyMemory);
            startLengths[i] = startKeyLength;

            int limitKeyLength = ranges[i].getLimitKey().length;
            Memory limitKeyMemory = new Memory(limitKeyLength);
            limitKeyMemory.write(0, ranges[i].getLimitKey(), 0, limitKeyLength);

            limitKeys.setPointer(i * Pointer.SIZE, limitKeyMemory);
            limitLengths[i] = limitKeyLength;
        }
        Pointer sizes = new Memory(ranges.length * Native.getNativeSize(Long.TYPE));
        LevelDBNative.leveldb_approximate_sizes(levelDB, ranges.length, startKeys, startLengths, limitKeys, limitLengths, sizes);

        return sizes.getLongArray(0, ranges.length);
    }

    public static void repair(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_repair_db(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public static void destroy(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_destroy_db(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public static int majorVersion() {
        return LevelDBNative.leveldb_major_version();
    }

    public static int minorVersion() {
        return LevelDBNative.leveldb_minor_version();
    }
}
