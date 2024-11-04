package particles;

import Obiekty.Kamera;
import engine.Loader;
import org.lwjgl.util.vector.Matrix4f;

import java.util.*;

public class ParticleManager {

    private  static Map<ParticleTextureInfo, List<Particle>> particles = new HashMap<ParticleTextureInfo, List<Particle>>();
    private static ParticleRenderer renderer;

    public static  void init(Loader loader, Matrix4f projectionMatrix){
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    public static void update(){
        Iterator<Map.Entry<ParticleTextureInfo, List<Particle>>> mapIterator = particles.entrySet().iterator();
        while(mapIterator.hasNext()){
            List<Particle> list = mapIterator.next().getValue();
            Iterator<Particle> iterator = list.iterator();
            while(iterator.hasNext()){
                Particle p = iterator.next();
                boolean stillAlive = p.update();
                if(!stillAlive){
                    iterator.remove();
                    if(list.isEmpty()){
                        mapIterator.remove();
                    }
                }
            }
        }
    }

    public static void renderParticles(Kamera kamera){
        renderer.render(particles, kamera);
    }

    public static void cleanUp(){
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle){
        List<Particle> list = particles.get(particle.getTexture());
        if(list == null){
            list = new ArrayList<Particle>();
            particles.put(particle.getTexture(), list);
        }
        list.add(particle);
    }

}
