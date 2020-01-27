import executors.ExecutorExample;
import java.util.concurrent.ExecutionException;
import scheduled_executor_service.ScheduledExecutorExample;

public class Main {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ScheduledExecutorExample scheduledExecutorExample = new ScheduledExecutorExample();
    //scheduledExecutorExample.run1();
    //scheduledExecutorExample.run2();
    //scheduledExecutorExample.run3();

    ExecutorExample executorExample = new ExecutorExample();
    //executorExample.run1();
    //executorExample.run2();
  }
}
