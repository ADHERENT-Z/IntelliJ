package testDemo;

import java.util.Date;
import java.util.Random;


public class Producer implements Runnable {

    private ThreadContainer<Ticket> threadContainer;
    private Date time;

    public Producer(ThreadContainer<Ticket> threadContainer, Date time) {
        this.threadContainer = threadContainer;
        this.time = time;
    }

    @Override
    public void run() {
        for (;;) {
            Ticket ticket = new Ticket("南京", "亳州", DateUtils.addDays(time, 1));
            try {
                threadContainer.produce(ticket);
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
