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
    private static final Random RANDOM = new Random();
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
    protected int halfWidth;
    protected int halfDepth;
    protected int halfHeight;
    
    // CONSTRUCTION
    //------------------------------------------------------
    public Building() {
        width = RANDOM.intBetween(4, maxWidth);
        depth = RANDOM.intBetween(4, maxDepth);
        height = RANDOM.nextInt(99) + 1;
        halfWidth = width / 2;
        halfDepth = depth / 2;
    }
    
    // GENERATION
    //------------------------------------------------------
    public Object3D generate() {
        // STEPS:
        // 1) Create top block
        // 2) Reduce height by random number of stories between 1 and height / 2
        // 3) Create another block
        // 4) Goto step 2
        
        
        
        int max_left = 1,
            max_right = 1,
            max_front = 1,
            max_back = 1,
            height = this.height,
            min_height = height / 2,
            tiers = 0;
        boolean skip = false;
        
        List<Block> blocks = new ArrayList<Block>();
        float lidHeight = RANDOM.nextFloat() * 3 + 1;
        int max_tiers = tiersForHeight(height);
        
        // Start from the top and work down
        do {
            if(height < min_height) {
                break;
            }
            if(tiers >= max_tiers) {
                break;
            }
            // Pick locations for the outer walls that are between center and
            // the maximum
            int left = RANDOM.intBetween(halfWidth + 1, width);
            int right = RANDOM.intBetween(0, halfWidth - 1);
            int front = RANDOM.intBetween(halfDepth + 1, depth);
            int back = RANDOM.intBetween(0, halfDepth - 1);
            skip = false;
            
            // At least one wall must reach beyond a previous maximum
            if(left <= max_left && right <= max_right && front <= max_front
                    && back <= max_back) {
                skip = true;
            }
            // If any wall is in the same position as the previous maximum
            if(left == max_left || right == max_right || front == max_front
                    || back == max_back) {
                skip = true;
            }
            
            if(!skip) {
                max_left = Math.max(left, max_left);
                max_right = Math.max(right, max_right);
                max_front = Math.max(front, max_front);
                max_back = Math.max(back, max_back);
                
                blocks.add(new Block(left, back, right, front, height));
                if(tiers == 0) {
                    // ConstructRoof ((float)(mid_x - left), (float)(mid_x + right), (float)(mid_z - front), (float)(mid_z + back), (float)height);
                } else {
                    // ConstructCube ((float)(mid_x - left), (float)(mid_x + right), (float)(mid_z - front), (float)(mid_z + back), (float)height, (float)height + lid_height);
                }
                height -= RANDOM.intBetween(1, height / 2) + 1;
                tiers++;
            }
            height--;
            break;
        } while(true);
        blocks.add(new Block(0, width, depth, 0, 2));
        
        Object3D mesh = new Object3D(0);
        for(Block block : blocks) {
            mesh = Object3D.mergeObjects(mesh, createBox(block));
        }
        return mesh;
    }
    
    protected void buildWall(SimpleVector start, SimpleVector end,
            int windowGroups, float uvStart, boolean blankCorners) {
        float height = Math.abs(start.y - end.y);
        
        Object3D wall = new Object3D(4);
    }
    
    // HELPERS
    //------------------------------------------------------
    protected Object3D createBox(Block block) {
        System.out.println("X1: "+block.w());
        System.out.println("X2: "+block.e());
        System.out.println("Y1: "+block.s());
        System.out.println("Y2: "+block.n());
        System.out.println("Z1: 0");
        System.out.println("Z2: "+block.height());
        Vertex start = new Vertex(block.w(), block.s(), block.height());
        Vertex end = new Vertex(block.e(), block.n(), 0);
        return createBox(start, end);
    }
    /**
     * 
     * @param start
     * @param end
     * @return 
     */
    protected Object3D createBox(Vertex start, Vertex end) {
        SimpleVector s = start.toVector();
        SimpleVector e = end.toVector();
        return createBox(s, e);
    }
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
//    SimpleVector upperLeftFront=new SimpleVector(-1,-1,-1); // x, z, y
//    SimpleVector upperRightFront=new SimpleVector(1,-1,-1); // x, z, y
//    SimpleVector lowerLeftFront=new SimpleVector(-1,1,-1); // x, z, y
//    SimpleVector lowerRightFront=new SimpleVector(1,1,-1); // x, z, y
//    
//    SimpleVector upperLeftBack = new SimpleVector( -1, -1, 1); // x, z, y
//    SimpleVector upperRightBack = new SimpleVector(1, -1, 1); // x, z, y
//    SimpleVector lowerLeftBack = new SimpleVector( -1, 1, 1); // x, z, y
//    SimpleVector lowerRightBack = new SimpleVector(1, 1, 1); // x, z, y

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