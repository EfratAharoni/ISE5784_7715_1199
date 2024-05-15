package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import static primitives.Util.isZero;

import java.util.List;

import geometries.*;
import primitives.*;

/**
 * @author Moy and Efrat
 *
 */
class PlaneTest {

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

}