package zyw.absfactory;

public class AbstractFactoryDemo {

    public static void main(String[] args) {

        //获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");

        //获取形状为 Circle 的对象
        Shape shape_circle = shapeFactory.getShape("CIRCLE");

        //调用 Circle 的 draw 方法
        shape_circle.draw();

        //获取形状为 Rectangle 的对象
        Shape shape_rectangle = shapeFactory.getShape("RECTANGLE");

        //调用 Rectangle 的 draw 方法
        shape_rectangle.draw();

        //获取形状为 Square 的对象
        Shape shape_square = shapeFactory.getShape("SQUARE");

        //调用 Square 的 draw 方法
        shape_square.draw();

        //获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");

        //获取颜色为 Red 的对象
        Color color_red = colorFactory.getColor("RED");

        //调用 Red 的 fill 方法
        color_red.fill();

        //获取颜色为 Green 的对象
        Color color_green = colorFactory.getColor("GREEN");

        //调用 Green 的 fill 方法
        color_green.fill();

        //获取颜色为 Blue 的对象
        Color color_blue = colorFactory.getColor("BLUE");

        //调用 Blue 的 fill 方法
        color_blue.fill();
    }
}
