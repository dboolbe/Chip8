package net.thesyndicate.emulators.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JPanel {

    private static final Color DEFAULT_PIXEL_OFF_COLOR = Color.black;
    private static final Color DEFAULT_PIXEL_ON_COLOR = Color.white;

    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_SCALE = 4;

    private Color pixelOffColor = DEFAULT_PIXEL_OFF_COLOR;
    private Color pixelOnColor = DEFAULT_PIXEL_ON_COLOR;

    private BufferedImage image;
    private int scale;

    public DisplayPanel() {
        initDisplay(DEFAULT_SCALE);
    }

    public DisplayPanel(int scale) {
        initDisplay(scale);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);
    }

    public void draw(boolean[][] data) {
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                for(int x = i * scale; x < i * scale + scale; x++) {
                    for(int y = j * scale; y < j * scale + scale; y++) {
                        Color c = data[i][j] ? pixelOnColor : pixelOffColor;
                        image.setRGB(x, y, c.getRGB());
                    }
                }
            }
        }
        repaint();
    }

    public void clear() {
        Graphics2D g = image.createGraphics();
        g.setColor(pixelOffColor);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        repaint();
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
