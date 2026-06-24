package com.koen.bfe.memory;

import com.koen.bfe.Main;
import com.koen.bfe.instructions.parsing.CPU;
import com.koen.bfe.video.BImg;
import com.koen.bfe.video.Pixel;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;

public class TextBuffer implements MemoryDevice {

    private char[] characters;
    int width;
    int height;

    public TextBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        characters = new char[width * height];
    }

    @Override
    public byte read(int address) {
        return (byte) characters[address];
    }

    @Override
    public void write(int address, byte value) {
        characters[address] = (char) value;
    }

    @Override
    public int size() {
        return width * height;
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

    public void setText(int startIdx, String text) {
        System.arraycopy(text.toCharArray(), 0, characters, startIdx, text.length());
    }
}
