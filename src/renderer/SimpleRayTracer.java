package renderer;

import geometries.Geometry;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import java.util.List;
import geometries.Intersectable.GeoPoint;

public class SimpleRayTracer extends RayTracerBase {

    public SimpleRayTracer(Scene scene) {
        super(scene);

    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null
                ? scene.background
                : calcColor(ray.findClosestGeoPoint(intersections));
    }

    /**
     * Calculates the color at a given point.
     * In this simple implementation, it returns the ambient light intensity of the scene.
     *
     * @param gp the point at which to calculate the color.
     * @return the color at the given point.
     */
    private Color calcColor(GeoPoint gp)
    {
        return scene.ambientLight.getIntensity()
                .add(gp.geometry.getEmission());
    }
}
