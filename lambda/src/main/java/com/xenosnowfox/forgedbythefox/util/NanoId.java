package com.xenosnowfox.forgedbythefox.util;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/** A class for generating unique String IDs. */
public final class NanoId {

    /** Base64 Encoder. */
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    /** Base64 Decoder. */
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    /**
     * <code>NanoId</code> instances should NOT be constructed in standard programming. Instead, the
     * class should be used as <code>NanoId.fromLong(value);</code>.
     */
    private NanoId() {
        // Do Nothing
    }

    public static String random() {
        return NanoId.fromLong(ThreadLocalRandom.current().nextLong()).replace("-", "_");
    }

    /**
     * Converts a Long to a NanoID String.
     *
     * @param withValue value to convert.
     * @return Nano ID String.
     */
    public static String fromLong(final long withValue) {
        return BASE64_ENCODER
                .encodeToString(ByteUtils.longToBytes(withValue))
                .replace("=", "")
                .replace("+", "-")
                .replace("/", "_");
    }

    /**
     * Converts NanoID String to a Long value.
     *
     * @param withValue Nano ID String
     * @return long
     */
    public static long toLong(final String withValue) {
        return ByteUtils.bytesToLong(
                BASE64_DECODER.decode(withValue.replace("-", "+").replace("_", "/")));
    }
}
