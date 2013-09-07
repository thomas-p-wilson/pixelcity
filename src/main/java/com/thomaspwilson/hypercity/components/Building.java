/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaspwilson.hypercity.components;

import com.thomaspwilson.hypercity.gl.Vertex;
import com.thomaspwilson.utils.Random;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author twilson
 */
public class Building {
    private static final Random RANDOM = new Random(1382317);
    enum Type {
        MODERN,
        LOWRISE,
        MEDIUMRISE,
        HIGHRISE,
        SIMPLE,
        BLOCKY
    }
    
    protected Object3D building = new Object3D(0);
    protected List<Block> blocks = new ArrayList<Block>();
    protected int maxWidth = 16;
    protected int maxDepth = 16;
    protected int width;
    protected int depth;
    protected int height;
    protected int maxTiers;
    protected int halfWidth;
    protected int halfDepth;
    protected int halfHeight;
    
    // CONSTRUCTION
    //------------------------------------------------------
    public Building() {
        width = RANDOM.intBetween(4, maxWidth);
        depth = RANDOM.intBetween(4, maxDepth);
        height = RANDOM.nextInt(99) + 1;
        maxTiers = tiersForHeight(height);
        halfWidth = width / 2;
        halfDepth = depth / 2;
    }
    
    // GENERATION
    //------------------------------------------------------
    public Object3D generate() {
        // STEPS:
        // 1) Create block
        // 2) Reduce height by random number of stories between 1 and height / 2
        // 3) Goto step 1
        
        int currentHeight = height;
        int tiers = 0;
        int max_left = 1;
        int max_right = 1;
        int max_front = 1;
        int max_back = 1;
        
        Object3D mesh = new Object3D(0);
        do {
            if(currentHeight <= 1 || tiers >= maxTiers) {
                break;
            }
            // Pick the locations for the outer walls that are between center
            // and the maximum
            int left = RANDOM.intBetween(halfWidth + 1, width);
            int right = RANDOM.intBetween(0, halfWidth - 1);
            int front = RANDOM.intBetween(halfDepth + 1, depth);
            int back = RANDOM.intBetween(0, halfDepth - 1);
                         
            // At least one wall must reach beyond a previous maximum
            if(left <= max_left && right <= max_right && front <= max_front
                    && back <= max_back) {
                tiers++;
                height--;
                continue;
            }
            // If any wall is in the same position as the previous maximum
            if(left == max_left || right == max_right || front == max_front
                    || back == max_back) {
                tiers++;
                height--;
                continue;
            }
            
            // Set the new maximums
            max_left = Math.max(left, max_left);
            max_right = Math.max(right, max_right);
            max_front = Math.max(front, max_front);
            max_back = Math.max(back, max_back);
            
            mesh = Object3D.mergeObjects(mesh,
                    createBox(new SimpleVector(left, front, height), new SimpleVector(right, back, 0)));
            height -= RANDOM.intBetween(1, Math.max(2, height / 2)) + 1;
            tiers++;
        } while(true);
        return mesh;
    }
    
    protected void buildWall(SimpleVector start, SimpleVector end,
            int windowGroups, float uvStart, boolean blankCorners) {
        float height = Math.abs(start.y - end.y);
        
        Object3D wall = new Object3D(4);
    }
    
    // HELPERS
    //------------------------------------------------------
    /**
     * 
     * @param s
     * @param e
     * @return 
     */
    protected Object3D createBox(SimpleVector s, SimpleVector e) {
        float left = Math.max(s.x, e.x),
             upper = Math.max(s.y, s.y),
             front = Math.max(s.z, e.z),
             right = Math.min(s.x, e.x),
             lower = Math.min(s.x, e.y),
              back = Math.min(s.z, e.z);

        SimpleVector upperLeftFront  = new SimpleVector(left, upper, front);
        SimpleVector upperRightFront = new SimpleVector(right, upper, front);
        SimpleVector lowerLeftFront  = new SimpleVector(left, lower, front);
        SimpleVector lowerRightFront = new SimpleVector(right, lower, front);

        SimpleVector upperLeftBack   = new SimpleVector(left, upper, back);
        SimpleVector upperRightBack  = new SimpleVector(right, upper, back);
        SimpleVector lowerLeftBack   = new SimpleVector(left, lower, back);
        SimpleVector lowerRightBack  = new SimpleVector(right, lower, back);

        Object3D box = new Object3D(12);
        // Front
        box.addTriangle(upperLeftFront,0,0, lowerLeftFront,0,1, upperRightFront,1,0);
        box.addTriangle(upperRightFront,1,0, lowerLeftFront,0,1, lowerRightFront,1,1);

        // Back
        box.addTriangle(upperLeftBack,0,0, upperRightBack,1,0, lowerLeftBack,0,1);
        box.addTriangle(upperRightBack,1,0, lowerRightBack,1,1, lowerLeftBack,0,1);

        // Upper
        box.addTriangle(upperLeftBack,0,0, upperLeftFront,0,1, upperRightBack,1,0);
        box.addTriangle(upperRightBack,1,0, upperLeftFront,0,1, upperRightFront,1,1);

        // Lower
        box.addTriangle(lowerLeftBack,0,0, lowerRightBack,1,0, lowerLeftFront,0,1);
        box.addTriangle(lowerRightBack,1,0, lowerRightFront,1,1, lowerLeftFront,0,1);

        // Left
        box.addTriangle(upperLeftFront,0,0, upperLeftBack,1,0, lowerLeftFront,0,1);
        box.addTriangle(upperLeftBack,1,0, lowerLeftBack,1,1, lowerLeftFront,0,1);

        // Right
        box.addTriangle(upperRightFront,0,0, lowerRightFront,0,1, upperRightBack,1,0);
        box.addTriangle(upperRightBack,1,0, lowerRightFront, 0,1, lowerRightBack,1,1);
        
        return box;
    }
    /**
     * 
     * @param stories
     * @return 
     */
    protected static int tiersForHeight(int stories) {
        if(stories > 40) {
            return 15;
        }
        if(stories > 30) {
            return 10;
        }
        if(stories > 20) {
            return 5;
        }
        if(stories > 10) {
            return 2;
        }
        return 1;
    }
}