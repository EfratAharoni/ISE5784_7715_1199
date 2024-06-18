package scene;

import java.util.LinkedList;
import java.util.List;
import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;


public class Scene {
    public final String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();
    public List<LightSource> lights = new LinkedList<>();


    /**
     * Creates and names a scene
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the scene's background
     *
     * @param background the background to set
     * @return the scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the scene's ambient light
     *
     * @param ambientLight the ambient light to set
     * @return the scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the scene's geometries
     *
     * @param geometries the geometries to set
     * @return the scene
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * setter for light source return this for builder
     *
     * @param lights list of source lights
     * @return this for builder
     */
    public Scene setlights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    @Override
    public String toString() {
        return "Scene [name=" + name + ", background=" + background + ", ambientLight=" + ambientLight + ", geometries="
                + geometries + ", lightSources=" + lights + "]";
    }
}
