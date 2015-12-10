package whl;

import java.util.*;

/**
 * The Scope class is used to store and retrieve the variables of a scope.
 *
 * @author Christian Klaussner
 */
public class Scope {
  private HashMap<String, Integer> table;

  /**
   * Constructs a new Scope object with an empty set of variables.
   */
  public Scope() {
    table = new HashMap<>();
  }

  /**
   * Retrieves the value of of the variable with the given name.
   * This method throws a {@link WhlException} if the variable is undefined.
   *
   * @param name the name of the variable
   * @return the value of the variable
   */
  public int get(String name) {
    if (table.containsKey(name)) {
      return table.get(name);
    } else {
      throw new WhlException("undefined variable '" + name +  "'");
    }
  }

  /**
   * Checks whether the given variable exists in the scope.
   *
   * @param name the name of the variable
   * @return true if the variable exists, otherwise false
   */
  public boolean exists(String name) {
    return table.containsKey(name);
  }

  /**
   * Sets the value of the variable with the given name and defines it if
   * necessary.
   *
   * @param name the name of the variable
   * @param value the new value of the variable
   */
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
