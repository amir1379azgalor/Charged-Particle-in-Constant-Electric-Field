package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class Controller  {

    private final double ELECTRIC_FIELD_START_WIDTH = 150 , ELECTRIC_FIELD_END_WIDTH = 550;
    private final double ELECTRIC_FIELD_START_HEIGHT = 10 , ELECTRIC_FIELD_END_HEIGHT = 355;
    private final double E_CONSTANT = 0.0001;

    private final double targetWidth = 5;
    private final double targetHeight = 50;

    private Rectangle target;
    private double backward = -5;


    @FXML
    private AnchorPane world;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button stepButton;

    private Movement clock;

    private class Movement extends AnimationTimer {

        private long FRAMES_PER_SEC = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;

        private long last = 0;

        @Override
        public void handle(long now) {
            if(now - last > INTERVAL) {
                step();
                last = now;
            }
        }
    }

    @FXML
    private TextField charge;

    @FXML
    private TextField initialVelocity;

    @FXML
    TextField electricField;

    ImageView positiveBar = new ImageView("PositiveBar.png");
    ImageView negativeBar = new ImageView("NegativeBar.png");
    boolean barsPlaced = false;

    Simulation sim;
    private double gravity = 0;

    @FXML
    public void initialize() {
        clock = new Movement();
        world.setBackground(new Background(new BackgroundFill(Color.WHITE , null , null)));
    }

    @FXML
    public void reset() {
        stop();
        electricField.setText("0");
        initialVelocity.setText("50");
        charge.setText("0");
        world.getChildren().removeAll(positiveBar , negativeBar);
        world.getChildren().clear();
        barsPlaced = false;
        createTarget(world);
        sim = new Simulation(world);
    }

    @FXML
    public void start() {
        calculateGravity();
        sim.charge.setHeading(new Heading(calculateXVelocity() , sim.charge.getHeading().getDy() + gravity));
        clock.start();
        disableButtons(true , false , true);
    }

    @FXML
    public void stop() {
        clock.stop();
        disableButtons(false , true , false);
    }

    @FXML
    public void step() {
        if(Double.parseDouble(electricField.getText()) != 0)
            if(!barsPlaced) {
                placeBars(world);
                calculateGravity();
            }
        if(isInField(sim.charge.getPosition().getX()))
            sim.charge.setHeading(new Heading(sim.charge.getHeading().getDx() , sim.charge.getHeading().getDy() + gravity));
        checkCollision(sim.charge.getPosition().getX() , sim.charge.getPosition().getY() , world);
        sim.move();
        sim.draw();
    }

    public void placeBars(AnchorPane world) {
        if(Double.parseDouble(electricField.getText()) < 0) {
            barsPlaced = true;
            world.getChildren().add(positiveBar);
            world.getChildren().add(negativeBar);
            positiveBar.setLayoutX(150);
            positiveBar.setLayoutY(0);
            negativeBar.setLayoutX(150);
            negativeBar.setLayoutY(365);

        } else if(Double.parseDouble(electricField.getText()) > 0) {
            barsPlaced = true;
            world.getChildren().add(negativeBar);
            world.getChildren().add(positiveBar);
            negativeBar.setLayoutX(150);
            negativeBar.setLayoutY(0);
            positiveBar.setLayoutX(150);
            positiveBar.setLayoutY(365);
        }
    }

    public boolean isInField(double x) {
        if(x >= ELECTRIC_FIELD_START_WIDTH && x <= ELECTRIC_FIELD_END_WIDTH)
            return true;
        return false;
    }

    public void checkCollision(double x , double y , AnchorPane world) {
        if(isInField(x)) {
            if(x >= world.getWidth() - 5 || y <= ELECTRIC_FIELD_START_HEIGHT + 8 || y >= ELECTRIC_FIELD_END_HEIGHT) {
                sim.charge.setHeading(new Heading(0 , 0));
                sim.charge.getCircle().setFill(Color.DARKRED);
            }
        } else {
            if(x >= world.getWidth() - 5 || y <= ELECTRIC_FIELD_START_HEIGHT - 3 || y >= ELECTRIC_FIELD_END_HEIGHT + 15) {
                sim.charge.setHeading(new Heading(0 , 0));
                sim.charge.getCircle().setFill(Color.DARKRED);
            }
        }
    }

    public void disableButtons(boolean start , boolean stop , boolean step) {
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }

    public void calculateGravity() {
        gravity = -Double.parseDouble(electricField.getText()) * Double.parseDouble(charge.getText()) * E_CONSTANT;
    }

    public double calculateXVelocity() {
        return Double.parseDouble(initialVelocity.getText()) / 10;
    }

    public void createTarget(AnchorPane world) {
        double r = Math.random();
        double g = Math.random();
        double b = Math.random();

        double x = world.getWidth() + backward;
        backward -= 0.05;
        double y = (int)(Math.random() * 320);
        target = new Rectangle(x , y , targetWidth , targetHeight);
        target.setFill(new Color(0 , r , g , b));
        world.getChildren().add(target);
    }
}
