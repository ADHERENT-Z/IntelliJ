package v1ch06.innerClass;

/**
 * 外部类
 */
public class OuterClass {

    private int outerVar1 = 1;
    public int outerVar2 = 2;
    private static int outerVar3 = 3;

    private InnerClass innerClassInstance;

    public InnerClass getInnerClassInstance() {
        return new InnerClass();
    }

    /**
     * 静态内部类
     */
    static class InnerClass{

        private final int innerVar1 = 1;
        public int innerVar2;

        public int getInnerVar1() {
            return innerVar1;
        }

        public void show() {
//            System.out.println(outerVar1);
//            System.out.println(outerVar2);
            System.out.println(outerVar3);
        }
    }
}
