package engine;

import Obiekty.Obiekt;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import Obiekty.TextureInfo;
import toolBox.Maths;

public class Renderer {
    // klasa renderujaca obiekty

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    private Matrix4f projectionMatrix;

    public Renderer(StaticShader staticShader){
        GL11.glEnable(GL11.GL_CULL_FACE); // brak renderowanaia wewnatrz obiketu
        GL11.glCullFace(GL11.GL_BACK); // tych wewnatrz
        createProjectionMatrix();
        staticShader.startProgram();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stopProgram();
    }

    public void prepareToRender(){
        GL11.glEnable(GL11.GL_DEPTH_TEST); // decyduje ktore trojkaty maja sie rederowac
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(1,1,1,1);
    }

    public void render(Obiekt obiekt, StaticShader shader){
        TexturedModel texturedModel = obiekt.getModel();
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID()); // bindowanie modelu
        enableVertex(); // enable atrybuty
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(obiekt.getPosition(),
                obiekt.getRotX(),obiekt.getRotY(),
                obiekt.getRotZ(),obiekt.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        TextureInfo textureInfo = texturedModel.getTextureInfo();
        shader.loadShineVariable(textureInfo.getShineDamper(),textureInfo.getReflectivity()); // ladowanie odbicia swiatla z klasy tekstury
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // bindowanie tekstury
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTextureInfo().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        disableVertex(); // disable atrybuty
    }

    private void enableVertex(){
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private void disableVertex(){
        GL20.glDisableVertexAttribArray(0); // unbindowanie poprzednihc rzeczy (model, tekstura...)
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
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

}
