package Obiekty;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Kamera {

    private float pitch; // rotacja x,y,z
    private float yaw; // pozycja jak bardzo na lewo lub prawo jest
    private float roll; // pochylenie kamery
    private Vector3f position = new Vector3f(0,1,0);

    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -= 0.08f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z += 0.08f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -= 0.08f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x += 0.08f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_X)){
            position.y -= 0.08f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
            position.y += 0.08f;
        }
    }


    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public Vector3f getPosition() {
        return position;
    }

}
