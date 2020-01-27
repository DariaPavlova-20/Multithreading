package atomics.sequence_generator_example;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/*
* SequenceGenerator для генерирования последовательности
Для работы в многопоточной среде без блокировок используем атомарную ссылку AtomicReference, которая обеспечит хранение
* целочисленного значения типа java.math.BigInteger. Метод next возвращает текущее значение; переменная next вычисляет
* следующее значение. Метод compareAndSet атомарного класса element обеспечивает сохранение нового значения, если
* текущее не изменилось. Таким образом, метод next возвращает текущее значение и увеличивает его в 2 раза.
* */
public class SequenceGenerator {

  private static BigInteger MULTIPLIER;
  private AtomicReference<BigInteger> element;

  public SequenceGenerator() {
    if (MULTIPLIER == null) {
      MULTIPLIER = BigInteger.valueOf(2);
    }
    element = new AtomicReference<BigInteger>(
        BigInteger.ONE);
  }

  public BigInteger next() {
    BigInteger value;
    BigInteger next;
    do {
      value = element.get();
      next = value.multiply(MULTIPLIER);
    } while (!element.compareAndSet(value, next));
    return value;
  }
}
