package geometries;

import primitives.Point;
import primitives.Vector;

public class RadialGeometry implements Geometry {
    final double radius;

    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
