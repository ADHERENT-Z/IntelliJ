package zyw.bridge;

public class Square extends Shape{

    private int x, y;

    public Square(int x, int y, IDraw draw) {
        super(draw);
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw() {
        System.out.println("Shape: Square " + "[x = " + x + ", y = " + y + "]");
        draw.drawColor();
    }
}
