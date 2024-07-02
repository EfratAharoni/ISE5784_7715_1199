package geometries;
import primitives.*;
import java.util.List;
import java.util.Objects;

/**
 * Interface representing an intersectable geometrical shape.
 * Classes implementing this interface can be intersected by a ray,
 * and the intersections can be found and returned as a list of points.
 *
 * @author  Moy Georgi & Efrat Aharoni
 */
public abstract class Intersectable {
    public static class GeoPoint {
        public Geometry geometry;
        public Point point;

        /**
         * Constructs a GeoPoint object with the specified geometry and point.
         *
         * @param geometry the geometry
         * @param point    the point
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoPoint geoPoint)) return false;
            return geometry == geoPoint.geometry && point.equals(geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
        public final List<GeoPoint> findGeoIntersections(Ray ray) {
            return findGeoIntersectionsHelper(ray);
        }

        protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

        /**
         * @param ray A method that receives a ray
         * @return a list of intersection points between the ray and geometry
         */
        public final List<Point> findIntersections(Ray ray) {
            List<GeoPoint> geoList = findGeoIntersections(ray);
            return geoList == null ? null
                    : geoList.stream().map(gp -> gp.point).toList();
        }
}
