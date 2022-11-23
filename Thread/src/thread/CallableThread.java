package thread;

import java.util.concurrent.Callable;

public class CallableThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("实现了Callable接口");
        return "这不是一个线程类，而是一个任务类";
    }
}
