package testDemo;

import java.util.Date;
import java.util.LinkedList;

/**
 * 消费者和生产者资源存储容器
 */
public class ThreadContainer<T> {

    // 资源池允许的最小值
    private volatile int minSize;
    // 资源池允许的最大值
    private volatile int maxSize;
    // 数据资源存储对象
    private LinkedList<T> linkedList;

    // 构造器初始化参数限制
    public ThreadContainer(int minSize, int maxSize, LinkedList<T> linkedList){
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.linkedList = linkedList;
    }

    // 生产者生产方法，线程安全
    public synchronized void produce(T data) throws InterruptedException {
        while (maxSize == linkedList.size()) {
            wait();
        }
        linkedList.add(data);
        System.out.println(">>>>>>>>>> 生产时间：" + new Date() +
                ", 生产内容：" + data.toString() + ", 资源容器中资源数量" + linkedList.size());
        notify();
    }

    // 消费者消费方法，线程安全
    public synchronized T custom() throws InterruptedException {
        while (minSize >= linkedList.size()) {
            wait();
        }
        T t = linkedList.poll();
        System.out.println("<<<<<<<<<< 消费时间：" + new Date() +
                ", 消费内容：" + t.toString() + ", 资源容器中资源数量" + linkedList.size());
        notify();
        return t;
    }

}
