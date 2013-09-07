/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaspwilson.hypercity.texture;

import com.thomaspwilson.utils.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author twilson
 */
public class WindowGenerator {
    public static final Random RANDOM = new Random();
    
    public Image generate() {
        BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        
        // Travel across the x axis
        for(int x = 0; x < 32; x++) {
            for(int y = 0; y < 32; y++) {
                g.setColor(generateWindowColor());
                g.fillRect(x * 32 + 1, y * 32 + 1, 30, 30);
            }
        }
        return image;
    }
    
    private Color generateWindowColor() {
        int shade = RANDOM.intBetween(10, 40);
        if(RANDOM.nextBoolean()) {
            return new Color(shade, shade, shade);
        } else {
            shade += 215;
            return new Color(shade, shade, shade - 20);
        }
    }
}