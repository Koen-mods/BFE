package com.koen.bfe.essentials;

public class Helpers {
    public static int BigEndian(byte[] bytes) throws IllegalAccessException {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Too many bytes inputted, only 4 is allowed.");
        }

        return ((bytes[0] & 0xFF) << 24)  | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }

    public static byte[] BigEndianReverse(int word) {
        byte b0 = (byte) ((word >>> 24) & 0xFF);
        byte b1 = (byte) ((word >>> 16) & 0xFF);
        byte b2 = (byte) ((word >>> 8) & 0xFF);
        byte b3 = (byte) (word & 0xFF);

        return new byte[]{b0, b1, b2, b3};
    }
}
