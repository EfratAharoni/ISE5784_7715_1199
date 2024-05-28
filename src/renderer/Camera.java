package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Camera implements Cloneable{

    private Point position;
    private Vector vUp;
    private Vector vRight;
    private Vector vTo;
    private Double height=0.0;
    private Double width=0.0;
    private Double distance=0.0;

    private Camera() {
    }

    public Point getPosition() {
        return position;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWidth() {
        return width;
    }

    public Double getDistance() {
        return distance;
    }

    public static Builder getBuilder() {
        return null;
    }

    public Ray constructRay(int nX, int nY, int j, int i){
        return null;
    }

    public static class Builder{

        final private Camera camera = new Camera();

//        public Builder() {
//            camera=new Camera();
//        }
//
//        public Builder(Camera camera) {
//            this.camera=camera;
//        }

        public Builder setLocation(Point p0){
        }

        public Builder setDirection(Vector vUp, Vector vTo){

        }

        public Builder setVpSize(Double height, Double width){

        }

        public Builder setVpDistance(Double distance){

        }

    }

}
