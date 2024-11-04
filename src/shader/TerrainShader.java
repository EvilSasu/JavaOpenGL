package shader;

import Obiekty.Kamera;
import Obiekty.Light;
import org.lwjgl.util.vector.Matrix4f;
import toolBox.Maths;

public class TerrainShader extends ShaderProgram{

    // klasa z danymi o shaderach

    private static final String VERTEX_SHADER_FILE = "src/shader/terrainVertexShader.txt";
    private static final String FRAGMENT_SHADER_FILE = "src/shader/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;

    public TerrainShader(){
        super(VERTEX_SHADER_FILE,FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    // ladowanie macierzy do shadera
    // zmiany w swiecie
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }
    // widok z kamry
    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadShineVariable(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }
    // pozycja kamery w worldspace
    public void loadViewMatrix(Kamera kamera){
        Matrix4f viewMatrix = Maths.createViewMatrix(kamera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
