package sinchronizeExample;

/*
  * Конструкторы Semaphore
Класс Semaphore имеет два приведенных ниже конструктора :
* Semaphore(int permits);
Semaphore(int permits, boolean fair);
Параметр permits определяет исходное значение счетчика разрешений, т.е. количество потоков исполнения,
* которым может быть одновременно предоставлен доступ к общему ресурсу. По умолчанию ожидающим потокам
* предоставляется разрешение в неопределенном порядке. Если же использовать второй конструктор и параметру
* справедливости fair присвоить значение true, то разрешения будут предоставляться ожидающим
* потокам исполнения в том порядке, в каком они его запрашивали.
*
*
* Метод получения разрешения acquire
Чтобы получить у семафора разрешение необходимо вызвать у него один из перегруженных методов acquire :

void acquire()           throws InterruptedException
void acquire(int number) throws InterruptedException
Первый метод без параметра запрашивает одно разрешение, а второй в качестве параметра использует количество разрешений. Обычно используется первый метод. Если разрешение не будет получено, то исполнение вызывающего потока будет приостановлено до тех пор, пока не будет получено разрешение, т.е. поток блокируется.

Освобождение ресурса
Чтобы освободить разрешение у семафора следует вызвать у него один из перегруженных методов release :

void release()
void release(int number)
В первом методе освобождается одно разрешение, а во втором — количество разрешений, обозначенное параметром number.
* */

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

  private static final int COUNT_CONTROL_PLACES = 5;
  private static final int COUNT_RIDERS = 7;
  // Флаги мест контроля
  private static boolean[] CONTROL_PLACES = null;

  // Семафор
  private static Semaphore SEMAPHORE = null;

  public static class Rider implements Runnable {

    private int ruderNum;

    public Rider(int ruderNum) {
      this.ruderNum = ruderNum;
    }

    @Override
    public void run() {
      System.out.println("Всадник " + ruderNum + " подошел к зоне контроля");
      try {
        // Запрос разрешения
        SEMAPHORE.acquire();
        System.out.println("Всадник " + ruderNum + " проверяет наличие свободного контроллера");
        int controlNum = -1;
        // Ищем свободное место и
        // подходим к контроллеру
        synchronized (CONTROL_PLACES) {
          for (int i = 0;
              i < COUNT_CONTROL_PLACES; i++)
          // Есть ли свободные контроллеры?
          {
            if (CONTROL_PLACES[i]) {
              // Занимаем место
              CONTROL_PLACES[i] = false;
              controlNum = i;
              System.out.println("Всадник " + ruderNum + " подошел к контроллеру " + i);
              break;
            }
          }
        }

        // Время проверки лошади и всадника
        Thread.sleep((int)
            (Math.random() * 10 + 1) * 1000);

        // Освобождение места контроля
        synchronized (CONTROL_PLACES) {
          CONTROL_PLACES[controlNum] = true;
        }

        // Освобождение ресурса
        SEMAPHORE.release();
        System.out.println("Всадник " + ruderNum + " завершил проверку");
      } catch (InterruptedException e) {
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    // Определяем количество мест контроля
    CONTROL_PLACES = new boolean[COUNT_CONTROL_PLACES];
    // Флаги мест контроля [true-свободно,false-занято]
    for (int i = 0; i < COUNT_CONTROL_PLACES; i++) {
      CONTROL_PLACES[i] = true;
    }
    /*
     *  Определяем семафор со следующими параметрами :
     *  - количество разрешений 5
     *  - флаг очередности fair=true (очередь
     *                             first_in-first_out)
     */
    SEMAPHORE = new Semaphore(CONTROL_PLACES.length,
        true);

    for (int i = 0; i <= COUNT_RIDERS; i++) {
      new Thread(new Rider(i)).start();
      Thread.sleep(400);
    }
  }
}
