JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath /usr/share/java/cup.jar:.
#CUP=$(JAVA) $(CLASSPATH) java_cup.Main <
CUP=cup

all: CM.class

CM.class: absyn/*.java parser.java sym.java Lexer.java symbol/*.java SymbolTable.java TypeChecker.java CodeGenerator.java CM.java

# -Xdiags:verbose
%.class: %.java
	$(JAVAC) $(CLASSPATH) $^

Lexer.java: cminus.flex
	$(JFLEX) cminus.flex

# -dump
parser.java: cminus.cup
	$(CUP) -expect 3 cminus.cup

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class symbol/*.class *~

