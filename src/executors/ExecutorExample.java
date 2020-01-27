package executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorExample {

  /*
  У исполнителя с планировщиком есть два метода для установки задач: scheduleAtFixedRate() и scheduleWithFixedDelay(). Первый устанавливает задачи с определенным интервалом, например, в одну секунду.
  Кроме того, он принимает начальную задержку, которая определяет время до первого запуска.
  метод scheduleAtFixedRate() не берет в расчет время выполнения задачи
  * */
  public void run1() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime() / 1000000000.0);

    int initialDelay = 0;
    int period = 1;
    executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
  }

  /*
  метод scheduleWithFixedDelay(). Он работает примерно так же, как и предыдущий, но указанный интервал будет отсчитываться от времени завершения предыдущей задачи
  * */
  public void run2() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    Runnable task = () -> {
      try {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Scheduling: " + System.nanoTime() / 1000000000.0);
      } catch (InterruptedException e) {
        System.err.println("task interrupted");
      }
    };

    executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
  }
}
