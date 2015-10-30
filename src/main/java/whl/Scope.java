package whl;

import java.util.*;

public class Scope {
  private HashMap<String, Integer> table;

  public Scope() {
    table = new HashMap<>();
  }

  public int get(String name) {
    if (table.containsKey(name)) {
      return table.get(name);
    } else {
      throw new WhlException("undefined variable '" + name +  "'");
    }
  }

  public void set(String name, int value) {
    table.put(name, value);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();

    // Sort keys by name
    List<String> keys = new ArrayList<>(table.keySet());
    Collections.sort(keys);

    for (String name : keys) {
      s.append(name);
      s.append(": ");
      s.append(table.get(name));

      s.append("\n");
    }

    return s.toString();
  }
}
