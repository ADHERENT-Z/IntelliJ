package lock;

import java.util.concurrent.locks.LockSupport;

/**
 * 三个线程分别打印 A、B、C
 * 打印结果是ABC
 */
public class LockSupportDemo {

    private void printA(Thread thread) {

        try {
            Thread.sleep(20L);
            System.out.println("A");
            LockSupport.unpark(thread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printB(Thread thread) {

        try {
            Thread.sleep(10L);
            LockSupport.park();
            System.out.println("B");
            LockSupport.unpark(thread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printC() {

        try {
            Thread.sleep(5L);
            LockSupport.park();
            System.out.println("C");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        LockSupportDemo t = new LockSupportDemo();
        Thread tC = new Thread(t::printC);
        Thread tB = new Thread(()->t.printB(tC));
        Thread tA = new Thread(()->t.printA(tB));

        tA.start();
        tB.start();
        tC.start();
    }
}
