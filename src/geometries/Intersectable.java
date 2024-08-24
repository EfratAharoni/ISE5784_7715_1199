package geometries;
import primitives.*;
import java.util.List;
import java.util.Objects;
import java.util.List;
import static java.lang.Math.*;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Interface representing an intersectable geometrical shape.
 * Classes implementing this interface can be intersected by a ray,
 * and the intersections can be found and returned as a list of points.
 *
 * @author  Moy Georgi & Efrat Aharoni
 */
public abstract class Intersectable {
    public static long count = 0;
    public static long positives = 0;

    /** a box- for BVH */
    protected Box box = null;
    /** Sets CBR improvement */
    protected static boolean cbr = false;

    /**
     * Set CBR improvement
     *
     */
    public static void createCBR(boolean cbr) {
        Intersectable.cbr = cbr;
        Box.count = 0;
        Box.positives = 0;
        count = 0;
    }

    /**
     * Class for representing a Box for BVH contains 6 double values of x,y,z
     * minimum and maximum
     *
     * @author Shilat Sharon and Lea Drach
     *
     */
    public static class Box {
        public static long count = 0;
        public static long positives = 0;

        /** x minimum */
        protected double minX;
        /** x maximum */
        protected double maxX;
        /** y minimum */
        protected double minY;
        /** y maximum */
        protected double maxY;
        /** z minimum */
        protected double minZ;
        /** z maximum */
        protected double maxZ;

        /**
         * Constructor, build a box around among of shapes
         *
         * @param x0 x minimum
         * @param x1 x max
         * @param y0 y minimum
         * @param y1 y max
         * @param z0 z minimum
         * @param z1 z max
         */
        public Box(double x0, double x1, double y0, double y1, double z0, double z1) {
            this.minX = x0;
            this.maxX = x1;
            this.minY = y0;
            this.maxY = y1;
            this.minZ = z0;
            this.maxZ = z1;
        }

        /**
         * Empty Constructor, builds an infinity box
         *
         */
        public Box() {
            this.minX = Double.POSITIVE_INFINITY;
            this.maxX = Double.NEGATIVE_INFINITY;
            this.minY = Double.POSITIVE_INFINITY;
            this.maxY = Double.NEGATIVE_INFINITY;
            this.minZ = Double.POSITIVE_INFINITY;
            this.maxZ = Double.NEGATIVE_INFINITY;
        }

        /**
         * Returns true if the ray intersects the box
         *
         * @param r ray
         * @return True if the ray intersects the box. Otherwise, False
         */
        public boolean isIntersected(Ray r) {
            ++count;
            Point origin = r.getHead();
            Vector dir = r.getDirection();
            double v1, v2;

            double tMin = Double.NEGATIVE_INFINITY;
            double tMax = Double.POSITIVE_INFINITY;
            double originX = origin.getX();
            double dirX = dir.getX();
            if (dirX != 0) {
                // b=D*t+O => y=mx+b =>dirx*tmin+originx=minx
                v1 = (minX - originX) / dirX;
                v2 = (maxX - originX) / dirX;
                tMin = min(v1, v2);
                tMax = max(v1, v2);
            }

            double tMinY = Double.NEGATIVE_INFINITY;
            double tMaxY = Double.POSITIVE_INFINITY;
            double originY = origin.getY();
            double dirY = dir.getY();
            if (dirY != 0) {
                // b=D*t+O => y=mx+b =>dirx*tmin+originx=minx
                v1 = (minY - originY) / dirY;
                v2 = (maxY - originY) / dirY;
                tMinY = min(v1, v2);
                tMaxY = max(v1, v2);
            }
            if ((tMin > tMaxY) || (tMinY > tMax))
                return false;

            if (tMinY > tMin)
                tMin = tMinY;
            if (tMaxY < tMax)
                tMax = tMaxY;

            double originZ = origin.getZ();
            double dirZ = dir.getZ();
            double tMinZ = Double.NEGATIVE_INFINITY;
            double tMaxZ = Double.POSITIVE_INFINITY;
            if (dirZ != 0) {
                // b=D*t+O => y=mx+b =>dirx*tmin+originx=minx
                v1 = (minZ - originZ) / dirZ;
                v2 = (maxZ - originZ) / dirZ;
                tMinZ = min(v1, v2);
                tMaxZ = max(v1, v2);
            }

            // If either the max value of Z is smaller than overall min value, or min value
            // of Z is bigger than the overall
            // max, we can already return false. Otherwise we can return true since no other
            // coordinate checks are needed.
            boolean check = tMin <= tMaxZ && tMinZ <= tMax;
            if (check)
                ++positives;
            return check;
        }

    }

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
//        public final List<GeoPoint> findGeoIntersections(Ray ray) {
//            return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
//    }

    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return box == null || box.isIntersected(ray) //
                ? intermediate(ray) //
                : null;
    }

    private final List<GeoPoint> intermediate(Ray ray) {
        ++count;
        var list = findGeoIntersectionsHelper(ray);
        if (list != null)
            ++positives;
        return list;
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
            return findGeoIntersectionsHelper(ray, maxDistance);
    }

        abstract protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

        /**
         * @param ray A method that receives a ray
         * @return a list of intersection points between the ray and geometry
         */
        public final List<Point> findIntersections(Ray ray) {
            List<GeoPoint> geoList = findGeoIntersections(ray);
            return geoList == null ? null
                    : geoList.stream().map(gp -> gp.point).toList();
        }

    /**
     * finds the closest intersection point to a given ray
     *
     * @param ray the given ray
     * @return the point and its geometry, null if there is no such point
     */
    public GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }
}
