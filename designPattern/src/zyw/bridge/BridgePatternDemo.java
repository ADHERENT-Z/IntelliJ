package zyw.bridge;

public class BridgePatternDemo {

    public static void main(String[] args) {

        Shape redCircle = new Circle(100,100, 10, new DrawRed());
        Shape greenCircle = new Circle(100,100, 10, new DrawGreen());

        Shape redSquare = new Square(100,100, new DrawRed());
        Shape greenSquare = new Square(100,100, new DrawGreen());

        redCircle.draw();
        greenCircle.draw();

        redSquare.draw();
        greenSquare.draw();
    }

}
