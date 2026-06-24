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
        System.out.println("IO registers start:" + (bus.getTextStart() + bus.getTextSize()));
        bus.setMode(false);
        //CPU.ExecuteProgram(FileHandling.readFileContents(args[0]));
        System.out.println(CPU.registers[1].value);
        System.out.println(CPU.registers[2].value);
        System.out.println(bus.read(996));
        bus.printf(0, "HEY, KOEN HERE, I CAN USE COMMAS NOW");
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
* 01: load_mem [addr] [value]
* 02: load_reg [register name] [value]
* 03: add [reg] [value]
* 04: sub [reg] [value]
* 05: jump [instruction addr]
* 06: cmp [register] [value]
* 07: jump_eq [instruction addr]
* 08: jump_less [instruction addr]
* 09: jump_more [instruction addr]
* 10: jump_neq [instruction addr]
* 11: jump_moreq [instruction addr]
* 12: jump_lereq [instruction addr]
* 13: load_reg_mem [reg] [addr]
* 14: load_mem_reg [addr] [reg]
* 15: load_mem_regptr [addr] [regptr]
* 16: load_reg_memptr [reg] [addr]
* 17: load_memptr_reg [reg] [val]
* 99: halt
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