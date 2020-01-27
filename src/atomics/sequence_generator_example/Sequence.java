package atomics.sequence_generator_example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/*
* Sequence
Для тестирования генератора последовательности SequenceGenerator используем класс Sequence, реализующий интерфейс Runnable.
* В качестве параметра конструктор класса получает идентификатор потока id, размер последовательности count и генератор
* последовательности sg. В методе run в цикле с незначительными задержками формируется последовательность чисел sequence.
* После завершения цикла значения последовательности «выводятся» в консоль методом printSequence.
* */
public class Sequence implements Runnable {

  Thread thread;
  int id;
  int count;
  SequenceGenerator sg;
  List<BigInteger> sequence = new ArrayList<>();
  boolean printed = false;

  Sequence(final int id, final int count,
      SequenceGenerator sg) {
    this.count = count;
    this.id = id;
    this.sg = sg;
    thread = new Thread(this);

    System.out.println("Создан поток " + id);
    thread.start();
  }

  @Override
  public void run() {
    try {
      for (int i = 0; i < count; i++) {
        sequence.add(sg.next());
        Thread.sleep((long) (
            (Math.random() * 2 + 1) * 30));
      }
    } catch (InterruptedException e) {
      System.out.println("Поток " + id
          + " прерван");
    }
    System.out.print("Поток " + id + " завершён");
    printSequence();
  }

  public void printSequence() {
    if (printed) {
      return;
    }
    String tmp = "[";
    for (int i = 0; i < sequence.size(); i++) {
      if (i > 0) {
        tmp += ", ";
      }
      String nb = String.valueOf(sequence.get(i));
      while (nb.length() < 9) {
        nb = " " + nb;
      }
      tmp += nb;
    }
    tmp += "]";
    System.out.println("Последовательность потока "
        + id + " : " + tmp);
    printed = true;
  }
}
