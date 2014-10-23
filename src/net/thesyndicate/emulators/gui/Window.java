package net.thesyndicate.emulators.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.Random;

public class Window extends JFrame {

    private Screen screen;

    public Window() {
        super("Emulator");

        initScreen();
        initMenuBar();
        initWindow();
    }

    private void initScreen() {
        screen = new Screen();
        add(screen);
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

    public void testScreen() {
        clear();

        System.out.println("Changing the image.");
        Random random = new Random();

        boolean[][] testImage = new boolean[64][32];
        for(int r = 0; r < testImage.length; r++)
            for(int c = 0; c < testImage[r].length; c++)
                testImage[r][c] = (random.nextInt(2) == 0);
        draw(testImage);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
