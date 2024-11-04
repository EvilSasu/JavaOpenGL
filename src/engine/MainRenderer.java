package engine;

import Obiekty.Kamera;
import Obiekty.Light;
import Obiekty.Obiekt;
import Obiekty.Terrain;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import shader.TerrainShader;

import java.util.*;

public class MainRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    private Matrix4f projectionMatrix;

    private StaticShader staticShader = new StaticShader();
    private ObiektRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Obiekt>> obiekty = new HashMap<TexturedModel, List<Obiekt>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MainRenderer(){
        enableCulling();
        createProjectionMatrix();
        renderer = new ObiektRenderer(staticShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(Light light, Kamera kamera){
        prepareToRender();
        staticShader.startProgram();
        staticShader.loadLight(light);
        staticShader.loadViewMatrix(kamera);
        renderer.render(obiekty);
        staticShader.stopProgram();
        terrainShader.startProgram();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(kamera);
        terrainRenderer.render(terrains);
        terrainShader.stopProgram();
        obiekty.clear();
        terrains.clear();
    }

    public void przetworzTeren(Terrain terrain){
        terrains.add(terrain);
    }

    public void przetworzObiekt(Obiekt obiekt){
        TexturedModel modelObiektu = obiekt.getModel();
        List<Obiekt> batch = obiekty.get(modelObiektu);
        if(batch != null){
            batch.add(obiekt);
        }else{
            List<Obiekt> newBatch = new ArrayList<Obiekt>();
            newBatch.add(obiekt);
            obiekty.put(modelObiektu, newBatch);
        }
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE); // brak renderowanaia wewnatrz obiketu
        GL11.glCullFace(GL11.GL_BACK); // tych wewnatrz
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE); // brak renderowanaia wewnatrz obiketu
    }

    public void prepareToRender(){
        GL11.glEnable(GL11.GL_DEPTH_TEST); // decyduje ktore trojkaty maja sie rederowac
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0,1,1,1);
    }

    public void cleanUp(){
        staticShader.deleteShadersAndProgram();
        terrainShader.deleteShadersAndProgram();
    }

    // wzor na projectionMatrix oficjalny z neta
    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
