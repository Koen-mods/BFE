package com.koen.bfe.instructions.handling;

import com.koen.bfe.essentials.Helpers;
import com.koen.bfe.instructions.parsing.CPU;
import com.koen.bfe.memory.Bus;

import static com.koen.bfe.Init.*;
import static com.koen.bfe.instructions.parsing.CPU.*;

public class Handler {

    private static Bus bus = CPU.getBus();

    public static void handleLoadMem(int addr) throws IllegalAccessException {
        //System.out.println("PC:" +PC.getValue());
        int ldAddr;
        byte b0 = bus.read(addr + 1);
        byte b1 = bus.read(addr + 2);
        byte b2 = bus.read(addr + 3);
        byte b3 = bus.read(addr + 4);
        ldAddr = ((b0 & 0xFF) << 24)  | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
        bus.write(ldAddr, bus.read(addr + 5));
        PC.loadValue(PC.getValue() + 6);
        //System.out.println("PC:"+PC.getValue());
        //System.out.println("added val to addr " + ldAddr + ": " + bus.read(ldAddr));
    }

    public static void handleLoadReg(int addr) throws IllegalAccessException {
        int val = Helpers.BigEndian(new byte[]{bus.read(addr+2),bus.read(addr+3),bus.read(addr+4),bus.read(addr+5)});
        registers[bus.read(addr+1)].loadValue(val);
        PC.loadValue(PC.getValue() + 6);
        //System.out.println("PC:"+PC.getValue());
    }

    public static void handleAdd(int addr) throws IllegalAccessException {
        registers[bus.read(addr + 1)].value += bus.read(addr+2);
        PC.value += 3;
    }

    public static void handleSub(int addr) throws IllegalAccessException {
        registers[bus.read(addr+1)].value -= bus.read(addr + 2);
        PC.value += 3;
    }

    public static void handleJmp(int addr) throws IllegalAccessException {
        int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
        PC.value = bus.getRomStart() + jmpAddr;
    }

    public static void handleCmp(int addr) throws IllegalAccessException {
        int CmpVal = Helpers.BigEndian(new byte[]{bus.read(addr + 2), bus.read(addr + 3), bus.read(addr + 4), bus.read(addr + 5)});
        if (registers[bus.read(addr+1)].getValue() == CmpVal) {
            CMP_FLG.value = 1;
        }if (registers[bus.read(addr+1)].getValue() > CmpVal) {
            CMP_FLG.value = 2;
        }if (registers[bus.read(addr+1)].getValue() < CmpVal) {
            CMP_FLG.value = 3;
        }

        PC.value += 6;
    }

    public static void handleJmpLess(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 3) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpEq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 1) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpNeq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value != 1) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpMore(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 2) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpLereq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 3 || CMP_FLG.value == 1) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpMoreq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 2 || CMP_FLG.value == 1) {
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleLoadRegMem(int addr) throws IllegalAccessException {
        int reg = bus.read(addr + 1);
        int ldAddr;
        byte b0 = bus.read(addr + 2);
        byte b1 = bus.read(addr + 3);
        byte b2 = bus.read(addr + 4);
        byte b3 = bus.read(addr + 5);
        ldAddr = ((b0 & 0xFF) << 24)  | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
        if (ldAddr + 4 >= bus.finalAddr()) {
            registers[reg].loadValue(bus.read(ldAddr));
            PC.value += 6;
        } else {
            byte[] bytes = new byte[4];
            for (int i = 0; i < 4; i++) {
                bytes[i] = bus.read(ldAddr + i);
            }

            int ldVal = Helpers.BigEndian(bytes);
            //System.out.println(ldVal);

            registers[reg].loadValue(ldVal);
            PC.value += 6;
        }

    }

    public static void handleLoadMemReg(int addr) throws IllegalAccessException {
        int ldAddr;
        byte b0 = bus.read(addr + 1);
        byte b1 = bus.read(addr + 2);
        byte b2 = bus.read(addr + 3);
        byte b3 = bus.read(addr + 4);
        ldAddr = ((b0 & 0xFF) << 24)  | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
        //System.out.println(ldAddr);
        int reg = bus.read(addr + 5);
        //System.out.println(reg);
        byte[] bytes = Helpers.BigEndianReverse(registers[reg].getValue());
        for (int i = 0; i < 4; i++) {
            bus.write(ldAddr + i, bytes[i]);
            //System.out.println("Wrote " + bytes[i] + " to address " + (ldAddr + i));
        }
        PC.value += 6;
    }

    public static void handleLoadMemRegptr(int addr) throws IllegalAccessException {
        int ldAddr;
        byte b0 = bus.read(addr+1);
        byte b1 = bus.read(addr+2);
        byte b2 = bus.read(addr+3);
        byte b3 = bus.read(addr+4);
        ldAddr = Helpers.BigEndian(new byte[]{b0,b1,b2,b3});
        int reg = bus.read(addr+5);
        int ptr = registers[reg].getValue();
        bus.write(ldAddr, bus.read(ptr));
        PC.value+=6;
    }

    public static void handleLoadRegMemptr(int addr) throws IllegalAccessException {
        int reg = bus.read(addr + 1);
        int ldAddr;
        byte b0 = bus.read(addr + 2);
        byte b1 = bus.read(addr + 3);
        byte b2 = bus.read(addr + 4);
        byte b3 = bus.read(addr + 5);
        ldAddr = Helpers.BigEndian(new byte[]{b0,b1,b2,b3});

        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = bus.read(ldAddr+i);
        }

        registers[reg].loadValue(Helpers.BigEndian(bytes));
        PC.value += 6;

    }

    public static void handleLoadMemptrReg(int addr) throws IllegalAccessException {
        int reg = bus.read(addr+1);
        int ptr = registers[reg].getValue();
        System.out.println(ptr);
        bus.write(ptr, bus.read(addr+2));
        PC.value+=3;
    }

    public static void handleInterruptReturn(int addr) {
        interruptHandling.stateReturn();
    }

    public static void handleInterrupt(int addr) throws IllegalAccessException {
        interruptHandling.raiseInterrupt(bus.read(addr + 1));
        PC.value += 2;
    }

    public static void handleToggleInterrupts(int addr) throws IllegalAccessException {
        if (bus.read(addr + 1) < 2) {
            INT_FLG.loadValue(bus.read(addr + 1));
            PC.value += 2;
        } else {
            throw new IllegalArgumentException("Interrupt flag cannot be set higher than 1");
        }
    }

    public static void handleSetInterrupt(int addr) throws IllegalAccessException {
        int type = bus.read(addr + 1);
        int vector = Helpers.BigEndian(new byte[]{bus.read(addr + 2),bus.read(addr + 3),bus.read(addr + 4),bus.read(addr + 5)});
        interruptHandling.interruptVectorTable.set(type, vector);
        PC.value += 6;
    }

    public static void handleJmpPtr(int addr) throws IllegalAccessException {
        int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
        int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
        PC.value = bus.getRomStart() + jmpAddr;
    }

    public static void handleCmpMem(int addr) throws IllegalAccessException {
        int ldAddr = Helpers.BigEndian(new byte[]{bus.read(addr + 2), bus.read(addr + 3), bus.read(addr + 4), bus.read(addr + 5)});
        int CmpVal = Helpers.BigEndian(new byte[]{bus.read(ldAddr), bus.read(ldAddr+1), bus.read(ldAddr+2),bus.read(ldAddr+3)});
        if (registers[bus.read(addr+1)].getValue() == CmpVal) {
            CMP_FLG.value = 1;
        }if (registers[bus.read(addr+1)].getValue() > CmpVal) {
            CMP_FLG.value = 2;
        }if (registers[bus.read(addr+1)].getValue() < CmpVal) {
            CMP_FLG.value = 3;
        }

        PC.value += 6;
    }

    public static void handleJmpPtrLess(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 3) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpPtrEq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 1) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpPtrNeq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value != 1) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpPtrMore(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 2) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpPtrLereq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 3 || CMP_FLG.value == 1) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleJmpPtrMoreq(int addr) throws IllegalAccessException {
        if (CMP_FLG.value == 2 || CMP_FLG.value == 1) {
            int ptrAddr = Helpers.BigEndian(new byte[]{bus.read(addr+1),bus.read(addr+2),bus.read(addr+3),bus.read(addr+4)});
            int jmpAddr = Helpers.BigEndian(new byte[]{bus.read(ptrAddr),bus.read(ptrAddr+1),bus.read(addr+2),bus.read(addr+3)});
            PC.value = bus.getRomStart() + jmpAddr;
        } else {
            PC.value += 5;
        }
    }

    public static void handleHalt(int addr) {
        PC.loadValue(PC.getValue() + 1);
        //System.out.println("halting CPU... (PC: " + PC.getValue() + ")");
    }

}
