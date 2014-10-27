package net.thesyndicate.emulators;

import net.thesyndicate.emulators.exception.*;
import net.thesyndicate.emulators.input.ROM;
import net.thesyndicate.emulators.output.Window;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class CPU implements Runnable {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 32;

    private static final int MAX_MEMORY = 4096;
    private static final int MAX_REGISTERS = 16;
    private static final int MAX_STACK = 16;
    private static final int MAX_KEYS = 16;

    private static final int NIBBLE = 4;
    private static final int BYTE = 8;

    private boolean[][] pixels;
    private boolean[] keys;

    private int[] memory;
    private int[] register;
    private int[] stack;

    private boolean drawFlag;
    private boolean isRomLoaded;
    private boolean isRunning;
    private boolean isPaused;

    private int programCounter;
    private int stackPointer;
    private int indexRegister;
    private int delayTimer;
    private int soundTimer;

    private Emulator emulator;
    private Window window;

    private static final short[] font = {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    public CPU(Emulator emulator) {
        this.emulator = emulator;

        isRunning = true;
        isPaused = false;
        isRomLoaded = false;

        // instantiate registers and memory
        memory = new int[MAX_MEMORY];
        register = new int[MAX_REGISTERS];
        stack = new int[MAX_STACK];
        drawFlag = false;
        pixels = new boolean[WIDTH][HEIGHT];
        keys = new boolean[MAX_KEYS];

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!isPaused) {
                    if(soundTimer == 0)
                        Toolkit.getDefaultToolkit().beep();
                    delayTimer = Math.max(delayTimer - 1, -1);
                    soundTimer = Math.max(soundTimer - 1, -1);
                }
            }
        };

        timer.schedule(task, 0, Math.round(1000.0 / 600.0));

        initialize();
    }

    public void init() {
        this.window = emulator.getWindow();
    }

    private void initialize() {
        // initialize memory and registers
        Arrays.fill(memory, 0);
        Arrays.fill(register, 0);
        Arrays.fill(stack, 0);

        for(int i = 0; i < pixels.length; i++)
            Arrays.fill(pixels[i], false);
        Arrays.fill(keys, false);

        drawFlag = false;

        indexRegister = 0;
        programCounter = 0x200;
        stackPointer = 0;

        delayTimer = 0;
        soundTimer = 0;

        // load font set into memory
        for(int i = 0; i < font.length; i++)
            memory[i] = font[i] & 0xFF;
    }

    public void reset() {
        // initialize memory and registers
        Arrays.fill(register, 0);
        Arrays.fill(stack, 0);

        for(int i = 0; i < pixels.length; i++)
            Arrays.fill(pixels[i], false);
        Arrays.fill(keys, false);

        drawFlag = false;

        indexRegister = 0;
        programCounter = 0x200;
        stackPointer = 0;

        delayTimer = 0;
        soundTimer = 0;

        clearScreen();
    }

    public void loadROM(ROM rom) {
        // load ROM data into memory
        ByteBuffer buffer = rom.getBuffer();
        for(int i = 0; i < buffer.limit(); i++)
            memory[0x200 + i] = buffer.get();

        isRomLoaded = true;
    }

    @Override
    public void run() {
        if(!isRomLoaded)
            throw new NullPointerException("No ROM has been loaded.");
        while(isRunning) {
            // execution cycle
            try {
                executionCycle();
            } catch (ProgramCounterOutOfBoundsException e) {
                e.printStackTrace();
            } catch (UnknownOpcodeException e) {
                e.printStackTrace();
            } catch (StackOverflowException e) {
                e.printStackTrace();
            } catch (RegisterOutOfBoundsException e) {
                e.printStackTrace();
            }

            // draw cycle
            if(drawFlag && window != null) {
                window.draw(pixels);}

            // loop to simulate a pause
            pauseLoop();
        }
    }

    private void pauseLoop() {
        while(isPaused) { /** holds execution when paused **/
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized private void executionCycle() throws ProgramCounterOutOfBoundsException, UnknownOpcodeException, StackOverflowException, RegisterOutOfBoundsException {
        // Validate Program Counter
        if(programCounter + 1 >= MAX_MEMORY || programCounter < 0)
            throw new ProgramCounterOutOfBoundsException("CounterOutOfBounds reading opcode at PC = " + programCounter);

        // Fetch Opcode
        int high = memory[programCounter] & 0xFF;
        int low = memory[programCounter + 1] & 0xFF;
        int opcode = high << BYTE | low;

        // Increment Counter
        programCounter += 2;

        // Reset Draw Flag
        drawFlag = false;

        // Decode Opcode
        int x = high & 0xF;             //0x00 for x
        int y = (low & 0xF0) >> NIBBLE; //00y0 for y
        int n = low & 0xF;              //000n for n
        int nn = low;                   //00nn for nn
        int nnn = (x << BYTE) | low;    //0nnn for nnn

        // Execute Opcode
        switch(high >> NIBBLE) {
            case 0x0:
                // 0NNN Calls RCA 1802 program at address NNN.
                switch(low) {
                    case 0xE0: // 00E0  Clears the screen.
                        clearScreen();
                        break;
                    case 0xEE: // 00EE  Returns from a subroutine.
                        if(stackPointer - 1 < 0)
                            throw new StackOverflowException("Stack point out of bounds at " + (stackPointer - 1));
                        programCounter = stack[--stackPointer];
                        break;
                    default:
                        invalidOpcode(opcode);
                        break;
                }
                break;
            case 0x1: // 1NNN   Jumps to address NNN.
                programCounter = nnn;
                break;
            case 0x2: // 2NNN   Calls subroutine at NNN.
                if (stackPointer + 1 > MAX_STACK)
                    throw new StackOverflowException("Stack point out of bounds at " + (stackPointer + 1));
                stack[stackPointer++] = programCounter;
                programCounter = nnn;
                break;
            case 0x3: // 3XNN   Skips the next instruction if VX equals NN.
                if(register[x] == nn)
                    programCounter += 2;
                break;
            case 0x4: // 4XNN   Skips the next instruction if VX doesn't equal NN.
                if(register[x] != nn)
                    programCounter += 2;
                break;
            case 0x5: // 5XY0   Skips the next instruction if VX equals VY.
                if(register[x] == register[y])
                    programCounter += 2;
                break;
            case 0x6: // 6XNN   Sets VX to NN.
                register[x] = nn;
                break;
            case 0x7: // 7XNN   Adds NN to VX.
                register[x] += nn;
                register[x] &= 0xFF;
                break;
            case 0x8:
                switch(n) {
                    case 0x0: // 8XY0   Sets VX to the value of VY.
                        register[x] = register[y];
                        break;
                    case 0x1: // 8XY1   Sets VX to VX or VY.
                        register[x] |= register[y];
                        break;
                    case 0x2: // 8XY2   Sets VX to VX and VY.
                        register[x] &= register[y];
                        break;
                    case 0x3: // 8XY3   Sets VX to VX xor VY.
                        register[x] ^= register[y];
                        break;
                    case 0x4: // 8XY4   Adds VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't.
                        register[x] += register[y];
                        register[0xF] = register[x] > 0xFF ? 1 : 0;
                        register[x] &= 0xFF;
                        break;
                    case 0x5: // 8XY5   VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
                        register[0xF] = register[y] > register[x] ? 0 : 1;
                        register[x] -= register[y];
                        register[x] &= 0xFF;
                        break;
                    case 0x6: // 8XY6   Shifts VX right by one. VF is set to the value of the least significant
                        // bit of VX before the shift.[2]
                        register[0xF] = register[x] & 0x1;
                        register[x] = register[x] >> 1;
                        break;
                    case 0x7: // 8XY7   Sets VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
                        register[0xF] = register[x] > register[y] ? 0 : 1;
                        register[x] = register[y] - register[x];
                        register[x] &= 0xFF;
                        break;
                    case 0xE: // 8XYE   Shifts VX left by one. VF is set to the value of the most significant
                        // bit of VX before the shift.[2]
                        register[0xF] = register[x] >> 7;
                        register[x] = register[x] << 1 & 0xFF;
                        break;
                }
                break;
            case 0x9: // 9XY0   Skips the next instruction if VX doesn't equal VY.
                if(register[x] != register[y])
                    programCounter += 2;
                break;
            case 0xA: // ANNN   Sets I to the address NNN.
                indexRegister = nnn;
                break;
            case 0xB: // BNNN   Jumps to the address NNN plus V0.
                programCounter = (nnn + register[0x0]) & 0xFFF;
                break;
            case 0xC: // CXNN   Sets VX to a random number and NN.
                register[x] = ((int) (Math.random() * 0xFF)) & nn;
                break;
            case 0xD: // DXYN   Sprites stored in memory at location in index register (I), maximum 8bits wide.
                // Wraps around the screen. If when drawn, clears a pixel, register VF is set to 1
                // otherwise it is zero. All drawing is XOR drawing (e.g. it toggles the screen pixels)
                draw(register[x], register[y], n);
                break;
            case 0xE:
                switch(low) {
                    case 0x9E: // EX9E  Skips the next instruction if the key stored in VX is pressed.
                        if(keys[register[x]]) {
                            programCounter += 2;
                            keys[register[x]] = false;
                        }
                        break;
                    case 0xA1: // EXA1  Skips the next instruction if the key stored in VX isn't pressed.
                        if(!keys[register[x]])
                            programCounter += 2;
                        break;
                    default:
                        invalidOpcode(opcode);
                        break;
                }
                break;
            case 0xF:
                switch(low) {
                    case 0x07: // FX07  Sets VX to the value of the delay timer.
                        register[x] = delayTimer;
                        register[x] &= 0xFF;
                        break;
                    case 0x0A: // FX0A  A key press is awaited, and then stored in VX.
                        awaitKeyPress(x);
                        break;
                    case 0x15: // FX15  Sets the delay timer to VX.
                        delayTimer = register[x];
                        break;
                    case 0x18: // FX18  Sets the sound timer to VX.
                        soundTimer = register[x];
                        break;
                    case 0x1E: // FX1E  Adds VX to I.[3]
                        indexRegister += register[x];
                        indexRegister &= 0xFFF;
                        break;
                    case 0x29: // FX29  Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font.
                        indexRegister = register[x] * 5;
                        break;
                    case 0x33: // FX33  Stores the Binary-coded decimal representation of VX, with the most significant of three digits at the address in I, the middle digit at I plus 1, and the least significant digit at I plus 2. (In other words, take the decimal representation of VX, place the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.)
                        memory[indexRegister] = register[x] / 100;
                        memory[indexRegister + 1] = (register[x] / 10) % 10;
                        memory[indexRegister + 2] = (register[x] % 100) % 10;
                        break;
                    case 0x55: // FX55  Stores V0 to VX in memory starting at address I.[4]
                        for(int i = 0x0; i <= x; i++)
                            memory[indexRegister + i] = register[i];
                        break;
                    case 0x65: // FX65  Fills V0 to VX with values from memory starting at address I.[4]
                        for(int i = 0x0; i <= x; i++)
                            register[i] = memory[indexRegister + i];
                        break;
                    default:
                        invalidOpcode(opcode);
                        break;
                }
                break;
            default:
                invalidOpcode(opcode);
                break;
        }
    }

    public void kill() {
        isRunning = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    private void draw(int x, int y, int height) {
        // first set flag to off. It will later be set if a pixel is flipped from on to off
        register[0xF] = 0;

        for(int j = 0; j < height; j++) {
            int dat = memory[j + indexRegister];

            for(int i = 0; i < 8; i++) {
                // if the bit is 0, we aren't changing this value
                if((dat & (0x80 >> i)) == 0) continue;

                // NOTE: not sure if meant to skip out-of-bounds pixels or modulo them
                // for the time being, I am ignoring them, as all games seem to function using this mechanism

                int rx = i + x;
                int ry = j + y;

                // ignore them.
                if(rx >= WIDTH || ry >= HEIGHT) continue;

                // modulo version. Causes weird visual disturbances on the Blitz game
                //rx %= 64;
                //ry %= 32;

                //if the pixel was on, it means we are now unsetting it, and we must set the carry flag
                if(pixels[rx][ry]) register[0xF] = 1;
                //flip the pixel
                pixels[rx][ry] ^= true;
            }
        }

        //set draw flag to show we need to redraw
        drawFlag = true;
    }

    private void awaitKeyPress(int destReg) throws RegisterOutOfBoundsException {
        if(destReg < 0x0 || destReg > 0xF)
            throw new RegisterOutOfBoundsException("Cannot await on register: " + destReg);

        for(int i = 0; i < keys.length; i++) {
            //if a key is pressed, the await succeeded, and we set it and return
            if(keys[i]) {
                register[destReg] = 1 << i;
                return;
            }
        }System.out.println("destReg: " + destReg);

        //if we had no key pressed, we decrement out pc which causes the instruction to repeat againtest
        programCounter -= 2;
    }

    private void invalidOpcode(int opcode) throws UnknownOpcodeException {
        throw new UnknownOpcodeException("Unknown opcode: " + Integer.toHexString(opcode).toUpperCase() + " at PC = " + (programCounter - 2));
    }

    public void keyInteracted(int i, boolean pressed) throws InvalidKeyException {
        if(i < 0x0 | i > 0xF)
            throw new InvalidKeyException("Invalid Key Pressed.");

        keys[i] = pressed;
    }

    private void clearScreen() {
        for(int i = 0; i < pixels.length; i++)
            Arrays.fill(pixels[i], false);

        drawFlag = true;
    }
}
