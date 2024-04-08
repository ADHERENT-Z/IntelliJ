package testDemo;

/**
 * 建造者模式就是用静态内部类的函数创建外部类的对象
 * 一个名为 Car的普通类，内部有一个名为 Maker的静态内部类
 */
public class Car {

    // 外部类的两个属性
    private String name;
    private int size;

    // 外部类构造方法，此方法在内部类的 Show()中创建 Car对象时被调用
    public Car(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // 静态内部类
    public static class Builder {
        // 静态内部类的属性
        private String name;
        private int size;

        public Builder(){}

        // 有参构造方法
        public Builder(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public Builder setName(String name) {    //将set函数的返回值改成返回内部类
            this.name = name;
            return this;
        }

        public Builder setSize(int size) {           //将set函数的返回值改成返回内部类
            this.size = size;
            return this;
        }

        // 创建并返回外部类对象
        public Car build() {
            return new Car(this);
        }
    }


    public static void main(String[] args) {

        //链式编程 创建 Car类型的引用变量来接收 Builder.build()方法返回的 Car类型变量
        Car car1 = new Car.Builder("宝马", 23).build();

        Car car2 = new Car.Builder().setName("宝马").setSize(24).build();


    }

}

