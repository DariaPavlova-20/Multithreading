package queues;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeExample {

  private ConcurrentLinkedDeque<String> queue;

  public ConcurrentLinkedDequeExample() {
    queue = new ConcurrentLinkedDeque<String>();

    Thread producer = new Thread(new Producer());
    producer.start();

    Thread consumer = new Thread(new Consumer());
    consumer.start();

    while (consumer.isAlive()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
    System.exit(0);
  }

  class Producer implements Runnable {

    @Override
    public void run() {
      System.out.println("Producer started");
      String name = "producer ";
      String ins = "";
      for (int i = 0; i < 10; i++) {
        String element = "'" + name + i + "'";
        if (queue.size() % 2 == 1) {
          queue.addFirst(element);
          ins = "addFirst (" + element + ")";
        } else {
          queue.addLast(element);
          ins = "addLast (" + element + ")";
        }
        System.out.println(name + ins
            + ": queue.size()="
            + queue.size());
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
        }
      }
    }
  }

  class Consumer implements Runnable {

    @Override
    public void run() {
      System.out.println("Consumer started");
      for (int i = 0; i < 10; i++) {
        String text = "\n   consumer : queue.size()="
            + queue.size();
        String element;
        if (queue.size() % 2 == 1) {
          element = "pollFirst : "
              + queue.pollFirst();
        } else {
          element = "pollLast : "
              + queue.pollLast();
        }
        text += ", " + element;
        System.out.println(text);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
        }
      }
    }
  }

  /*
  В примере использования неблокирующей очереди ConcurrentLinkedDeque создаются два потока.
  Первый поток Producer с задержками в 200 мс помещает в очередь 10 текстовых сообщений.
  В зависимости от размера очереди (четное/нечетное) элемент помещается либо в хвост очереди, либо в начало.
  Второй поток Consumer проверяет наполнение очереди и извлекает из очереди сообщения с задержкой 500 мс.
  Consumer также проверяет четность размера очереди и извлекает элемент либо из головы, либо из хвоста очереди.

Так как задержки в Producer меньше, чем в Consumer, то очередь наполняется быстрее, чем опустошается.
Пример демонстрирует, каким образом можно использовать неблокирующую двухстороннюю очередь ConcurrentLinkedDeque.
  * */
  public static void main(String[] args) {
    new ConcurrentLinkedDequeExample();
  }
}
