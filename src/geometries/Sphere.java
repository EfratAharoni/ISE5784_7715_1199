package geometries;

import primitives.Point;
import java.util.List;
import java.lang.Math;
import primitives.*;
import static primitives.Util.*;

public class Sphere extends RadialGeometry {
    /**
     * center point of the sphere
     */
    final private Point center;

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector u=center.subtract(ray.getHead()); //(1,0,0)-(-1,0,0)=(2,0,0)
        double tm=(ray.getDirection()).dotProduct(u); //(3,1,0)*(2,0,0)=6
        double d=u.lengthSquared()-tm*tm;//4-36
        double th=radius*radius-d*d;
        // 4-10
        if(alignZero(th)<=0)
            return null;
        //d<radius

        th=Math.sqrt(th);
        double t1,t2;
        t1=tm+th;
        t2=tm-th;
        if (t1>0 & t2>0)
            return List.of(ray.getPoint(t1),ray.getPoint(t2));
        if((isZero(t1) || t1<0) && t2>0)
            return List.of(ray.getPoint(t2));
        return List.of(ray.getPoint(t1));
    }
}


