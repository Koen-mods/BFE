package com.koen.bfe.video;

import com.koen.bfe.instructions.parsing.CPU;
import com.koen.bfe.memory.Bus;

import javax.swing.*;
import java.awt.*;

public class ScreenPanel extends JPanel {
    public Bus bus = CPU.getBus();
    public ScreenPanel() {
        setPreferredSize(new Dimension(bus.getVideoWidth() * 10, bus.getVideoHeight() * 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < bus.getVideoSize(); i++) {

            try {
                int colour = bus.read(bus.getVideoStart() + i);
                int x = (i % bus.getVideoWidth()) * 10;
                int y = (i / bus.getVideoWidth()) * 10;
                g.setColor(new Color(0, colour, 0));
                g.fillRect(x, y, 10, 10);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
