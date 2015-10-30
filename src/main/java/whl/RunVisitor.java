package whl;

import whl.gen.*;

import java.util.Stack;

/**
 * @author Christian Klaussner
 */
public class RunVisitor extends WhileBaseVisitor<Void> {
  private Scope rootScope;

  private Stack<Integer> intStack;
  private Stack<Boolean> boolStack;

  /**
   * Constructs a new RunVisitor object.
   */
  public RunVisitor() {
    rootScope = new Scope();

    intStack = new Stack<>();
    boolStack = new Stack<>();
  }

  /**
   * Gets the root scope containing all variables and their values.
   * @return the root scope
   */
  public Scope getRootScope() {
    return rootScope;
  }

  // Arithmetic expressions

  @Override
  public Void visitIntOpd(WhileParser.IntOpdContext ctx) {
    intStack.push(Types.intValue(ctx.INT()));

    return null;
  }

  @Override
  public Void visitVarOpd(WhileParser.VarOpdContext ctx) {
    intStack.push(rootScope.get(ctx.IDENT().getText()));

    return null;
  }

  @Override
  public Void visitProduct(WhileParser.ProductContext ctx) {
    int a, b;

    super.visitProduct(ctx);

    b = intStack.pop();
    a = intStack.pop();

    intStack.push(a * b);

    return null;
  }

  @Override
  public Void visitSum(WhileParser.SumContext ctx) {
    int a, b, result = 0;

    super.visitSum(ctx);

    b = intStack.pop();
    a = intStack.pop();

    switch(ctx.op.getType()) {
      case WhileParser.ADD:
        result = a + b; break;

      case WhileParser.SUB:
        result = a - b; break;
    }

    intStack.push(result);

    return null;
  }

  // Boolean expressions

  @Override
  public Void visitBoolOpd(WhileParser.BoolOpdContext ctx) {
    boolStack.push(Types.boolValue(ctx.BOOL()));

    return null;
  }

  @Override
  public Void visitNot(WhileParser.NotContext ctx) {
    visit(ctx.boolExp());
    boolStack.push(! boolStack.pop());

    return null;
  }

  @Override
  public Void visitCompare(WhileParser.CompareContext ctx) {
    int a, b;
    boolean result = false;

    visitChildren(ctx);

    b = intStack.pop();
    a = intStack.pop();

    switch (ctx.op.getType()) {
      case WhileParser.EQ:
        result = a == b; break;

      case WhileParser.NEQ:
        result = a != b; break;

      case WhileParser.LT:
        result = a < b; break;

      case WhileParser.LTE:
        result = a <= b; break;

      case WhileParser.GT:
        result = a > b; break;

      case WhileParser.GTE:
        result = a >= b; break;
    }

    boolStack.push(result);

    return null;
  }

  @Override
  public Void visitAnd(WhileParser.AndContext ctx) {
    visitChildren(ctx);
    boolStack.push(boolStack.pop() && boolStack.pop());

    return null;
  }

  @Override
  public Void visitOr(WhileParser.OrContext ctx) {
    visitChildren(ctx);
    boolStack.push(boolStack.pop() || boolStack.pop());

    return null;
  }

  // Statements

  @Override
  public Void visitAssign(WhileParser.AssignContext ctx) {
    visit(ctx.arthExp());
    rootScope.set(ctx.IDENT().getText(), intStack.pop());

    return null;
  }

  @Override
  public Void visitIf(WhileParser.IfContext ctx) {
    visit(ctx.boolExp());

    if (boolStack.pop()) {
      visit(ctx.stmtSeq(0));
    } else {
      WhileParser.StmtSeqContext elseContext = ctx.stmtSeq(1);

      if (elseContext != null) {
        visit(ctx.stmtSeq(1));
      }
    }

    return null;
  }

  @Override
  public Void visitWhile(WhileParser.WhileContext ctx) {
    visit(ctx.boolExp());

    while (boolStack.pop()) {
      visit(ctx.stmtSeq());
      visit(ctx.boolExp());
    }

    return null;
  }
}