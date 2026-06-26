package com.koen.bfe.memory;

import com.koen.bfe.instructions.parsing.CPU;

import java.util.ArrayDeque;
import java.util.Queue;

public class InterruptHandling {
    private final int[] interruptVectors = {
            1850,
            1900,
            1950
    };

    public Queue<Integer> pendingInterrupts = new ArrayDeque<>();

    public void raiseInterrupt(int type) {
        //System.out.println("Interrupt raised!");
        pendingInterrupts.add(type);
    }

    public void handleInterrupt(int type) {
        //System.out.println("Handling interrupt!");
        CPU.IRP.loadValue(CPU.PC.getValue());
        CPU.CRF.loadValue(CPU.CMP_FLG.getValue());

        CPU.INT_FLG.loadValue(2 + type);

        CPU.PC.loadValue(this.interruptVectors[type]);
    }

    public void stateReturn() {
        //System.out.println("Returning operation!");
        //System.out.println("R_00: " + CPU.registers[0].getValue());

        if (CPU.INT_FLG.getValue() < 2) {
            return;
        }

        CPU.PC.loadValue(CPU.IRP.getValue());
        CPU.CMP_FLG.loadValue(CPU.CRF.getValue());
        CPU.INT_FLG.loadValue(1);
        //System.out.println("PC set back to " + CPU.PC.getValue());
        //System.out.println("CMP_FLG set back to " + CPU.CMP_FLG.getValue());
    }

    public boolean hasPending() {
        return !pendingInterrupts.isEmpty();
    }
}
