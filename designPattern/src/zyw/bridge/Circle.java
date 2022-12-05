package zyw.bridge;

public class Circle extends Shape {

    private int x, y, radius;

    public Circle(int x, int y, int radius, IDraw draw) {
        super(draw);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

//    @Override
//    public void draw() {
//        draw.drawCircle(radius, x, y);
//    }

    @Override
    public void draw() {
        System.out.println("Shape: Circle " + "[radius = " + radius + ", x = " + x + ", y = " + y + "]");
        draw.drawColor();
    }

}
