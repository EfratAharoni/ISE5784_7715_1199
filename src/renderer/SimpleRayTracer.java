package renderer;

import geometries.Geometry;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import java.util.List;
import geometries.Intersectable.GeoPoint;

/**
 * The SimpleRayTracer class extends RayTracerBase and provides a basic implementation
 * for tracing rays in a scene. It calculates the color of the closest intersection point
 * or returns the background color if no intersections are found.
 */
public class SimpleRayTracer extends  RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final int NUM_SAMPLES = 3;


    @Override
    public Color traceRay(Ray ray) {
        // return traceRay(ray, NUM_SAMPLES);
        GeoPoint intersectionPoint = scene.geometries.findClosestIntersection(ray);
        return intersectionPoint == null
                ? scene.background
                : calcColor(intersectionPoint, ray);
        //findGeoIntersections
    }

//    public Color traceRay(Ray ray, int numSamples) {
//        Color color = Color.BLACK;
//
//        TargetArea targetArea=new TargetArea(ray,numSamples);
//        // Generate multiple rays for super sampling
//        List<Ray> rays = targetArea.constructRayBeamGrid();
//
//        for (Ray sampleRay : rays) {
//            GeoPoint intersectionPoint = scene.geometries.findClosestIntersection(sampleRay);
//            if (intersectionPoint != null) {
//                color = color.add(calcColor(intersectionPoint, sampleRay));
//            }
//            else {
//                color = color.add(scene.background);
//            }
//
//        }
//
//        return color.reduce(rays.size());
//    }


    /**
     * Calculates the color at a given point.
     * In this simple implementation, it returns the ambient light intensity of the scene.
     *
     * @param intersection the point at which to calculate the color.
     * @return the color at the given point.
     */
    private Color calcColor(GeoPoint intersection,Ray ray)
    {
        return scene.ambientLight.getIntensity()
                .add(calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, Double3.ONE));
    }


    private Color calcColor(GeoPoint gp, Ray ray,int level,Double3 k){
        Color color=calcLocalEffects(gp, ray,k);

        return 1==level?color:color.add(calcGlobalEffects(gp,ray,level,k));

    }

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

    private Color calcGlobalEffect( Ray ray,Double3 kX, int level, Double3 k) {
        Double3 kKx=kX.product(k);
//        double nv= Util.alignZero(n.dotProduct(v));
//        if (nv==0)
//            return color;
        if(kKx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp=scene.geometries.findClosestIntersection(ray);
//        if(gp==null)
//            return Color.BLACK;//scene.background.scale(kx)

        return (gp==null?Color.BLACK:calcColor(gp,ray,level-1,kKx)).scale(kX);
    }


    private Color calcLocalEffects(GeoPoint gp, Ray ray,Double3 k){
        Color color = gp.geometry.getEmission();
        Vector n=gp.geometry.getNormal(gp.point);
        Vector v=ray.getDirection();
        double nv= Util.alignZero(n.dotProduct(v));
        if (nv==0)
            return color;
        Material material=gp.geometry.getMaterial();
        for(LightSource lightSource:scene.lights){

            Vector l=lightSource.getL(gp.point);
            double nl=Util.alignZero(n.dotProduct(l));
            if(Util.alignZero(nl*nv)>0){
                Double3 ktr = transperency(gp, lightSource, l, n,nl);
                if (!(ktr.product(k).lowerThan(MIN_CALC_COLOR_K))) {
                    Color il = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(il.scale(calcDiffusive(material, nl)), il.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }
        return color;
    }
    private Double3 calcDiffusive(Material mat, double nl) {
        return mat.kD.scale(nl>0?nl:-nl);
    }

    private Double3 calcSpecular(Material material, Vector normal, Vector lightDir, double cosAngle, Vector rayDir) {
        Vector r = lightDir.subtract(normal.scale(2 * cosAngle));
        double coefficient = -rayDir.dotProduct(r);
        coefficient = Util.alignZero(coefficient) > 0 ? coefficient : 0;
        return material.kS.scale(Math.pow(coefficient, material.nShininess));

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


//    private double powr(double b, int e) {
//        double res = 1;
//        for (int i = 0; i < e; ++i)
//            res *= b;
//        for (int i = 0; i > e; --i)
//            res /= b;
//        return res;
//    }
}
