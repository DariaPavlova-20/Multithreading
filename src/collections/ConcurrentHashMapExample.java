package collections;

/*
 * Информационные сообщения при выполнении примера выводятся в консоль. Что мы видим? При использование класса
 * ConcurrentHashMap цикл перебора с использованием итератора завершился нормально; в консоль попал также новый
 * объект с ключом "2", добавленный в набор во время итерации. А вот при использовании класса HashMap цикл был
 * прерван вызовом исключения ConcurrentModificationException, как и ожидалось. Место ошибки : String key = it.next();.
 * Т.е. итератор вызывает исключение при обращении к следующему объекту, если набор изменился.
 * */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {

  Map<String, String> map;

  public ConcurrentHashMapExample() {
    System.out.println("ConcurrentHashMap");
    createMap(true);
    addValue(true);

    System.out.println("\n\nHashMap");
    createMap(false);
    addValue(false);
  }

  private void addValue(boolean concurrent) {
    System.out.println("  before iterator : " + map);
    Iterator<String> it = map.keySet().iterator();

    System.out.print("  cycle : ");
    while (it.hasNext()) {
      String key = it.next();
      if (key.equals("2")) {
        map.put(key + "new", "222");
        /*if (!concurrent)
          break;*/
      } else {
        System.out.print("  " + key + "="
            + map.get(key));
      }
    }
    System.out.println();
    System.out.println("  after iterator : " + map);
  }

  private void createMap(boolean concurrent) {
    if (concurrent) {
      map = new ConcurrentHashMap<>();
    } else {
      map = new HashMap<>();
    }
    map.put("1", "1");
    map.put("2", "1");
    map.put("3", "1");
    map.put("4", "1");
    map.put("5", "1");
    map.put("6", "1");
  }

  public static void main(String[] args) {
    new ConcurrentHashMapExample();
    System.exit(0);
  }
}