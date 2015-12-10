package whl;

import whl.gen.WhileParser;

/**
 * The Procedure class is used to store the name, signature, and body of a
 * procedure.
 *
 * @author Christian Klaussner
 */
public class Procedure {
  private String name, result;
  private String[] params;
  private WhileParser.StmtSeqContext body;

  /**
   * Constructs a new Procedure object.
   *
   * @param name the name of the procedure
   * @param result the name of the result variable
   * @param params the parameter names
   * @param body the body of the procedure
   */
  public Procedure(String name, String[] params, String result,
                   WhileParser.StmtSeqContext body) {
    this.name = name;
    this.params = params;
    this.result = result;
    this.body = body;
  }

  /**
   * Gets the name of the procedure.
   *
   * @return the name of the procedure
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the parameter names.
   *
   * @return the parameter names
   */
  public String[] getParams() {
    return params;
  }

  /**
   * Gets the result variable name.
   *
   * @return the result variable name
   */
  public String getResult() {
    return result;
  }

  /**
   * Gets the body, i.e., statement sequence, of the procedure.
   *
   * @return the body of the procedure
   */
  public WhileParser.StmtSeqContext getBody() {
    return body;
  }
}
