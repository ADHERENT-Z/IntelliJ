package zyw.decorator;


// 创建实现了 Shape 接口的抽象装饰类
public class ShapeDecorator implements Shape {

    protected Shape decoratorShap;

    public ShapeDecorator(Shape decoratorShap) {
        this.decoratorShap = decoratorShap;
    }

    @Override

    public void draw() {
        decoratorShap.draw();
    }
}
