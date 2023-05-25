import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

public class MoonMotion extends Application {
    private static final double G = 6.6743e-11;
    private static final double Ms = 1.989e30;
    private static final double Mz = 5.972e24;
    private static final double Mk = 7.347e22;
    private static final double Rzs = 1.5e11;
    private static final double Rzk = 384400000;

    private static final double SCALING_FACTOR = 2e-9;
    private static final double MOON_SCALING_FACTOR = 2e-7;
    private static final double TIME_STEP = 2000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 800, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Moon Motion");
        primaryStage.show();
        Image spaceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("space.jpg")));
        ImagePattern spacePattern = new ImagePattern(spaceImage);
        Rectangle background = new Rectangle(0, 0, 800, 800);
        background.setFill(spacePattern);

        Image sunImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sun.png")));
        ImageView sun = new ImageView(sunImage);
        sun.setFitWidth(80);
        sun.setFitHeight(80);

        Image earthImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("earth.png")));
        ImageView earth = new ImageView(earthImage);
        earth.setFitWidth(40);
        earth.setFitHeight(40);

        Image moonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("moon.png")));
        ImageView moon = new ImageView(moonImage);
        moon.setFitWidth(24);
        moon.setFitHeight(24);

        root.getChildren().add(background);
        root.getChildren().addAll(sun, earth, moon);


        sun.setTranslateX(scene.getWidth() / 2 - 20);
        sun.setTranslateY(scene.getHeight() / 2 - 20);

        AnimationTimer timer = new AnimationTimer() {
            double time = 0;

            @Override
            public void handle(long now) {
                time += TIME_STEP;
                updatePositions(sun, earth, moon, time);
            }
        };
        timer.start();
    }

    private void updatePositions(ImageView sun, ImageView earth, ImageView moon, double time) {
        double halfDt = 0.5 * TIME_STEP;

        double earthAngularSpeed = -Math.sqrt(G * Ms / Math.pow(Rzs, 3));
        double earthX = Rzs * Math.cos(earthAngularSpeed * time);
        double earthY = Rzs * Math.sin(earthAngularSpeed * time);

        double earthPosX = sun.getTranslateX() + earthX * SCALING_FACTOR;
        double earthPosY = sun.getTranslateY() + earthY * SCALING_FACTOR;

        earth.setTranslateX(earthPosX);
        earth.setTranslateY(earthPosY);

        double moonAngularSpeed = -Math.sqrt(G * (Mz + Mk) / Math.pow(Rzk, 3));
        double moonX = Rzk * Math.cos(moonAngularSpeed * time);
        double moonY = Rzk * Math.sin(moonAngularSpeed * time);

        // Midpoint method (improved Euler method) for Moon's position
        double k2x = Rzk * Math.cos(moonAngularSpeed * (time + halfDt));
        double k2y = Rzk * Math.sin(moonAngularSpeed * (time + halfDt));
        double moonMidX = 0.5 * (moonX + k2x);
        double moonMidY = 0.5 * (moonY + k2y);

        double moonPosX = earth.getTranslateX() + moonMidX * MOON_SCALING_FACTOR;
        double moonPosY = earth.getTranslateY() + moonMidY * MOON_SCALING_FACTOR;

        moon.setTranslateX(moonPosX);
        moon.setTranslateY(moonPosY);
    }

}
