package collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class ArraySetExample {

  List<User> list;
  CopyOnWriteArraySet<User> cowSet;

  public ArraySetExample() {
    list = new ArrayList<User>();
    list.add(new User("Прохор "));
    list.add(new User("Георгий"));
    list.add(new User("Михаил"));

    cowSet = new CopyOnWriteArraySet<User>(list);

    System.out.println("Цикл с измением");

    Iterator<User> itr = cowSet.iterator();
    int cnt = 0;
    while (itr.hasNext()) {
      User user = itr.next();
      System.out.println("  " + user.name);
      if (++cnt == 2) {
        cowSet.add(new User("Павел"));
        user.name += " Иванович";
      }
    }

    System.out.println("\nЦикл без измения");
    itr = cowSet.iterator();
    while (itr.hasNext()) {
      User user = itr.next();
      System.out.println("  " + user.name);
    }
  }

  class User {

    private String name;

    public User(String name) {
      this.name = name;
    }
  }

  /*
  Рассмотрим пример ArraySetExample с использованием класса CopyOnWriteArraySet. В примере формируется набор данных list,
  на основании которого создается потокобезопасный набор cowSet типа CopyOnWriteArraySet. В качестве данных используется
  внутренний класс User. Данные коллекции cowSet с помощью итератора выводятся в консоль два раза.
  В первом цикле в коллекцию вносятся изменения : изменяется имя одного объекта и добавляется другой.
  Во втором цикле данные выводятся без изменений.

  В первом цикле в консоль выдены исходные данные несмотря на внесение изменений в набор. Во втором цикле —
  измененный набор данных. Пример подтверждает, что итератор набора данных CopyOnWriteArraySet не вызвал исключения
  ConcurrentModificationException при одновременном переборе и изменении значений.
  * */
  public static void main(String args[]) {
    new ArraySetExample();
  }
}
