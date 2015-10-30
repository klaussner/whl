package whl;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * The Types class contains helpers for converting data types.
 *
 * @author Christian Klaussner
 */
public class Types {
  /**
   * Gets the integer value of the given node.
   *
   * @param node the node to convert
   * @return the integer value of the node
   */
  public static int intValue(TerminalNode node) {
    return Integer.parseInt(node.getText());
  }

  /**
   * Gets the boolean value of the given node.
   *
   * @param node the node to convert
   * @return the boolean value of the node
   */
  public static boolean boolValue(TerminalNode node) {
    String text = node.getText();

    return text.equals("true");
  }
}
