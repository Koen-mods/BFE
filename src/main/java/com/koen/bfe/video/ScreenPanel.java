package com.koen.bfe.video;

import com.koen.bfe.Main;
import com.koen.bfe.instructions.parsing.CPU;
import com.koen.bfe.memory.Bus;

import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

        if (bus.isVidEnabled()) {
            for (int i = 0; i < bus.getVideoSize(); i++) {

                try {
                    int colour = bus.read(bus.getVideoStart() + i);
                    if (bus.isVidEnabled()) {
                        int x = (i % bus.getVideoWidth()) * 10;
                        int y = (i / bus.getVideoWidth()) * 10;
                        g.setColor(new Color(0, colour & 0xFF, colour & 0xFF));
                        g.fillRect(x, y, 10, 10);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            for (int i = 0; i < bus.getTextSize(); i++) {
                try {
                    char curr = (char) bus.read(bus.getTextStart() + i);
                    //System.out.println(curr);
                    Pixel[] glyph = BImg.parseBImgFromResources("letters/" + curr + ".bin");
                    for (Pixel p : glyph) {
                        p.x += (i % bus.getTextWidth()) * 6;
                        p.y += ((int) i / bus.getTextWidth()) * 9;

                        g.setColor(new Color(0, p.c & 0xFF, p.c & 0xFF));

                        g.fillRect(p.x, p.y, 1, 1);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (SizeLimitExceededException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    continue;
                }
            }
        }


    }
}
