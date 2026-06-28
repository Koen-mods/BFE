package com.koen.bfe;

import com.koen.bfe.essentials.Helpers;
import com.koen.bfe.essentials.input.FileHandling;
import com.koen.bfe.help.menu.Menu;
import com.koen.bfe.instructions.handling.Handler;
import com.koen.bfe.instructions.parsing.CPU;
import com.koen.bfe.memory.Bus;
import com.koen.bfe.video.BImg;
import com.koen.bfe.video.ScreenPanel;

import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Main {
    public static ScreenPanel panel = new ScreenPanel();
    public static long bootTime;

    public static void main(String[] args) throws IllegalAccessException, IOException, SizeLimitExceededException, InterruptedException {
        JFrame frame = new JFrame("Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        CPU cpu = new CPU();
        Bus bus = CPU.getBus();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                bus.keyboard.keyPressed((byte)e.getKeyChar());
            }
        });
        System.out.println("RAM start: 0\nROM start: " + bus.getRomStart() + "\nVideo start: " + bus.getVideoStart() + "\nText start: " + bus.getTextStart());
        System.out.println("IO registers start:" + (bus.getTextStart() + bus.getTextSize()) + "\nSyscontrol" + (bus.getTextStart() + bus.getTextSize() + 5));
        bus.setMode(true);
        bootTime = System.currentTimeMillis();
        CPU.ExecuteProgram(FileHandling.readFileContents(args[0]));
        frame.setVisible(false);
        frame.remove(panel);
        System.exit(0);
    }
}

/*
* ~~Memory layout~~ DISCONTINUED
* ___________________________________________
* Registers:
* ___________________________________________
* R_01
* R_02
* R_03
* R_04
* R_05
* R_06
* R_07
* R_08
* R_09
* R_10
* R_11
* R_12
* R_13
* R_14
* R_15
* R_16
* PC
* CMP_FLG
* ___________________________________________
* Instruction set:
* 01 (0x01): load_mem [addr] [value]
* 02 (0x02): load_reg [register name] [value]
* 03 (0x03): add [reg] [value]
* 04 (0x04): sub [reg] [value]
* 05 (0x05): jump [instruction addr]
* 06 (0x06): cmp [register] [value]
* 07 (0x07): jump_eq [instruction addr]
* 08 (0x08): jump_less [instruction addr]
* 09 (0x09): jump_more [instruction addr]
* 10 (0x0A): jump_neq [instruction addr]
* 11 (0x0B): jump_moreq [instruction addr]
* 12 (0x0C): jump_lereq [instruction addr]
* 13 (0x0D): load_reg_mem [reg] [addr]
* 14 (0x0E): load_mem_reg [addr] [reg]
* 15 (0x0F): load_mem_regptr [addr] [regptr]
* 16 (0x10): load_reg_memptr [reg] [addr]
* 17 (0x11): load_memptr_reg [reg] [val]
* 18 (0x12): int_return
* 19 (0x13): int [type]
* 20 (0x14): toggle_int [0/1]
* 21 (0x15): set_int [entry] [addr]
 * 22 (0x16): jumpptr [addr]
 * 23 (0x17): cmpmem [register] [addr]
 * 24 (0x18): jumpptr_eq [addr]
 * 25 (0x19): jumpptr_less [addr]
 * 26 (0x1A): jumpptr_more [addr]
 * 27 (0x1B): jumpptr_neq [addr]
 * 28 (0x1C): jumpptr_moreq [addr]
 * 29 (0x1D): jumpptr_lereq [addr]
* 99 (0x63): halt
*
*
*
*
* Programmm
* reg 0 is ctr
* if ctr < 1024 jmp to the start of the loop
* reg 1 is the pointer
* the pointer starts at 2000
* the pointer is incremented each time it loops fully
* stuff
* */