# C- Compiler
This repository contains a compiler for the C- language written for the Compilers course offered at the University of Guelph (CIS4650) in Winter 2017.

Browse this repository at your own risk if you are taking this course. I do not recommend copying anything from here verbatim as this is not a perfect solution.
There are _**quite a few errors**_ in this code (especially in the generation of assembly code).

## Authors
- Neivin Mathew
- Braydon Johnson


## Building the Project

To build the program, simply navigate to the directory that this README is located in and run 'make'

Note: The program requires CUP, whose location should be added to the Java classpath in the Makefile. Currently, it is set to:

    CLASSPATH=-classpath /usr/share/java/cup.jar:.

Run `make clean` to delete all build files.


## Running the Project

To run the project, first build it by running `make`.
Then, run the following command, substituting the CUP classpath for
the location of CUP on your machine:

	java -classpath /usr/share/java/cup.jar:. CM input.cm

To print out the Abstract Syntax Tree, use the command line option -a:

	java -classpath /usr/share/java/cup.jar:. CM -a input.cm

To print out the Symbol Table that is generated when parsing, use the
command line option -s:

	java -classpath /usr/share/java/cup.jar:. CM -s input.cm

To generate the Assembly code, use the command line option -c:

	java -classpath /usr/share/java/cup.jar:. CM -c input.cm


## Testing
Ten test files: `[1234567890].cm` are included in the `test` directory.
`[1234].cm` will compile properly without errors and can generate
assembly code.

`[567890].cm` contain multiple syntax and semantic errors and will
not generate assembly code.

They can be executed by running the above commands from the previous
section on the test files.


## Acknowledgements
For this checkpoint, we built upon our work from Checkpoint One and Two.

For the first checkpoint we used Professor Fei Song's provided code
as a starting point to build the project. We also followed the
recommended syntax tree structures for the C- language from the course
slides.
