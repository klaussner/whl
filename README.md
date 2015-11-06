# whl

A small interpreter for the WHILE language.

## Usage

First, [download a release](https://github.com/klaussner/whl/releases) and unpack it.
Then you can run the interpreter (`whl.jar`) using the `whl` script (Bash script for Mac OS X/Linux and batch script for Windows):

    whl run <source file>

Alternatively, you can run the interpreter directly with the `java` command:

    java -jar whl.jar run <source file>

After the execution terminates, the interpreter outputs the final program state σ with all used variables and their values.

## History

* v0.1.0 (2015-10-30) - Initial release
* v0.2.0 (2015-11-02) - Added division operator; improved error output
* v0.3.0 (2015-11-06) - Added single-line comments; added `whl` script for Windows

## License

MIT
