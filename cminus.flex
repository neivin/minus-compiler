/*
	cminus.flex

*/

import java_cup.runtime;

%%

/* Class name for the lexer*/
%class Lexer

%line
%column

/* CUP compatibility */
%cup
%{
	private Symbol symbol(int type) {
			return new Symbol(type, yyline, yycolumn);
	}

	private Symbol symbol(int type, Object value) {
			return new Symbol(type, yyline, yycolumn, value);
	}
%}
/*
	Macro Declarations
*/
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator}|[\t\f]
letter = [a-zA-Z]
digit = [0-9]
ID = [letter][letter]*
NUM = [digit][digit]*

%%

"if"				{ return symbol(sym.IF);}
"else"			{ return symbol(sym.ELSE); }
"while"			{ return symbol(sym.WHILE); }
"int"				{ return symbol(sym.INT); }
"void"			{ return symbol(sym.VOID); }
"+"					{ return symbol(sym.PLUS); }
"-"					{ return symbol(sym.MINUS); }
"*"					{ return symbol(sym.MUL); }
"/"					{ return symbol(sym.DIV); }
"<"					{ return symbol(sym.LESS); }
"<="				{ return symbol(sym.LEQUIV); }
">"					{ return symbol(sym.GREATER); }
">="				{ return symbol(sym.GEQUIV); }
"=="				{ return symbol(sym.EQUIV); }
"!="				{ return symbol(sym.NEQUIV); }
"="					{ return symbol(sym.EQU); }
";"					{ return symbol(sym.SEMI); }
","					{ return symbol(sym.COMMA); }
"("					{ return symbol(sym.LPAREN); }
")"					{ return symbol(sym.RPAREN); }
"["					{ return symbol(sym.LSQUARE); }
"]"					{ return symbol(sym.RSQUARE); }
"{"					{ return symbol(sym.LCURLY); }
"}"					{ return symbol(sym.RCURLY); }
{NUM}		{ return symbol(sym.NUM); }
{ID}				{ return symbol(sym.ID); }
{WhiteSpace}	{ /* Do Nothing */ }
\/\*.*\*\/	{ /* Do Nothing */ }
.						{ return symbol(sym.ERROR); }
