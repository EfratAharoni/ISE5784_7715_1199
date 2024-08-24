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

import static primitives.Util.isZero;
import com.sun.source.tree.ReturnTree;

public class  Vector extends Point {

    public Vector(double x,double y,double z){
        super(x,y,z);
        if(Double3.ZERO.equals(this.xyz))
            throw  new IllegalArgumentException("x,y,z can't be 0");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if(Double3.ZERO.equals(xyz))
            throw  new IllegalArgumentException("can't be (0,0,0)");
    }

    public Vector add(Vector v){
        if(this.equals(v.scale(-1)))
            throw new IllegalArgumentException("Adding opposite vectors gives the zero vector");
        return new Vector(xyz.add(v.xyz));
    }

    /**
     * Scale (multiply) Vector by a number into a new Vector where each coordinate
     * is multiplied by the number
     *
     * @param scalar right hand side operand for scaling
     * @return result of scale
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
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
     * dot product between two vectors (scalar product)
     * @param other
     * @return new vector
     */
    public double dotProduct(Vector other) {
        return other.xyz.d1 * xyz.d1 +
                other.xyz.d2 * xyz.d2 +
                other.xyz.d3 * xyz.d3;
    }

    /**
     * @return the length Squared of vector
     */
    public double lengthSquared() {
        return xyz.d1 * xyz.d1 +
                xyz.d2 * xyz.d2 +
                xyz.d3 * xyz.d3;
    }

    /**
     * @return the sqrt of length Squared (the length of vector)
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * @return Normalized vector
     */
    public Vector normalize() {
        double len = length();
        if (len == 0)
            throw new ArithmeticException("Divide by zero!");
        return new Vector(xyz.reduce((len)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.equals(other);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     *
     * Finds orthogonal vector to the current vector.
     *
     * @return A new Vector object that is orthogonal to the current vector.
     */
    public Vector findOrthogonal() {
        double x = this.xyz.d1, //
                y = this.xyz.d2, //
                z = this.xyz.d3, //
                absX = Math.abs(x), //
                absY = Math.abs(y), //
                absZ = Math.abs(z);
        if (absX < absY)
            return absX < absZ ? new Vector(0, -z, y).normalize() : new Vector(-y, x, 0).normalize();
        else
            return absY < absZ ? new Vector(z, 0, -x).normalize() : new Vector(-y, x, 0).normalize();
    }
}