University of Guelph
CIS 4650 - Compilers
Checkpoint One
March 6, 2017


---------- Authors ----------

Neivin Mathew (0948831)
Braydon Johnson (0785445)


---------- Building the Project ----------

To build the program, simply navigate to the directory that this README
is located in and run 'make'

Note: The program requires CUP, whose location should be added to the 
Java classpath in the Makefile. Currently, it is set to:

	CLASSPATH=-classpath /usr/share/java/cup.jar:.

Run 'make clean' to delete all build files.


---------- Running the Project ----------

To run the project, first build it by running 'make'.
Then, run the following command, substituting the CUP classpath for
the location of CUP on your machine:

	java -classpath /usr/share/java/cup.jar:. Main input.cm

To print out the Abstract Syntax Tree, use the command line option -a:

	java -classpath /usr/share/java/cup.jar:. Main -a input.cm


---------- Testing ----------

Five test files: [12345].cm are included in the 'test' directory.
They can be executed by running the above commands from the previous
section on the test files.


---------- Acknowledgements ----------

For this checkpoint, we used Professor Song's provided code in 'java_tiny'
as a starting point to build the project. We also followed the
recommended syntax tree structures for the C- language from the course
slides.