package engine;

import Obiekty.Obiekt;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import Obiekty.TextureInfo;
import toolBox.Maths;

import java.util.List;
import java.util.Map;

public class ObiektRenderer {
    // klasa renderujaca obiekty

    private StaticShader staticShader;

    public ObiektRenderer(StaticShader staticShader, Matrix4f projectionMatrix){
        this.staticShader = staticShader;
        staticShader.startProgram();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stopProgram();
    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID()); // bindowanie modelu
        enableVertex();
        TextureInfo textureInfo = texturedModel.getTextureInfo();
        if(textureInfo.isTransparency()){
            MainRenderer.disableCulling();
        }
        staticShader.LoadFakeLightningVariable(textureInfo.isUseFakeLightning());
        staticShader.loadShineVariable(textureInfo.getShineDamper(),textureInfo.getReflectivity()); // ?ladowanie odbicia swiatla z klasy tekstury
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // bindowanie tekstury
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTextureInfo().getTextureID());
    }

    private void unbindTexturedModel(){
        disableVertex();
    }

    private void prepareInstance(Obiekt obiekt){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(obiekt.getPosition(),
                obiekt.getRotX(),obiekt.getRotY(),
                obiekt.getRotZ(),obiekt.getScale());
        staticShader.loadTransformationMatrix(transformationMatrix);
    }

    public void render(Map<TexturedModel, List<Obiekt>> obiekty){
        for(TexturedModel texturedModel:obiekty.keySet()){
            prepareTexturedModel(texturedModel);
            List<Obiekt> batch = obiekty.get(texturedModel);
            for(Obiekt obiekt:batch){
                prepareInstance(obiekt);
                GL11.glDrawElements(GL11.GL_TRIANGLES, obiekt.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    /*public void render(Obiekt obiekt, StaticShader shader){
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
    }*/

    private void enableVertex(){
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private void disableVertex(){
        MainRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0); // unbindowanie poprzednihc rzeczy (model, tekstura...)
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }


}
