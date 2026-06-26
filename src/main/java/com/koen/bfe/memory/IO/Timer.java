package com.koen.bfe.memory.IO;

import com.koen.bfe.Main;

import java.nio.ByteBuffer;

public class Timer {
    private int uptime;

    public void update() {
        uptime = Math.toIntExact(System.currentTimeMillis() - Main.bootTime);
    }

    public byte read(int addr) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(uptime).array();
        return bytes[addr];
    }
}
