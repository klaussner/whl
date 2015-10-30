package whl;

import java.util.HashMap;

public class Scope {
  private HashMap<String, Integer> table;

  public Scope() {
    table = new HashMap<>();
  }

  public int get(String name) {
    if (table.containsKey(name)) {
      return table.get(name);
    } else {
      table.put(name, 0);

      return 0;
    }
  }

  public void set(String name, int value) {
    table.put(name, value);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();

    for (String name : table.keySet()) {
      s.append(name);
      s.append(": ");
      s.append(table.get(name));

      s.append("\n");
    }

    return s.toString();
  }
}
