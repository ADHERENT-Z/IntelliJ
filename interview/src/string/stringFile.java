package string;

public class stringFile {

    /**
     * String对象的创建
     * 1.创建字符串的方式
     * 1.1 使用 new关键字创建字符串，比如 String s = new String("abc");
     * 1.2 直接指定，比如String s = "abc";
     * 1.3 使用串联生成新的字符串，比如 String s = "ab" + "c";
     *
     * 2.原理
     * 2.1 当使用任何方式来创建一个字符串对象 s = “xxx”时，Java运行时（运行中JVM）会拿着这个 "xxx"
     * 在 String池中找是否存在内容相同的字符串对象，如果不存在，则在池中创建一个字符串 s，否则，不在池中添加
     * 2.2 Java中只要使用 new 关键字来创建对象，则一定会（在堆区或栈区）创建一个新的对象
     * 2.3 使用直接指定或者使用纯字符串串联来创建 String对象，仅仅会检查维护 String 池中的字符串，
     * 池中没有就在池中创建一个，有则罢了，但绝不会在堆栈区再去创建该 String对象
     * 2.4 使用包含变量的表达式来创建 String对象，则不仅会检查维护 String池，而且还会在堆栈区创建一个 String对象
     */



    public static void main(String[] args) {

        /**
         * 栈中开辟一块空间存放引用 s1
         * String 池中开辟一块空间存放 String 常量 "abc"
         * 引用  s1指向池中String常量 "abc"
         * s1 所指代的地址即常量 "abc" 所在地址，输出为 true
         */
        String s1 = "abc";
        System.out.println(s1 == "abc"); // true


        /**
         * 栈中开辟一块空间存放引用 s1
         * 堆中开辟一块空间存放一个新建的 String 对象 "abc"
         * 引用 s2 指向堆中的新建的 String 对象 "abc"
         * s2 所指代的对象地址为堆中地址，而常量 "abc" 地址在池中，输出为 false
         */
        String s2 = new String("abc");
        System.out.println(s2 == "abc"); // false


        /**
         * 栈中开辟一块空间存放引用 s3
         * 堆中开辟一块空间存放一个新建的 String 对象 "abc"(不同于 s2)
         * 引用 s3 指向堆中的新建的 String 对象 "abc"
         * s2 和 s3 指向堆中的不同对象，地址也不同，输出为 false
         */
        String s3 = new String("abc");
        System.out.println(s3 == s2); // false


        /**
         * 栈中开辟一块空间存放引用 s4
         * 根据编译器合并已知量的优化功能，池中开辟一块空间，存放合并后的 String常量 "ab"
         * 引用 s4 指向池中常量 "ab"
         * s4 所指即池中常量 "ab"，输出为 true
         */
        String s4 = "a" + "b";
        System.out.println(s4 == "ab"); // true


        /**
         * 栈中开辟一块空间存放引用 s4
         * 根据编译器合并已知量的优化功能，池中开辟一块空间，存放合并后的 String常量 "ab"
         * 引用 s4 指向池中常量 "ab"
         * s4 所指即池中常量 "ab"，输出为 true
         */
        final String s = "a"; //注意:这里 s用 final修饰，相当于一个常量
        String s5 = s + "b";
        System.out.println(s5 == "ab"); // true


        /**
         * 栈中开辟一块中间存放引用 s6，s6 指向池中 String 常量 "a"
         * 栈中开辟一块中间存放引用 s7，s7 指向池中 String 常量 "b"
         * 栈中开辟一块中间存放引用 s8
         * s1 + s2 通过 StringBuilder的最后一步 toString()方法还原一个新的 String对象 "ab"，因此堆中开辟一块空间存放此对象
         * 引用 s8指向堆中 (s1 + s2) 所还原的新 String对象
         * s8 指向的对象在堆中，而常量 "ab"在池中，输出为 false
         */
        String s6 = "a";
        String s7 = "b";
        String s8 = s6 + s7;
        System.out.println(s8 == "ab"); // false


        /**
         * 栈中开辟一块空间存放引用 s9
         * substring()方法还原一个新的 String对象 "ab"（不同于s9所指），堆中开辟一块空间存放此对象
         * 引用 s9指向堆中的新 String对象
         */
        String s9 = "abc".substring(0, 2);


        /**
         * 栈中开辟一块空间存放引用str6，
         * toUpperCase()方法还原一个新的 String对象 "ABC"，池中并未开辟新的空间存放 String常量 "ABC"
         * 引用 s10指向堆中的新 String对象
         */
        String s10 = "abc".toUpperCase();




    }
}
