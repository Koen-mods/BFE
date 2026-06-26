package com.koen.bfe.memory.IO;

import com.koen.bfe.Main;
import com.koen.bfe.instructions.parsing.CPU;

import java.nio.ByteBuffer;

public class Timer {
    private int uptime;
    private int lastTick = 0;

    public void update() {
        uptime = Math.toIntExact(System.currentTimeMillis() - Main.bootTime);
        int tick = uptime / 100;
        if (tick > lastTick) {
            lastTick = tick;
            CPU.interruptHandling.raiseInterrupt(0);
        }
    }

    public byte read(int addr) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(uptime).array();
        return bytes[addr];
    }
}
