package scheduled_executor_service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorExample {
  public void run1() {
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    Runnable task2 = () -> System.out.println("Running task2...");

    task1();

    //run this task after 5 seconds, nonblock for task3
    ses.schedule(task2, 5, TimeUnit.SECONDS);

    task3();

    ses.shutdown();
  }

  public void run2() throws ExecutionException, InterruptedException {
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    Callable<String> task2 = () -> "Running task 2";

    task1();

    //run this task after 5 seconds, nonblock for task3, returns a future
    ScheduledFuture<String> schedule = ses.schedule(task2, 5, TimeUnit.SECONDS);

    task3();

    //block and get the result
    System.out.println(schedule.get());

    System.out.println("shutdown!");

    ses.shutdown();
  }

  private static int count = 0;
  public void run3() throws InterruptedException {
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    Runnable task1 = () -> {
      count++;
      System.out.println("Running...task1 - count : " + count);
    };

    //init Delay = 5, repeat the task every 1 second
    ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(task1, 5, 1, TimeUnit.SECONDS);

    while (true) {
      System.out.println("count :" + count);
      Thread.sleep(1000);
      if (count == 5) {
        System.out.println("Count is 5, cancel the scheduledFuture!");
        scheduledFuture.cancel(true);
        ses.shutdown();
        break;
      }
    }
  }

  private void task1() {
    System.out.println("Running task1...");
  }

  private void task3() {
    System.out.println("Running task3...");
  }

}
