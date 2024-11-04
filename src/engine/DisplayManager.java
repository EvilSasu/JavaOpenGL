package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {
    // klasa wyswietlajaca obraz

    private static final int HIGHT = 720;
    private static final int WIDTH = 1280;
    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(){
        ContextAttribs contextAttribs = new ContextAttribs();
        contextAttribs.withForwardCompatible(true);
       // contextAttribs.withProfileCompatibility(true);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH,HIGHT));
            Display.create(new PixelFormat(), contextAttribs);
            Display.setTitle("OpenGL Project");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HIGHT);
        lastFrameTime = getCurrentTime();
    }
    public static void updateDisplay(){
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }

    public static void destroyDisplay(){
        Display.destroy();
    }

    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
