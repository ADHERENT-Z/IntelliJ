package zyw.absfactory;


// 为 Color 和 Shape 对象创建抽象类来获取工厂
public abstract class AbstractFactory {

    public abstract Color getColor(String colorType);

    public abstract Shape getShape(String shapeType);

}
