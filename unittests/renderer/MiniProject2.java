package renderer;

import java.util.Random;
import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.*;
import scene.Scene;
import static java.awt.Color.*;

/**
 * Tests for BVH improvement
 * @author Toiby Glazer and Noa Shuster
 */
public class MiniProject2 {
    /** Camera builder of the tests */
    private final Camera.Builder camera= Camera.getBuilder()
            .setDirection(new Vector(0, 0, 50), new Vector(50, -100, 0))
            .setLocation(new Point(10, -10, -1000))
            .setVpSize(200.0, 200.0)
            .setVpDistance(600.0)
            .setDebugPrint(0.2).setMultiThreading(0);

    /**
     * Produce the picture by boxing manually
     */
    @Test
    public void miniPro2part1() {
        Scene scene = new Scene("Test scene");
        Geometry a = new Sphere(new Point(60, -50, 50), 30).setEmission(new Color(BLUE))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setKT(0.6).setNShininess(30));
        Geometry b = new Sphere(new Point(0, 0, 115), 30).setEmission(new Color(MAGENTA))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(70));
        Geometry c = new Sphere(new Point(0, -100, 70), 15).setEmission(new Color(ORANGE))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setKT(0.5).setNShininess(50));
        Geometry e = new Sphere(new Point(50, -80, 90), 15).setEmission(new Color(GREEN))
                .setMaterial(new Material().setKd(0.5).setKT(0.5).setNShininess(100));
        Geometry g = new Sphere(new Point(-50, -10, 80), 20).setEmission(new Color(CYAN))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(100));
        Geometry d = new Triangle(new Point(-150, 150, 115), new Point(150, 150, 135), new Point(75, -75, 150))
                .setEmission(Color.BLACK).setMaterial(new Material().setKs(1).setNShininess(60));
        Geometry f = new Triangle(new Point(-150, 150, 115), new Point(-70, -70, 140), new Point(75, -75, 150))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setKR(0.25).setNShininess(50));
        Geometry h = new Sphere(new Point(20, -60, 10), 15).setEmission(new Color(DARK_GRAY))
                .setMaterial(new Material().setKd(0.2).setKs(0.5).setKT(0.6).setNShininess(30));
        Geometries geo1 = new Geometries(a, b, c);
        Geometries geo2 = new Geometries(d, e, g);
        Geometries geo3 = new Geometries(geo2, f, geo1, h);
        Geometries geos = new Geometries(geo2, geo3, geo1);
        scene.geometries.add(geos);

        scene.setBackGround(Color.BLACK).setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(new SpotLight(new Color(WHITE), new Point(70, -70, -60), new Vector(-1, 2, 3)).setKc(1)
                .setKl(0.000005).setKq(0.00000005));
        scene.lights.add(new DirectionalLight(new Color(BLACK), new Vector(-10, 20, 30)));
        scene.lights.add(new PointLight(new Color(BLUE), new Point(-30, 70, 140)).setKc(2.5).setKl(0.0000005)
                .setKq(0.0000000005));
        scene.lights.add(new PointLight(new Color(YELLOW), new Point(-50, 50, 100)).setKc(2.5).setKl(0.0000005)
                .setKq(0.0000000005));

        camera.setImageWriter(new ImageWriter("miniPro2part1", 2000, 2000)) //
                .setRayTracer(new SimpleRayTracer(scene).setDistanceGrid(1000))
                .build()//
                .renderImage() //
                .writeToImage();

    }

    private static Scene setupScene(boolean cbr) {
        Scene scene = new Scene("Test scene");
        if (cbr)
            scene.setCBR();

        // Create 10 special spheres and triangles
        scene.geometries.add(
                new Sphere(new Point(60, -50, 50), 20).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setKT(0.6).setNShininess(30)),
                new Sphere(new Point(0, 0, 115), 20).setEmission(new Color(MAGENTA))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(70)),
                new Sphere(new Point(0, -100, 70), 10).setEmission(new Color(ORANGE))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKT(0.5).setNShininess(50)),
                new Sphere(new Point(50, -80, 90), 10).setEmission(new Color(GREEN))
                        .setMaterial(new Material().setKd(0.5).setKT(0.5).setNShininess(100)),
                new Sphere(new Point(-50, -10, 80), 15).setEmission(new Color(CYAN))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(100)),
                new Triangle(new Point(-150, 150, 115), new Point(150, 150, 135), new Point(75, -75, 150))
                        .setEmission(Color.BLACK).setMaterial(new Material().setKd(1).setKs(1).setNShininess(60)),
//           new Triangle(new Point(-150, 150, 115), new Point(-70, -70, 140), new Point(75, -75, 150))
//                 .setEmission(Color.BLACK)
//                 .setMaterial(new Material().setKd(0.5).setKs(0.5).setKr(0.25).setShininess(50)),
                new Sphere(new Point(20, -60, 10), 10).setEmission(new Color(DARK_GRAY))
                        .setMaterial(new Material().setKd(0.2).setKs(0.5).setKT(0.6).setNShininess(30)),
                new Sphere(new Point(0, 60, 115), 7).setEmission(new Color(GRAY))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(70)),
                new Sphere(new Point(20, 60, 115), 7).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setNShininess(70)));

        // Create 25 more spheres
        Random r = new Random();
        Color color = Color.BLACK;
        for (int i = 0; i < 2000; i++) {
            if (i % 2 == 0)
                color = new Color(CYAN);
            if (i % 3 == 0)
                color = new Color(RED);
            if (i % 5 == 0)
                color = new Color(MAGENTA);
            if (i % 6 == 0)
                color = new Color(BLUE);
            if (i % 7 == 0)
                color = new Color(GREEN);
            if (i % 9 == 0)
                color = new Color(ORANGE);
            scene.geometries.add(new Sphere(new Point(r.nextInt(160) - 50, r.nextInt(200) - 80, r.nextInt(40) - 30),
                    r.nextInt(3) + 0.5).setEmission(color)
                    .setMaterial(new Material().setKd(0.5).setKs(0.4).setNShininess(70)));
        }

        scene.setBackGround(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
//     scene.lights.add(new SpotLight(new Color(WHITE), new Point(70, -70, -60), new Vector(-1, 2, 3)).setKc(1)
//           .setKl(0.000005).setKq(0.00000005));
//     scene.lights.add(new DirectionalLight(Color.BLACK, new Vector(-10, 20, 30)));
//
//     scene.lights.add(new PointLight(new Color(BLUE), new Point(-30, 70, 140)).setKc(2.5).setKl(0.0000005)
//           .setKq(0.0000000005));
//     scene.lights.add(new PointLight(new Color(YELLOW), new Point(-50, 50, 100)).setKc(2.5).setKl(0.0000005)
//           .setKq(0.0000000005));
        return scene;
    }

    private static void printCounters(String test) {
        System.out.println(test //
                + "[ checks:" + Intersectable.Box.count //
                + " / positives:" + Intersectable.Box.positives //
                + " / ints:" + Intersectable.count //
                + " / positives:" + Intersectable.positives);
    }

    /**
     * Produce the picture without any improvement
     */
    @Test
    public void miniPro2FlatWithoutMultiThreading() {
        Scene scene = setupScene(false);

        camera.setImageWriter(new ImageWriter("miniPro2part1", 2000, 2000)) //
                .setRayTracer(new SimpleRayTracer(scene).setDistanceGrid(1000))
                .setMultiThreading(0)
                .build()
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce the picture without any improvement
     */
    @Test
    public void miniPro2Test1FlatWithoutCbr() {
        Scene scene = setupScene(false);

        camera.setImageWriter(new ImageWriter("miniPro2part2", 2000, 2000)) //
                .setRayTracer(new SimpleRayTracer(scene).setDistanceGrid(1000))
                .build()//
                .renderImage() //
                .writeToImage();

        printCounters("FlatWithoutCBR");
    }

    /**
     * Produce the picture with CBR improvement
     */
    @Test
    public void miniPro2Test2FlatWithCBR() {
        Scene scene = setupScene(true);

        camera.setImageWriter(new ImageWriter("miniPro2part3", 2000, 2000)) //
                .setRayTracer(new SimpleRayTracer(scene).setDistanceGrid(1000))
                .build()
                .renderImage() //
                .writeToImage();

        printCounters("FlatWithCBR");
    }

    /**
     * Produce the picture with CBR + BVH improvement
     */
    @Test
    public void miniPro2Test3WithCbrAndAutoBvh() {
        Scene scene = setupScene(true).setBVH();

        camera.setImageWriter(new ImageWriter("miniPro2part4", 2000, 2000)) //
                .setRayTracer(new SimpleRayTracer(scene).setDistanceGrid(1000))
                .build()//
                .renderImage() //
                .writeToImage();

        printCounters("FlatWithBVH");
    }

}