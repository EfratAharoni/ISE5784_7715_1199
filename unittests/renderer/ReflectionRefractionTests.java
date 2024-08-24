package renderer;

import static java.awt.Color.*;

import geometries.Plane;
import lighting.DirectionalLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;
import geometries.Plane;
import lighting.DirectionalLight;

/** Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Moy & Efrat */
public class ReflectionRefractionTests {
   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()
           .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
           .setRayTracer(new SimpleRayTracer(scene).setGridResolution(9).setSoftShadows(true));

   /**
    * Produce a picture of a sphere lighted by spot lights
    */
   @Test
   public void twoSpheres() {
      scene.geometries.add(
              new Sphere(new Point(0, 0, -50) ,50d ).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKd(0.4).setKs(0.3).setNShininess(100).setKT(0.3)),
              new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(100)));
      scene.lights.add(
              new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                      .setKl(0.0004).setKq(0.0000006));

      cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000d)
              .setVpSize(150d, 150d)
              .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
              .build()
              .renderImage()
              .writeToImage();
   }

   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   public void twoSpheresOnMirrors() {
      scene.geometries.add(
              new Sphere( new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100))
                      .setMaterial(new Material().setKd(0.25).setKs(0.25).setNShininess(20)
                              .setKT(new Double3(0.5, 0, 0))),
              new Sphere( new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20))
                      .setMaterial(new Material().setKd(0.25).setKs(0.25).setNShininess(20)),
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                      new Point(670, 670, 3000))
                      .setEmission(new Color(20, 20, 20))
                      .setMaterial(new Material().setKR(1)),
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                      new Point(-1500, -1500, -2000))
                      .setEmission(new Color(20, 20, 20))
                      .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
              .setKl(0.00001).setKq(0.000005));

      cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000d)
              .setVpSize(2500d, 2500d)
              .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
              .build()
              .renderImage()
              .writeToImage();
   }

   /**
    * Produce a picture of two triangles lighted by a spot lights with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   public void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(60)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(60)),
              new Sphere( new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setNShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                      .setKl(4E-5).setKq(2E-7));

      cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000d)
              .setVpSize(200d, 200d)
              .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
              .build()
              .renderImage()
              .writeToImage();
   }

   @Test
   public void bonusTest() {
      // Adding a green ground plane and a blue background plane to the scene
      scene.geometries.add(
              // Green ground plane
              new Plane(new Point(0, 0, -10), new Vector(0, 0, 1)) // Slightly below the objects
                      .setEmission(new Color(0, 255, 0)) // Green color
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30)),
              // Blue background plane
              new Plane(new Point(0, -200, 0), new Vector(0, 1, 0)) // Farther in the -Y direction
                      .setEmission(new Color(37, 144, 200)) // Light blue color
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
      );

      // Adding a sphere to the scene

      scene.geometries.add(
              new Sphere( new Point(-100, 0, 100), 30d)
                      .setEmission(new Color(yellow))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

      scene.geometries.add(
              new Sphere( new Point(100, 0, 100), 10d)
                      .setEmission(new Color(WHITE))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

      scene.geometries.add(
              new Sphere( new Point(80, 0, 100), 10d)
                      .setEmission(new Color(WHITE))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

      scene.geometries.add(
              new Sphere( new Point(90, 0, 115), 10d)
                      .setEmission(new Color(WHITE))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );


      // Parameters for the flower
      double flowerCenterRadius = 5;
      double petalRadius = 5;
      double petalDistance = 8; // Reduced distance between petals
      Color flowerCenterColor = new Color(255, 215, 0); // Yellow color for flower center
      Color petalColor = new Color(pink); // Red color for petals
      Color stemColor = new Color(34, 139, 34); // Green color for stem

      // Position of the flower relative to the existing sphere
      double flowerXOffset = -50;
      double flowerYOffset = 50; // Same as before
      double flowerZOffset = 30; // Raised position to be above the plane

      // Adding the center of the flower
      Point flowerCenterPosition = new Point(flowerXOffset, flowerYOffset, flowerZOffset + flowerCenterRadius);
      scene.geometries.add(
              new Sphere( flowerCenterPosition, flowerCenterRadius)
                      .setEmission(flowerCenterColor)
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
      );

      // Adding petals around the center of the flower
      for (int i = 0; i < 6; i++) {
         double angle = Math.PI / 3 * i;
         double petalX = flowerXOffset + Math.cos(angle) * petalDistance;
         double petalY = flowerYOffset;
         double petalZ = flowerZOffset + flowerCenterRadius + Math.sin(angle) * petalDistance;
         Point petalPosition = new Point(petalX, petalY, petalZ);

         scene.geometries.add(
                 new Sphere( petalPosition, petalRadius)
                         .setEmission(petalColor)
                         .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
         );
      }

      // Adding the stem below the flower
      double stemHeight = 50; // Height of the stem
      double segmentHeight = 2.5; // Reduced height of each segment of the stem
      for (int i = 0; i < stemHeight / segmentHeight; i++) {
         double stemX = flowerXOffset;
         double stemY = flowerYOffset;
         double stemZ = flowerZOffset + flowerCenterRadius - (i + 1) * segmentHeight;
         Point stemPosition = new Point(stemX, stemY, stemZ);

         scene.geometries.add(
                 new Sphere( stemPosition, 1.5) // Radius of the stem segment
                         .setEmission(stemColor)
                         .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
         );
      }

      // Setting ambient light
      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

      // Adding a spotlight to the scene, positioned above the plane
      scene.lights.add(
              new PointLight(new Color(255, 255, 255), new Point(0, -60, 160)).setRadius(40)
                      .setKl(4E-5).setKq(2E-7)
      );

      // Setting the camera location above the plane and directing it downwards
      cameraBuilder
              .setLocation(new Point(800, 800, 200))
              .setDirection(new Vector(-100, -100, -20), new Vector(-164.8, -181.09, 1729.45))
              .setVpSize(200.0, 200.0)
              .setVpDistance(1000.0)
              .setImageWriter(new ImageWriter("flower", 1024, 1024))
              .build()
              .renderImage()
              .writeToImage();
   }


   @Test
   public void softShadowsTest() {
      // Adding a green ground plane and a blue background plane to the scene
      scene.geometries.add(
              // Green ground plane
              new Plane(new Point(0, 0, -10), new Vector(0, 0, 1)) // Slightly below the objects
                      .setEmission(new Color(0, 255, 0)) // Green color
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30)),
              // Blue background plane
              new Plane(new Point(0, -200, 0), new Vector(0, 1, 0)) // Farther in the -Y direction
                      .setEmission(new Color(135, 206, 250)) // Light blue color
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
      );

      // Adding a sphere to the scene
      scene.geometries.add(
              new Sphere( new Point(0, 0, 30), 30d)
                      .setEmission(new Color(80, 80, 200))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

      scene.geometries.add(
              new Sphere( new Point(-100, 0, 30), 30d)
                      .setEmission(new Color(yellow))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

      scene.geometries.add(
              new Sphere( new Point(100, 0, 30),30d)
                      .setEmission(new Color(red))
                      .setMaterial(new Material().setKd(0.4).setKs(0.8).setNShininess(100))
      );

//      // Parameters for the flower
//      double flowerCenterRadius = 5;
//      double petalRadius = 5;
//      double petalDistance = 8; // Reduced distance between petals
//      Color flowerCenterColor = new Color(255, 215, 0); // Yellow color for flower center
//      Color petalColor = new Color(pink); // Red color for petals
//      Color stemColor = new Color(34, 139, 34); // Green color for stem
//
//      // Position of the flower relative to the existing sphere
//      double flowerXOffset = -50;
//      double flowerYOffset = 50; // Same as before
//      double flowerZOffset = 30; // Raised position to be above the plane
//
//      // Adding the center of the flower
//      Point flowerCenterPosition = new Point(flowerXOffset, flowerYOffset, flowerZOffset + flowerCenterRadius);
//      scene.geometries.add(
//              new Sphere( flowerCenterRadius,flowerCenterPosition)
//                      .setEmission(flowerCenterColor)
//                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
//      );
//
//      // Adding petals around the center of the flower
//      for (int i = 0; i < 6; i++) {
//         double angle = Math.PI / 3 * i;
//         double petalX = flowerXOffset + Math.cos(angle) * petalDistance;
//         double petalY = flowerYOffset;
//         double petalZ = flowerZOffset + flowerCenterRadius + Math.sin(angle) * petalDistance;
//         Point petalPosition = new Point(petalX, petalY, petalZ);
//
//         scene.geometries.add(
//                 new Sphere( petalRadius,petalPosition)
//                         .setEmission(petalColor)
//                         .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
//         );
//      }
//
//      // Adding the stem below the flower
//      double stemHeight = 50; // Height of the stem
//      double segmentHeight = 2.5; // Reduced height of each segment of the stem
//      for (int i = 0; i < stemHeight / segmentHeight; i++) {
//         double stemX = flowerXOffset;
//         double stemY = flowerYOffset;
//         double stemZ = flowerZOffset + flowerCenterRadius - (i + 1) * segmentHeight;
//         Point stemPosition = new Point(stemX, stemY, stemZ);
//
//         scene.geometries.add(
//                 new Sphere( 1.5,stemPosition) // Radius of the stem segment
//                         .setEmission(stemColor)
//                         .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(30))
//         );
//      }

      // Setting ambient light
      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

      // Adding a spotlight to the scene, positioned above the plane
      scene.lights.add(
              new PointLight(new Color(255, 255, 255), new Point(0, -60, 160)).setRadius(40)
                      .setKl(4E-5).setKq(2E-7)
      );

      // Setting the camera location above the plane and directing it downwards
      cameraBuilder
              .setLocation(new Point(800, 800, 200))
              .setDirection(new Vector(-100, -100, -20), new Vector(-164.8, -181.09, 1729.45))
              .setVpSize(200.0, 200.0)
              .setVpDistance(1000.0)
              .setImageWriter(new ImageWriter("softShadow", 1024, 1024))
              .build()
              .renderImage()
              .writeToImage();
   }
}
