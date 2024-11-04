package engine;

import Obiekty.Obiekt;
import Obiekty.Terrain;
import Obiekty.TextureInfo;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shader.TerrainShader;
import toolBox.Maths;

import java.util.List;

public class TerrainRenderer {

    private TerrainShader terrainShader;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix){
        this.terrainShader = terrainShader;
        terrainShader.startProgram();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.stopProgram();
    }

    private void prepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID()); // bindowanie modelu
        enableVertex();
        TextureInfo textureInfo = terrain.getTextureInfo();
        terrainShader.loadShineVariable(textureInfo.getShineDamper(),textureInfo.getReflectivity()); // ladowanie odbicia swiatla z klasy tekstury
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // bindowanie tekstury
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureInfo.getTextureID());
    }

    private void unbindTexturedModel(){
        disableVertex();
    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(),0,terrain.getZ()),
                0,0, 0,1);
        terrainShader.loadTransformationMatrix(transformationMatrix);
    }

    public void render(List<Terrain> terrains){
        for(Terrain terrain:terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
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

}
