package net.thesyndicate.emulators;

import net.thesyndicate.emulators.input.KeyBoard;
import net.thesyndicate.emulators.output.Window;

public class Emulator {

    private Window window;
    private CPU cpu;
    private KeyBoard keyBoard;

    public Emulator() {
        initComponents();
    }

    private void initComponents() {
        window = new Window(this);
        cpu = new CPU(this);
        keyBoard = new KeyBoard(this);

        window.addKeyListener(keyBoard);
        init();
    }

    private void init() {
        window.init();
        cpu.init();
    }

    public void start() {
        (new Thread(cpu)).start();
    }

    public Window getWindow() {
        return window;
    }

    public CPU getCpu() {
        return cpu;
    }

    public KeyBoard getKeyBoard() {
        return keyBoard;
    }
}
