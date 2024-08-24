//package renderer;
//
//import static primitives.Util.isZero;
//import java.util.LinkedList;
//import java.util.MissingResourceException;
//import primitives.Color;
//import primitives.Point;
//import primitives.Ray;
//import primitives.Vector;
//import renderer.PixelManager.Pixel;
//import primitives.*;
//
///*
// * Camera class represents a camera in 3D space with position, direction, and view plane properties.
// * It provides functionality to construct rays through a view plane.
// *
// * @author Efrat and Moy
// */
//public class Camera implements Cloneable{
//
//    private Point position;
//    private Vector vUp;
//    private Vector vRight;
//    private Vector vTo;
//
//    //view plane
//    private Double height = 0.0;
//    private Double width = 0.0;
//    private Double distance = 0.0;
//
//    private ImageWriter imageWriter;
//    private RayTracerBase rayTracer;
//    private static int threadsCount = 0;
//    private double printInterval = 0;
//    private PixelManager pixelManager;
//    private int antiAliasingFactor = 1;
//
//    /**
//     * Private constructor to enforce the use of the builder for creating Camera instances.
//     */
//    private Camera() {
//    }
//
//    public Camera(Point p0, Vector vTo, Vector vUp) {
//        if (!isZero(vUp.dotProduct(vTo)))
//            throw new IllegalArgumentException("Error: vUp and vTo are not orthogonal");
//
//        this.position = p0;
//        this.vUp = vUp.normalize();
//        this.vTo = vTo.normalize();
//        this.vRight = this.vTo.crossProduct(this.vUp);
//    }
//
//    /**
//     * Gets the position of the camera.
//     *
//     * @return the position of the camera.
//     */
//    public Point getPosition() {
//        return position;
//    }
//
//    /**
//     * Gets the up vector of the camera.
//     *
//     * @return the up vector of the camera.
//     */
//    public Vector getvUp() {
//        return vUp;
//    }
//
//    /**
//     * Gets the right vector of the camera.
//     *
//     * @return the right vector of the camera.
//     */
//    public Vector getvRight() {
//        return vRight;
//    }
//
//    /**
//     * Gets the to vector of the camera.
//     *
//     * @return the to vector of the camera.
//     */
//    public Vector getvTo() {
//        return vTo;
//    }
//
//    /**
//     * Gets the height of the view plane.
//     *
//     * @return the height of the view plane.
//     */
//    public Double getHeight() {
//        return height;
//    }
//
//    /**
//     * Gets the width of the view plane.
//     *
//     * @return the width of the view plane.
//     */
//    public Double getWidth() {
//        return width;
//    }
//
//    /**
//     * Gets the distance from the camera to the view plane.
//     *
//     * @return the distance from the camera to the view plane.
//     */
//    public Double getDistance() {
//        return distance;
//    }
//
//    /**
//     * Creates a new builder for Camera.
//     *
//     * @return a new Camera.Builder instance.
//     */
//    public static Builder getBuilder() {
//        return new Builder();
//    }
//
//    /**
//     * Constructs a ray through a specific pixel in the view plane.
//     *
//     * @param nX number of pixels in the X direction.
//     * @param nY number of pixels in the Y direction.
//     * @param j  pixel index in the X direction.
//     * @param i  pixel index in the Y direction.
//     * @return a Ray from the camera through the specified pixel.
//     */
//    public Ray constructRay(int nX, int nY, int j, int i) {
//        Double rY = height / nY;
//        Double rX = width / nX;
//        Point pixelIJ = position.add(vTo.scale(distance));
//
//        Double yI = -(i - (nY - 1) / 2.0) * rY;
//        Double xJ = (j - (nX - 1) / 2.0) * rX;
//        // Check if the pixel is at the center of the view plane
//        if (isZero(xJ) && isZero(yI)) {
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Check if the pixel is on the horizontal axis of the view plane
//        if (isZero(xJ)) {
//            pixelIJ = pixelIJ.add(vUp.scale(yI));
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Check if the pixel is on the vertical axis of the view plane
//        if (isZero(yI)) {
//            pixelIJ = pixelIJ.add(vRight.scale(xJ));
//            return new Ray(position, pixelIJ.subtract(position));
//        }
//
//        // Calculate the final point on the view plane for the specified pixel
//        pixelIJ = pixelIJ.add(vRight.scale(xJ).add(vUp.scale(yI)));
//
//        // Return the constructed ray from the camera's location to the calculated point on the view plane
//        return new Ray(position, pixelIJ.subtract(position));
//
//    }
//
//    @Override
//    public Camera clone() {
//        try {
//            return (Camera) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException("Cloning not supported", e);
//        }
//    }
//
//
//    /*
//     * Builder class for constructing Camera instances with a fluent API.
//     */
//    public static class Builder {
//
//        final private Camera camera = new Camera();
//
//        /**
//         *
//         * Sets the number of threads for multi-threading in the Camera.
//         *
//         * @param threads the number of threads to be set
//         * @return The Camera object itself (for method chaining)
//         * @throws IllegalArgumentException if the number of threads is negative
//         */
//        public Builder setMultiThreading(int threads) {
//            if (threads < 0)
//                throw new IllegalArgumentException("number of threads must not be negative");
//            threadsCount = threads;
//            return this;
//        }
//
//        /**
//         * Sets the location of the camera.
//         *
//         * @param p0 the position of the camera.
//         * @return the builder instance.
//         */
//        public Builder setLocation(Point p0) {
//            camera.position = p0;
//            return this;
//        }
//
//        /**
//         * Sets the ImageWriter for the camera.
//         *
//         * @param imageWriter the ImageWriter instance to be used by the camera.
//         * @return the builder instance.
//         */
//        public Builder setImageWriter(ImageWriter imageWriter) {
//            if (imageWriter == null) {
//                throw new IllegalArgumentException("ImageWriter cannot be null");
//            }
//            camera.imageWriter = imageWriter;
//            return this;
//        }
//
//        /**
//         * Sets the direction vectors of the camera.
//         *
//         * @param vUp the up vector.
//         * @param vTo the to vector.
//         * @return the builder instance.
//         * @throws IllegalArgumentException if the up vector and to vector are not orthogonal.
//         */
//        public Builder setDirection(Vector vTo,Vector vUp ) {
//            if (!(isZero(vUp.dotProduct(vTo))))
//                throw new IllegalArgumentException("The vectors aren't ortogonal");
//            camera.vUp = vUp.normalize();
//            camera.vTo = vTo.normalize();
//            return this;
//        }
//
//        public Builder setAntiAliasingFactor(int antiAliasingFactor) {
//            camera.antiAliasingFactor = antiAliasingFactor;
//            return this;
//        }
//
//        /**
//         * Sets the size of the view plane.
//         *
//         * @param height the height of the view plane.
//         * @param width  the width of the view plane.
//         * @return the builder instance.
//         * @throws IllegalArgumentException if the height or width is not positive.
//         */
//        public Builder setVpSize(Double height, Double width) {
//
//            if (height <= 0 || width <= 0)
//                throw new IllegalArgumentException("width and height should be positive");
//            camera.height = height;
//            camera.width = width;
//            return this;
//        }
//
//        /**
//         * Sets the RayTracer for the camera.
//         *
//         * @param rayTracer the SimpleRayTracer instance to be used by the camera.
//         * @return the builder instance.
//         */
//        public Builder setRayTracer(SimpleRayTracer rayTracer) {
//            if (rayTracer == null) {
//                throw new IllegalArgumentException("RayTracer cannot be null");
//            }
//            camera.rayTracer = rayTracer;
//            return this;
//        }
//
//        /**
//         * Sets the distance from the camera to the view plane.
//         *
//         * @param distance the distance to the view plane.
//         * @return the builder instance.
//         * @throws IllegalArgumentException if the distance is not positive.
//         */
//        public Builder setVpDistance(Double distance) {
//            if (distance <= 0)
//                throw new IllegalArgumentException("distance should be not negative");
//            camera.distance = distance;
//            return this;
//        }
//
//        /**
//         *
//         * Sets the debug print interval for the Camera.
//         *
//         * @param interval the debug print interval to be set
//         * @return the updated Camera object
//         * @throws IllegalArgumentException if the print interval is negative
//         */
//        public Camera setDebugPrint(double interval) {
//            if (interval < 0)
//                throw new IllegalArgumentException("print interval must not be negative");
//            camera.printInterval = interval;
//            return this.build();
//        }
//
//        public Camera build() {
//            String missingRenderingData = "missing rendering data";
//            String cameraClass = "Camera";
//            if (camera.position == null)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's position");
//            if (camera.vTo == null)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's vTo");
//            if (camera.vUp == null)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's vUp");
//            if (camera.imageWriter == null)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's image Writer");
//            if (camera.rayTracer == null)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's ray tracer");
//            if (Util.alignZero(camera.height) <= 0)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's height");
//            if (Util.alignZero(camera.width) <= 0)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's width");
//            if (Util.alignZero(camera.distance) <= 0)
//                throw new MissingResourceException(missingRenderingData, cameraClass, "camera's distance");
//            camera.vRight = (camera.vTo.crossProduct(camera.vUp)).normalize();
//
//            return camera.clone();
//        }
//    }
////
////    public Camera renderImage() {
////        // pixelManager = new PixelManager(imageWriter.getNy(), imageWriter.getNx(), printInterval);
////        PixelManager.initialize(imageWriter.getNy(), imageWriter.getNx(), printInterval);
////        if (threadsCount == 0) {
////            for (int i = 0; i < imageWriter.getNy(); i++)
////                for (int j = 0; j < imageWriter.getNx(); j++) {
////
////                    this.imageWriter.writePixel(j, i, castRay(j, i));
////                }
////        }
////        else {
////            while (threadsCount-- > 0) {
////                new Thread(() -> {
////                    // Iterate over each pixel in the image
////                    for (PixelManager pixel=new PixelManager(); pixel.nextPixel(); PixelManager.pixelDone()) {
////                        // Apply adaptive super-sampling to determine the pixel color
////                        if (useAdaptive){
////                            Color pixelColorAdaptive = SuperSampling(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row, antiAliasingFactor, false);
////                            imageWriter.writePixel(pixel.col, pixel.row, pixelColorAdaptive);
////                        }
////                        else{
////                            // Construct rays for the current pixel and trace them using the ray tracer
////                            List<Ray> rays = constructRays(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row);
////                            Color pixelColor = rayTracer.traceRays(rays);
////                            imageWriter.writePixel(pixel.col, pixel.row, pixelColor);
////                        }
////                    }
////                }).start();
////            }
////            // Wait for all the threads to finish processing the pixels
////            PixelManager.waitToFinish();
////        }
////        return this;
////    }
////private Color SuperSampling(int nX, int nY, int j, int i,  int numOfRays, boolean adaptiveAlising)  {
////    // Get the right and up vectors of the camera
////    Vector Vright = vRight;
////    Vector Vup = vUp;
////    // Get the location of the camera
////    Point cameraLoc =position;
////    // Calculate the number of rays in each row and column
////    int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));
////    // If the number of rays is 1, perform regular ray tracing
////    if(numOfRaysInRowCol == 1)
////        return rayTracer.traceRay(constructRayThroughPixel(nX, nY, j, i));
////    // Calculate the center point of the current pixel
////    Point pIJ = findPixelLocation(nX, nY, j, i);///////////////////////////////////////
////    // Calculate the height and width ratios of the pixel
////    double rY = Util.alignZero(height / nY);
////    double rX = Util.alignZero(width / nX);
////
////    // Calculate the pixel row and column ratios
////    double PRy = rY/numOfRaysInRowCol;
////    double PRx = rX/numOfRaysInRowCol;
////
////    if (adaptiveAlising)
////        return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
////    else
////        return rayTracer.RegularSuperSampling(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
////}
//
//    /**
//     * Renders the image by casting rays
//     *
//     * @throws MissingResourceException if any required field is null
//     * @return The Camera object itself (for method chaining)
//     */
//    public Camera renderImage() {
//        if (position == null || vUp == null || vTo == null || vRight == null || imageWriter == null || rayTracer == null)
//            throw new MissingResourceException("All the camera's fields mustn't be null", "Camera", null);
//        // throw new UnsupportedOperationException();
//        int nX = imageWriter.getNx();
//        int nY = imageWriter.getNy();
//        pixelManager = new PixelManager(nY, nX, printInterval);
//        if (threadsCount == 0)// without multi threading improvement
//            for (int i = 0; i < nY; i++)
//                for (int j = 0; j < nX; j++)
//                    // imageWriter.writePixel(j, i, castRay(nX, nY, j, i));
//                    castRay(nX, nY, j, i);
//        else { // multi threading
//            var threads = new LinkedList<Thread>(); // list of threads
//            while (threadsCount-- > 0) // add appropriate number of threads
//                threads.add(new Thread(() -> { // add a thread with its code
//                    Pixel pixel; // current pixel(row,col)
//                    // allocate pixel(row,col) in loop until there are no more pixels
//                    while ((pixel = pixelManager.nextPixel()) != null)
//                        // cast ray through pixel (and color it – inside castRay)
//                        castRay(nX, nY, pixel.col(), pixel.row());
//                }));
//            // start all the threads
//            for (var thread : threads)
//                thread.start();
//            // wait until all the threads have finished
//            for (var thread : threads)
//                try {
//                    thread.join();
//                } catch (InterruptedException ignore) {
//                }
//        }
//        return this;
//    }
//
//    /**
//     * Prints a grid on the image with the specified interval and color.
//     *
//     * @param interval the spacing between grid lines.
//     * @param color the color of the grid lines.
//     */
//    public Camera printGrid(int interval, Color color){
//        if (imageWriter == null)
//            throw new MissingResourceException("The render's field imageWriter mustn't be null", //
//                    "ImageWriter", "imageWriter");
//
//        int nY = imageWriter.getNy();
//        int nX = imageWriter.getNx();
//
//        if (nX % interval != 0 || nY % interval != 0)
//            throw new IllegalArgumentException(
//                    "The grid is supposed to have squares, therefore the given interval must be a divisor of both nX and nY");
//
//        for (int i = 0; i < nY; i++)
//            for (int j = 0; j < nX; j++)
//                if (i % interval == 0 || j % interval == 0)
//                    imageWriter.writePixel(j, i, color);
//        return this;
//    }
//
//
//    /**
//     * Writes the image to the output.
//     */
//    public void writeToImage(){
//        imageWriter.writeToImage();
//    }
//
////    private Color castRay(int j, int i) {
////        Ray ray = constructRay(this.imageWriter.getNx(),this.imageWriter.getNy(),j,i);
////        return this.rayTracer.traceRay(ray);
////    }
//
//    private void castRay(int nX, int nY, int col, int row) {
//        imageWriter.writePixel(col, row, rayTracer.traceRay(constructRay(nX, nY, col, row)));
//        pixelManager.pixelDone();
//    }
//}
package renderer;
import java.util.LinkedList;
import renderer.PixelManager.Pixel;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.MissingResourceException;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera implements Cloneable {
    /**
     * The point where the Camera is located.
     */
    Point location = null;

    /**
     * Vector to the right of the Camera, up, and where it was pointing.
     */
    Vector vUp = null, vTo = null, vRight = null;

    /**
     * The height of the view plane, the width of the view plane, and its distance from the camera.
     */
    double viewPlaneH = 0.0, viewPlaneW = 0.0, viewPlaneD = 0.0;

    /**
     * Intended for creating the image file
     */
    private ImageWriter imageWriter;
    /**
     * Intended for dyeing the rays.
     */
    private RayTracerBase rayTrace;
    /** Num of threads */
    private int threadsCount = 0;
    /** The Interval */
    private double printInterval = 0;
    /** The pixelManager */
    private PixelManager pixelManager;

    /**
     * empty constructor
     */
    private Camera() {
    }

    /**
     * getter for Camera location.
     *
     * @return Camera location.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * getter for above the Camera.
     *
     * @return above the Camera.
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * getter for Camera direction.
     *
     * @return Camera direction.
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * getter for view plane height.
     *
     * @return view plane height.
     */
    public double getViewPlaneH() {
        return viewPlaneH;
    }

    /**
     * getter for view plane width.
     *
     * @return view plane width.
     */
    public double getViewPlaneW() {
        return viewPlaneW;
    }

    /**
     * getter for view plane distance.
     *
     * @return view plane distance.
     */
    public double getViewPlaneD() {
        return viewPlaneD;
    }

    /**
     * Returns a Builder object for constructing a Camera.
     *
     * @return A new Builder object.
     */
    public static Builder getBuilder() {
        return new Builder();
    }
    /**
     *
     * Constructs a ray through a specified pixel on the view plane.
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x coordinate of the pixel
     * @param i  the y coordinate of the pixel
     * @return the ray passing through the specified pixel
     */

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pc = location.add(vTo.scale(viewPlaneD));     // center of the view plane
        double Ry = viewPlaneH / (double) nY;                  // Ratio - pixel height
        double Rx = viewPlaneW / (double) nX;                  // Ratio - pixel width

        double yJ = alignZero(-(i - ((double) nY - 1) / 2) * Ry);       // move pc Yi pixels
        double xJ = alignZero((j - ((double) nX - 1) / 2) * Rx);        // move pc Xj pixels

        Point PIJ = pc;
        if (!isZero(xJ)) PIJ = PIJ.add(vRight.scale(xJ));
        if (!isZero(yJ)) PIJ = PIJ.add(vUp.scale(yJ));

        return new Ray(location, PIJ.subtract(location));
    }

    /**
     * Cast ray from camera and color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int col, int row) {
        imageWriter.writePixel(col, row, rayTrace.traceRay(constructRay(nX, nY, col, row)));
        pixelManager.pixelDone();
    }


    /**
     * Renders the image by casting rays
     *
     * @throws MissingResourceException if any required field is null
     */

    public Camera renderImage() {
        if (this.rayTrace == null || this.imageWriter == null || this.viewPlaneW == 0 || this.viewPlaneH == 0 || this.viewPlaneD == 0)
            throw new UnsupportedOperationException("MissingResourcesException");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        pixelManager = new PixelManager(nY, nX, printInterval);
        if (threadsCount == 0)// without multi threading improvement
            for (int i = 0; i < nY; i++)
                for (int j = 0; j < nX; j++)
                    // imageWriter.writePixel(j, i, castRay(nX, nY, j, i));
                    castRay(nX, nY, j, i);
        else { // multi threading
            var threads = new LinkedList<Thread>(); // list of threads
            while (threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // cast ray through pixel (and color it – inside castRay)
                        castRay(nX, nY, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads)
                thread.start();
            // wait until all the threads have finished
            for (var thread : threads)
                try {
                    thread.join();
                } catch (InterruptedException ignore) {
                }
        }
        return this;
    }



    /* @param interval The interval between grid lines.
     * @param color    The color of the grid lines.
     *
     * @throws MissingResourceException if the imageWriter field is null.
     * @throws IllegalArgumentException if the interval is not a divisor of both nX
     *                                  and nY.
     */
    public Camera printGrid(int interval, Color color) throws MissingResourceException, IllegalArgumentException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);

        if (this.imageWriter.getNx() % interval != 0 || this.imageWriter.getNy() % interval != 0)
            throw new IllegalArgumentException("The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny");

        for (int i = 0; i < this.imageWriter.getNx(); i++)
            for (int j = 0; j < this.imageWriter.getNy(); j++)
                if (i % interval == 0 || (i + 1) % interval == 0 || j % interval == 0 || (j + 1) % interval == 0)
                    this.imageWriter.writePixel(i, j, color);
        return this;
    }

    /**
     *
     * Creates the image by writing it to the imageWriter. This function delegates
     * the function writeToImage of imageWriter class
     *
     * @throws MissingResourceException if the imageWriter field is null.
     */

    public Camera writeToImage() throws MissingResourceException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
        this.imageWriter.writeToImage();
        return this;
    }
    /**
     * inner class
     */
    public static class Builder {

        final Camera camera = new Camera();

        /**
         * Set the location of the camera.
         *
         * @param location the location point of the camera.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the location is null
         */
        public Builder setLocation(Point location) throws IllegalArgumentException{
            if (location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            camera.location = location;
            return this;
        }

        /**
         * Set the direction vectors of the camera.
         *
         * @param vTo the forward direction vector.
         * @param vUp the up direction vector.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the vectors are null or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) throws IllegalArgumentException{
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null");
            }
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Set the size of the view plane.
         * @param width the width of the view plane.
         * @param height the height of the view plane.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the dimensions aren't positive
         */
        public Builder setVpSize(double width, double height) throws IllegalArgumentException  {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be positive");
            }
            camera.viewPlaneW = width;
            camera.viewPlaneH = height;
            return this;
        }

        /**
         * Set the distance between the camera and the view plane.
         * @param distance the distance between the camera and the view plane.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the distance is not positive
         */
        public Builder setVpDistance(double distance) throws IllegalArgumentException {
            if (distance <= 0) {
                throw new IllegalArgumentException("View plane distance must be positive");
            }
            camera.viewPlaneD = distance;
            return this;
        }

        /**
         * setter for image writer.
         *
         * @param imageWriter image writer.
         * @return the Camera.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter= imageWriter;
            return this;
        }
        /**
         *
         * Sets the number of threads for multi-threading in the Camera.
         *
         * @param threads the number of threads to be set
         * @return The Camera object itself (for method chaining)
         * @throws IllegalArgumentException if the number of threads is negative
         */
        public Builder setMultiThreading(int threads) {
            if (threads < 0)
                throw new IllegalArgumentException("number of threads must not be negative");
            camera.threadsCount = threads;
            return this;
        }

        /**
         *
         * Sets the debug print interval for the Camera.
         *
         * @param interval the debug print interval to be set
         * @return the updated Camera object
         * @throws IllegalArgumentException if the print interval is negative
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0)
                throw new IllegalArgumentException("print interval must not be negative");
            camera.printInterval = interval;
            return this;
        }
        /**
         * Setter for ray tracer base.
         *
         * @param rayTracerBase Ray tracer base.
         * @return The Camera.
         */
        public Builder setRayTracer(RayTracerBase rayTracerBase) {
            camera.rayTrace = rayTracerBase;
            return this;
        }
        /**
         * Builds the Camera object.
         * @return the constructed Camera object.
         * @throws MissingResourceException if any required field is missing.
         */
        public Camera build() throws MissingResourceException {

            String missingData = "Missing rendering data";

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (camera.location == null) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "location");
            }
            if (camera.vTo == null || camera.vUp == null || camera.vRight == null) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "direction vectors");
            }
            if (camera.viewPlaneW == 0.0 || camera.viewPlaneH == 0.0) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "view plane size");
            }
            if (camera.viewPlaneD == 0.0) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "view plane distance");
            }
            if(camera.imageWriter == null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "image_writer");
            if(camera.rayTrace==null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "rayTrace");
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}