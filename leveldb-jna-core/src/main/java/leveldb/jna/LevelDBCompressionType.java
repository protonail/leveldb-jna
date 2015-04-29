package leveldb.jna;

public enum LevelDBCompressionType {
    NoCompression(0),
    SnappyCompression(1);

    private int compressionType;

    LevelDBCompressionType(int compressionType) {
        this.compressionType = compressionType;
    }

    public int getCompressionType() {
        return compressionType;
    }
}
