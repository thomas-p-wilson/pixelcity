package com.thomaspwilson.hypercity;

import com.thomaspwilson.hypercity.components.Building;
import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import java.awt.Color;
import org.lwjgl.opengl.Display;

/**
 *
 * @author twilson
 */
public class HyperCity {
    public static void main(String[] args) throws InterruptedException {
//        Config.glAvoidTextureCopies = true;
        Config.maxPolysVisible = 100000;
        Config.glColorDepth = 24;
        Config.glFullscreen = false;
        Config.farPlane = 40000;
        Config.glShadowZBias = 0.8f;
        Config.lightMul = 1;
//        Config.collideOffset = 500;
        Config.glTrilinear = true;
        
        World world = new World();
        world.setAmbientLight(0, 250, 0);
        
        // Build a plane
        Object3D plane = Primitives.getPlane(100, 1);
        plane.setTexture("box");
        plane.setEnvmapped(true);
        plane.rotateX((float)Math.PI / 2f);
        plane.translate(0, 100, 0);
        world.addObject(plane);
        
        
        TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));
        Object3D building = (Object3D)new Building().generate();
        building.setTexture("box");
        building.setEnvmapped(Object3D.ENVMAP_ENABLED);
        world.addObject(building);
        world.buildAllObjects();
        
        world.getCamera().setPosition(0, -50, 50);
        world.getCamera().lookAt(building.getTransformedCenter());
        
        FrameBuffer buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_GL_AA_2X);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        while(!Display.isCloseRequested()) {
            building.rotateY(0.01f);
            buffer.clear(Color.BLUE);
            world.renderScene(buffer);
            world.draw(buffer);
            buffer.update();
            buffer.displayGLOnly();
            Thread.sleep(10);
        }
        buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.dispose();
    }
}