package renderer;

import primitives.*;

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
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

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
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i){
        Double rY = height / nY;
        Double rX = width / nX;
        Point pixelIJ = position.add(vTo.scale(distance));

        Double yI = -(i - (nY - 1) / 2.0) * rY;
        Double xJ = (j - (nX - 1) / 2.0) * rX;
        // Check if the pixel is at the center of the view plane
        if (Util.isZero(xJ) && Util.isZero(yI)) {
            return new Ray(position, pixelIJ.subtract(position));
        }

        // Check if the pixel is on the horizontal axis of the view plane
        if (Util.isZero(xJ)) {
            pixelIJ = pixelIJ.add(vUp.scale(yI));
            return new Ray(position, pixelIJ.subtract(position));
        }

        // Check if the pixel is on the vertical axis of the view plane
        if (Util.isZero(yI)) {
            pixelIJ = pixelIJ.add(vRight.scale(xJ));
            return new Ray(position, pixelIJ.subtract(position));
        }

        // Calculate the final point on the view plane for the specified pixel
        pixelIJ = pixelIJ.add(vRight.scale(xJ).add(vUp.scale(yI)));

        // Return the constructed ray from the camera's location to the calculated point on the view plane
        return new Ray(position, pixelIJ.subtract(position));
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
         * Sets image writer in builder pattern
         *
         * @param iw writer
         * @return Camera that results
         */
        public Builder setImageWriter(ImageWriter iw) {
            camera.imageWriter = iw;
            return this;
        }

        public void renderImage() {
            if (camera.imageWriter == null)
                throw new MissingResourceException("Image writer was null", getClass().getName(), "");
            if (camera.rayTracer == null)
                throw new MissingResourceException("Ray tracer was null", getClass().getName(), "");

            int nY = camera.imageWriter.getNy();
            int nX = camera.imageWriter.getNx();

            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; j++)
                    camera.imageWriter.writePixel(j, i, camera.castRay(i, j));
        }

        /**
         * Sets ray tracer in builder pattern
         *
         * @param rt tracer
         * @return Camera that results
         */
        public Builder setRayTracer(RayTracerBase rt) {
            camera.rayTracer = rt;
            return this;
        }

        public void writeToImage() {
            if (camera.imageWriter == null)
                throw new MissingResourceException("Image writer was null", ImageWriter.class.getCanonicalName(), "");
            camera.imageWriter.writeToImage();
        }
        public void printGrid(int interval, Color color) {
            if (camera.imageWriter == null)
                throw new MissingResourceException("Image writer was null", getClass().getName(), "");
            int nY = camera.imageWriter.getNy();
            int nX = camera.imageWriter.getNx();
            for (int i = 0; i < nY; i += interval)
                for (int j = 0; j < nX; j += 1)
                    camera.imageWriter.writePixel(i, j, color);
            for (int i = 0; i < nY; i += 1)
                for (int j = 0; j < nX; j += interval)
                    camera.imageWriter.writePixel(i, j, color);

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

    /**
     * Casts a ray through every pixel in the image writer, writing to it the color
     * of each pixel (calculated using the castRay function).
     */


    /**
     * Prints a grid over the image at an interval of pixels and colors it
     * accordingly
     *
     * @param interval the space in pixels
     * @param color    color of the grid
     */


    /**
     * Writes pixels to final image by delegating to the ImageWriter
     */


    /**
     * Uses the constructRay function to build the ray from the camera to the pixel,
     * and then uses the traceRay function to send back the color of the nearest
     * intersection point along that ray.
     *
     * @param i the pixel's number on the y axis
     * @param j the pixel's number on the x axis
     * @return the color of the closest intersection point on the ray through pixel
     *         i,j
     */
    private Color castRay(int i, int j) {
        return rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
    }

}