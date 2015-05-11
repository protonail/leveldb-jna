package com.protonail.leveldb.jna;

public class Range {
    private byte[] startKey;
    private byte[] limitKey;

    public Range(byte[] startKey, byte[] limitKey) {
        this.startKey = startKey;
        this.limitKey = limitKey;
    }

    public byte[] getStartKey() {
        return startKey;
    }

    public byte[] getLimitKey() {
        return limitKey;
    }
}
