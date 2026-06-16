package com.koen.bfe.help.menu;

import java.io.IOException;

public class Menu {
    public static void start() throws IOException {
        System.out.print("Hey there! My name is Koen! Let me walk you through the system!\nBFE is a so-called emulator, a program meant to mimic the work of a CPU or entire computer system. In a nutshell, it reads some numbers and does some things doing stuff with more numbers.\nIs there any part you'd like to know more about?\n1: memory\n2: instructions\n3: video\n4: programs\n> ");
        byte[] input = System.in.readNBytes(1);
        if (input[0] == '1') {
            System.out.println("The memory? Great!\nMemory is where the computer stores temporary values such as variables, programs and even pixel data.\nThe memory in our emulator is divided into four sections:\n- The RAM (Random Access Memory)\n- The ROM (Read-Only Memory)\n- The Text Buffer\n- The Video Memory");
            System.out.println("The RAM is meant for variables, the ROM is meant for one program, the Text Buffer is meant for text displayed on the screen if it is in text mode\nand the video memory is meant for pixel data if it is in video mode.\nThe CPU can access the memory through the 'Bus'.\nThe Bus is not an actual bus of course, on real computers it's a sort of data cable. Here however, the bus is simply a class with functions for accessing all sections of the memory.\nIt resolves what section addresses belongs to and sets up the memory sections when the emulator starts.");
        }
    }
}
