package primitives;

public class Point {
    final protected Double3 xyz;
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructor for creating a point with specific coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Constructor for creating a point from a Double3 object.
     *
     * @param xyz A Double3 object representing the coordinates (x, y, z).
     */
    public Point(Double3 xyz) {

        this.xyz= xyz;
    }

    public Double3 getXyz() {
        return xyz;
    }

    /**
     * Calculates the vector resulting from subtracting another point from this point.
     *
     * @param p1 The point to be subtracted.
     * @return A new Vector representing the difference between this point and p1.
     */
    public Vector subtract(Point p1) {
        return new Vector (xyz.subtract(p1.xyz));
    }

    /**
     * Calculates a new point by adding a vector to this point.
     *
     * @param v1 The vector to be added.
     * @return A new Point representing the sum of this point and v1.
     */
    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param p The other point.
     * @return The squared distance between this point and p.
     */
    public double distanceSquared(Point p) {
        // Improved readability with separate variable names
        double dx = this.xyz.d1 - p.xyz.d1;
        double dy = this.xyz.d2 - p.xyz.d2;
        double dz = this.xyz.d3 - p.xyz.d3;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param p1 The other point.
     * @return The distance between this point and p1.
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    @Override
    public String toString() {
        return "Point{" + "xyz=" + xyz + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)&& xyz.equals(other.xyz);
    }

    public double getX() {
        return xyz.d1;
    }
}
