package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.isZero;
/**
 * Testing Polygons
 *
 * @author Moy and Efrat
 */
class PolygonTest {
    private final Point p05051 = new Point(0.5, 0.5, 1);

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts = {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1]))),
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     */
    @Test
    void findIntersections() {
    }
//        // all tests assume a point on the plane in which the Polygon is on and check
//        // if the function identifies whether the point is inside the Polygon or not
//        Polygon t = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1));
//
//        // ============ Equivalence Partitions Tests ==============
//        // TC01: Ray intersects the Polygon
//        List<Point> result = t.findIntersections(new Ray(p05051, new Vector(-0.5, -1, -1)));
//        assertEquals(1, result.size(), "ERROR: findIntersections() did not return the right number of points");
//        assertEquals(List.of(new Point(0.3, 0.1, 0.6)), result, "Incorrect intersection points");
//
//        // TC02: Ray outside against edge
//        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, -2, -1))),
//                "ERROR: findIntersections() did not return null");
//
//        // TC03: Ray outside against vertex
//        assertNull(t.findIntersections(new Ray(p05051, new Vector(1, -0.5, -1))),
//                "ERROR: findIntersections() did not return null");
//
//        // =============== Boundary Values Tests ==================
//        // TC04: Ray on edge
//        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, -1.5, -1))),
//                "ERROR: findIntersections() did not return null");
//
//        // TC05: Ray on vertex
//        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, 0.5, -1))),
//                "ERROR: findIntersections() did not return null");
//
//        // TC06: Ray on edge's continuation
//        assertNull(t.findIntersections(new Ray(p05051, new Vector(-0.5, -1, 0.5))),
//                "ERROR: findIntersections() did not return null");
//    }
}
