import java.util.Timer;
import java.util.TimerTask;

public class ThreadTest {

    public static void main(String[] args){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行任务中");
            }
        }, 0, 1000);
    }
}
