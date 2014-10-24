package net.thesyndicate.emulators.output;

import net.thesyndicate.emulators.input.ROM;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuBar extends JMenuBar implements ActionListener {

    private Window window;
    private JMenu screenSizeItem;
    private JMenuItem loadItem, resetItem, exitItem, saveStateItem, openStateItem, pickStateItem, inputItem, helpItem, aboutItem;
    private JRadioButtonMenuItem pauseItem, muteItem;

    private static HashMap<String, Object[]> keyBindings;
    private ArrayList<JRadioButtonMenuItem> scaleButtons;

    static {
        keyBindings = new HashMap<String, Object[]>();

        keyBindings.put("1", new Object[]{"1", KeyEvent.VK_1});
        keyBindings.put("2", new Object[]{"2", KeyEvent.VK_2});
        keyBindings.put("3", new Object[]{"3", KeyEvent.VK_3});
        keyBindings.put("C", new Object[]{"C", KeyEvent.VK_C});
        keyBindings.put("4", new Object[]{"4", KeyEvent.VK_4});
        keyBindings.put("5", new Object[]{"5", KeyEvent.VK_5});
        keyBindings.put("6", new Object[]{"6", KeyEvent.VK_6});
        keyBindings.put("D", new Object[]{"D", KeyEvent.VK_D});
        keyBindings.put("7", new Object[]{"7", KeyEvent.VK_7});
        keyBindings.put("8", new Object[]{"8", KeyEvent.VK_8});
        keyBindings.put("9", new Object[]{"9", KeyEvent.VK_9});
        keyBindings.put("E", new Object[]{"E", KeyEvent.VK_E});
        keyBindings.put("A", new Object[]{"A", KeyEvent.VK_A});
        keyBindings.put("0", new Object[]{"0", KeyEvent.VK_0});
        keyBindings.put("B", new Object[]{"B", KeyEvent.VK_B});
        keyBindings.put("F", new Object[]{"F", KeyEvent.VK_F});
    }

    public MenuBar(Window window) {
        this.window = window;

        JMenu gameMenu = new JMenu("Game");
        JMenu configMenu = new JMenu("Config");
        JMenu helpMenu = new JMenu("Help");

        loadItem = new JMenuItem("Load");
        pauseItem = new JRadioButtonMenuItem("Pause", false);
        resetItem = new JMenuItem("Reset");
        saveStateItem = new JMenuItem("Save State");
        openStateItem = new JMenuItem("Open State");
        pickStateItem = new JMenuItem("Pick State");
        exitItem = new JMenuItem("Exit");

        loadItem.addActionListener(this);
        pauseItem.addActionListener(this);
        resetItem.addActionListener(this);
        saveStateItem.addActionListener(this);
        openStateItem.addActionListener(this);
        pickStateItem.addActionListener(this);
        exitItem.addActionListener(this);

        gameMenu.add(loadItem);
        gameMenu.add(pauseItem);
        gameMenu.add(resetItem);
        gameMenu.addSeparator();
        gameMenu.add(saveStateItem);
        gameMenu.add(openStateItem);
        gameMenu.add(pickStateItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        inputItem = new JMenuItem("Input");
        screenSizeItem = new JMenu("Screen Size");
        muteItem = new JRadioButtonMenuItem("Mute");

        inputItem.addActionListener(this);
        screenSizeItem.addActionListener(this);
        muteItem.addActionListener(this);

        configMenu.add(inputItem);
        configMenu.add(screenSizeItem);
        configMenu.add(muteItem);

        scaleButtons = new ArrayList<JRadioButtonMenuItem>();
        for(int i = 0; i < 4; i++) {
            int val = (int)Math.pow(2, i);

            scaleButtons.add(new JRadioButtonMenuItem("x" + val, val == 2));
        }

        ButtonGroup buttonGroup = new ButtonGroup();
        for(JRadioButtonMenuItem button : scaleButtons) {
            buttonGroup.add(button);
            button.addActionListener(this);
            screenSizeItem.add(button);
        }

        helpItem = new JMenuItem("Chip8 Help");
        aboutItem = new JMenuItem("About Chip8");

        helpItem.addActionListener(this);
        aboutItem.addActionListener(this);

        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        add(gameMenu);
        add(configMenu);
        add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if(o == loadItem) {
            System.out.println("do Load action");
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(window);
            System.out.println("returnVal = " + returnVal);
            File file = fc.getSelectedFile();
            System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
            window.loadROM(ROM.createROM(file.getAbsolutePath()));
            window.start();
        } else if(o == pauseItem) {
            System.out.println("do Pause action");
        } else if(o == resetItem) {
            System.out.println("do Reset action");
        } else if(o == saveStateItem) {
            System.out.println("do Save State action");
        } else if(o == openStateItem) {
            System.out.println("do Open State action");
        } else if(o == pickStateItem) {
            System.out.println("do Pick State action");
        } else if(o == inputItem) {
            System.out.println("do Input action");
        } else if(o == screenSizeItem) {
            System.out.println("do Screen Size action");
        } else if(o == muteItem) {
            System.out.println("do Mute action");
        } else if(o == helpItem) {
            System.out.println("do Help action");
        } else if(o == aboutItem) {
            System.out.println("do About action");
        } else if(o == exitItem) {
            System.out.println("do Exit action");
        } else if(o instanceof JRadioButtonMenuItem) {
            int i = scaleButtons.indexOf(o);
            if(i != -1) System.out.println("do x" + (int)Math.pow(2, i) + " action");
            switch(i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    int multipler = (int)Math.pow(2, i);
                    int side = (4 * multipler);
                    System.out.println("Changing scale to x" + multipler + " which is " + side + "x" + side + ".");
                    window.setScale(side);
                    window.testScreen();
                    break;
                default:
                    System.out.println("Unimplemented Action: " + i);
                    break;
            }
        }
    }
}
