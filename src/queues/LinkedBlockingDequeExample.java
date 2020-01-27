package queues;

import java.util.Calendar;
import java.util.Deque;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class LinkedBlockingDequeExample {

  final String EXTRACT = "Извлечение из map : %s%n";
  final String INSERT = "Добавление в очередь : %s%n";
  final String WAIT = "... ожидание : %s%n";
  final String SIZE = "--- deque.size=%d ---%n";
  final String REMOVE_HEAD = "\tremove head: %s%n";
  final String REMOVE_TAIL = "\tremove tail: %s%n";

  Map<String, Integer> names;
  Deque<String> deque;

  LinkedBlockingDequeExample() {
    Calendar now = Calendar.getInstance();
    Locale locale = Locale.getDefault();

    names = now.getDisplayNames(Calendar.MONTH,
        Calendar.ALL_STYLES,
        locale);

    System.out.printf("Список коллекции : %s%n", names);

    deque = new LinkedBlockingDeque<String>(6);

    Thread producer = new Thread(new Producer());
    producer.start();

    (new Thread(new Consumer())).start();

    while (producer.isAlive()) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {
      }
    }
    System.exit(0);
  }

  class Producer implements Runnable {

    public Producer() {
    }

    public void run() {
      Set<String> keys = names.keySet();
      Iterator<String> iter = keys.iterator();
      String element = null;
      while ((iter.hasNext()) || (element != null)) {
        if (element == null) {
          element = iter.next();
          System.out.printf(EXTRACT, element);
        }
        // Добавление элемента в начало
        if (deque.offerFirst(element)) {
          System.out.printf(INSERT, element);
          iter.remove();
          element = null;
        } else {
          System.out.printf(WAIT, element);
          try {
            Thread.sleep(250);
          } catch (InterruptedException ignored) {
          }
        }
        System.out.printf(SIZE, deque.size());
      }
      try {
        Thread.sleep(3500);
      } catch (InterruptedException ignored) {
      }
    }
  }

  class Consumer implements Runnable {

    public Consumer() {
    }

    public void run() {
      while (true) {
        if ((deque.size() % 2 == 1))
        // удаление из начала
        {
          System.out.printf(REMOVE_HEAD,
              deque.pollFirst());
        } else
        // удаление из конца
        {
          System.out.printf(REMOVE_TAIL,
              deque.pollLast());
        }
        try {
          // пауза между циклами
          Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  /*
   * Коллекция включает различные варианты представления месяцев календаря. Producer извлекает элемент из map и добавляет
   * его в начало очереди. После этого в консоль выводится размер очереди. Если очередь полная, то Producer ждет
   * освобождения места. Consumer извлекает элемент из очереди. Поскольку он всегда «приходит» к полной очереди (6 элементов),
   * то извлекает он из хвоста, до тех пор, пока Producer не закончит добавлять элементы в очередь.
   * */
  public static void main(String args[]) {
    new LinkedBlockingDequeExample();
  }
}
