package sinchronizeExample;

/*
* Класс Exchanger (обменник) предназначен для упрощения процесса обмена данными между двумя потоками исполнения.
* Принцип действия класса Exchanger связан с ожиданием того, что два отдельных потока должны вызвать его метод exchange.
* Как только это произойдет, Exchanger произведет обмен данными, предоставляемыми обоими потоками.

Обменник является обобщенным классом, он параметризируется типом объекта передачи :

Exchanger<V>();
Необходимо отметить, что обменник поддерживает передачу NULL значения, что дает возможность использовать его для передачи
* объекта в одну сторону или места синхронизации двух потоков.

Exchanger содержит перегруженный метод exchange, имеющий следующие формы :

V exchange(V buffer) throws InterruptedException;
V exchange(V buffer, long wait, TimeUnit unit)
                     throws InterruptedException;
Параметр buffer является ссылкой на обмениваемые данные. Метод возвращает данные из другого потока исполнения.
* Вторая форма метода позволяет определить время ожидания. Параметры wait и unit описаны выше. Метод exchange,
* вызванный в одном потоке, не завершится успешно до тех пор, пока он не будет вызван из второго потока исполнения.
* */

import java.util.concurrent.Exchanger;

/*
 * В примере использования объекта синхронизации Exchanger два почтальона из пунктов А и Б отправляются в соседние поселки
 * В и Г доставить письма. Каждый из почтальонов должен доставить по письму в каждый из поселков. Чтобы не делать лишний
 * круг, они встречаются в промежуточном поселке Д и обмениваются одним письмом. В результате этого каждому из почтальонов
 * придется доставить письма только в один поселок. В примере все «шаги» почтальонов фиксируются выводом
 * соответствующих сообщений в консоль.
 * */
public class ExchangerExample {

  // Обменник почтовыми письмами
  private static Exchanger<Letter> EXCHANGER;

  public static class Postman implements Runnable {

    private String id;
    private String departure;
    private String destination;
    private Letter[] letters;

    public Postman(String id, String departure,
        String destination,
        Letter[] letters) {
      this.id = id;
      this.departure = departure;
      this.destination = destination;
      this.letters = letters;
    }

    @Override
    public void run() {
      try {
        System.out.println("Почтальон " + id + " получил письма: " + letters[0] + " " + letters[1]);
        System.out.println("Почтальон " + id + " выехал из " + departure + " в " + destination);
        Thread.sleep((long) Math.random() * 5000
            + 5000);
        System.out.println("Почтальон " + id + " приехал в пункт Д ");
        // Самоблокировка потока для
        // обмена письмами
        letters[1] = EXCHANGER.exchange(letters[1]);
        // Обмен письмами
        System.out.println("Почтальон " + id + " получил письма для " + destination);
        Thread.sleep(1000 + (long)
            Math.random() * 5000);
        System.out.println("Почтальон " + id + " привез в " + destination + ": " + letters[0] + ", " + letters[1]);
      } catch (InterruptedException e) {
      }
    }
  }

  public static class Letter {

    private String address;

    public Letter(final String address) {
      this.address = address;
    }

    public String toString() {
      return address;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    EXCHANGER = new Exchanger<Letter>();
    // Формирование отправлений
    Letter[] posts1 = new Letter[2];
    Letter[] posts2 = new Letter[2];

    posts1[0] = new Letter("п.В - Петров");
    posts1[1] = new Letter("п.Г - Киса Воробьянинов");
    posts2[0] = new Letter("п.Г - Остап Бендер");
    posts2[1] = new Letter("п.В - Иванов");
    // Отправление почтальонов
    new Thread(new Postman("a", "А", "В", posts1)).start();
    Thread.sleep(100);
    new Thread(new Postman("б", "Б", "Г", posts2)).start();
  }
}
