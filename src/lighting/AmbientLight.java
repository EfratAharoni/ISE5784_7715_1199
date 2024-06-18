package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {

    /** Black background */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * constructor calculate the intensity (Double3 object) use super constructor
     *
     * @param iA intensity
     * @param kA attenuation factor
     */
    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA));
    }

    /**
     * constructor calculate the intensity (java.double object) use super
     *
     * @param iA intensity
     * @param kA attenuation factor
     */
    public AmbientLight(Color iA, double kA) {
        super(iA.scale(kA));
    }
}

