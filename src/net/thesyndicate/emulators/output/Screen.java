package net.thesyndicate.emulators.output;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen extends JPanel implements OutputController {

    private static final Color DEFAULT_PIXEL_OFF_COLOR = Color.black;
    private static final Color DEFAULT_PIXEL_ON_COLOR = Color.white;

    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_SCALE = 4;

    private Color pixelOffColor = DEFAULT_PIXEL_OFF_COLOR;
    private Color pixelOnColor = DEFAULT_PIXEL_ON_COLOR;

    private BufferedImage image;
    private int scale;

    public Screen() {
        initDisplay(DEFAULT_SCALE);
    }

    public Screen(int scale) {
        initDisplay(scale);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);
    }

    @Override
    synchronized public void draw(boolean[][] data) {
        long start = System.currentTimeMillis();
        Graphics g = image.getGraphics();
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                g.setColor(data[i][j] ? pixelOnColor : pixelOffColor);
                g.fillRect(i * scale, j * scale, scale, scale);
            }
        }
        repaint();

        long end = System.currentTimeMillis();
        // needed to slow down the frames to keep up with the keystrokes
        if(end - start < (1000 / 60))
        try {
            Thread.sleep((1000 / 60) - (end - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // called repaint to allow the pack command to work
        repaint();
    }

    @Override
    public void clear() {
        Graphics2D g = image.createGraphics();
        g.setColor(pixelOffColor);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        repaint();
    }

    public void setScale(int scale) {
        initDisplay(scale);
    }

    private synchronized void initDisplay(int scale) {
        this.scale = scale;

        Dimension dimension = new Dimension(DEFAULT_WIDTH * scale, DEFAULT_HEIGHT * scale);
        setPreferredSize(dimension);

        image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);

        repaint();
    }

    public void setPixelOffColor(Color pixelOffColor) {
        this.pixelOffColor = pixelOffColor;
    }

    public void setPixelOnColor(Color pixelOnColor) {
        this.pixelOnColor = pixelOnColor;
    }
}
