package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import java.util.List;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
    private static final Double3 INITIAL_K = Double3.ONE;

    private boolean softShadows = false;
    private int gridResolution = 1;

    private int nXY = 22;
    private double distanceGrid = 1;


    private Double3 softShadows(GeoPoint geopoint, LightSource light, Vector l, Vector n) {
        Double3 ktr = Double3.ZERO;
        double radius = light.getRadius();
        int rays = gridResolution * gridResolution;
        Point position = light.getPosition();

        for (int i = 0; i < gridResolution; i++) {
            for (int j = 0; j < gridResolution; j++) {
                Point randomPoint = position.add(new Vector(
                        (i - gridResolution / 2.0) * radius / gridResolution,
                        (j - gridResolution / 2.0) * radius / gridResolution,
                        0
                ));
                Vector lightDirection = randomPoint.subtract(geopoint.point).normalize();
                Ray lightRay = new Ray(geopoint.point, lightDirection, n);
                double lightDistance = light.getDistance(geopoint.point);

                var intersections = this.scene.geometries.findGeoIntersections(lightRay);
                if (intersections == null) {
                    ktr = ktr.add(Double3.ONE);
                    continue;
                }
                Double3 ktrLocal = Double3.ONE;
                for (GeoPoint gp : intersections) {
                    if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                        ktrLocal = ktrLocal.product(gp.geometry.getMaterial().kT);
                        if (ktrLocal.lowerThan(MIN_CALC_COLOR_K)) {
                            ktrLocal = Double3.ZERO;
                            break;
                        }
                    }
                }
                ktr = ktr.add(ktrLocal);
            }
        }
        return ktr.scale(1.0 / rays);
    }

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
                ktr = softShadows ? softShadows(gp, lightSource, l, n) : transperency(gp, lightSource, l, n,nl);
                if (!(ktr.product(k).lowerThan(MIN_CALC_COLOR_K))) {
                    Color il = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(il.scale(calcDiffusive(material, nl)), il.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }
        return color;
    }

//    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 kx) {
//        int nShininess = intersection.geometry.getMaterial().nShininess;
//        Double3 kd = intersection.geometry.getMaterial().kD;
//        Double3 ks = intersection.geometry.getMaterial().kS;
//        Color color = Color.BLACK;
//        Vector n = intersection.geometry.getNormal(intersection.point);
//        double nv = alignZero(n.dotProduct(ray.getDirection()));
//
//        for (LightSource lightSource : scene.lights) {
//            Vector l = lightSource.getL(intersection.point);
//            double nl = alignZero(n.dotProduct(l));
//            if (nl * nv > 0) {
//                Double3 ktr =
//
//                        softShadows ? softShadows(intersection, lightSource, l, n) : transperency(intersection, lightSource, l, n,nl);
//                if (!ktr.product(kx).lowerThan(MIN_CALC_COLOR_K)) {
//                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
//                    color = color.add(calcDiffuse(kd, nl, lightIntensity),
//                            calcSpecular(ks, l, n, nl, ray.getDirection(), nShininess, lightIntensity));
//                }
//            }
//        }
//        return color;
//    }

    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity) {
        return lightIntensity.scale(kd.scale(Math.abs(nl)));
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

    private List<Ray> constructTransparencyRays(Point p, Vector v, Vector n, double gd) {
        Ray transparencyRay = new Ray(p, v, n);
        if (isZero(gd))
            return List.of(transparencyRay);
        return new Blackboard(nXY, transparencyRay, distanceGrid, gd).gridRays();
        // return gridRays(n, transparencyRay, 1, gd);
    }

    private List<Ray> constructReflectionRays(Point p, Vector v, Vector n, double gd) {
        double vn = v.dotProduct(n);
        if (isZero(vn))
            return null;
        Vector reflectionDirection = (v.subtract(n.scale(2 * vn))).normalize();
        if (isZero(gd))
            return List.of(new Ray(p, reflectionDirection, n));
        return new Blackboard(nXY, new Ray(p, reflectionDirection, n), distanceGrid, gd).gridRays();

    }

    public SimpleRayTracer setSoftShadows(boolean softShadows) {
        this.softShadows = softShadows;
        return this;
    }

    public SimpleRayTracer setGridResolution(int grid) {
        this.gridResolution = grid;
        return this;
    }

    public RayTracerBase setDistanceGrid(double distanceGrid) {
        this.distanceGrid = distanceGrid;
        return this;
    }

    public void setNxy(int nXY) {
        this.nXY = nXY;
    }
}