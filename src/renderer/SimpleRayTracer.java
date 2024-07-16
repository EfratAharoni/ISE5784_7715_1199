package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import java.util.List;

/**
 * The SimpleRayTracer class is a simple implementation of the RayTracerBase class.
 */
public class SimpleRayTracer extends RayTracerBase
{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final int NUM_SAMPLES = 3;

    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be traced.
     */
    public SimpleRayTracer(Scene scene)
    {
        super(scene);
    }

    /**
     * Traces a ray through the scene and calculates the color at the point where the ray intersects with an object.
     * @param ray the ray to trace through the scene
     * @return the color at the point where the ray intersects with an object, or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray) {
        // return traceRay(ray, NUM_SAMPLES);
        GeoPoint intersectionPoint = scene.geometries.findClosestIntersection(ray);
        return intersectionPoint == null
                ? scene.background
                : calcColor(intersectionPoint, ray);
        //findGeoIntersections
    }


    /**
     * Calculates the color of a point in the scene.
     *
     * @param intersection The point on the geometry in the scene.
     * @param ray      The ray from the camera to the intersection.
     * @return The color of the point.
     * */
    private Color calcColor(GeoPoint intersection, Ray ray)
    {
        return scene.ambientLight.getIntensity()
                .add(calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, Double3.ONE));
    }
    //הפונקציה traceRay מאתרת את נקודות החיתוך של הקרן עם הגאומטריות בסצנה.
    // אם לא נמצאו נקודות חיתוך, היא מחזירה את צבע הרקע של הסצנה.
    // אחרת, היא מוצאת את הנקודה הקרובה ביותר לראש הקרן ומחשבת את הצבע של הנקודה הזו.
    //הפונקציה calcColor מחשבת את הצבע של נקודה בסצנה בהתבסס על תאורת הסביבה הקיימת ומחזירה את הצבע הזה.

    /**
     * Calculates the color at a given intersection point, considering local and global effects.
     *
     * @param gp The intersection point on a geometry.
     * @param ray The ray intersecting the geometry.
     * @param level The recursion level for global effects.
     * @param k Coefficient vector for global effects.
     * @return The computed color at the intersection point.
     */
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        // Calculate ambient and local effects
        Color color=calcLocalEffects(gp, ray);
        // If recursion level is 1, return the color with only local effects
        if (level == 1) {
            return color;
        }
        // Add global effects to the color
        return color.add(calcGlobalEffects(gp, ray, level, k));
    }

    /**
     * Calculates the global effects (refraction and reflection) at a given intersection point.
     *
     * @param gp The intersection point on a geometry.
     * @param ray The ray intersecting the geometry.
     * @param level The recursion level for global effects.
     * @param k Coefficient vector for global effects.
     * @return The computed color contribution from global effects.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material=gp.geometry.getMaterial();
        return calcGlobalEffect(constructRefractedRay(gp,ray),material.kT,level,k)
                .add(calcGlobalEffect(constructReflectedRay(gp,ray),material.kR,level,k));
    }

    private Ray constructReflectedRay(GeoPoint gp, Ray ray) {
        Vector normal=gp.geometry.getNormal(gp.point);
        return new Ray( gp.point, ray.getDirection().subtract(normal.scale(2*ray.getDirection().dotProduct(normal))), normal);
    }

    private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
        Vector normal=gp.geometry.getNormal(gp.point);
        return new Ray( gp.point, ray.getDirection(), normal);
    }

    /**
     * Calculates the contribution of a specific global effect (reflection or refraction).
     *
     * @param ray The ray for the global effect (reflection or refraction).
     * @param kX Coefficient vector for the specific effect (reflection or refraction).
     * @param level The recursion level for global effects.
     * @param k Coefficient vector for global effects.
     * @return The computed color contribution from the specific global effect.
     */
    private Color calcGlobalEffect(Ray ray, Double3 kX, int level, Double3 k) {
        // Calculate combined coefficient vector
        Double3 kKx = kX.product(k);

        // If combined coefficient is below threshold, return black (no contribution)
        if (kKx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        // Find closest intersection point
        GeoPoint gp = scene.geometries.findClosestIntersection(ray);

        // Calculate color contribution considering recursion
        return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kKx)).scale(kX);
    }

    /**
     * Calculates the effect of different light sources on a point in the scene
     * according to the Phong model.
     *
     * @param intersection The point on the geometry in the scene.
     * @param ray          The ray from the camera to the intersection.
     * @return The color of the point affected by local light sources.
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray)
    {
        Vector v = ray.getDirection();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = Util.alignZero(n.dotProduct(v));
        if (nv == 0) //כאשר המקור אור מתחת לגוף
            return Color.BLACK;

        int nShininess = intersection.geometry.getMaterial().nShininess;

        Double3 kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;

        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights)
        {
            Vector l = lightSource.getL(intersection.point);
            double nl = Util.alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sign(nv) (בודק אם הם שווי סימן שזה אומר שהתאורה והמצלמה באותו כיוון
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffuse(kd, nl, lightIntensity),
                        calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse component of light reflection.
     *
     * @param kd             The diffuse reflection coefficient.
     * @param nl             The dot product between the normal vector and the light
     *                       vector.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the diffuse reflection.
     */
    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity)
    {
        return lightIntensity.scale(kd.scale(Math.abs(nl)));
    }

    /**
     * Calculates the specular component of light reflection.
     *
     * @param ks             The specular reflection coefficient.
     * @param l              The light vector.
     * @param n              The normal vector.
     * @param nl             The dot product between the normal vector and the light
     *                       vector.
     * @param v              The view vector.
     * @param nShininess     The shininess coefficient.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color lightIntensity)
    {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -Util.alignZero(r.dotProduct(v));
        if (minusVR <= 0) {
            return new primitives.Color(Color.BLACK.getColor()); // View from direction opposite to r vector
        }
        return lightIntensity.scale(ks.scale(Math.pow(minusVR, nShininess)));
    }

    private Double3 transperency(GeoPoint gp,LightSource light, Vector l, Vector n,double nl){
        Ray lightRay = new Ray( gp.point, l.scale(-1),n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        //  GeoPoint intersectionPoint=scene.geometries.findClosestIntersection(lightRay);

        Double3 ktr = Double3.ONE;
        if (intersections == null)
            return ktr;
        for (GeoPoint intersection : intersections)
            if(intersection.point.distance(gp.point)<light.getDistance(gp.point))
                ktr = ktr.product(intersection.geometry.getMaterial().kT);
        return ktr;
    }

    private boolean unshaded(GeoPoint gp,LightSource light, Vector l, Vector n,double nl){
        Vector lightDirection=l.scale(-1);
        Vector deltaVector=n.scale(Util.alignZero(nl)<0?DELTA:-DELTA);
        Point point=gp.point.add(deltaVector);
        Ray lightRay=new Ray(point,lightDirection);
        GeoPoint intersectionPoint=scene.geometries.findClosestIntersection(lightRay);
        if(intersectionPoint==null)
            return true;
        if(intersectionPoint.point.distance(point)<light.getDistance(point))
            return false;
        return true;
    }
}



