* C-Minus Compilation to TM Code
* File: fac.tm
* Standard prelude:
  0:     LD  6,0(0) 	load gp with maxaddress
  1:    LDA  5,0(6) 	copy to gp to fp
  2:     ST  0,0(0) 	clear location 0
* Jump around i/o routines here
* code for input routine
  4:     ST  0,-1(5) 	store return
  5:     IN  0,0,0 	input
  6:     LD  7,-1(5) 	return to caller
* code for output routine
  7:     ST  0,-1(5) 	store return
  8:     LD  0,-2(5) 	load output value
  9:    OUT  0,0,0 	output
 10:     LD  7,-1(5) 	return to caller
  3:    LDA  7,7(7) 	jump around i/o code
* End of standard prelude.
* processing function: main
* jump around function body here
 12:     ST  0,-1(5) 	store return
* -> compound statement
* processing local var: x
* processing local var: fac
* -> op
* -> id
* looking up id: x
 13:    LDA  0,-2(5) 	load id address
* <- id
 14:     ST  0,-4(5) 	op: push left
* -> call of function: input
 15:     ST  5,-5(5) 	push ofp
 16:    LDA  5,-5(5) 	push frame
 17:    LDA  0,1(7) 	load ac with ret ptr
 18:    LDA  7,-15(7) 	jump to fun loc
 19:     LD  5,0(5) 	pop frame
* <- call
 20:     LD  1,-4(5) 	op: load left
 21:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: fac
 22:    LDA  0,-3(5) 	load id address
* <- id
 23:     ST  0,-4(5) 	op: push left
* -> constant
 24:    LDC  0,1(0) 	load const
* <- constant
 25:     LD  1,-4(5) 	op: load left
 26:     ST  0,0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: x
 27:     LD  0,-2(5) 	load id value
* <- id
 28:     ST  0,-4(5) 	op: push left
* -> constant
 29:    LDC  0,1(0) 	load const
* <- constant
 30:     LD  1,-4(5) 	op: load left
 31:    SUB  0,1,0 	op >
 32:    JGT  0,2(7) 	br if true
 33:    LDC  0,0(0) 	false case
 34:    LDA  7,1(7) 	unconditional jmp
 35:    LDC  0,1(0) 	true case
* <- op
* while: jump to end belongs here
* -> compound statement
* -> op
* -> id
* looking up id: fac
 37:    LDA  0,-3(5) 	load id address
* <- id
 38:     ST  0,-4(5) 	op: push left
* -> op
* -> id
* looking up id: fac
 39:     LD  0,-3(5) 	load id value
* <- id
 40:     ST  0,-5(5) 	op: push left
* -> id
* looking up id: x
 41:     LD  0,-2(5) 	load id value
* <- id
 42:     LD  1,-5(5) 	op: load left
 43:    MUL  0,1,0 	op *
* <- op
 44:     LD  1,-4(5) 	op: load left
 45:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: x
 46:    LDA  0,-2(5) 	load id address
* <- id
 47:     ST  0,-4(5) 	op: push left
* -> op
* -> id
* looking up id: x
 48:     LD  0,-2(5) 	load id value
* <- id
 49:     ST  0,-5(5) 	op: push left
* -> constant
 50:    LDC  0,1(0) 	load const
* <- constant
 51:     LD  1,-5(5) 	op: load left
 52:    SUB  0,1,0 	op -
* <- op
 53:     LD  1,-4(5) 	op: load left
 54:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
 55:    LDA  7,-29(7) 	while: absolute jmp to test
 36:    JEQ  0,19(7) 	while: jmp to end
* <- while
* -> call of function: output
* -> id
* looking up id: fac
 56:     LD  0,-3(5) 	load id value
* <- id
 57:     ST  0,-6(5) 	store arg val
 58:     ST  5,-4(5) 	push ofp
 59:    LDA  5,-4(5) 	push frame
 60:    LDA  0,1(7) 	load ac with ret ptr
 61:    LDA  7,-55(7) 	jump to fun loc
 62:     LD  5,0(5) 	pop frame
* <- call
* <- compound statement
 63:     LD  7,-1(5) 	return to caller
 11:    LDA  7,52(7) 	jump around fn body
* <- fundecl
 64:     ST  5,0(5) 	push ofp
 65:    LDA  5,0(5) 	push frame
 66:    LDA  0,1(7) 	load ac with ret ptr
 67:    LDA  7,-56(7) 	jump to main loc
 68:     LD  5,0(5) 	pop frame
* End of execution.
 69:   HALT  0,0,0 	
