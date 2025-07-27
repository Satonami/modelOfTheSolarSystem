package com.example.demo1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.RadialGradient;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

abstract class CelestialBody {
    protected String name;
    protected double radius;
    protected double mass;
    protected Orbit orbit;
    protected Circle visualRepresentation;
    protected Paint color;

    public CelestialBody(String name, double mass, double radius, Orbit orbit, Paint color) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.orbit = orbit;
        this.color = color;
    }

    public abstract void updatePosition();

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Circle getVisualRepresentation() {
        return visualRepresentation;
    }
}

class SolarSystem {
    private ArrayList<Planet> planets;

    public SolarSystem() {
        this.planets = new ArrayList<>();
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void update() {
        // Обновление орбит планет, спутников и поясов астероидов
        for (Planet planet : planets) {
            planet.updatePosition();
        }
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }
}

class Sun extends CelestialBody {
    public Sun(String name, double mass, double radius, Paint color) {
        super(name, mass, radius, null, color);
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void updatePosition() {
        // Солнце неподвижно
    }
}

class Planet extends CelestialBody  {
    private double speed;
    private Circle visualRepresentation;
    private ArrayList<Ellipse> rings;
    private double sceneWidth;
    private double sceneHeight;

    public Planet(String name, double mass, double radius, Orbit orbit, double speed, Paint color, double sceneWidth, double sceneHeight) {
        super(name, mass, radius, orbit, color);
        this.speed = speed;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.rings = new ArrayList<>();
    }

    public Orbit getOrbit() {
        return orbit;
    }

    public double getMass(){
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Circle createCircleRepresentation(double planetRadius) {
        visualRepresentation = new Circle(orbit.getSemiMajorAxis(), 400, planetRadius);
        visualRepresentation.setFill(color);
        return visualRepresentation;
    }

    public void addRing(Ellipse ring) {
        rings.add(ring);
    }

    @Override
    public void updatePosition() {
        orbit.updatePosition(speed);
        double angle = Math.toRadians(orbit.getAngle());

        double a = orbit.getSemiMajorAxis();
        double b = a * Math.sqrt(1 - Math.pow(orbit.getEccentricity(), 2));

        double x = sceneWidth / 2 + a * Math.cos(angle);
        double y = sceneHeight / 2 + b * Math.sin(angle);

        if (visualRepresentation == null) {
            visualRepresentation = new Circle(x, y, radius, color);
        } else {
            visualRepresentation.setCenterX(x);
            visualRepresentation.setCenterY(y);
        }

        for (Ellipse ring : rings) {
            ring.setCenterX(x);
            ring.setCenterY(y);
        }
    }

    public Circle getVisualRepresentation() {
        return visualRepresentation;
    }

    public String getName() {
        return name;
    }
}

class Orbit {
    private double semiMajorAxis;
    private double eccentricity;
    private double angle;

    public Orbit(double semiMajorAxis, double eccentricity) {
        this.semiMajorAxis = semiMajorAxis;
        this.eccentricity = eccentricity;
        this.angle = 0;
    }

    public void updatePosition(double speed) {
        angle += speed;
        if (angle > 360) {
            angle = 0;
        }
    }

    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public double getAngle() {
        return angle;
    }
}

class Moon extends CelestialBody {
    public Moon(String name, double mass, Orbit orbit, double radius, Paint color) {
        super(name, mass, radius, orbit, color);
    }

    public double getRadius() {
        return radius;
    }

    public void updatePosition() {
        orbit.updatePosition(0.6);
    }

    public Circle createCircleRepresentation(double planetRadius) {
        visualRepresentation = new Circle(orbit.getSemiMajorAxis(), 400, planetRadius);
        visualRepresentation.setFill(color);
        return visualRepresentation;
    }

    public Circle getVisualRepresentation() {
        return visualRepresentation;
    }
}

class Star {
    private double x, y;
    private double size;
    private Color color;

    public Star(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public Circle createCircleRepresentation() {
        Circle star = new Circle(x, y, size);
        star.setFill(color);
        return star;
    }
}

class AsteroidBelt {
    private ArrayList<Circle> asteroids;
    private double screenWidth;
    private double screenHeight;

    public AsteroidBelt(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.asteroids = new ArrayList<>();
    }

    public void createAsteroids(int numAsteroids) {
        for (int i = 0; i < numAsteroids; i++) {
            double randomRadius = (Math.random() * 0.07 + 0.4) * Math.min(screenWidth, screenHeight);
            double angle = Math.random() * 360;
            double x = screenWidth / 2 + randomRadius * Math.cos(Math.toRadians(angle));
            double y = screenHeight / 2 + randomRadius * Math.sin(Math.toRadians(angle));
            Circle asteroid = new Circle(x, y, 2);
            asteroid.setFill(Color.LINEN);
            asteroids.add(asteroid);
        }
    }

    public void updatePosition(double angleSpeed) {
        for (int i = 0; i < asteroids.size(); i++) {
            Circle asteroid = asteroids.get(i);

            double x = asteroid.getCenterX() - screenWidth / 2;
            double y = asteroid.getCenterY() - screenHeight / 2;
            double currentRadius = Math.sqrt(x * x + y * y);

            double minRadius = 0.4 * Math.min(screenWidth, screenHeight);
            double maxRadius = 0.47 * Math.min(screenWidth, screenHeight);

            if (currentRadius < minRadius) currentRadius = minRadius;
            if (currentRadius > maxRadius) currentRadius = maxRadius;

            double angle = Math.atan2(y, x) + angleSpeed;

            x = currentRadius * Math.cos(angle) + screenWidth / 2;
            y = currentRadius * Math.sin(angle) + screenHeight / 2;

            asteroid.setCenterX(x);
            asteroid.setCenterY(y);
        }
    }

    public ArrayList<Circle> getAsteroids() {
        return asteroids;
    }
}

public class Main extends Application {
    private static final double SUN_RADIUS = 60;
    private static final double REAL_SUN_RADIUS = 353000;
    private SolarSystem solarSystem = new SolarSystem();
    private boolean isTextFixed = false;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double sceneWidth = (screenBounds.getWidth())/2;
        double sceneHeight = screenBounds.getHeight();
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        Text planetInfoText = new Text(50, 50, "");
        planetInfoText.setFill(Color.WHITE);
        planetInfoText.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 14;");
        root.getChildren().add(planetInfoText);

        scene.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.BLACK),
                new Stop(1, Color.DARKBLUE)
        ));


        Sun sunObject = new Sun("Sun", 1.989e30, 696.340,
                new RadialGradient(
                        0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.YELLOW),
                        new Stop(0.6, Color.ORANGE),
                        new Stop(1, Color.RED)
                ));

        Circle sun = new Circle(sceneWidth / 2, sceneHeight / 2, SUN_RADIUS);
        sun.setFill(sunObject.color);
        Glow glow = new Glow();
        glow.setLevel(1);
        sun.setEffect(glow);
        root.getChildren().add(sun);

        AsteroidBelt asteroidBelt = new AsteroidBelt(sceneWidth, sceneHeight);
        asteroidBelt.createAsteroids(200);

        for (int i = 0; i < 60; i++) {
            double x = Math.random() * scene.getWidth();
            double y = Math.random() * scene.getHeight();
            double size = Math.random() * 2 + 1;
            Color color = Color.WHITE;
            Star star = new Star(x, y, size, color);
            root.getChildren().add(star.createCircleRepresentation());
        }

        double baseRadius = sceneWidth * 0.085;

        Orbit mercuryOrbit = new Orbit(baseRadius * 1.3, 0.2056 * 2.5);
        Orbit venusOrbit = new Orbit(baseRadius * 1.5, 0.0067 * 2.5);
        Orbit earthOrbit = new Orbit(baseRadius * 1.7, 0.0167 * 2.5);
        Orbit marsOrbit = new Orbit(baseRadius * 1.9, 0.0934 * 2.5);
        Orbit jupiterOrbit = new Orbit(baseRadius * 3.0, 0.0484 * 2.5);
        Orbit saturnOrbit = new Orbit(baseRadius * 4.0, 0.0565 * 2.5);
        Orbit uranusOrbit = new Orbit(baseRadius * 4.5, 0.0461 * 2.5);
        Orbit neptuneOrbit = new Orbit(baseRadius * 5.0, 0.0097 * 2.5);
        Orbit plutoOrbit = new Orbit(baseRadius * 5.5, 0.0087 * 2.5);

        Planet mercury = new Planet("Mercury", 3.3e23, 2439.7*8, mercuryOrbit, 0.65, new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#7f7f7f")), new Stop(0.6, Color.web("#4c4c4c")), new Stop(1, Color.web("#2a2a2a"))),
                sceneWidth, sceneHeight);

        Planet venus = new Planet("Venus", 4.9e24, 6052.8*6, venusOrbit, 0.59,new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#E5B97E")), new Stop(0.6, Color.web("#D77F3C")), new Stop(1, Color.web("#9F4F20"))),
                sceneWidth, sceneHeight);

        Planet earth = new Planet("Earth", 5.98e24, 6371*6, earthOrbit, 0.45, Color.STEELBLUE, sceneWidth, sceneHeight);

        Planet mars = new Planet("Mars", 6.44e23, 3386*7, marsOrbit, 0.408, new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#D16C4B")), new Stop(0.6, Color.web("#A13A2E")), new Stop(1, Color.web("#7F2A1C"))),
                sceneWidth, sceneHeight);

        Planet jupiter = new Planet("Jupiter", 1.9e27, 71492*2, jupiterOrbit, 0.221, new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.REFLECT, new Stop(0.0, Color.BURLYWOOD), new Stop(0.1, Color.TAN), new Stop(0.2, Color.SADDLEBROWN), new Stop(0.3, Color.DARKORANGE),
                new Stop(0.4, Color.BROWN), new Stop(0.5, Color.BURLYWOOD), new Stop(0.6, Color.TAN), new Stop(0.7, Color.SADDLEBROWN), new Stop(0.8, Color.DARKORANGE), new Stop(0.9, Color.BROWN)),
                sceneWidth, sceneHeight);

        Planet saturn = new Planet("Saturn", 5.68e26, 60268*2, saturnOrbit, 0.164, new RadialGradient(0, 0, 0.5, 0.5, 1, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE, new Stop(0, Color.PERU), new Stop(0.4, Color.GOLDENROD),new Stop(1, Color.LIGHTGOLDENRODYELLOW)),
                sceneWidth, sceneHeight);

        Planet uranus = new Planet("Uranus", 8.7e25, 25559*2, uranusOrbit, 0.115, new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#A0D8D3")), new Stop(0.6, Color.web("#66B1B1")), new Stop(1, Color.web("#4F7D7D"))),
                sceneWidth, sceneHeight);

        Planet neptune = new Planet("Neptune", 1.03e26, 24764*2, neptuneOrbit, 0.09, new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#4A90E2")), new Stop(0.6, Color.web("#306C9B")), new Stop(1, Color.web("#1A3A5A"))),
                sceneWidth, sceneHeight);

        Planet pluto = new Planet("Pluto", 1.3e22, 2376.3*8, plutoOrbit, 0.079,new RadialGradient(
                0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#A89785")), new Stop(0.6, Color.web("#7A5D45")), new Stop(1, Color.web("#5A3A2C"))),
                sceneWidth, sceneHeight);

        solarSystem.addPlanet(mercury);
        solarSystem.addPlanet(venus);
        solarSystem.addPlanet(earth);
        solarSystem.addPlanet(mars);
        solarSystem.addPlanet(jupiter);
        solarSystem.addPlanet(saturn);
        solarSystem.addPlanet(uranus);
        solarSystem.addPlanet(neptune);
        solarSystem.addPlanet(pluto);

        Orbit moonOrbit = new Orbit(20, 0.0549);

        Moon moon = new Moon("Moon", 7.35e22, moonOrbit, 3476*4, Color.LIGHTSTEELBLUE);

        Circle moonCircle = moon.createCircleRepresentation((moon.getRadius() / REAL_SUN_RADIUS) * SUN_RADIUS);

        root.getChildren().add(moonCircle);

        for (Circle asteroid : asteroidBelt.getAsteroids()) {
            root.getChildren().add(asteroid);
        }

        for (Planet planet : solarSystem.getPlanets()) {
            Circle planetCircle = planet.createCircleRepresentation((planet.getRadius()/ REAL_SUN_RADIUS) * SUN_RADIUS);
            root.getChildren().add(planetCircle);

            planetCircle.setOnMouseEntered(event -> {
                String speed = "";
                String orbitRadius = "";
                String radius = "";
                switch (planet.getName()) {
                    case "Mercury":
                        speed = "47.87 km/s";
                        orbitRadius = "57.9E9 m";
                        radius = "2439 km";
                        break;
                    case "Venus":
                        speed = "35.02 km/s";
                        orbitRadius = "108.3E9 m";
                        radius = "6052 km";
                        break;
                    case "Earth":
                        speed = "29.78 km/s";
                        orbitRadius = "149.6E9 m";
                        radius = "6371 km";
                        break;
                    case "Mars":
                        speed = "24.07 km/s";
                        orbitRadius = "228.1E9 m";
                        radius = "3386 km";
                        break;
                    case "Jupiter":
                        speed = "13.07 km/s";
                        orbitRadius = "778.4E9 m";
                        radius = "71 492 km";
                        break;
                    case "Saturn":
                        speed = "9.69 km/s";
                        orbitRadius = "1426.98E9 m";
                        radius = "60 268 km";
                        break;
                    case "Uranus":
                        speed = "6.81 km/s";
                        orbitRadius = "2869.5E9 m";
                        radius = "25 559 km";
                        break;
                    case "Neptune":
                        speed = "5.43 km/s";
                        orbitRadius = "4496.7E9 m";
                        radius = "24 764 km";
                        break;
                    case "Pluto":
                        speed = "4.66 km/s";
                        orbitRadius = "5919.4E9 m";
                        radius = "1188 km";
                        break;
                }
                planetInfoText.setText(
                        "Name: " + planet.getName() + "\n" +
                                "Mass: " + planet.getMass() + " kg\n" +
                                "Orbit Radius: " + orbitRadius+ "\n" +
                                "Speed: " + speed + "\n" +
                                "Radius: " + radius
                );
            });

            sun.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                if (!isTextFixed) {
                    planetInfoText.setText(
                            "Name: " + sunObject.getName() +
                                    "\nMass: " + sunObject.getMass()  + " kg" +
                                    "\nRadius: " + sunObject.getRadius() + " km"
                    );
                }
            });

            moonCircle.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                if (!isTextFixed) {
                    planetInfoText.setText(
                            "Name: " + moon.getName()+
                                    "\nMass: " + moon.getMass() + " kg" +
                                    "\nRadius: 1.737 km" +
                                    "\nSpeed: 1.02 km/s" +
                                    "\nOrbit Radius: 384,399 km"
                    );
                }
            });


            Timeline hideTextTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                planetInfoText.setText("");
            }));

            hideTextTimeline.setCycleCount(200);
            hideTextTimeline.play();

            Ellipse saturnRing1 = new Ellipse(500, 500, 25, 5);
            saturnRing1.setStrokeWidth(2);
            saturnRing1.setStroke(Color.BURLYWOOD);
            saturnRing1.setFill(Color.TRANSPARENT);

            Ellipse saturnRing2 = new Ellipse(500, 500, 30, 10);
            saturnRing2.setStrokeWidth(1.5);
            saturnRing2.setStroke(Color.LIGHTGOLDENRODYELLOW);
            saturnRing2.setFill(Color.TRANSPARENT);

            saturn.addRing(saturnRing1);
            saturn.addRing(saturnRing2);
            root.getChildren().addAll(saturnRing1, saturnRing2);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                solarSystem.update();
                for (Planet planet : solarSystem.getPlanets()) {
                    Circle planetCircle = planet.getVisualRepresentation();
                    double semiMajorAxis = planet.getOrbit().getSemiMajorAxis();
                    double eccentricity = planet.getOrbit().getEccentricity();
                    double angle = Math.toRadians(planet.getOrbit().getAngle());
                    double semiMinorAxis = semiMajorAxis * Math.sqrt(1 - eccentricity * eccentricity);
                    double x = (sceneWidth / 2) + semiMajorAxis * Math.cos(angle);
                    double y = (sceneHeight / 2) + semiMinorAxis * Math.sin(angle);

                    planetCircle.setCenterX(x);
                    planetCircle.setCenterY(y);

                    if (planet.getName().equals("Earth")) {
                        moon.updatePosition();
                        double moonX = x + moonOrbit.getSemiMajorAxis() * Math.cos(Math.toRadians(moonOrbit.getAngle()));
                        double moonY = y + moonOrbit.getSemiMajorAxis() * Math.sin(Math.toRadians(moonOrbit.getAngle()));
                        moon.getVisualRepresentation().setCenterX(moonX);
                        moon.getVisualRepresentation().setCenterY(moonY);
                    }
                }
                asteroidBelt.updatePosition(0.0013);
            }
        }.start();

        primaryStage.setTitle("Solar System Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

