package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.awt.AWTEventMulticaster.add;

public class Geometries implements Intersectable{
    List<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() {
    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    private void add(Intersectable... geometries) {
        Collections.addAll(this.geometries , geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections=null;
        for(Intersectable geometry: geometries){
            List<Point> currentIntersections=geometry.findIntersections(ray);
            if(currentIntersections!=null){
                if(intersections==null)
                    intersections=new LinkedList<>();
                intersections.addAll(currentIntersections);
            }

        }
        return intersections;
    }
}
