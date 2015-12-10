package whl;

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
}
