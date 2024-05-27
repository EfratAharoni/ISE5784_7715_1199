package geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;
import java.util.List;
import primitives.*;

/**
 * @author Moy and Efrat
 *
 */
class PlaneTest {

    private final Point p05025025 = new Point(0.5, 0.25, 0.25);
    private final Point p06025025 = new Point(0.6, 0.25, 0.25);
    private final Vector v352 = new Vector(-3, 5, 2);
    private final Vector v111 = new Vector(-1, -1, -1);

    @Test
    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on plane
        Point[] pts = {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 1, 0)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Plane's normal is not a unit vector");
        // ensure the result is orthogonal to plane
        assertTrue(pln.getNormal().equals(result) || pln.getNormal().equals(result.scale(-1)), "getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void findIntersections() {

        Plane p = new Plane(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the plane (the ray is not orthogonal or parallel to the
        // plane)
        List<Point> result = p.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(-0.5, -1, -1)));
        assertEquals(1, result.size(), "ERROR: findIntersections() did not return the right number of points");
        assertEquals(List.of(new Point(0.3, 0.1, 0.6)), result, "Incorrect intersection points");

        // TC02: Ray does not intersect the plane
        assertNull(p.findIntersections(new Ray(new Point(0, 0, 1.5), new Vector(1, 0, 1.5))),
                "ERROR: findIntersections() did not return null when the ray does not intersect the plane");

        // =============== Boundary Values Tests ==================
        // **** Group: the ray is parallel to the plane
        // TC03: Ray is parallel to the plane and included in the plane
        assertNull(p.findIntersections(new Ray(p05025025, new Vector(-0.5, 0.2, 0.3))),
                "ERROR: findIntersections() did not return null when the ray is parallel to the plane and included in the plane");

        // TC04: ray is parallel to the plane and not included in the plane
        assertNull(p.findIntersections(new Ray(p06025025, new Vector(-0.5, 0.2, 0.3))),
                "ERROR: findIntersections() did not return null when the ray is parallel to the plane and not included in the plane");

        // **** Group: the ray is orthogonal to the plane
        // TC05: Ray is orthogonal to the plane and begins before the plane
        result = p.findIntersections(new Ray(p06025025, v111));
        assertEquals(1, result.size(),
                "ERROR: findIntersections() returned incorrect number of points when the ray is orthogonal to the plane and begins before the plane");

        // TC06: Ray is orthogonal to the plane and begins in the plane
        assertNull(p.findIntersections(new Ray(p05025025, v111)),
                "ERROR: findIntersections() did not return null when the ray is orthogonal to the plane and begins in the plane");

        // TC07: Ray is orthogonal to the plane and begins after the plane
        assertNull(p.findIntersections(new Ray(new Point(0.4, 0.25, 0.25), v111)),
                "ERROR: findIntersections() did not return null when the ray is orthogonal to the plane and begins after the plane");

        // **** Group: the ray is neither orthogonal nor parallel to the plane
        // TC08: Ray begins in the plane
        assertNull(p.findIntersections(new Ray(p05025025, v352)),
                "ERROR: findIntersections() did not return null when the ray begins in the plane");

        // Ray is neither orthogonal nor parallel to the plane and begins in the same
        // point which appears as reference point in the plane
        assertNull(p.findIntersections(new Ray(p.getP(), v352)),
                "ERROR: findIntersections() did not return null when the ray begins in the same point which appears as reference point in the plane");
    }
}