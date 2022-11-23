package zyw.decorator;


// 创建扩展了 ShapeDecorator 类的实体装饰类
public class RedShapeDecorator extends ShapeDecorator {

    public RedShapeDecorator(Shape decoratorShap) {
        super(decoratorShap);
    }

    @Override
    public void draw() {
        decoratorShap.draw();
        setRedBorder(decoratorShap);
    }


    private void setRedBorder(Shape decoratedShape){
        System.out.println("Border Color: Red");
    }
}
