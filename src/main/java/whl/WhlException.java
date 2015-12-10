package whl;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * The WhlException class is used to report input, parsing, interpretation, and
 * analysis errors.
 *
 * @author Christian Klaussner
 */
public class WhlException extends RuntimeException {
  /**
   * Constructs a new WhlException object.
   *
   * @param message the message
   */
  public WhlException(String message) {
    super(message);
  }

  /**
   * Constructs a new WhlException object for the given context. The line
   * number of the first token in this context is added to the message.
   *
   * @param message the message
   * @param ctx the context in which the exception occured
   */
  public WhlException(String message, ParserRuleContext ctx) {
    super("line " + ctx.start.getLine() + " " + message);
  }
}
