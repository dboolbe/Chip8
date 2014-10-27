package net.thesyndicate.emulators.output;

import net.thesyndicate.emulators.CPU;
import net.thesyndicate.emulators.Emulator;
import net.thesyndicate.emulators.Memory;
import net.thesyndicate.emulators.input.ROM;

import javax.swing.*;
import java.io.*;
import java.util.Random;

public class Window extends JFrame {

    private static final String title = "Emulator";

    private Screen screen;
    private CPU cpu;
    private Emulator emulator;

    public Window(Emulator emulator) {
        super(title);

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

        System.out.println("MD5: " + rom.generateMD5());
        System.out.println("SHA-1: " + rom.generateSHA1());
        System.out.println("SHA-256: " + rom.generateSHA256());
        System.out.println("File.getFileName(): " + rom.getFileName());
        setTitle(String.format("%s: %s", title, rom.getFileName()));

        cpu.loadROM(rom);
    }

    public void reset() {
        emulator.reset();
    }

    public void pause() {
        emulator.pause();
    }

    public void resume() {
        emulator.resume();
    }

    public void start() {
        emulator.start();
    }

    public void saveState() {
        try {
            FileOutputStream fout = new FileOutputStream("window_test.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(cpu.createMemoryState());
            oos.close();
            System.out.println("Done saving state");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState() {
        try {
            FileInputStream fin = new FileInputStream("window_test.txt");
            ObjectInputStream ois = new ObjectInputStream(fin);
            cpu.loadMemoryState((Memory)ois.readObject());
            ois.close();
            System.out.println("Done loading state");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
