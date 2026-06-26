package com.koen.bfe.memory.IO;

import com.koen.bfe.instructions.parsing.CPU;

public class DisplayMode {
    private byte mode;

    public byte read() {
        return mode;
    }

    public void write(byte m) {
        mode = m;
    }

    public void update() {
        CPU.getBus().setMode(mode == 1);
    }
}
