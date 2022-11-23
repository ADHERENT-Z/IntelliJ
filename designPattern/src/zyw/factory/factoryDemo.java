package zyw.factory;

public class factoryDemo {

    // 使用工厂，通过传递类型信息来获取实体类的对象
    public static void main(String[] args) {

        ShapeFactory shapeFactory = new ShapeFactory();

        //获取 Circle 的对象，并调用它的 draw 方法
        Shape shape_circle = shapeFactory.getShape("CIRCLE");

        //调用 Circle 的 draw 方法
        shape_circle.draw();

        //获取 Rectangle 的对象，并调用它的 draw 方法
        Shape shape_rectangle = shapeFactory.getShape("RECTANGLE");

        //调用 Rectangle 的 draw 方法
        shape_rectangle.draw();

        //获取 Square 的对象，并调用它的 draw 方法
        Shape shape_square = shapeFactory.getShape("SQUARE");

        //调用 Square 的 draw 方法
        shape_square.draw();
    }
}
