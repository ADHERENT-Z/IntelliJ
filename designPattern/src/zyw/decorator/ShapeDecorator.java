package zyw.decorator;


// 创建实现了 Shape 接口的抽象装饰类
public class ShapeDecorator implements Shape {

    protected Shape decoratorShape;

    public ShapeDecorator(Shape decoratorShap) {
        this.decoratorShape = decoratorShap;
    }

    @Override
    public void draw() {
        decoratorShape.draw();
    }
}
