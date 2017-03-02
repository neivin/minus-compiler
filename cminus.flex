/*
	cminus.flex

*/

import java_cup.runtime.*;

%%

/* Class name for the lexer*/
%class Lexer
%line
%column

/* CUP compatibility */
%cup

/* Return null on EOF */
%eofval{
	return null;
%eofval}

%{
	private Symbol symbol(int type) {
			return new Symbol(type, yyline, yycolumn);
	}

	private Symbol symbol(int type, Object value) {
			return new Symbol(type, yyline, yycolumn, value);
	}
%}

/*	Macro Declarations */
lineTerminator = \r|\n|\r\n
whitespace = {lineTerminator}|[ \t\f]

letter = [a-zA-Z]
digit = [0-9]
id = {letter}{letter}*
num = {digit}{digit}*
comment = \/\*(.|[\r\n])*\*\/

%%

"if"						{ return symbol(sym.IF);}
"else"					{ return symbol(sym.ELSE); }
"while"					{ return symbol(sym.WHILE); }
"int"						{ return symbol(sym.INTEGER); }
"void"					{ return symbol(sym.VOID); }
"+"							{ return symbol(sym.PLUS); }
"-"							{ return symbol(sym.MINUS); }
"*"							{ return symbol(sym.MUL); }
"/"							{ return symbol(sym.DIV); }
"<"							{ return symbol(sym.LESS); }
"<="						{ return symbol(sym.LEQUIV); }
">"							{ return symbol(sym.GREATER); }
">="						{ return symbol(sym.GEQUIV); }
"=="						{ return symbol(sym.EQUIV); }
"!="						{ return symbol(sym.NEQUIV); }
"="							{ return symbol(sym.EQU); }
";"							{ return symbol(sym.SEMI); }
","							{ return symbol(sym.COMMA); }
"("							{ return symbol(sym.LPAREN); }
")"							{ return symbol(sym.RPAREN); }
"["							{ return symbol(sym.LSQUARE); }
"]"							{ return symbol(sym.RSQUARE); }
"{"							{ return symbol(sym.LCURLY); }
"}"							{ return symbol(sym.RCURLY); }
{num}						{ return symbol(sym.NUM, yytext()); }
{id}						{ return symbol(sym.ID, yytext()); }
{whitespace}		{ /* Do Nothing */ }
{comment}				{ /* Do Nothing */ }
.								{ return symbol(sym.ERROR); }
