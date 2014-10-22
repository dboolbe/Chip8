package net.thesyndicate.emulators.input;

import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public interface InputController extends KeyListener {

    final Map<Integer, Integer> buttonMapping = new HashMap<Integer, Integer>();

    public void setButtonMapping(int code, int position);
}
