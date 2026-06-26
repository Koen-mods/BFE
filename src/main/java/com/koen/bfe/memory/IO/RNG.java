package com.koen.bfe.memory.IO;

import java.util.Random;

public class RNG {
    private Random rand;

    public byte read() {
        return (byte) rand.nextInt(255);
    }

    public void write(byte val) {
        throw new IllegalStateException("Random number register is read-only");
    }
}
