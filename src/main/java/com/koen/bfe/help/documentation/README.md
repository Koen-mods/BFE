<h2 align="center">BFE, Beginner Friendly Emulator</h2>
--
### Introduction
BFE is, as the name suggests, a (hopefully) beginner-friendly emulator. It has a small but effective instruction set and a simple, somewhat dynamic memory layout. 
### How to use?
To execute a program, simply execute the jar or package (depending on what release you are using) with the -e (--execute) flag and the path as follows:
```shell
java -jar ./BFE-1.0-SNAPSHOT.jar -e ./program.bin
```
This will set up the GUI, map memory with the default settings (more on that later), load the interrupt vector table by the default settings and load the bytes of the program into the ROM section of memory.

### Instruction set
The instruction set consists of 23 instructions. Directly using the instructions as hexadecimal instead of assembly is __not__ recommended usually.
<br>The instruction set is as follows (0x is the hexadecimal prefix):
```text
01 (0x01): load_mem [address] [value]
02 (0x02): load_reg [register name] [value]
03 (0x03): add [reg] [value]
04 (0x04): sub [reg] [value]
05 (0x05): jump [instruction address]
06 (0x06): cmp [register] [value]
07 (0x07): jump_eq [instruction address]
08 (0x08): jump_less [instruction address]
09 (0x09): jump_more [instruction address]
10 (0x0A): jump_neq [instruction address]
11 (0x0B): jump_moreq [instruction address]
12 (0x0C): jump_lereq [instruction address]
13 (0x0D): load_reg_mem [reg] [address]
14 (0x0E): load_mem_reg [address] [reg]
15 (0x0F): load_mem_regptr [address] [regptr]
16 (0x10): load_reg_memptr [reg] [address]
17 (0x11): load_regptr [reg] [val]
18 (0x12): int_return
19 (0x13): int [type]
20 (0x14): toggle_int [0/1]
21 (0x15): set_int [entry] [address]
22 (0x16): jumpptr [address]
23 (0x17): cmpmem [register] [address]
24 (0x18): jumpptr_eq [address]
25 (0x19): jumpptr_less [address]
26 (0x1A): jumpptr_more [address]
27 (0x1B): jumpptr_neq [address]
28 (0x1C): jumpptr_moreq [address]
29 (0x1D): jumpptr_lereq [address]
99 (0x63): halt
```
Before I can properly explain what all these instructions do, let me explain some terminology.
<br>
- "mem" is used as an abbreviation for "Memory"
- "reg" is used as an abbreviation for "Register"
- "cmp" is used as an abbreviation for "Compare"
- "eq" is used as an abbreviation for "Equal"
- "ptr" is used as an abbreviation for "Pointer"
- "int" is used as an abbreviation for "Interrupt"
- A "Pointer" is simply a memory address, "memptr" is an address loaded into memory that references another cell of memory
- An "Interrupt" is a signal to the CPU that will make it jump to a certain address, like a predefined function.

Knowing this, most instructions are mostly self-explanatory. For this reason, I will not explain them in this document.
### Registers
The Registers are deliberately very simple.
There are 21 Registers, 16 general purpose registers (named R_[index] respectively. Naming starts at 1, referencing at 0), the PC (Program Counter), the CMP_FLG (Compare Flag), the INT_FLG (Interrupt status Flag), the IRP (Interrupt Return Pointer) and CRF (Compare Return Flag).
You might notice there is no dedicated Stack Pointer. That is because BFE expects users to dedicate one general purpose register to that on their own (allowing freedom for the operating system in the functioning of the stack).
### Memory Layout
The memory is divided into four sections:
- The RAM (Random Access Memory)
- The ROM (Read-Only Memory)
- The Text Buffer
- The Video Memory

The RAM is meant for variables, the ROM is meant for one program, the Text Buffer is meant for text displayed on the screen if it is in text mode and the video memory is meant for pixel data if it is in video mode.<br>Memory is accessed through the Bus class internally.<br>
Besides the four sections, the Bus also exposes a few (dynamically positioned) I/O-registers. There are five I/O registers taking up nine bytes. They sit right after the Text Buffer in memory. The I/O registers are:
- The Timer (4 bytes)
- The Random Number Register (1 byte)
- The System Control Register (1 byte)
- The Display Mode Register (1 byte)
- The Keyboard Status Register (1 byte)
- The Keyboard Data Register (1 byte)

The Timer is a read-only register exposing the uptime in milliseconds as a 32-bit number. The Timer also has another function we will cover later.<br>
The Random Number Register is a read-only register returning a random number between 0 and 255 (or -127 and 127 if read signed).<br>
The System Control Register is a write-only register that can shut down, reboot or continue the operation of the system. If the byte written is 0x01, the system will shut down. If the byte written is 0x02, the system will reboot. If it is any other number, the system will continue operation.<br>
The Display Mode Register is a read-write register handling the Display Mode of the system. If the mode is equal to one, it displays video, if it isn't, it displays text.<br>
The Keyboard status and data registers are read-only registers. Status simply returns a one if the Keyboard (de)Queue is __not__ empty and a zero if it is. Data pops the First item off the Queue and returns that byte (ASCII). The Keyboard Data Queue is FIFO (First In First Out) structured.
<br>
The Default memory layout is as follows:
```text
0x00000000 - 0x000003E8 RAM
0x000003E8 - 0x000007D0 ROM
0x000007D0 - 0x00000BD0 Video Memory
0x00000BD0 - 0x00001344 Text Buffer
0x00001344 - 0x00001348 Timer
0x00001348 Random Number Register
0x00001349 System Control Register
0x0000134A Display Mode Register
0x0000134B Keyboard Status Register
0x0000134C Keyboard Data  Register
```
As you can see, there is a __lot__ of space that could be used in memory, everything can be configured up to a total of 4294967295 bytes of memory (4.3 GiB). 
### Interrupts
Interrupts are mostly user controlled but there are some exceptions. Firstly and most importantly, the Timer interrupt (type 0), is an interrupt firing every 100 milliseconds.<br>
There is also the Keyboard interrupt (type 1), it isn't actually necessary, but it can be used to improve efficiency. <br>
And type 2 is set in the Vector Table, but it can only manually be raised as of now.<br>
You can set up to 256 different interrupts. The standard Vector Table therefore is:
```text
0x00 - 0x0000073A
0x01 - 0x0000076C
0x02 - 0x0000079E
0x03 - 0x0000038E (The start of ROM)
...
0xFF - 0x0000038E (The start of ROM)
```