package com.koen.bfe.memory;

public class VideoMemory implements MemoryDevice {
    private byte[] pixels;
    private int width;
    private int height;
    private boolean enabled = false;

    public VideoMemory(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new byte[width * height];
    }

    @Override
    public byte read(int address) {
        return pixels[address];
    }

    @Override
    public void write(int address, byte value) {
        pixels[address] = value;
    }

    @Override
    public int size() {
        return width*height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {
        for (int i = 0; i < width*height; i++) {
            pixels[i] = 0;
        }
    }

    public void setPixel(int x, int y, byte c) {
        pixels[x + y * width] = c;
    }

    public byte getPixel(int x, int y) {
        return pixels[x + y * width];
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
