package net.thesyndicate.emulators.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DisplayFrame extends JFrame {

    public DisplayFrame() {
        super("Emulator");

        DisplayPanel panel = new DisplayPanel();

        panel.setPixelOffColor(Color.green);

        add(panel);
        setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        panel.clear();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Changing the image.");
        Random random = new Random();

        boolean[][] testImage = new boolean[64][32];
        for(int r = 0; r < testImage.length; r++)
            for(int c = 0; c < testImage[r].length; c++)
                testImage[r][c] = (random.nextInt(2) == 0);
        panel.draw(testImage);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        panel.setPixelOnColor(Color.blue);
        panel.clear();
        for(int r = 0; r < testImage.length; r++)
            for(int c = 0; c < testImage[r].length; c++)
                testImage[r][c] = (random.nextInt(2) == 0);
        panel.draw(testImage);
    }
}
