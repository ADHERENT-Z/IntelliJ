package zyw.singleton;


// 懒汉式，线程不安全
/**
 * 这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。
 * 因为没有加锁 synchronized，所以严格意义上它并不算单例模式。
 */
//public class Singleton {
//
//    // 创建类的一个实例对象
//    private static Singleton instance = null;
//
//    // 构造函数为 private，这样该类就不会被实例化
//    private Singleton(){};
//
//    //获取可用的对象
//    public static Singleton getInstance(){
//        if (instance == null) {
//            instance = new Singleton();
//        }
//
//        return instance;
//    }
//
//    public void showMessage(){
//        System.out.println("Hello World!");
//    }
//}


// 懒汉式，线程安全

/**
 * 能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步
 * 优点：第一次调用才初始化，避免内存浪费
 * 缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率
 */
//public class Singleton {
//
//    // 创建类的一个实例对象
//    private static Singleton instance = null;
//
//    // 构造函数为 private，这样该类就不会被实例化
//    private Singleton(){};
//
//    //获取可用的对象
//    public static synchronized Singleton getInstance(){
//        if (instance == null) {
//            instance = new Singleton();
//        }
//
//        return instance;
//    }
//
//    public void showMessage(){
//        System.out.println("Hello World!");
//    }
//}


// 饿汉式

/**
 * 这种方式比较常用，但容易产生垃圾对象
 * 优点：没有加锁，执行效率会提高
 * 缺点：类加载时就初始化，浪费内存
 *
 */
//public class Singleton {
//
//    // 创建类的一个实例对象
//    private static Singleton instance = new Singleton();
//
//    // 构造函数为 private，这样该类就不会被实例化
//    private Singleton(){};
//
//    //获取可用的对象
//    public static Singleton getInstance(){
//        return instance;
//    }
//
//    public void showMessage(){
//        System.out.println("Hello World!");
//    }
//}

// 双检锁/双重校验锁（DCL，即 double-checked locking）

/**
 * 是否多线程安全：是
 * 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能
 */
public class Singleton {

    // 创建类的一个实例对象
    private volatile static Singleton instance = null;

    // 构造函数为 private，这样该类就不会被实例化
    private Singleton(){};

    //获取可用的对象
    public static Singleton getInstance(){
        if(instance == null){
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }
}


// 登记式/静态内部类

/**
 * 是否多线程安全：是
 * 这种方式能达到双检锁方式一样的功效，但实现更简单。
 * 这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。
 *
 */
//public class Singleton {
//
//    private static class SingletonHolder {
//        private static final Singleton instance = new Singleton();
//    }
//
//    // 构造函数为 private，这样该类就不会被实例化
//    private Singleton(){};
//
//    //获取可用的对象
//    public static Singleton getInstance(){
//        return SingletonHolder.instance;
//    }
//
//    public void showMessage(){
//        System.out.println("Hello World!");
//    }
//}


// 枚举
