package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {

    /** none ambient light */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    // ******************* constructors *****************
    /**
     * constructor calculate the intensity (Double3 object) use super constructor
     *
     * @param light light intensity according to the RGB component
     * @param ka    attenuation coefficient of the light on the surface
     */
    public AmbientLight(Color light, Double3 ka) {
        super(light.scale(ka));
    }

    /**
     * constructor calculate the intensity (java.double object) use super
     * constructor
     *
     * @param light light intensity according to the RGB component
     * @param ka    attenuation coefficient of the light on the surface
     */
    public AmbientLight(Color light, double ka) {
        super(light.scale(ka));
    }
}

