package com.koen.bfe.memory;

public class Bus {
    private RAM ram;
    private ROM rom;
    private VideoMemory video;
    private TextBuffer text;
    private int romStart;
    private int videoStart;
    private int textStart;

    public Bus(int ramSize, int romSize, int videoWidth, int videoHeight, int textWidth, int textHeight) {
        ram = new RAM(ramSize);
        rom = new ROM(romSize);
        video = new VideoMemory(videoWidth, videoHeight);
        text = new TextBuffer(textWidth, textHeight);
        romStart = ram.size();
        videoStart = romStart + rom.size();
        textStart = videoStart + video.size();
    }

    public byte read(int address) throws IllegalAccessException {
        if (address < romStart) {
            return ram.read(address);
        } else if (address < videoStart) {
            return rom.read(address - romStart);
        } else if (address < textStart) {
            return video.read(address -videoStart);
        } else if (address < (textStart + text.size())) {
            return text.read(address - textStart);
        } else {
            throw new IllegalAccessException("Address out of bounds");
        }
    }

    public void write(int address, byte value) throws IllegalAccessException {
        if (address < romStart) {
            ram.write(address, value);
        } else if (address < videoStart) {
            rom.write(address - romStart, value);
        } else if (address < textStart) {
            video.write(address - videoStart, value);
        } else if (address < (textStart + text.size())) {
            text.write(address - textStart, value);
        } else {
            throw new IllegalAccessException("Address out of bounds");
        }
    }

    public int getRomStart() {
        return romStart;
    }

    public int getVideoStart() {
        return videoStart;
    }

    public int getTextStart() {
        return textStart;
    }

    public void setVideoPixel(int x, int y, byte c) {
        video.setPixel(x, y, c);
    }

    public byte getVideoPixel(int x, int y) {
        return video.getPixel(x, y);
    }

    public void setTextChar(int x, int y, char c) {
        text.setChar(x, y, c);
    }

    public char getTextChar(int x, int y) {
        return text.getChar(x, y);
    }

    public void clearAll() {
       clearClearable();

        byte[] prog = new byte[rom.size()];

        for (int i = 0; i < rom.size(); i++) {
            prog[i] = 0;
        }

        rom.loadProgram(prog);
    }

    public void clearClearable() {
        ram.clear();
        video.clear();
        text.clear();
    }

    public void clearRam() {
        ram.clear();
    }

    public void clearVideo() {
        video.clear();
    }

    public void clearText() {
        text.clear();
    }

    public int getVideoSize() { return video.size(); }

    public int getVideoWidth() { return video.getWidth(); }

    public int getVideoHeight() { return video.getHeight(); }

    public void loadProgram(byte[] program) {
        rom.loadProgram(program);
    }
}
