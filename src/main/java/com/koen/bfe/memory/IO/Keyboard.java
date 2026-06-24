package com.koen.bfe.memory.IO;

public class Keyboard {
    private byte lastKey;
    private boolean keyAvailable = false;

    public byte read() {
        keyAvailable = false;
        return lastKey;
    }

    public void keyPressed(byte key) {
        lastKey = (byte) Character.toUpperCase((char) key);
        keyAvailable = true;
    }

    public byte isKeyAvailable() {
        return (byte) (keyAvailable ? 1 : 0);
    }
}
