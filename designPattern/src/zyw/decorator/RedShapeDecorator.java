package zyw.decorator;


// 创建扩展了 ShapeDecorator 类的实体装饰类
public class RedShapeDecorator extends ShapeDecorator {

    public RedShapeDecorator(Shape decoratorShap) {
        super(decoratorShap);
    }


    @Override
    public void draw() {
        decoratorShape.draw();
        setRedBorder(decoratorShape);
    }

    private void setRedBorder(Shape decoratedShape){
        System.out.println("Border Color: Red");
    }
}
