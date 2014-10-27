package net.thesyndicate.emulators;

import java.io.Serializable;

public class Memory implements Serializable {

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

    private String hashString;

    public void setPixels(boolean[][] pixels) {
        this.pixels = pixels;
    }

    public boolean[][] getPixels() {
        return pixels;
    }

    public void setKeys(boolean[] keys) {
        this.keys = keys;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public void setMemory(int[] memory) {
        this.memory = memory;
    }

    public int[] getMemory() {
        return memory;
    }

    public void setRegister(int[] register) {
        this.register = register;
    }

    public int[] getRegister() {
        return register;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public int[] getStack() {
        return stack;
    }

    public void setDrawFlag(boolean drawFlag) {
        this.drawFlag = drawFlag;
    }

    public boolean getDrawFlag() {
        return drawFlag;
    }

    public void setRomLoaded(boolean isRomLoaded) {
        this.isRomLoaded = isRomLoaded;
    }

    public boolean getRomLoaded() {
        return isRomLoaded;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean getRunning() {
        return isRunning;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean getPaused() {
        return isPaused;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setIndexRegister(int indexRegister) {
        this.indexRegister = indexRegister;
    }

    public int getIndexRegister() {
        return indexRegister;
    }

    public void setDelayTimer(int delayTimer) {
        this.delayTimer = delayTimer;
    }

    public int getDelayTimer() {
        return delayTimer;
    }

    public void setSoundTimer(int soundTimer) {
        this.soundTimer = soundTimer;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }

    public String getHashString() {
        return hashString;
    }

    @Override
    public String toString() {
        return new StringBuffer(" pixels: ")
                .append(pixels)
                .append(" keys ")
                .append(keys)
                .append(" memory ")
                .append(memory)
                .append(" register ")
                .append(register)
                .append(" stack ")
                .append(stack)
                .append(" drawFlag ")
                .append(drawFlag)
                .append(" isRomLoaded ")
                .append(isRomLoaded)
                .append(" isRunning ")
                .append(isRunning)
                .append(" isPaused ")
                .append(isPaused)
                .append(" programCounter ")
                .append(programCounter)
                .append(" stackPointer ")
                .append(stackPointer)
                .append(" indexRegister ")
                .append(indexRegister)
                .append(" delayTimer ")
                .append(delayTimer)
                .append(" soundTimer ")
                .append(soundTimer)
                .append(" hashString ")
                .append(hashString).toString();
    }
}