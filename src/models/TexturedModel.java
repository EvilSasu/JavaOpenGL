package models;

import Obiekty.TextureInfo;

public class TexturedModel {
    // klasa, ktora tworzy model z tekstura
    private RawModel rawModel;
    private TextureInfo textureInfo;

    public TexturedModel(RawModel rawModel, TextureInfo textureInfo){
        this.rawModel = rawModel;
        this.textureInfo = textureInfo;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureInfo getTextureInfo() {
        return textureInfo;
    }

}
