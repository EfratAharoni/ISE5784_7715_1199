//package renderer;
//
//import primitives.*;
//
//import java.util.MissingResourceException;
//
//
//public class Camera implements Cloneable{
//
//    private Point position;
//    private Vector vUp;
//    private Vector vRight;
//    private Vector vTo;
//    private Double height=0.0;
//    private Double width=0.0;
//    private Double distance=0.0;
//    private ImageWriter imageWriter;
//    private RayTracerBase rayTracer;
//
//    private Camera() {
//    }
//
//    public Point getPosition() {
//        return position;
//    }
//
//    public Vector getvUp() {
//        return vUp;
//    }
//
//    public Vector getvRight() {
//        return vRight;
//    }
//
//    public Vector getvTo() {
//        return vTo;
//    }
//
//    public Double getHeight() {
//        return height;
//    }
//
//    public Double getWidth() {
//        return width;
//    }
//
//    public Double getDistance() {
//        return distance;
//    }
//
//    public static Builder getBuilder() {
//        return new Builder();
//    }
//
//    public Ray constructRay(int nX, int nY, int j, int i){
//        Double rY = height / nY;
//        Double rX = width / nX;
//        Point pixelIJ = position.add(vTo.scale(distance));
//
//        Double yI = -(i - (nY - 1) / 2.0) * rY;
//        Double xJ = (j - (nX - 1) / 2.0) * rX;
//        // Check if the pixel is at the center of the view plane
//        if (Util.isZero(xJ) && Util.isZero(yI)) {
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Check if the pixel is on the horizontal axis of the view plane
//        if (Util.isZero(xJ)) {
//            pixelIJ = pixelIJ.add(vUp.scale(yI));
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Check if the pixel is on the vertical axis of the view plane
//        if (Util.isZero(yI)) {
//            pixelIJ = pixelIJ.add(vRight.scale(xJ));
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Calculate the final point on the view plane for the specified pixel
//        pixelIJ = pixelIJ.add(vRight.scale(xJ).add(vUp.scale(yI)));
//
//        // Return the constructed ray from the camera's location to the calculated point on the view plane
//        return new Ray(position, pixelIJ.subtract(position));
//    }
//
//  @Override
//    public Camera clone() {
//        try {
//            return (Camera) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException("Cloning not supported", e);
//        }
//    }
//
//    public static class Builder {
//        final private Camera camera = new Camera();
//
////        public Builder() {
////            camera=new Camera();
////        }
////
////        public Builder(Camera camera) {
////            this.camera=camera;
////        }
//
//        /**
//         * Camera position determination method
//         *
//         * @param p0 dot parameter
//         * @return
//         */
//        public Builder setLocation(Point p0) {
//            camera.position = p0;
//            return this;
//        }
//
//        /**
//         * The camera orientation method that verifies that two vectors are vertical and normalizes them before saving
//         *
//         * @param vUp Vector Up
//         * @param vTo Vector forward
//         * @return
//         */
//        public Builder setDirection(Vector vUp, Vector vTo) {
//            if (!(Util.isZero(vUp.dotProduct(vTo))))
//                throw new IllegalArgumentException("The vectors aren't ortogonal");
//            camera.vUp = vUp.normalize();
//            camera.vTo = vTo.normalize();
//            return this;
//        }
//
//        /**
//         * View plane size method
//         *
//         * @param height of the view plane
//         * @param width  of the view plane
//         * @return
//         */
//        public Builder setVpSize(Double height, Double width) {
//            if (height <= 0 || width <= 0)
//                throw new IllegalArgumentException("width and height should be positive");
//            camera.height = height;
//            camera.width = width;
//            return this;
//        }
//
//        /**
//         * @param distance
//         * @return Viewing surface position method for camera distance to view plane
//         */
//        public Builder setVpDistance(Double distance) {
//            if (distance <= 0)
//                throw new IllegalArgumentException("distance should be not negative");
//            camera.distance = distance;
//            return this;
//        }
//
//        /**
//         * Sets image writer in builder pattern
//         *
//         * @param iw writer
//         * @return Camera that results
//         */
//        public Builder setImageWriter(ImageWriter iw) {
//            camera.imageWriter = iw;
//            return this;
//        }
//        /**
//         * Sets ray tracer in builder pattern
//         *
//         * @param rt tracer
//         * @return Camera that results
//         */
//        public Builder setRayTracer(RayTracerBase rt) {
//            camera.rayTracer = rt;
//            return this;
//        }
//        /**
//         *
//         * @return For each field, if its value is zero, we throw out an exception with 3 parameters:
//         * a description of the problem, the name of the camera department, a description of the missing data.
//         * If you do not set up a defection, you will throw out an appropriate deviation from the existing deviations in Java
//         */
//        public Camera build(){
//            if(camera.position==null)
//                throw new MissingResourceException("missing rendering data","Camera","camera's position");
//            if(camera.vTo==null)
//                throw new MissingResourceException("missing rendering data","Camera","camera's vTo");
//            if(camera.vUp==null)
//                throw new MissingResourceException("missing rendering data","Camera","camera's vUp");
//            if(Util.alignZero(camera.height)<=0)
//                throw new MissingResourceException("missing rendering data","Camera","camera's height");
//            if(Util.alignZero(camera.width)<=0)
//                throw new MissingResourceException("missing rendering data","Camera","camera's width");
//            if(Util.alignZero(camera.distance)<=0)
//                throw new MissingResourceException("missing rendering data","Camera","camera's distance");
//
//            camera.vRight=(camera.vTo.crossProduct(camera.vUp)).normalize();
//            return (Camera)camera.clone();
//        }
//    }
//
//        /**
//
//         Renders the image using the configured ray tracer and image writer.
//
//         @throws UnsupportedOperationException If the image writer or ray tracer is missing.
//         **/
//
//        public Camera renderImage()//מרנדרת את התמונה באמצעות סריקה על כל הפיקסלים בתמונה, משתמשת ב-Tracer שמקבלת ממנו צבע ומכתבת אותו לפיקסל התואם.
//        {
//            if (this.imageWriter == null)
//                throw new UnsupportedOperationException("Missing imageWriter");
//            if (this.rayTracer == null)
//                throw new UnsupportedOperationException("Missing rayTracerBase");
//
//            for (int i = 0; i < this.imageWriter.getNy(); i++)
//            {
//                for (int j = 0; j < this.imageWriter.getNx(); j++)
//                {
//                    Color color = castRay(j, i);//מחשבת את הצבע של הפיקסל הנוכחי באמצעות קרן
//                    this.imageWriter.writePixel(j, i, color);//כותבת את הצבע המחושב לפיקסל המתאים ב-imageWriter
//                }
//            }
//            return this;
//        }
//
//
//
//        public void writeToImage() {
//            if (this.imageWriter == null)
//                throw new MissingResourceException("Image writer was null", ImageWriter.class.getCanonicalName(), "");
//            this.imageWriter.writeToImage();
//        }
//        public Camera printGrid(int interval, Color color) {
//            if (this.imageWriter == null)
//                throw new MissingResourceException("Image writer was null", getClass().getName(), "");
//            int nY = this.imageWriter.getNy();
//            int nX = this.imageWriter.getNx();
//            for (int i = 0; i < nY; i += interval)
//                for (int j = 0; j < nX; j += 1)
//                    this.imageWriter.writePixel(i, j, color);
//            for (int i = 0; i < nY; i += 1)
//                for (int j = 0; j < nX; j += interval)
//                    this.imageWriter.writePixel(i, j, color);
//            return this;
//        }
//
//
//
//
//    /**
//     * Casts a ray through every pixel in the image writer, writing to it the color
//     * of each pixel (calculated using the castRay function).
//     */
//
//
//    /**
//     * Prints a grid over the image at an interval of pixels and colors it
//     * accordingly
//     *
//     * @param interval the space in pixels
//     * @param color    color of the grid
//     */
//
//
//    /**
//     * Writes pixels to final image by delegating to the ImageWriter
//     */
//
//
//    /**
//     * Uses the constructRay function to build the ray from the camera to the pixel,
//     * and then uses the traceRay function to send back the color of the nearest
//     * intersection point along that ray.
//     *
//     * @param i the pixel's number on the y axis
//     * @param j the pixel's number on the x axis
//     * @return the color of the closest intersection point on the ray through pixel
//     *         i,j
//     */
//    private Color castRay(int i, int j) {
//        return rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i));
//    }
//
//}

package renderer;
import primitives.*;
import java.util.MissingResourceException;

/*
 * Camera class represents a camera in 3D space with position, direction, and view plane properties.
 * It provides functionality to construct rays through a view plane.
 *
 * @author Hadar Nagar & Elinoy Damari
 */
public class Camera implements Cloneable {

    private Point position;

    private Vector vUp;
    private Vector vRight;
    private Vector vTo;

    //view plane
    private Double height = 0.0;
    private Double width = 0.0;
    private Double distance = 0.0;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /**
     * Private constructor to enforce the use of the builder for creating Camera instances.
     */
    private Camera() {
    }

    /**
     * Gets the position of the camera.
     *
     * @return the position of the camera.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the up vector of the camera.
     *
     * @return the up vector of the camera.
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Gets the right vector of the camera.
     *
     * @return the right vector of the camera.
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Gets the to vector of the camera.
     *
     * @return the to vector of the camera.
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Gets the height of the view plane.
     *
     * @return the height of the view plane.
     */
    public Double getHeight() {
        return height;
    }

    /**
     * Gets the width of the view plane.
     *
     * @return the width of the view plane.
     */
    public Double getWidth() {
        return width;
    }

    /**
     * Gets the distance from the camera to the view plane.
     *
     * @return the distance from the camera to the view plane.
     */
    public Double getDistance() {
        return distance;
    }


    /**
     * Creates a new builder for Camera.
     *
     * @return a new Camera.Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }





    /**
     * Constructs a ray through a specific pixel in the view plane.
     *
     * @param nX number of pixels in the X direction.
     * @param nY number of pixels in the Y direction.
     * @param j  pixel index in the X direction.
     * @param i  pixel index in the Y direction.
     * @return a Ray from the camera through the specified pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
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
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported", e);
        }
    }

    /*
     * Builder class for constructing Camera instances with a fluent API.
     */
    public static class Builder {

        final private Camera camera = new Camera();

        /**
         * Sets the location of the camera.
         *
         * @param p0 the position of the camera.
         * @return the builder instance.
         */
        public Builder setLocation(Point p0) {
            camera.position = p0;
            return this;
        }

        /**
         * Sets the ImageWriter for the camera.
         *
         * @param imageWriter the ImageWriter instance to be used by the camera.
         * @return the builder instance.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            if (imageWriter == null) {
                throw new IllegalArgumentException("ImageWriter cannot be null");
            }
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vUp the up vector.
         * @param vTo the to vector.
         * @return the builder instance.
         * @throws IllegalArgumentException if the up vector and to vector are not orthogonal.
         */
        public Builder setDirection(Vector vTo,Vector vUp ) {
            if (!(Util.isZero(vUp.dotProduct(vTo))))
                throw new IllegalArgumentException("The vectors aren't ortogonal");
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param height the height of the view plane.
         * @param width  the width of the view plane.
         * @return the builder instance.
         * @throws IllegalArgumentException if the height or width is not positive.
         */
        public Builder setVpSize(Double height, Double width) {

            if (height <= 0 || width <= 0)
                throw new IllegalArgumentException("width and height should be positive");
            camera.height = height;
            camera.width = width;
            return this;
        }

        /**
         * Sets the RayTracer for the camera.
         *
         * @param rayTracer the RayTracerBase instance to be used by the camera.
         * @return the builder instance.
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            if (rayTracer == null) {
                throw new IllegalArgumentException("RayTracer cannot be null");
            }
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to the view plane.
         * @return the builder instance.
         * @throws IllegalArgumentException if the distance is not positive.
         */
        public Builder setVpDistance(Double distance) {

            if (distance <= 0)
                throw new IllegalArgumentException("distance should be not negative");
            camera.distance = distance;
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         *
         * @return the constructed Camera instance.
         * @throws MissingResourceException if any of the required parameters are not set.
         */
        public Camera build() {
            String missingRenderingData="missing rendering data";
            String cameraClass="Camera";
            if (camera.position == null)
                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's position");
            if (camera.vTo == null)
                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's vTo");
            if (camera.vUp == null)
                throw new MissingResourceException(missingRenderingData,cameraClass, "camera's vUp");
            if (camera.imageWriter == null)
                throw new MissingResourceException(missingRenderingData,cameraClass, "camera's image Writer");
            if (camera.rayTracer == null)
                throw new MissingResourceException(missingRenderingData,cameraClass, "camera's ray tracer");
            if (Util.alignZero(camera.height) <= 0)
                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's height");
            if (Util.alignZero(camera.width) <= 0)
                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's width");
            if (Util.alignZero(camera.distance) <= 0)
                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's distance");



            camera.vRight = (camera.vTo.crossProduct(camera.vUp)).normalize();

            return camera.clone();
        }

//

//

//
    }
//

//
    /**
     * Renders the image by casting rays through each pixel.
     */
    public Camera renderImage(){
        for (int i=0;i<imageWriter.getNy();i++)
            for (int j=0;j<imageWriter.getNx();j++)
            {

                this.imageWriter.writePixel(j, i, castRay(j,i));
            }
        return this;
    }

    /**
     * Prints a grid on the image with the specified interval and color.
     *
     * @param interval the spacing between grid lines.
     * @param color the color of the grid lines.
     */
    public Camera printGrid(int interval, Color color){
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to the output.
     */
    public void writeToImage(){
        imageWriter.writeToImage();
    }

    private Color castRay(int j, int i) {
        Ray ray = constructRay(this.imageWriter.getNx(),this.imageWriter.getNy(),j,i);
        return this.rayTracer.traceRay(ray);
    }
}
