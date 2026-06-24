package com.koen.bfe.video;

import com.koen.bfe.Main;
import com.koen.bfe.essentials.input.FileHandling;
import com.koen.bfe.instructions.parsing.CPU;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.io.InputStream;

public class BImg {
    public static Pixel[] parseBImg(String path) throws IOException, SizeLimitExceededException {
        byte[] data = FileHandling.readFileContents(path);
        if (data.length > CPU.getBus().getVideoSize()) {
            throw new SizeLimitExceededException("Image too large to parse into pixel data");
        }

        Pixel[] pix = new Pixel[data.length];

        for (int i = 0; i < data.length; i++) {
            pix[i] = new Pixel(i % CPU.getBus().getVideoWidth(), (int) i / CPU.getBus().getVideoWidth(), (byte) (data[i] & 0xFF));
        }

        return pix;
    }

    public static Pixel[] parseBImgFromResources(String path) throws IOException, SizeLimitExceededException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("images/" + path);
        if (is == null) {
            throw new IOException("No");
        }
        byte[] data = is.readAllBytes();
        if (data.length > CPU.getBus().getVideoSize()) {
            throw new SizeLimitExceededException("Image too large to parse into pixel data");
        }

        Pixel[] pix = new Pixel[data.length];

        for (int i = 0; i < data.length; i++) {
            pix[i] = new Pixel(i % CPU.getBus().getVideoWidth(), (int) i / CPU.getBus().getVideoWidth(), (byte) (data[i] & 0xFF));
        }

        return pix;
    }

    public static void loadBImg(String path) throws SizeLimitExceededException, IOException, IllegalAccessException {
        CPU.getBus().clearVideo();
        Pixel[] pix = parseBImg(path);
        for (int i = 0; i < pix.length; i++) {
            CPU.getBus().write(CPU.getBus().getVideoStart() + i, pix[i].c);
        }

        Main.panel.repaint();
    }
}
