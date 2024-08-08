package lighting;

import primitives.*;
import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;
import static primitives.Util.alignZero;

/**
 * The SpotLight class represents a spotlight in a scene. It is a type of point
 * light with an additional direction vector.
 */
public class SpotLight extends PointLight {

    private Vector direction;
    private int narrowness = 1;

    /**
     * Constructs a SpotLight object with the specified intensity, position, and
     * direction.
     *
     * @param intensity The intensity of the spotlight.
     * @param position  The position of the spotlight.
     * @param direction The direction vector of the spotlight.
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }


    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * narrows the beam of the ray
     *
     * @param narrowness the narrowness of the beam
     * @return this object
     */
    public SpotLight setNarrowBeam(int narrowness) {
        this.narrowness = narrowness;
        return this;
    }


    @Override
    public Color getIntensity(Point p) {
        Vector l = super.getL(p);
        if (l == null)
            return super.getIntensity();
        double directionDotL = Util.alignZero(direction.dotProduct(l));
        return super.getIntensity(p).scale(directionDotL > 0 ? Math.pow(directionDotL, narrowness) : 0); // the denominator from the super!!
    }
}
//    @Override
//    public Color getIntensity(Point p)
//    {
//        double projection = alignZero(direction.dotProduct(getL(p)));
//        double factor = Math.max(0, projection);
//        Color pointlightIntensity = super.getIntensity(p);
//        return pointlightIntensity.scale(factor);
//    }