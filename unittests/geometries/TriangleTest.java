package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static primitives.Util.isZero;
import primitives.Ray;


import java.util.List;

class TriangleTest {
    private final Point  p05051 = new Point(0.5, 0.5, 1);
    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on triangle
        Point[] pts = {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Triangle tri = new Triangle(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tri.getNormal(new Point(0, 1, 0)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = tri.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1]))),
                    "Triangle's normal is not orthogonal to one of the vectors of the plane");
    }
    @Test
    void testFindIntersections() {
        // all tests assume a point on the plane in which the triangle is on and check
        // if the function identifies whether the point is inside the triangle or not

        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the triangle
        List<Point> result = triangle.findIntersections(new Ray(p05051, new Vector(-0.5, -1, -1)));
        assertEquals(1, result.size(), "ERROR: findIntersections() did not return the right number of points");
        assertEquals(List.of(new Point(0.3, 0.1, 0.6)), result, "Incorrect intersection points");

        // TC02: Ray outside against edge
        assertNull(triangle.findIntersections(new Ray(p05051, new Vector(-2, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC03: Ray outside against vertex
        assertNull(triangle.findIntersections(new Ray(p05051, new Vector(1, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // =============== Boundary Values Tests ==================
        // TC04: Ray on edge
        assertNull(triangle.findIntersections(new Ray(p05051, new Vector(-0.5, -0.1, -0.4))),
                "ERROR: findIntersections() did not return null");

        // TC05: Ray on vertex
        assertNull(triangle.findIntersections(new Ray(p05051, new Vector(-0.5, 0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC06: Ray on edge's continuation
        assertNull(triangle.findIntersections(new Ray(p05051, new Vector(-0.5, -1, 0.5))),
                "ERROR: findIntersections() did not return null");
    }
}