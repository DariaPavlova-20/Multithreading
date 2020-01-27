package sinchronizeExample;

import java.util.concurrent.CountDownLatch;

/*
Объект синхронизации потоков CountDownLatch представляет собой «защелку с обратным отсчетом» : несколько потоков,
выполняя определенный код, блокируются до тех пор, пока не будут выполнены заданные условия. Количество условий определяются счетчиком.
Как только счетчик обнулится, т.е. будут выполнены все условия, самоблокировки выполняемых потоков снимаются, и они продолжают выполнение кода.

Таким образом, CountDownLatch также, как и Semaphore, работает со счетчиком, обнуление которого снимает самоблокировки выполняемых потоков. Конструктор CountDownLatch :

CountDownLatch(int number);
Параметр number определяет количество событий, которые должны произойти до того момента, когда будет снята самоблокировка.

Метод самоблокировки await
CountDownLatch имеет два перегруженных метода await для самоблокировки :

void await() throws InterruptedException;
boolean await(long wait, TimeUnit unit) throws InterruptedException;
В первом методе ожидание длится до тех пор, пока счетчик CountDownLatch не достигнет нуля. А во втором методе ожидание
длится только в течение определенного периода времени, определяемого параметром ожидание wait. Время ожидания указывается
в единицах unit объекта перечисления TimeUnit, определяюший временно́е разбиение. Существуют следующие значения данного перечисления :

DAYS
HOURS
MINUTES
SECONDS
MICROSECONDS
MILLISECONDS
NANOSECONDS
Метод уменьшения счетчика countDown
Чтобы уменьшить счетчик объекта CountDownLatch следует вызвать метод countDown :

void countDown();
Примером CountDownLatch может служить паром, собирающий определенное количество транспорта и пассажиров, или экскурсовод, собирающий группу из заданного количества туристов.
* */

/*
* При «выходе на старт» поток вызывает методы countDown, уменьшая значение счетчика на 1, и await, блокируя самого себя
* в ожидании обнуления счетчика «защелки». Как только все потоки выстроятся на «старте» с интервалом в 1 сек. подаются команды.
* Каждая команда сопровождается уменьшением счетчика. После обнуления счетчика «защелки» потоки продолжают выполнение дальнейшего кода.
* */
public class CountDownLatchExample {

  // Количество всадников
  private static final int RIDERS_COUNT = 5;
  // Объект синхронизации
  private static CountDownLatch LATCH;
  // Условная длина трассы
  private static final int trackLength = 3000;

  public static class Rider implements Runnable {

    // номер всадника
    private int riderNumber;
    // скорость всадника постоянная
    private int riderSpeed;

    public Rider(int riderNumber, int riderSpeed) {
      this.riderNumber = riderNumber;
      this.riderSpeed = riderSpeed;
    }

    @Override
    public void run() {
      try {
        System.out.println("Всадник " + riderNumber + " вышел на старт");
        //  Уменьшаем счетчик на 1
        LATCH.countDown();

        // Метод await блокирует поток до тех пор,
        // пока счетчик CountDownLatch не обнулится
        LATCH.await();
        // Ожидание, пока всадник не
        // преодолеет трассу
        Thread.sleep(trackLength / riderSpeed * 10);
        System.out.println("Всадник " + riderNumber + " финишировал");
      } catch (InterruptedException e) {
      }
    }
  }

  public static Rider createRider(final int number) {
    return new Rider(number, (int)
        (Math.random() * 10 + 5));
  }

  public static void main(String[] args) throws InterruptedException {
    // Определение объекта синхронизации
    LATCH = new CountDownLatch(RIDERS_COUNT + 3);
    // Создание потоков всадников
    for (int i = 0; i <= RIDERS_COUNT; i++) {
      new Thread(createRider(i)).start();
      Thread.sleep(1000);
    }

    // Проверка наличия всех всадников на старте
    while (LATCH.getCount() > 3) {
      Thread.sleep(100);
    }

    Thread.sleep(1000);
    System.out.println("На старт!");
    LATCH.countDown();  // Уменьшаем счетчик на 1
    Thread.sleep(1000);
    System.out.println("Внимание!");
    LATCH.countDown(); // Уменьшаем счетчик на 1
    Thread.sleep(1000);
    System.out.println("Марш!");
    LATCH.countDown(); // Уменьшаем счетчик на 1

    // Счетчик обнулен, и все ожидающие этого события
    // потоки разблокированы
  }
}
