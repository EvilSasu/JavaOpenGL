package MainLoop;

import Obiekty.*;
import engine.*;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import particles.Particle;
import particles.ParticleManager;
import particles.ParticleTextureInfo;
import particles.ParticlesSystem;
import shader.StaticShader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainLoop {

    // klasa z glowna petla
    public static void main(String[] args){
        DisplayManager.createDisplay();
        List<Obiekt> trees = new ArrayList<>();
        List<Obiekt> rocks = new ArrayList<>();
        List<Obiekt> krzaki = new ArrayList<>();
        List<ParticlesSystem> chmury = new ArrayList<>();
        MainRenderer renderer = new MainRenderer();
        Random rand = new Random();
        Loader loader = new Loader();
        ParticleManager.init(loader, renderer.getProjectionMatrix());
        boolean wlChmury = false;
        boolean wlMgla = false;

        RawModel pien = OBJLoader.loadOBJModel("Tree/koraDrzewa", loader);
        RawModel korona = OBJLoader.loadOBJModel("Tree/korona", loader);
        RawModel kamien = OBJLoader.loadOBJModel("Rock/Rock_1", loader);
        RawModel krzakModel = OBJLoader.loadOBJModel("fern", loader);
        TextureInfo texture = new TextureInfo(loader.loadTexture("Tree/l4"));
        TextureInfo texture2 = new TextureInfo(loader.loadTexture("Tree/liscie2"));
        TextureInfo textureOfKamien = new TextureInfo(loader.loadTexture("Rock/rocktex2"));
        TextureInfo textureOfLiscie3 = new TextureInfo(loader.loadTexture("Tree/liscie3"));
        TextureInfo textureOfKrzak = new TextureInfo(loader.loadTexture("fern"));
        textureOfKrzak.setTransparency(true);
        textureOfKrzak.setUseFakeLightning(true);
        textureOfKamien.setShineDamper(10);
        textureOfKamien.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(pien, texture);
        TexturedModel texturedModel2 = new TexturedModel(korona, texture2);
        TexturedModel texturedModel3 = new TexturedModel(kamien, textureOfKamien);
        TexturedModel texturedModel4 = new TexturedModel(korona, textureOfLiscie3);
        TexturedModel texturedModelOfKrzak = new TexturedModel(krzakModel, textureOfKrzak);
        Kamera kamera = new Kamera();
        Light light = new Light(new Vector3f(0,0,-1000f),new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-0.5f,0, loader, texture2);
        Terrain terrain2 = new Terrain(-0.5f,-1, loader, texture2);

        Vector3f[] chumraPos = new Vector3f[50];

        // drzewa
        for(int i=0;i<1000;i++){
            float x = rand.nextFloat() * 400 - rand.nextFloat() * 200;
            float z = rand.nextFloat() * (-600) + rand.nextFloat() * 500;
            float rotY = (float)Math.random() * 360;
            float skala = rand.nextFloat() * 10;
            int jakiKolorKorony = rand.nextInt(2);
            trees.add(new Obiekt(texturedModel,new Vector3f(x,0,z),0,rotY,0,skala));
            if(jakiKolorKorony == 0)
                trees.add(new Obiekt(texturedModel2,new Vector3f(x,0,z),0,rotY,0,skala));
            if(jakiKolorKorony == 1)
                trees.add(new Obiekt(texturedModel4,new Vector3f(x,0,z),0,rotY,0,skala));
        }
        // krzaki
        for(int i=0;i<1000;i++){
            float x = rand.nextFloat() * 400 - rand.nextFloat() * 200;
            float z = rand.nextFloat() * (-600) + rand.nextFloat() * 500;
            float rotY = (float)Math.random() * 360;
            float skala = rand.nextFloat();
            krzaki.add(new Obiekt(texturedModelOfKrzak,new Vector3f(x,0,z),0,rotY,0,skala));
        }

        // kamienie
        for(int i=0;i<300;i++){
            float x = rand.nextFloat() * 400 - rand.nextFloat() * 200;
            float z = rand.nextFloat() * (-600) + rand.nextFloat() * 500;
            float rotY = (float)Math.random() * 360;
            float skala = rand.nextFloat();
            rocks.add(new Obiekt(texturedModel3,new Vector3f(x,0,z),rotY,rotY,0,skala));
        }

        ParticleTextureInfo particleTextureInfo = new ParticleTextureInfo(loader.loadTexture("ParticleCloudWhite_40"), 1);
        ParticlesSystem mgla = new ParticlesSystem(particleTextureInfo,100, 10.5f, 0.01f, 20, 40);

        // chmury
        for(int i=0;i<50;i++){
            float x = rand.nextFloat() * 400 - rand.nextFloat() * 200;
            float z = rand.nextFloat() * (-600) + rand.nextFloat() * 500;
            chumraPos[i] = new Vector3f(x,100,z);
            chmury.add(new ParticlesSystem(particleTextureInfo, 5, 1f, 0.001f, 3, 35));
        }

        while(!Display.isCloseRequested()){
            kamera.move();
            ParticleManager.update();
            renderer.przetworzTeren(terrain);
            renderer.przetworzTeren(terrain2);
            int i = 0;
            if(wlMgla == true)
                mgla.generateParticles(new Vector3f(-5,25,-100));
            if(wlChmury == true){
                for(ParticlesSystem chmurka : chmury){
                    chumraPos[i] = new Vector3f(chumraPos[i].x + 0.10f, chumraPos[i].y, chumraPos[i].z);
                    chmurka.generateParticles(new Vector3f(chumraPos[i].x, chumraPos[i].y, chumraPos[i].z));
                    if(chumraPos[i].x > 500){
                        float x = - rand.nextFloat() * 400;
                        float z = rand.nextFloat() * (-600) + rand.nextFloat() * 500;
                        chumraPos[i] = new Vector3f(x,100,z);
                    }
                    i++;
                    System.out.println(chumraPos[0].x);
                }
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_N)){
                wlChmury = false;
                wlMgla = false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_C))
                wlChmury = true;
            if(Keyboard.isKeyDown(Keyboard.KEY_F))
                wlMgla = true;

            for(Obiekt tree:trees) {
                renderer.przetworzObiekt(tree);
            };
            for(Obiekt krzakk:krzaki) {
                renderer.przetworzObiekt(krzakk);
            };
            for(Obiekt kamyk:rocks) {
                renderer.przetworzObiekt(kamyk);
            };
            renderer.render(light, kamera);
            ParticleManager.renderParticles(kamera);
            DisplayManager.updateDisplay();
        }
        ParticleManager.cleanUp();
        renderer.cleanUp();
        loader.clean();
        DisplayManager.destroyDisplay();
    }

}
