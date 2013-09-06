/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaspwilson.utils;

/**
 *
 * @author twilson
 */
public class Random extends java.util.Random {
    public Random() {
        super();
    }
    public Random(long seed) {
        super(seed);
    }
    
    public int intBetween(int min, int max) {
        return nextInt(max - min) + min;
    }
}