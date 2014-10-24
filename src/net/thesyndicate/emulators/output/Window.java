package net.thesyndicate.emulators.output;

import net.thesyndicate.emulators.CPU;
import net.thesyndicate.emulators.Emulator;
import net.thesyndicate.emulators.input.ROM;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.Random;

public class Window extends JFrame {

    private Screen screen;
    private CPU cpu;
    private Emulator emulator;

    public Window(Emulator emulator) {
        super("Emulator");

        this.emulator = emulator;

        initScreen();
        initCPU(emulator);
        initMenuBar();
        initWindow();
    }

    private void initScreen() {
        screen = new Screen();
        add(screen);
    }

    private void initCPU(Emulator emulator) {
        cpu = emulator.getCpu();
    }

    private void initMenuBar() {
        setJMenuBar(new MenuBar(this));
    }

    private void initWindow() {
        setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public synchronized void draw(boolean[][] data) {
        screen.draw(data);
    }

    public synchronized void clear() {
        screen.clear();
    }

    public void setScale(int scale) {
        screen.setScale(scale);
        pack();
    }

    public void loadROM(ROM rom) {
        if(cpu == null) {
            System.out.println("It appears that `cpu` is null.");
            System.out.print("Will try to initialize now...");
            cpu = emulator.getCpu();
            System.out.println("done.");
        }
        cpu.loadROM(rom);
    }

    public void start() {
        emulator.start();
    }

    public void testScreen() {
        clear();

        System.out.println("Changing the image.");
        Random random = new Random();

        boolean[][] testImage = new boolean[64][32];
        for(int r = 0; r < testImage.length; r++)
            for(int c = 0; c < testImage[r].length; c++)
                testImage[r][c] = (random.nextInt(2) == 0);
        draw(testImage);
    }
}
