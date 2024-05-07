package primitives;

public class Point {
    final protected Double3 xyz;
    public static final Point ZERO = new Point(Double3.ZERO);


    public Point(double x, double y, double z) {

        xyz = new Double3(x,y,z);
    }

    public Point(Double3 xyz) {

        this.xyz= xyz;
    }

    public Vector subtract(Point p1) {
        return new Vector (xyz.subtract(p1.xyz));
    }

    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    public double distanceSquared(Point p) {
          return (this.xyz.d1 - p.xyz.d1) * (this.xyz.d1 - p.xyz.d1) +
                (this.xyz.d2 - p.xyz.d2) * (this.xyz.d2 - p.xyz.d2) +
                (this.xyz.d3 - p.xyz.d3) * (this.xyz.d3 - p.xyz.d3);
    }

    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }
}