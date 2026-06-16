package com.koen.bfe.memory;

public class ROM implements MemoryDevice {

    private byte[] data;
    private int size;

    public ROM(int size) {
        this.size = size;
        this.data = new byte[size];
    }

    @Override
    public byte read(int address) {
        return data[address];
    }

    @Override
    public void write(int address, byte value) {
        throw new IllegalStateException("ROM is read-only");
    }

    public void loadProgram(byte[] program) {
        if (program.length > size) {
            throw new IllegalArgumentException("Program is too large");
        }

        System.arraycopy(program, 0, data, 0, program.length);
    }

    @Override
    public int size() {
        return size;
    }
}
