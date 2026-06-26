package com.koen.bfe.memory.IO;

import com.koen.bfe.instructions.parsing.CPU;

import java.util.ArrayDeque;
import java.util.Queue;

public class Keyboard {
    private Queue<Byte> lastKeys = new ArrayDeque<>();

    public byte read() {
        return lastKeys.isEmpty() ? 0 : lastKeys.remove();
    }

    public void keyPressed(byte key) {
        lastKeys.add((byte) Character.toUpperCase((char) key));
        CPU.interruptHandling.raiseInterrupt(1);
    }

    public byte isKeyAvailable() {
        return (byte) (lastKeys.isEmpty() ? 0 : 1);
    }
}
