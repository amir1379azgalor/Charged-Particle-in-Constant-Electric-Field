package sample;

import javafx.scene.layout.AnchorPane;

public class Simulation {

    Position position = new Position(14 , 188);
    Heading heading = new Heading(5, 0);

    Charge charge;

    public Simulation(AnchorPane world) {
        charge = new Charge(position , heading , world);
        charge.draw();
    }

    public void move() {
        charge.move();
    }

    public void draw() {
        charge.draw();
    }
}
