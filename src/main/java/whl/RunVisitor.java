package whl;

import whl.gen.*;

import java.util.*;

/**
 * The RunVisitor class is used for interpreting WHILE programs.
 *
 * @author Christian Klaussner
 */
public class RunVisitor extends WhileBaseVisitor<Object> {
  private Stack<Scope> scopes;

  private HashMap<String, Procedure> procs;

  private Stack<Integer> intStack;
  private Stack<Boolean> boolStack;

  /**
   * Constructs a new RunVisitor object.
   */
  public RunVisitor() {
    scopes = new Stack<>();
    scopes.push(new Scope());

    procs = new HashMap<>();

    intStack = new Stack<>();
    boolStack = new Stack<>();
  }

  /**
   * Gets the root scope containing all variables and their values.
   *
   * @return the root scope
   */
  public Scope getRootScope() {
    return scopes.get(0);
  }

  // Arithmetic expressions

  /*
   * arthExp : INT ;
   */
  @Override
  public Void visitIntOpd(WhileParser.IntOpdContext ctx) {
    intStack.push(Types.intValue(ctx.INT()));

    return null;
  }

  /*
   * arthExp : IDENT ;
   */
  @Override
  public Void visitVarOpd(WhileParser.VarOpdContext ctx) {
    String name = ctx.IDENT().getText();
    Scope scope = scopes.peek();

    if (scope.exists(name)) {
      intStack.push(scope.get(name));
    } else {
      throw new WhlException("undefined variable '" + name + "'", ctx);
    }

    return null;
  }

  /*
   * arthExp : arthExp op=('*'|'/') arthExp ;
   */
  @Override
  public Void visitProduct(WhileParser.ProductContext ctx) {
    int a, b, result = 0;

    super.visitProduct(ctx);

    b = intStack.pop();
    a = intStack.pop();

    switch (ctx.op.getType()) {
      case WhileParser.MUL:
        result = a * b; break;

      case WhileParser.DIV:
        if (b == 0) {
          throw new WhlException("division by zero", ctx);
        }

        result = a / b; break;
    }

    intStack.push(result);

    return null;
  }

  /*
   * arthExp : arthExp op=('+'|'-') arthExp ;
   */
  @Override
  public Void visitSum(WhileParser.SumContext ctx) {
    int a, b, result = 0;

    super.visitSum(ctx);

    b = intStack.pop();
    a = intStack.pop();

    switch (ctx.op.getType()) {
      case WhileParser.ADD:
        result = a + b; break;

      case WhileParser.SUB:
        result = a - b; break;
    }

    intStack.push(result);

    return null;
  }

  // Boolean expressions

  /*
   * boolExp : BOOL ;
   */
  @Override
  public Void visitBoolOpd(WhileParser.BoolOpdContext ctx) {
    boolStack.push(Types.boolValue(ctx.BOOL()));

    return null;
  }

  /*
   * boolExp : '!' boolExp ;
   */
  @Override
  public Void visitNot(WhileParser.NotContext ctx) {
    visit(ctx.boolExp());
    boolStack.push(! boolStack.pop());

    return null;
  }

  /*
   * boolExp : arthExp op=('='|'!='|'<'|'<='|'>'|'>=') arthExp ;
   */
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

  /*
   * boolExp : boolExp 'and' boolExp ;
   */
  @Override
  public Void visitAnd(WhileParser.AndContext ctx) {
    visitChildren(ctx);
    boolStack.push(boolStack.pop() && boolStack.pop());

    return null;
  }

  /*
   * boolExp : boolExp 'or' boolExp ;
   */
  @Override
  public Void visitOr(WhileParser.OrContext ctx) {
    visitChildren(ctx);
    boolStack.push(boolStack.pop() || boolStack.pop());

    return null;
  }

  // Statements

  /*
   * stmt : IDENT ':=' arthExp ;
   */
  @Override
  public Void visitAssign(WhileParser.AssignContext ctx) {
    visit(ctx.arthExp());
    scopes.peek().set(ctx.IDENT().getText(), intStack.pop());

    return null;
  }

  /*
   * stmt : 'if' boolExp 'then' stmtSeq ('else' stmtSeq)? 'fi' ;
   */
  @Override
  public Void visitIf(WhileParser.IfContext ctx) {
    visit(ctx.boolExp());

    if (boolStack.pop()) {
      visit(ctx.stmtSeq(0));
    } else {
      WhileParser.StmtSeqContext elseContext = ctx.stmtSeq(1);

      if (elseContext != null) {
        visit(elseContext);
      }
    }

    return null;
  }

  /*
   * stmt : 'while' boolExp 'do' stmtSeq 'od' ;
   */
  @Override
  public Void visitWhile(WhileParser.WhileContext ctx) {
    visit(ctx.boolExp());

    while (boolStack.pop()) {
      visit(ctx.stmtSeq());
      visit(ctx.boolExp());
    }

    return null;
  }

  // Procedures

  /*
   * IDENT (',' IDENT)*
   */
  @Override
  public Object visitParamList(WhileParser.ParamListContext ctx) {
    SortedSet<String> params = new TreeSet<>();

    for (int i = 0; i < ctx.IDENT().size(); i++) {
      String param = ctx.IDENT(i).getText();

      if (! params.add(param)) {
        throw new WhlException("duplicate parameter '" + param + "'", ctx);
      }
    }

    return params.toArray(new String[params.size()]);
  }

  /*
   * arthExp (',' arthExp)*
   */
  @Override
  public Object visitArgList(WhileParser.ArgListContext ctx) {
    int[] values = new int[ctx.arthExp().size()];

    for (int i = 0; i < values.length; i++) {
      visit(ctx.arthExp(i));
      values[i] = intStack.pop();
    }

    return values;
  }

  /*
   * 'proc' name=IDENT '(' 'val' paramList ',' 'res' result=IDENT ')' 'is'
   *   stmtSeq 'end'
   */
  @Override
  public Void visitProc(WhileParser.ProcContext ctx) {
    String name = ctx.name.getText();

    String[] params = (String[]) visit(ctx.paramList());
    String result = ctx.result.getText();

    for (String param : params) {
      if (param.equals(result)) {
        throw new WhlException("result variable has to be unique", ctx);
      }
    }

    Procedure proc = new Procedure(name, params, result, ctx.stmtSeq());
    procs.put(name, proc);

    return null;
  }

  /*
   * 'call' name=IDENT '(' argList ',' result=IDENT ')'
   */
  @Override
  public Void visitCall(WhileParser.CallContext ctx) {
    String name = ctx.name.getText();
    String result = ctx.result.getText();

    Procedure proc = procs.get(name);

    if (proc == null) {
      throw new WhlException("undefined procedure", ctx);
    }

    // Write arguments to new scope
    Scope scope = new Scope();

    String[] params = proc.getParams();
    int[] values = (int[]) visit(ctx.argList());

    if (values.length != params.length) {
      throw new WhlException("wrong number of arguments", ctx);
    }

    for (int i = 0; i < values.length; i++) {
      scope.set(params[i], values[i]);
    }

    // Call procedure with new scope
    scopes.push(scope);
    visit(proc.getBody());
    scopes.pop();

    // Write result back to parent scope
    String procResult = proc.getResult();

    if (! scope.exists(procResult)) {
      throw new WhlException("result variable not written", ctx);
    }

    scopes.peek().set(result, scope.get(procResult));

    return null;
  }
}
