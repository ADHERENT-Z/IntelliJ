package thread;

/**
 * 线程的创建方式
 * 将Runnable对象作为任务传入Thread中
 */
public class RunnableThread implements Runnable {

    @Override
    public void run() {
        System.out.println("继承Runnable接口，创建描述任务对象，实现多线程");
    }
}
