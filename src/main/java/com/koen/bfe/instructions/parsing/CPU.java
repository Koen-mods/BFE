package com.koen.bfe.instructions.parsing;

import com.koen.bfe.Main;
import com.koen.bfe.essentials.Register;
import com.koen.bfe.instructions.handling.Handler;
import com.koen.bfe.memory.Bus;
import com.koen.bfe.memory.InterruptHandling;

public class CPU {
    public static Bus bus = new Bus(1000, 1000, 32, 32, 53, 36);
    public static Register PC = new Register("PC");
    public static Register CMP_FLG = new Register("CMP_FLG");
    public static Register INT_FLG = new Register("INT_FLG");
    public static Register IRP = new Register("IRP");
    public static Register CRF = new Register("CRF");
    public static Register[] registers = new Register[16];
    public static boolean running = true;
    public static InterruptHandling interruptHandling = new InterruptHandling();

    public CPU() {
        for (int i = 1; i <= 16; i++) {
            registers[i - 1] = new Register("R_" + i);
        }

        PC.value = bus.getRomStart();
        INT_FLG.loadValue(1);
        System.out.println("PC:" + PC.value);
    }

    public static void ExecuteProgram(byte[] program) throws IllegalAccessException, InterruptedException {
        bus.loadProgram(program);
        byte nopcode = bus.read(PC.value);
        //System.out.println("PC:"+PC.value+"\nOPCODE:"+nopcode);
        while (PC.getValue() < ((bus.getRomStart() + program.length) - 1) && running) {
            byte opcode = bus.read(PC.getValue());
            switch (opcode) {
                case 1 ->
                    Handler.handleLoadMem(PC.getValue());
                case 2 ->
                    Handler.handleLoadReg(PC.getValue());
                case 3 ->
                    Handler.handleAdd(PC.getValue());
                case 4 ->
                    Handler.handleSub(PC.getValue());
                case 5 ->
                    Handler.handleJmp(PC.getValue());
                case 6 ->
                    Handler.handleCmp(PC.getValue());
                case 7 ->
                    Handler.handleJmpEq(PC.getValue());
                case 8 ->
                    Handler.handleJmpLess(PC.getValue());
                case 9 ->
                    Handler.handleJmpMore(PC.getValue());
                case 10 ->
                    Handler.handleJmpNeq(PC.getValue());
                case 11 ->
                    Handler.handleJmpMoreq(PC.getValue());
                case 12 ->
                    Handler.handleJmpLereq(PC.getValue());
                case 13 ->
                    Handler.handleLoadRegMem(PC.getValue());
                case 14 ->
                    Handler.handleLoadMemReg(PC.getValue());
                case 15 ->
                    Handler.handleLoadMemRegptr(PC.getValue());
                case 16 ->
                    Handler.handleLoadRegMemptr(PC.getValue());
                case 17 ->
                    Handler.handleLoadMemptrReg(PC.getValue());
                case 18 ->
                    Handler.handleInterruptReturn(PC.getValue());
                case 19 ->
                    Handler.handleInterrupt(PC.getValue());
                case 20 ->
                    Handler.handleToggleInterrupts(PC.getValue());
                case 21 ->
                    Handler.handleSetInterrupt(PC.getValue());
                case 99 ->
                    Handler.handleHalt(PC.getValue());
            }
            Main.panel.repaint();
            bus.dm.update();
            bus.timer.update();
            if (interruptHandling.hasPending() && INT_FLG.value == 1) {
                interruptHandling.handleInterrupt(interruptHandling.pendingInterrupts.remove());
            }
            //Thread.sleep(1000);
            //System.out.println("PC:"+PC.getValue());
        }
    }

    public static Bus getBus() {
        return bus;
    }
}
