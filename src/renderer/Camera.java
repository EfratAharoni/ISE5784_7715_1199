package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
        Point pc = position.add(vTo.scale(distance));     // center of the view plane
        double Ry = height/nY;                      // Ratio - pixel height
        double Rx = width/nX;                       // Ratio - pixel width

        double yJ = alignZero(-(i - (nY - 1) / 2d) * Ry);       // move pc Yi pixels
        double xJ = alignZero((j - (nX - 1) / 2d) * Rx);        // move pc Xj pixels

        Point PIJ = pc;
        if(!isZero(xJ))  PIJ = PIJ.add(vRight.scale(xJ));
        if(!isZero(yJ))  PIJ = PIJ.add(vUp.scale(yJ));

        return new Ray(position, PIJ.subtract(position));
    }

    @Override
    public Camera clone() {
        try {
            Camera clone = (Camera) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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

        /**
         * Camera position determination method
         * @param p0 dot parameter
         * @return
         */
        public Builder setLocation(Point p0){
            camera.position=p0;
            return this;
        }

        /**
         * The camera orientation method that verifies that two vectors are vertical and normalizes them before saving
         * @param vUp Vector Up
         * @param vTo Vector forward
         * @return
         */
        public Builder setDirection(Vector vUp, Vector vTo){
            if(!(Util.isZero(vUp.dotProduct(vTo))))
                throw new IllegalArgumentException("The vectors aren't ortogonal");
            camera.vUp=vUp.normalize();
            camera.vTo=vTo.normalize();
            return this;
        }

        /**
         *View plane size method
         * @param height of the view plane
         * @param width of the view plane
         * @return
         */
        public Builder setVpSize(Double height, Double width){
            if(height<=0||width<=0)
                throw new IllegalArgumentException("width and height should be positive");
            camera.height=height;
            camera.width=width;
            return this;
        }

        /**
         *
         * @param distance
         * @return Viewing surface position method for camera distance to view plane
         */
        public Builder setVpDistance(Double distance){
            if(distance<=0)
                throw new IllegalArgumentException("distance should be not negative");
            camera.distance=distance;
            return this;
        }

        /**
         *
         * @return For each field, if its value is zero, we throw out an exception with 3 parameters:
         * a description of the problem, the name of the camera department, a description of the missing data.
         * If you do not set up a defection, you will throw out an appropriate deviation from the existing deviations in Java
         */
        public Camera build(){
            if(camera.position==null)
                throw new MissingResourceException("missing rendering data","Camera","camera's position");
            if(camera.vTo==null)
                throw new MissingResourceException("missing rendering data","Camera","camera's vTo");
            if(camera.vUp==null)
                throw new MissingResourceException("missing rendering data","Camera","camera's vUp");
            if(Util.alignZero(camera.height)<=0)
                throw new MissingResourceException("missing rendering data","Camera","camera's height");
            if(Util.alignZero(camera.width)<=0)
                throw new MissingResourceException("missing rendering data","Camera","camera's width");
            if(Util.alignZero(camera.distance)<=0)
                throw new MissingResourceException("missing rendering data","Camera","camera's distance");

            camera.vRight=(camera.vTo.crossProduct(camera.vUp)).normalize();
            return (Camera)camera.clone();
        }
    }
}