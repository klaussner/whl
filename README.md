# whl

A small interpreter for the WHILE language.

## Usage

First, [download a release](https://github.com/klaussner/whl/releases) and unpack it.
Then you can run the interpreter (`whl.jar`) using the `whl` helper script (only for Mac OS X and Linux) or directly with the `java` command:

    whl run <source file>
    java -jar whl.jar run <source file>

After the execution terminates, the interpreter outputs the final program state σ with all used variables and their values.

## History

* v0.1.0 (2015-10-30) - Initial release

## License

MIT
