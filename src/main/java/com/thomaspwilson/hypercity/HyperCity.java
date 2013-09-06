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
        Config.glAvoidTextureCopies = true;
        Config.maxPolysVisible = 100000;
        Config.glColorDepth = 24;
        Config.glFullscreen = false;
        Config.farPlane = 40000;
        Config.glShadowZBias = 0.8f;
        Config.lightMul = 1;
        Config.collideOffset = 500;
        Config.glTrilinear = true;
        
        World world = new World();
        world.setAmbientLight(150, 150, 150);
        
        TextureManager.getInstance().addTexture("steel", new Texture(HyperCity.class.getClassLoader().getResourceAsStream("textures/steel.jpg"),false));
        Object3D building = new Building().generate();
        building.setTexture("steel");
        building.calcTextureWrap();
        building.setEnvmapped(Object3D.ENVMAP_ENABLED);
        building.setCulling(false);
        building.setSpecularLighting(true);
        world.addObject(building);
        world.buildAllObjects();
        building.compileAndStrip();
        
        // Set up the camera
        Camera cam = world.getCamera();
        cam.setPosition(-100, 0, 0);
        cam.lookAt(building.getTransformedCenter());
        cam.rotateCameraAxis(cam.getXAxis(), (float)Math.PI / 2f);
        
        FrameBuffer buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_GL_AA_4X);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
//        float amount = 0f;
        while(!Display.isCloseRequested()) {
//            amount+=0.01f;
//            System.out.println(amount);
//            cam.rotateCameraAxis(cam.getXAxis(), 0.01f);
            building.rotateZ(0.01f);
            buffer.clear();
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