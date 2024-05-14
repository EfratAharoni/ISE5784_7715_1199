package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import primitives.*;
import geometries.*;

class SphereTest {

    @Test
    void testgetNormal() {
            // ============ Equivalence Partitions Tests ==============
            // TC01: test getting a normal vector from any point on sphere
            Sphere sph = new Sphere(new Point(1, 4, 5), 3);
            // ensure there are no exceptions
            assertDoesNotThrow(() -> sph.getNormal(new Point(4, 4, 5)), "getNormal() throws an unexpected exception");
            // generate the test result
            Vector result = sph.getNormal(new Point(4, 4, 5));
            // ensure |result| = 1
            assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
            // ensure the result is orthogonal to sphere at the given point
            assertEquals(new Vector(1, 0, 0), result, "getNormal() wrong result");
    }
}