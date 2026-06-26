package com.koen.bfe.memory.IO;

import com.koen.bfe.Main;
import com.koen.bfe.instructions.parsing.CPU;

public class SysControl {
    public void write(byte val) {
        //System.out.println("Writing " + val + " to SysControl");
        if (val < 1 || val > 2) {
            return;
        } else if (val == 1) {
            CPU.running = false;
        } else {
            CPU.PC.value = CPU.getBus().getRomStart();
            CPU.getBus().clearClearable();
        }
    }

    public byte read() {
        return 0;
    }
}
