package net.thesyndicate.emulators.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuBar extends JMenuBar implements ActionListener {

    private JFrame frame;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if(o == loadItem) {
            System.out.println("do Load action");
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
        }
    }
}
