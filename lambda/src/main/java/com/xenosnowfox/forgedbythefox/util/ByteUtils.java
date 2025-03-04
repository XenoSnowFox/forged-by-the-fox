package com.xenosnowfox.forgedbythefox.util;

import java.nio.ByteBuffer;

/** Helper methods. */
public final class ByteUtils {

    /** Hidden constructor. */
    private ByteUtils() {}

    /**
     * Convert a long to byte array.
     *
     * @param x long
     * @return byte array
     */
    public static byte[] longToBytes(final long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * Convert a byte array to long.
     *
     * @param bytes byte array
     * @return long
     */
    public static long bytesToLong(final byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip(); // need flip
        return buffer.getLong();
    }
}
