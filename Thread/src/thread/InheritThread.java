package thread;

/**
 * 线程的创建方式
 * 继承Thread类
 */
public class InheritThread extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread类创建的线程");
    }
}
