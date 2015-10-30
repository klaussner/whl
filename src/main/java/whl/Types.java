package whl;

import org.antlr.v4.runtime.tree.TerminalNode;

public class Types {
  public static int intValue(TerminalNode node) {
    return Integer.parseInt(node.getText());
  }

  public static boolean boolValue(TerminalNode node) {
    String text = node.getText();

    return text.equals("true");
  }
}
