package v1ch06.innerClass;

public class Test {

    public static void main(String[] args) {

//        OuterClass.InnerClass innerClass = new OuterClass().new InnerClass();
//        OuterClass outerClass = new OuterClass();
//        OuterClass.InnerClass innerClass = outerClass.new InnerClass();

//        OuterClass.InnerClass innerClass = outerClass.getInnerClassInstance();

        OuterClass.InnerClass innerClass = new OuterClass.InnerClass();

        innerClass.show();

    }

}
