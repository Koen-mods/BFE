package com.koen.bfe.memory;

public class TextBuffer implements MemoryDevice {

    private char[] characters;
    private int width;
    private int height;

    public TextBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        characters = new char[width * height];
    }

    @Override
    public byte read(int address) {
        return 0;
    }

    @Override
    public void write(int address, byte value) {

    }

    @Override
    public int size() {
        return 0;
    }

    public void clear() {
        for (int i = 0; i < width*height; i++) {
            characters[i] = 0;
        }
    }

    public void setChar(int x, int y, char c) {
        characters[x + y * width] = c;
    }

    public char getChar(int x, int y) {
        return characters[x + y * width];
    }
}
