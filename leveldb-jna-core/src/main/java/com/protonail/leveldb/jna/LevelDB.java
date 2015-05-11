package com.protonail.leveldb.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class LevelDB implements AutoCloseable {
    protected LevelDBNative.LevelDB levelDB;

    public LevelDB(String levelDBDirectory, LevelDBOptions options) {
        options.checkOptionsOpen();

        PointerByReference error = new PointerByReference();
        levelDB = LevelDBNative.leveldb_open(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public void close() {
        checkDatabaseOpen();

        LevelDBNative.leveldb_close(levelDB);
        levelDB = null;
    }

    public byte[] get(byte[] key, LevelDBReadOptions readOptions) {
        checkDatabaseOpen();
        readOptions.checkReadOptionsOpen();

        IntByReference resultLength = new IntByReference();

        PointerByReference error = new PointerByReference();
        PointerByReference result;
        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            result = LevelDBNative.leveldb_get(levelDB, readOptions.readOptions, key, keyLength, resultLength, error);
        } else {
            int keyLength = key != null ? key.length : 0;
            result = LevelDBNative.leveldb_get(levelDB, readOptions.readOptions, key, keyLength, resultLength, error);
        }
        LevelDBNative.checkError(error);

        return result != null ? result.getPointer().getByteArray(0, resultLength.getValue()) : null;
    }

    public void put(byte[] key, byte[] value, LevelDBWriteOptions writeOptions) {
        checkDatabaseOpen();
        writeOptions.checkWriteOptionsOpen();

        PointerByReference error = new PointerByReference();
        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            long valueLength = value != null ? value.length : 0;
            LevelDBNative.leveldb_put(levelDB, writeOptions.writeOptions, key, keyLength, value, valueLength, error);
        } else {
            int keyLength = key != null ? key.length : 0;
            int valueLength = value != null ? value.length : 0;
            LevelDBNative.leveldb_put(levelDB, writeOptions.writeOptions, key, keyLength, value, valueLength, error);
        }
        LevelDBNative.checkError(error);
    }

    public void write(LevelDBWriteBatch writeBatch, LevelDBWriteOptions writeOptions) {
        checkDatabaseOpen();
        writeOptions.checkWriteOptionsOpen();

        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_write(levelDB, writeOptions.writeOptions, writeBatch.writeBatch, error);
        LevelDBNative.checkError(error);
    }

    public void delete(byte[] key, LevelDBWriteOptions writeOptions) {
        checkDatabaseOpen();
        writeOptions.checkWriteOptionsOpen();

        PointerByReference error = new PointerByReference();
        if (Native.POINTER_SIZE == 8) {
            long keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_delete(levelDB, writeOptions.writeOptions, key, keyLength, error);
        } else {
            int keyLength = key != null ? key.length : 0;
            LevelDBNative.leveldb_delete(levelDB, writeOptions.writeOptions, key, keyLength, error);
        }
        LevelDBNative.checkError(error);
    }

    public String property(String property) {
        checkDatabaseOpen();

        return LevelDBNative.leveldb_property_value(levelDB, property);
    }

    public long[] approximateSizes(Range ...ranges) {
        checkDatabaseOpen();

        if (ranges.length == 0)
            return new long[0];

        Memory startKeys = new Memory(ranges.length * Pointer.SIZE);
        Memory limitKeys = new Memory(ranges.length * Pointer.SIZE);

        for (int i = 0; i < ranges.length; i++) {
            int startKeyLength = ranges[i].getStartKey().length;
            Memory startKeyMemory = new Memory(startKeyLength);
            startKeyMemory.write(0, ranges[i].getStartKey(), 0, startKeyLength);

            startKeys.setPointer(i * Pointer.SIZE, startKeyMemory);

            int limitKeyLength = ranges[i].getLimitKey().length;
            Memory limitKeyMemory = new Memory(limitKeyLength);
            limitKeyMemory.write(0, ranges[i].getLimitKey(), 0, limitKeyLength);

            limitKeys.setPointer(i * Pointer.SIZE, limitKeyMemory);
        }

        Pointer sizes = new Memory(ranges.length * Native.getNativeSize(Long.TYPE));
        if (Native.POINTER_SIZE == 8) {
            long[] startLengths = new long[ranges.length];
            long[] limitLengths = new long[ranges.length];

            for (int i = 0; i < ranges.length; i++) {
                startLengths[i] = ranges[i].getStartKey().length;
                limitLengths[i] = ranges[i].getLimitKey().length;
            }

            LevelDBNative.leveldb_approximate_sizes(levelDB, ranges.length, startKeys, startLengths, limitKeys, limitLengths, sizes);
        } else {
            int[] startLengths = new int[ranges.length];
            int[] limitLengths = new int[ranges.length];

            for (int i = 0; i < ranges.length; i++) {
                startLengths[i] = ranges[i].getStartKey().length;
                limitLengths[i] = ranges[i].getLimitKey().length;
            }

            LevelDBNative.leveldb_approximate_sizes(levelDB, ranges.length, startKeys, startLengths, limitKeys, limitLengths, sizes);
        }

        return sizes.getLongArray(0, ranges.length);
    }

    public void compactRange(byte[] startKey, byte[] limitKey) {
        checkDatabaseOpen();

        if (Native.POINTER_SIZE == 8) {
            long startKeyLength = startKey != null ? startKey.length : 0;
            long limitKeyLength = limitKey != null ? limitKey.length : 0;
            LevelDBNative.leveldb_compact_range(levelDB, startKey, startKeyLength, limitKey, limitKeyLength);
        } else {
            int startKeyLength = startKey != null ? startKey.length : 0;
            int limitKeyLength = limitKey != null ? limitKey.length : 0;
            LevelDBNative.leveldb_compact_range(levelDB, startKey, startKeyLength, limitKey, limitKeyLength);
        }
    }

    public static void repair(String levelDBDirectory, LevelDBOptions options) {
        options.checkOptionsOpen();

        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_repair_db(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public static void destroy(String levelDBDirectory, LevelDBOptions options) {
        options.checkOptionsOpen();

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

    protected void checkDatabaseOpen() {
        if (levelDB == null) {
            throw new LevelDBException("LevelDB database was closed.");
        }
    }
}
