package collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {

  List<String> list;

  public CopyOnWriteArrayListExample() {
    List<String> lst = new ArrayList<String>();
    lst.add("Java");
    lst.add("J2EE");
    lst.add("J2SE");
    lst.add("Collection");
    lst.add("Concurrent");

    list = new CopyOnWriteArrayList<String>(lst);

    System.out.println("ЦИКЛ с изменением");
    printCollection(true);
    System.out.println("\nЦИКЛ без изменением");
    printCollection(false);

  }

  private void printCollection(boolean change) {
    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
      String element = iterator.next();
      System.out.printf("  %s %n", element);
      if (change) {
        if (element.equals("Collection")) {
          list.add("Новая строка");
          list.remove(element);
        }
      }
    }
  }

  /*
  Рассмотрим простой пример CopyOnWriteArrayListExample, в котором используем класс CopyOnWriteArrayList.
  В примере формируется набор данных lst, на основании которого создается потокобезопасная коллекция list
  типа CopyOnWriteArrayList. Данные коллекции list с помощью итератора выводятся в консоль два раза. В первом цикле
  в коллекцию вносятся изменения, во втором цикле данные выводятся без изменений.

  * В первом цикле, несмотря на внесение изменений в набор, в консоли представлены исходные данные.
  * Во втором цикле — измененный набор данных. Пример демонстрирует, что итератор набора данных CopyOnWriteArrayList
  * не вызвал исключения ConcurrentModificationException при одновременном внесении изменений и
  * переборе значений — это значит, что алгоритм CopyOnWrite действует.
   * */
  public static void main(String args[]) {
    new CopyOnWriteArrayListExample();
    System.exit(0);
  }
}
