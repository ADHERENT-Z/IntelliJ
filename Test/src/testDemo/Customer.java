package testDemo;

import java.util.Random;

/**
 * 业务功能
 */
public class Customer implements Runnable {

    private ThreadContainer<Ticket> threadContainer;

    public Customer(ThreadContainer<Ticket> threadContainer) {
        this.threadContainer = threadContainer;
    }


    @Override
    public void run() {
        for (;;) {
            try {
                Ticket custom = threadContainer.custom();
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
