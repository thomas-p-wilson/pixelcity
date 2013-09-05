/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaspwilson.hypercity.gl;

import com.threed.jpct.SimpleVector;

/**
 *
 * @author twilson
 */
public class Vertex {
    public int x;
    public int y;
    public int z;
    
    public Vertex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public SimpleVector toVector() {
        return new SimpleVector(x, z, y);
    }
}