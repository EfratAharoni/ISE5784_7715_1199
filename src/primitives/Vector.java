/**
 * Class representing a vector in 3D space.
 *
 * A vector has a magnitude (length) and direction. This class inherits from the Point class
 * since a vector can be represented by its starting point (origin) and its direction.
 *
 * @author Efrat and Moy
 * @see Point
 */
package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero.");
        }
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be zero.");
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * The cross product of two vectors results in a new vector that is orthogonal (perpendicular) to both original vectors.
     *
     * @param other The other vector to perform the cross product with.
     * @return A new Vector representing the cross product of this vector and the other vector.
     */
    public Vector crossProduct(Vector other) {
        double ax = xyz.d1;
        double ay = xyz.d2;
        double az = xyz.d3;

        double bx = other.xyz.d1;
        double by = other.xyz.d2;
        double bz = other.xyz.d3;

        double cx = ay * bz - az * by;
        double cy = az * bx - ax * bz;
        double cz = ax * by - ay * bx;

        return new Vector(cx, cy, cz);
    }

    /**
     * Calculates the dot product (scalar product) of this vector and another vector.
     *
     * The dot product of two vectors results in a scalar value representing the projection of one vector onto the other.
     *
     * @param other The other vector to perform the dot product with.
     * @return The dot product (scalar value) of this vector and the other vector.
     */
    public double dotProduct(Vector other) {
        return other.xyz.d1 * xyz.d1 +
                other.xyz.d2 * xyz.d2 +
                other.xyz.d3 * xyz.d3;
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return The squared length of this vector.
     */
    public double lengthSquared() {
        return xyz.d1 * xyz.d1 +
                xyz.d2 * xyz.d2 +
                xyz.d3 * xyz.d3;
    }

    /**
     * Calculates the length (magnitude) of this vector.
     *
     * @return The length (magnitude) of this vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector to a unit vector (length of 1).
     *
     * @return A new Vector representing the normalized version of this vector.
     * @throws ArithmeticException If the length of the vector is zero (division by zero).
     */
    public Vector normalize() {
        double len = length();
        if (len == 0)
            throw new ArithmeticException("Cannot normalize a zero vector (division by zero).");
        return new Vector(xyz.reduce(len));
    }

    @Override
    public String toString() {
        return "Vector " + xyz;
    }
}
