package net.thesyndicate.emulators.input;

import net.thesyndicate.emulators.CPU;
import net.thesyndicate.emulators.Emulator;
import net.thesyndicate.emulators.exception.InvalidKeyException;

import java.awt.event.KeyEvent;

public class KeyBoard implements InputController {

    private CPU cpu = null;

    private enum Key {
        x0 (0x0, KeyEvent.VK_Q),
        x1 (0x1, KeyEvent.VK_W),
        x2 (0x2, KeyEvent.VK_E),
        x3 (0x3, KeyEvent.VK_A),
        x4 (0x4, KeyEvent.VK_S),
        x5 (0x5, KeyEvent.VK_D),
        x6 (0x6, KeyEvent.VK_Z),
        x7 (0x7, KeyEvent.VK_X),
        x8 (0x8, KeyEvent.VK_C),
        x9 (0x9, KeyEvent.VK_1),
        xA (0xA, KeyEvent.VK_2),
        xB (0xB, KeyEvent.VK_3),
        xC (0xC, KeyEvent.VK_4),
        xD (0xD, KeyEvent.VK_R),
        xE (0xE, KeyEvent.VK_F),
        xF (0xF, KeyEvent.VK_V);

        private int code, position;

        Key(int position, int code) {
            this.position = position;
            this.code = code;
        }

        public int getPosition() {
            return position;
        }

        public int getCode() {
            return code;
        }
    }

    public KeyBoard(Emulator emulator) {
        this.cpu = emulator.getCpu();

        for(Key key : Key.values())
            setButtonMapping(key.getCode(), key.getPosition());
    }

    @Override
    public void setButtonMapping(int code, int position) {
        buttonMapping.put(code, position);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        interacted(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        interacted(e.getKeyCode(), false);
    }

    private void interacted(int val, boolean pushed) {
        Integer b = buttonMapping.get(val);
        if(b != null)
            try {
                cpu.keyInteracted(b, pushed);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
    }
}
