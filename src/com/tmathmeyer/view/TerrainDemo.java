package com.tmathmeyer.view;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import com.tmathmeyer.worldgen.Gen;
import com.tmathmeyer.worldgen.MapData;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;





public class TerrainDemo {

    private static final String WINDOW_TITLE = "Terrain!";
    private static final int[] WINDOW_DIMENSIONS = {1200, 650};
    private static final float ASPECT_RATIO = (float) WINDOW_DIMENSIONS[0] / (float) WINDOW_DIMENSIONS[1];
    private static final EulerCamera camera = new EulerCamera.Builder().setPosition(-5.4f, 19.2f, 33.2f).setRotation(30, 61, 0).setAspectRatio(ASPECT_RATIO).setFieldOfView(60).build();
    private static int shaderProgram;
    private static int lookupTexture;
    private static int heightmapDisplayList;
    private static float[][] data;
    private static boolean flatten = false;

    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        if (flatten) {
            glScalef(1, 0, 1);
        }
        glCallList(heightmapDisplayList);
    }

    private static void input() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                    flatten = !flatten;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_L) {
                    glUseProgram(0);
                    glDeleteProgram(shaderProgram);
                    glBindTexture(GL_TEXTURE_2D, 0);
                    glDeleteTextures(lookupTexture);
                    setUpShaders();
                    setUpHeightmap();
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                    int polygonMode = glGetInteger(GL_POLYGON_MODE);
                    if (polygonMode == GL_LINE) {
                        glPolygonMode(GL_FRONT, GL_FILL);
                    } else if (polygonMode == GL_FILL) {
                        glPolygonMode(GL_FRONT, GL_POINT);
                    } else if (polygonMode == GL_POINT) {
                        glPolygonMode(GL_FRONT, GL_LINE);
                    }
                }
            }
        }
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
        if (Mouse.isGrabbed()) {
            camera.processMouse(1, 80, -80);
        }
        camera.processKeyboard(16, 3);
    }

    private static void setUpHeightmap() {
        try {
        	int size = 1025;
        	MapData md = Gen.ds(size);
        	md.unitize(255);
        	
            data = md.map;
            Color colour;
            
            FileInputStream heightmapLookupInputStream = new FileInputStream("heightmap_lookup.png");
            // Create a class that will give us information about the image file (width and height) and give us the
            // texture data in an OpenGL-friendly manner
            PNGDecoder decoder = new PNGDecoder(heightmapLookupInputStream);
            // Create a ByteBuffer in which to store the contents of the texture. Its size is the width multiplied by
            // the height and 4, which stands for the amount of bytes a float is in Java.
            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            // 'Decode' the texture and store its data in the buffer we just created
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            // Make the contents of the ByteBuffer readable to OpenGL (and unreadable to us)
            buffer.flip();
            // Close the input stream for the heightmap 'lookup texture'
            heightmapLookupInputStream.close();
            // Generate a texture handle for the 'lookup texture'
            lookupTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, lookupTexture);
            // Hand the texture data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Use the GL_NEAREST texture filter so that the sampled texel (texture pixel) is not smoothed out. Usually
        // using GL_NEAREST will make the textured shape appear pixelated, but in this case using the alternative,
        // GL_LINEAR, will make the sharp transitions between height-colours ugly.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Generate a display list handle for the display list that will store the heightmap vertex data
        heightmapDisplayList = glGenLists(1);
        // TODO: Add alternative VBO rendering for pseudo-compatibility with version 3 and higher.
        glNewList(heightmapDisplayList, GL_COMPILE);
        // Scale back the display list so that its proportions are acceptable.
        glScalef(0.2f, 0.06f, 0.2f);
        // Iterate over the 'strips' of heightmap data.
        for (int z = 0; z < data.length - 1; z++) {
            // Render a triangle strip for each 'strip'.
            glBegin(GL_TRIANGLE_STRIP);
            for (int x = 0; x < data[z].length; x++) {
                // Take a vertex from the current strip
                glVertex3f(x, data[z][x], z);
                // Take a vertex from the next strip
                glVertex3f(x, data[z + 1][x], z + 1);
            }
            glEnd();
        }
        glEndList();
    }

    private static void setUpShaders() {
        shaderProgram = ShaderLoader.loadShaderPair("landscape.vs", "landscape.fs");
        glUseProgram(shaderProgram);
        // The following call is redundant because the default value is already 0, but illustrates how you would use
        // multiple textures
        glUniform1i(glGetUniformLocation(shaderProgram, "lookup"), 0);
    }

    private static void cleanUp(boolean asCrash) {
        glUseProgram(0);
        glDeleteProgram(shaderProgram);
        glDeleteLists(heightmapDisplayList, 1);
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(lookupTexture);
        System.err.println(GLU.gluErrorString(glGetError()));
        Display.destroy();
        System.exit(asCrash ? 1 : 0);
    }

    private static void setUpMatrices() {
        camera.applyPerspectiveMatrix();
    }

    private static void setUpStates() {
        camera.applyOptimalStates();
        glPointSize(2);
        // Enable the sorting of shapes from far to near
        glEnable(GL_DEPTH_TEST);
        // Set the background to a blue sky colour
        glClearColor(0, 0.75f, 1, 1);
        // Remove the back (bottom) faces of shapes for performance
        glEnable(GL_CULL_FACE);
    }

    private static void update() {
        Display.update();
        Display.sync(60);
    }

    private static void enterGameLoop() {
        while (!Display.isCloseRequested()) {
            render();
            input();
            update();
        }
    }

    private static void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
            Display.setVSyncEnabled(true);
            Display.setTitle(WINDOW_TITLE);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            cleanUp(true);
        }
    }

    public static void main(String[] args) {
        setUpDisplay();
        setUpStates();
        setUpHeightmap();
        setUpShaders();
        setUpMatrices();
        enterGameLoop();
        cleanUp(false);
    }
}