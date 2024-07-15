package geometries;
import primitives.Ray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable{
   final private List<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() {
    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {

        Collections.addAll(this.geometries , geometries);
    }

//    @Override
//    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
//        List<GeoPoint> intersections=null;
//        for(Intersectable geometry: geometries){
//            List<GeoPoint> currentIntersections=geometry.findGeoIntersections(ray);
//            if(currentIntersections!=null){
//                if(intersections==null)
//                    intersections=new ArrayList<GeoPoint>();
//                intersections.addAll(currentIntersections);
//            }
//        }
//        return intersections;
//    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        LinkedList<GeoPoint> intersections = null;
        for (Intersectable shape : geometries) {
            List<GeoPoint> shapeIntersections = shape.findGeoIntersectionsHelper(ray, maxDistance);
            if (shapeIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(shapeIntersections);
            }
        }
        return intersections;
    }
}
