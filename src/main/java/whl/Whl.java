package whl;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import whl.gen.*;

import java.io.*;

/**
 * WHILE interpreter main class.
 *
 * @author Christian Klaussner
 */
public class Whl {
  /**
   * Entry point.
   *
   * @param args the program arguments
   */
  public static void main(String[] args) {
    Whl whl = new Whl();

    try {
      whl.start(args);
    } catch (WhlException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Gets a parser for the given WHILE source file.
   *
   * @param filename the filename of the program
   * @return a parser for the program
   */
  private WhileParser getParser(String filename) {
    ANTLRFileStream in;

    try {
      in = new ANTLRFileStream(filename);
    } catch (IOException e) {
      throw new WhlException("failed to load source file");
    }

    WhileLexer lexer = new WhileLexer(in);

    return new WhileParser(new CommonTokenStream(lexer));
  }

  /**
   * Evaluates the program arguments and runs the appropriate command.
   *
   * @param args the program arguments
   */
  private void start(String[] args) {
    if (args.length < 1) {
      System.out.println(
        "whl - WHILE interpreter (v1.0.0)\n" +
        "Usage: run <source file>"
      );

      System.exit(0);
    }

    String cmd = args[0];

    switch (cmd) {
      case "run":
        if (args.length < 2) {
          throw new WhlException("no input file specified");
        } else if (args.length > 2) {
          throw new WhlException("too many arguments");
        }

        run(args[1]);
        break;

      default:
        throw new WhlException("unknown command '" + cmd + "'");
    }
  }

  /**
   * Interprets the given WHILE source file.
   *
   * @param filename the filename of the program to interpret
   */
  private void run(String filename) {
    WhileParser parser = getParser(filename);

    ParseTree tree = parser.program();

    if (parser.getNumberOfSyntaxErrors() == 0) {
      RunVisitor v = new RunVisitor();
      v.visit(tree);

      System.out.print(v.getRootScope());
    }
  }
}
