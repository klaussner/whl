package whl.analysis;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import whl.gen.*;

/**
 * The AnnotationListener class is used to annotate parse trees with labels.
 *
 * @author Christian Klaussner
 */
public class AnnotationListener extends WhileBaseListener {
  private ParseTreeProperty<Integer> labels;
  private int labelCounter;

  /**
   * Constructs a new AnnotationListener object.
   */
  public AnnotationListener() {
    labels = new ParseTreeProperty<>();
    labelCounter = 1;
  }

  /**
   * Gets the labels of the program.
   *
   * @return the set of labels
   */
  public ParseTreeProperty<Integer> getLabels() {
    return labels;
  }

  /**
   * Assigns a new label to the given context.
   *
   * @param ctx the context to assign a label to
   */
  private void assignLabel(ParserRuleContext ctx) {
    labels.put(ctx, labelCounter++);
  }

  @Override
  public void enterSkip(WhileParser.SkipContext ctx) {
    assignLabel(ctx);
  }

  @Override
  public void enterAssign(WhileParser.AssignContext ctx) {
    assignLabel(ctx);
  }

  @Override
  public void enterIf(WhileParser.IfContext ctx) {
    assignLabel(ctx.boolExp());
  }

  @Override
  public void enterWhile(WhileParser.WhileContext ctx) {
    assignLabel(ctx.boolExp());
  }
}
