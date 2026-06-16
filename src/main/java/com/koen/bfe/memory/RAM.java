package com.koen.bfe.memory;

public class RAM implements MemoryDevice {

    private byte[] data;
    private int size;

    public RAM(int size) {
        this.size = size;
        this.data = new byte[size];
    }

    @Override
    public byte read(int address) {
        return data[address];
    }

    @Override
    public void write(int address, byte value) {
        data[address] = value;
    }

    @Override
    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = 0;
        }
    }
}
