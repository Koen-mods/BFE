package com.koen.bfe.memory;

import com.koen.bfe.Main;
import com.koen.bfe.memory.IO.Keyboard;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;

public class Bus {
    private RAM ram;
    private ROM rom;
    private VideoMemory video;
    private TextBuffer text;
    private int romStart;
    private int videoStart;
    private int textStart;
    private boolean enabled = false;
    public Keyboard keyboard = new Keyboard();

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
        //System.out.println("Reading from address" + address);
        if (address < romStart) {
            return ram.read(address);
        } else if (address < videoStart) {
            return rom.read(address - romStart);
        } else if (address < textStart) {
            return video.read(address -videoStart);
        } else if (address < (textStart + text.size())) {
            return text.read(address - textStart);
        } else if (address == (textStart + text.size())) {
            return keyboard.isKeyAvailable();
        } else if (address == (textStart + text.size() + 1)) {
            return keyboard.read();
        } else if (address > (textStart + text.size() + 1)) {
            throw new IllegalAccessException("Address out of bounds, address read: " + address);
        } else {
            throw new IllegalAccessException("Something went wrong trying to read from this address");
        }
    }

    public void write(int address, byte value) throws IllegalAccessException {
        System.out.println("Writing to " + address);
        if (address < romStart) {
            ram.write(address, value);
        } else if (address < videoStart) {
            rom.write(address - romStart, value);
        } else if (address < textStart) {
            video.write(address - videoStart, value);
        } else if (address < (textStart + text.size())) {
            text.write(address - textStart, value);
        } else if (address <= textStart + text.size() + 1) {
            throw new IllegalAccessException("Writing to IO registers is illegal");
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

    public void repaintText() throws SizeLimitExceededException, IOException {
        Main.panel.repaint();
    }

    public void setMode(boolean vidEnabled) {
        video.setEnabled(vidEnabled);
        this.enabled = vidEnabled;
    }

    public boolean isVidEnabled() { return enabled; }

    public int getTextSize() {
        return text.size();
    }

    public int getTextWidth() {
        return text.width;
    }

    public int getTextHeight() {
        return text.height;
    }

    public void printf(int startIdx, String text) {
        this.text.setText(startIdx, text);
    }

    public int finalAddr() {
        return (textStart + getTextSize() + 1);
    }
}
