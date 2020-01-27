package atomics.sequence_generator_example;

import java.util.ArrayList;
import java.util.List;

/*
Каждый поток в цикле сформировал целочисленный массив из 3-х значений при обращении к
«атомарному» генератору последовательности. Как видно из результатов выполнения примера, значения не пересекаются.
* */
public class SequenceGeneratorExample {

  public static void main(String[] args) {
    SequenceGenerator sg = new SequenceGenerator();
    List<Sequence> sequences = new ArrayList<Sequence>();
    for (int i = 0; i < 10; i++) {
      Sequence seq = new Sequence(i + 1, 3, sg);
      sequences.add(seq);
    }
    System.out.println("\nРасчет последовательностей\n");
    int sum;
    // Ожидания завершения потоков
    do {
      sum = 0;
      for (int i = 0; i < sequences.size(); i++) {
        if (!sequences.get(i).thread.isAlive()) {
          sequences.get(i).printSequence();
          sum++;
        }
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    } while (sum < sequences.size());
    System.out.println("\n\nРабота потоков завершена");
    System.exit(0);
  }
}
