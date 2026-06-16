package com.koen.bfe.memory;

public interface MemoryDevice {
    byte read(int address);
    void write(int address, byte value);
    int size();
}
