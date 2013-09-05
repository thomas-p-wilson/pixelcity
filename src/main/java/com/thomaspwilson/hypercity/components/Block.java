/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaspwilson.hypercity.components;

/**
 *
 * @author twilson
 */
public class Block {
    private int height;
    private int n;
    private int s;
    private int e;
    private int w;
    
    public Block(int n, int e, int s, int w, int height) {
        this.n = n;
        this.e = e;
        this.s = s;
        this.w = w;
        this.height = height;
    }
    
    public int height() {
        return height;
    }
    public int n() {
        return n;
    }
    public int e() {
        return e;
    }
    public int s() {
        return s;
    }
    public int w() {
        return w;
    }
}
