package whl.analysis;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import whl.gen.*;

import java.util.*;

/**
 * The FinalVisitor class is used to retrieve the final labels of a program.
 *
 * @author Christian Klaussner
 */
public class FinalVisitor extends WhileBaseVisitor<Void> {
  private ParseTreeProperty<Integer> labels;
  private Set<Integer> finalLabels;

  /**
   * Constructs a new FinalVisitor object.
   *
   * @param labels the labels of the program
   */
  public FinalVisitor(ParseTreeProperty<Integer> labels) {
    this.labels = labels;

    finalLabels = new HashSet<>();
  }

  /**
   * Gets the final labels of the program.
   *
   * @return the set of final labels
   */
  public Set<Integer> getFinalLabels() {
    return finalLabels;
  }

  /*
   * stmt : 'skip' ;
   */
  @Override
  public Void visitSkip(WhileParser.SkipContext ctx) {
    finalLabels.add(labels.get(ctx));

    return null;
  }

  /*
   * stmt : 'IDENT ':=' arthExp ;
   */
  @Override
  public Void visitAssign(WhileParser.AssignContext ctx) {
    finalLabels.add(labels.get(ctx));

    return null;
  }

  /*
   * stmt : 'if' boolExp 'then' stmtSeq ('else' stmtSeq)? 'fi' ;
   */
  @Override
  public Void visitIf(WhileParser.IfContext ctx) {
    visit(ctx.stmtSeq(0));

    WhileParser.StmtSeqContext elseContext = ctx.stmtSeq(1);

    if (elseContext != null) {
      visit(elseContext);
    }

    return null;
  }

  /*
   * stmt : 'while' boolExp 'do' stmtSeq 'od' ;
   */
  @Override
  public Void visitWhile(WhileParser.WhileContext ctx) {
    finalLabels.add(labels.get(ctx.boolExp()));

    return null;
  }

  /*
   * stmtSeq : stmt ';' stmtSeq | stmt | ϵ ;
   */
  @Override
  public Void visitStmtSeq(WhileParser.StmtSeqContext ctx) {
    WhileParser.StmtSeqContext stmtSeq = ctx.stmtSeq();

    if (stmtSeq != null) {
      visit(stmtSeq);
    } else {
      visitChildren(ctx);
    }

    return null;
  }
}
