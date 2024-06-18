package renderer;

import primitives.*;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {

    public SimpleRayTracer(Scene scene) {
        super(scene);

    }

    @Override
    public Color traceRay(Ray ray) {

        List<Point> intersections=scene.geometries.findIntersections(ray);
        if(intersections==null)
            return scene.background;
        return calcColor(ray.findClosestPoint(intersections)) ;
    }

    /**
     * Calculates the color at a given point.
     * In this simple implementation, it returns the ambient light intensity of the scene.
     *
     * @param p the point at which to calculate the color.
     * @return the color at the given point.
     */
    private Color calcColor(Point p)
    {
        return scene.ambientLight.getIntensity();
    }
}
