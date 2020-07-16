package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Charge{

    private Position loc;
    private Heading heading;
    private AnchorPane world;
    private Circle c;

    public static int radius = 5;

    public Charge(Position loc, Heading heading , AnchorPane world) {

        c = new Circle(radius , Color.BLUE);
        this.world = world;
        this.loc = loc;
        this.heading = heading;
        c.setStroke(Color.BLACK);
        world.getChildren().add(c);
    }

    public void move() {
        loc.move(heading);
    }

    public void draw() {
        c.setRadius(radius);
        c.setTranslateX(loc.getX());
        c.setTranslateY(loc.getY());
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public Heading getHeading() {
        return heading;
    }

    public Position getPosition() {
        return loc;
    }

    public Circle getCircle() {
        return c;
    }
}
