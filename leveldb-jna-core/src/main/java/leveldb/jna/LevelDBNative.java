package leveldb.jna;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

class LevelDBNative {
    static {
        Native.register("leveldb");
    }

    // LevelDB

    public static native LevelDB leveldb_open(Options options, String name, PointerByReference errptr);

    public static native void leveldb_close(LevelDB levelDB);

    public static native PointerByReference leveldb_get(LevelDB levelDB, ReadOptions options,
                                                        byte[] key, long keylen,
                                                        IntByReference vallen,
                                                        PointerByReference errptr);
    public static native PointerByReference leveldb_get(LevelDB levelDB, ReadOptions options,
                                                        byte[] key, int keylen,
                                                        IntByReference vallen,
                                                        PointerByReference errptr);

    public static native void leveldb_put(LevelDB levelDB, WriteOptions options,
                                          byte[] key, long keylen,
                                          byte[] val, long vallen,
                                          PointerByReference errptr);
    public static native void leveldb_put(LevelDB levelDB, WriteOptions options,
                                          byte[] key, int keylen,
                                          byte[] val, int vallen,
                                          PointerByReference errptr);

    public static native void leveldb_delete(LevelDB levelDB, WriteOptions options,
                                             byte[] key, long keylen,
                                             PointerByReference errptr);
    public static native void leveldb_delete(LevelDB levelDB, WriteOptions options,
                                             byte[] key, int keylen,
                                             PointerByReference errptr);

    public static native void leveldb_write(LevelDB levelDB, WriteOptions options,
                                            WriteBatch batch,
                                            PointerByReference errptr);

    public static native String leveldb_property_value(LevelDB levelDB, String propname);

    public static native void leveldb_approximate_sizes(LevelDB levelDB, int num_ranges,
                                                        Pointer range_start_key, long[] range_start_key_len,
                                                        Pointer range_limit_key, long[] range_limit_key_len,
                                                        Pointer sizes);
    public static native void leveldb_approximate_sizes(LevelDB levelDB, int num_ranges,
                                                        Pointer range_start_key, int[] range_start_key_len,
                                                        Pointer range_limit_key, int[] range_limit_key_len,
                                                        Pointer sizes);

    public static native void leveldb_compact_range(LevelDB levelDB,
                                                    byte[] start_key, long start_key_len,
                                                    byte[] limit_key, long limit_key_len);
    public static native void leveldb_compact_range(LevelDB levelDB,
                                                    byte[] start_key, int start_key_len,
                                                    byte[] limit_key, int limit_key_len);

    public static native void leveldb_destroy_db(Options options, String name, PointerByReference errptr);

    public static native void leveldb_repair_db(Options options, String name, PointerByReference errptr);

    public static native int leveldb_major_version();

    public static native int leveldb_minor_version();

    // Options

    public static native Options leveldb_options_create();

    public static native void leveldb_options_destroy(Options options);

    public static native void leveldb_options_set_comparator(Options options, LevelDBNative.Comparator comparator);

    public static native void leveldb_options_set_filter_policy(Options options, LevelDBNative.FilterPolicy filterPolicy);

    public static native void leveldb_options_set_create_if_missing(Options options, byte value);

    public static native void leveldb_options_set_error_if_exists(Options options, byte value);

    public static native void leveldb_options_set_paranoid_checks(Options options, byte value);

    public static native void leveldb_options_set_env(Options options, LevelDBNative.Env env);

    public static native void leveldb_options_set_info_log(Options options, LevelDBNative.Logger logger);

    public static native void leveldb_options_set_write_buffer_size(Options options, long writeBufferSize);
    public static native void leveldb_options_set_write_buffer_size(Options options, int writeBufferSize);

    public static native void leveldb_options_set_max_open_files(Options options, int maxOpenFiles);

    public static native void leveldb_options_set_cache(Options options, LevelDBNative.Cache cache);

    public static native void leveldb_options_set_block_size(Options options, long blockSize);
    public static native void leveldb_options_set_block_size(Options options, int blockSize);

    public static native void leveldb_options_set_block_restart_interval(Options options, int blockRestartInterval);

    public static native void leveldb_options_set_compression(Options options, int compressionType);

    // ReadOptions

    public static native ReadOptions leveldb_readoptions_create();

    public static native void leveldb_readoptions_destroy(ReadOptions readOptions);

    public static native void leveldb_readoptions_set_verify_checksums(ReadOptions readOptions, byte value);

    public static native void leveldb_readoptions_set_fill_cache(ReadOptions readOptions, byte value);

    public static native void leveldb_readoptions_set_snapshot(ReadOptions readOptions, Snapshot snapshot);

    // WriteOptions

    public static native WriteOptions leveldb_writeoptions_create();

    public static native void leveldb_writeoptions_destroy(WriteOptions writeOptions);

    public static native void leveldb_writeoptions_set_sync(WriteOptions writeOptions, byte value);

    // Cache

    public static native Cache leveldb_cache_create_lru(long capacity);
    public static native Cache leveldb_cache_create_lru(int capacity);

    public static native void leveldb_cache_destroy(Cache cache);

    // Comparator

    public static native Comparator leveldb_comparator_create(Pointer state,
                                                              DestructorFunc destructorFunc,
                                                              CompareFunc compareFunc);

    public static native void leveldb_comparator_destroy(Comparator comparator);

    //TODO: required `int` interface for fix problem with `long` on 32-bit Windows systems
    public interface CompareFunc extends Callback {
        int invoke(Pointer pointer, byte[] a, long alen, byte[] b, long blen);
    }

    // Env

    public static native Env leveldb_create_default_env();

    public static native void leveldb_env_destroy(Env cache);

    // FilterPolicy

    public static native FilterPolicy leveldb_filterpolicy_create(Pointer state,
                                                                  DestructorFunc destructorFunc,
                                                                  CreateFilterFunc createFilterFunc,
                                                                  KeyMayMatchFunc keyMayMatchFunc,
                                                                  NameFunc nameFunc);

    public static native void leveldb_filterpolicy_destroy(FilterPolicy filterPolicy);

    public static native FilterPolicy leveldb_filterpolicy_create_bloom(int bits_per_key);

    public interface CreateFilterFunc extends Callback {
        void invoke(Pointer pointer, PointerByReference key_array, IntByReference key_length_array, int num_keys, IntByReference filter_length);
    }

    //TODO: required `int` interface for fix problem with `long` on 32-bit Windows systems
    public interface KeyMayMatchFunc extends Callback {
        void invoke(Pointer pointer, byte[] key, long length, byte[] filter, long filter_length);
    }

    public interface NameFunc extends Callback {
        byte[] invoke(Pointer pointer);
    }

    // Iterator

    public static native Iterator leveldb_create_iterator(LevelDBNative.LevelDB levelDB, ReadOptions options);

    public static native Iterator leveldb_iter_destroy(Iterator iterator);

    public static native byte leveldb_iter_valid(Iterator iterator);

    public static native void leveldb_iter_seek_to_first(Iterator iterator);

    public static native void leveldb_iter_seek_to_last(Iterator iterator);

    public static native void leveldb_iter_seek(Iterator iterator, byte[] k, long klen);
    public static native void leveldb_iter_seek(Iterator iterator, byte[] k, int klen);

    public static native void leveldb_iter_next(Iterator iterator);

    public static native void leveldb_iter_prev(Iterator iterator);

    public static native PointerByReference leveldb_iter_key(Iterator iterator, IntByReference klen);

    public static native PointerByReference leveldb_iter_value(Iterator iterator, IntByReference vlen);

    public static native void leveldb_iter_get_error(Iterator iterator, PointerByReference errptr);

    // Snapshot

    public static native Snapshot leveldb_create_snapshot(LevelDBNative.LevelDB levelDB);

    public static native void leveldb_release_snapshot(LevelDBNative.LevelDB levelDB, Snapshot snapshot);

    // WriteBatch

    public static native WriteBatch leveldb_writebatch_create();

    public static native void leveldb_writebatch_destroy(WriteBatch writeBatch);

    public static native void leveldb_writebatch_clear(WriteBatch writeBatch);

    public static native void leveldb_writebatch_put(WriteBatch writeBatch,
                                                     byte[] key, long klen,
                                                     byte[] val, long vlen);
    public static native void leveldb_writebatch_put(WriteBatch writeBatch,
                                                     byte[] key, int klen,
                                                     byte[] val, int vlen);

    public static native void leveldb_writebatch_delete(WriteBatch writeBatch,
                                                        byte[] key, long klen);
    public static native void leveldb_writebatch_delete(WriteBatch writeBatch,
                                                        byte[] key, int klen);

    public static native void leveldb_writebatch_iterate(WriteBatch writeBatch,
                                                         Pointer state,
                                                         PutFunc putFunc, DeleteFunc deleteFunc);

    //TODO: required `int` interface for fix problem with `long` on 32-bit Windows systems
    public interface PutFunc extends Callback {
        int invoke(Pointer pointer, byte[] k, long klen, byte[] value, long vlen);
    }

    //TODO: required `int` interface for fix problem with `long` on 32-bit Windows systems
    public interface DeleteFunc extends Callback {
        int invoke(Pointer pointer, byte[] k, long klen);
    }

    // Common

    public static native void leveldb_free(Pointer pointer);

    public interface DestructorFunc extends Callback {
        void invoke(Pointer pointer);
    }

    // Structures

    public static class LevelDB extends PointerType {
    }

    public static class Options extends PointerType {
    }

    public static class ReadOptions extends PointerType {
    }

    public static class WriteOptions extends PointerType {
    }

    public static class Cache extends PointerType {
    }

    public static class Comparator extends PointerType {
    }

    public static class Env extends PointerType {
    }

    public static class FilterPolicy extends PointerType {
    }

    public static class Iterator extends PointerType {
    }

    public static class Logger extends PointerType {
    }

    public static class Snapshot extends PointerType {
    }

    public static class WriteBatch extends PointerType {
    }

    // Helpers

    public static void checkError(PointerByReference error) {
        Pointer pointerError = error.getValue();
        if (pointerError != null) {
            String errorMessage = pointerError.getString(0);

            throw new LevelDBException(errorMessage);
        }
    }
}
