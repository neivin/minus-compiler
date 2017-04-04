* C-Minus Compilation to TM Code
* File: sort.tm
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
* allocating global var: x
* <- vardecl
* processing function: minloc
* jump around function body here
 12:     ST  0,-1(5) 	store return
* -> compound statement
* processing local var: i
* processing local var: x
* processing local var: k
* -> op
* -> id
* looking up id: k
 13:    LDA  0,-7(5) 	load id address
* <- id
 14:     ST  0,-8(5) 	op: push left
* -> id
* looking up id: low
 15:     LD  0,-3(5) 	load id value
* <- id
 16:     LD  1,-8(5) 	op: load left
 17:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: x
 18:    LDA  0,-6(5) 	load id address
* <- id
 19:     ST  0,-8(5) 	op: push left
* -> subs
 20:     LD  0,-2(5) 	load id value
 21:     ST  0,-9(5) 	store array addr
* -> id
* looking up id: low
 22:     LD  0,-3(5) 	load id value
* <- id
 23:    JLT  0,1(7) 	halt if subscript < 0
 24:    LDA  7,1(7) 	absolute jump if not
 25:   HALT  0,0,0 	halt if subscript < 0
 26:     LD  1,-9(5) 	load array base addr
 27:    SUB  0,1,0 	base is at top of array
 28:     LD  0,0(0) 	load value at array index
* <- subs
 29:     LD  1,-8(5) 	op: load left
 30:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
 31:    LDA  0,-5(5) 	load id address
* <- id
 32:     ST  0,-8(5) 	op: push left
* -> op
* -> id
* looking up id: low
 33:     LD  0,-3(5) 	load id value
* <- id
 34:     ST  0,-9(5) 	op: push left
* -> constant
 35:    LDC  0,1(0) 	load const
* <- constant
 36:     LD  1,-9(5) 	op: load left
 37:    ADD  0,1,0 	op +
* <- op
 38:     LD  1,-8(5) 	op: load left
 39:     ST  0,0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
 40:     LD  0,-5(5) 	load id value
* <- id
 41:     ST  0,-8(5) 	op: push left
* -> id
* looking up id: high
 42:     LD  0,-4(5) 	load id value
* <- id
 43:     LD  1,-8(5) 	op: load left
 44:    SUB  0,1,0 	op <
 45:    JLT  0,2(7) 	br if true
 46:    LDC  0,0(0) 	false case
 47:    LDA  7,1(7) 	unconditional jmp
 48:    LDC  0,1(0) 	true case
* <- op
* while: jump to end belongs here
* -> compound statement
* -> if
* -> op
* -> subs
 50:     LD  0,-2(5) 	load id value
 51:     ST  0,-8(5) 	store array addr
* -> id
* looking up id: i
 52:     LD  0,-5(5) 	load id value
* <- id
 53:    JLT  0,1(7) 	halt if subscript < 0
 54:    LDA  7,1(7) 	absolute jump if not
 55:   HALT  0,0,0 	halt if subscript < 0
 56:     LD  1,-8(5) 	load array base addr
 57:    SUB  0,1,0 	base is at top of array
 58:     LD  0,0(0) 	load value at array index
* <- subs
 59:     ST  0,-8(5) 	op: push left
* -> id
* looking up id: x
 60:     LD  0,-6(5) 	load id value
* <- id
 61:     LD  1,-8(5) 	op: load left
 62:    SUB  0,1,0 	op <
 63:    JLT  0,2(7) 	br if true
 64:    LDC  0,0(0) 	false case
 65:    LDA  7,1(7) 	unconditional jmp
 66:    LDC  0,1(0) 	true case
* <- op
* if: jump to else belongs here
* -> compound statement
* -> op
* -> id
* looking up id: x
 68:    LDA  0,-6(5) 	load id address
* <- id
 69:     ST  0,-8(5) 	op: push left
* -> subs
 70:     LD  0,-2(5) 	load id value
 71:     ST  0,-9(5) 	store array addr
* -> id
* looking up id: i
 72:     LD  0,-5(5) 	load id value
* <- id
 73:    JLT  0,1(7) 	halt if subscript < 0
 74:    LDA  7,1(7) 	absolute jump if not
 75:   HALT  0,0,0 	halt if subscript < 0
 76:     LD  1,-9(5) 	load array base addr
 77:    SUB  0,1,0 	base is at top of array
 78:     LD  0,0(0) 	load value at array index
* <- subs
 79:     LD  1,-8(5) 	op: load left
 80:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: k
 81:    LDA  0,-7(5) 	load id address
* <- id
 82:     ST  0,-8(5) 	op: push left
* -> id
* looking up id: i
 83:     LD  0,-5(5) 	load id value
* <- id
 84:     LD  1,-8(5) 	op: load left
 85:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
* if: jump to end belongs here
 67:    JEQ  0,19(7) 	if: jmp to else
 86:    LDA  7,0(7) 	jmp to end
* <- if
* -> op
* -> id
* looking up id: i
 87:    LDA  0,-5(5) 	load id address
* <- id
 88:     ST  0,-8(5) 	op: push left
* -> op
* -> id
* looking up id: i
 89:     LD  0,-5(5) 	load id value
* <- id
 90:     ST  0,-9(5) 	op: push left
* -> constant
 91:    LDC  0,1(0) 	load const
* <- constant
 92:     LD  1,-9(5) 	op: load left
 93:    ADD  0,1,0 	op +
* <- op
 94:     LD  1,-8(5) 	op: load left
 95:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
 96:    LDA  7,-57(7) 	while: absolute jmp to test
 49:    JEQ  0,47(7) 	while: jmp to end
* <- while
* -> return
* -> id
* looking up id: k
 97:     LD  0,-7(5) 	load id value
* <- id
 98:     LD  7,-1(5) 	return to caller
* <- return
* <- compound statement
 99:     LD  7,-1(5) 	return to caller
 11:    LDA  7,88(7) 	jump around fn body
* <- fundecl
* processing function: sort
* jump around function body here
101:     ST  0,-1(5) 	store return
* -> compound statement
* processing local var: i
* processing local var: k
* -> op
* -> id
* looking up id: i
102:    LDA  0,-5(5) 	load id address
* <- id
103:     ST  0,-7(5) 	op: push left
* -> id
* looking up id: low
104:     LD  0,-3(5) 	load id value
* <- id
105:     LD  1,-7(5) 	op: load left
106:     ST  0,0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
107:     LD  0,-5(5) 	load id value
* <- id
108:     ST  0,-7(5) 	op: push left
* -> op
* -> id
* looking up id: high
109:     LD  0,-4(5) 	load id value
* <- id
110:     ST  0,-8(5) 	op: push left
* -> constant
111:    LDC  0,1(0) 	load const
* <- constant
112:     LD  1,-8(5) 	op: load left
113:    SUB  0,1,0 	op -
* <- op
114:     LD  1,-7(5) 	op: load left
115:    SUB  0,1,0 	op <
116:    JLT  0,2(7) 	br if true
117:    LDC  0,0(0) 	false case
118:    LDA  7,1(7) 	unconditional jmp
119:    LDC  0,1(0) 	true case
* <- op
* while: jump to end belongs here
* -> compound statement
* processing local var: t
* -> op
* -> id
* looking up id: k
121:    LDA  0,-6(5) 	load id address
* <- id
122:     ST  0,-8(5) 	op: push left
* -> call of function: minloc
* -> id
* looking up id: a
123:     LD  0,-2(5) 	load id value
* <- id
124:     ST  0,-11(5) 	store arg val
* -> id
* looking up id: i
125:     LD  0,-5(5) 	load id value
* <- id
126:     ST  0,-12(5) 	store arg val
* -> id
* looking up id: high
127:     LD  0,-4(5) 	load id value
* <- id
128:     ST  0,-13(5) 	store arg val
129:     ST  5,-9(5) 	push ofp
130:    LDA  5,-9(5) 	push frame
131:    LDA  0,1(7) 	load ac with ret ptr
132:    LDA  7,-121(7) 	jump to fun loc
133:     LD  5,0(5) 	pop frame
* <- call
134:     LD  1,-8(5) 	op: load left
135:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: t
136:    LDA  0,-7(5) 	load id address
* <- id
137:     ST  0,-8(5) 	op: push left
* -> subs
138:     LD  0,-2(5) 	load id value
139:     ST  0,-9(5) 	store array addr
* -> id
* looking up id: k
140:     LD  0,-6(5) 	load id value
* <- id
141:    JLT  0,1(7) 	halt if subscript < 0
142:    LDA  7,1(7) 	absolute jump if not
143:   HALT  0,0,0 	halt if subscript < 0
144:     LD  1,-9(5) 	load array base addr
145:    SUB  0,1,0 	base is at top of array
146:     LD  0,0(0) 	load value at array index
* <- subs
147:     LD  1,-8(5) 	op: load left
148:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> subs
149:     LD  0,-2(5) 	load id value
150:     ST  0,-8(5) 	store array addr
* -> id
* looking up id: k
151:     LD  0,-6(5) 	load id value
* <- id
152:    JLT  0,1(7) 	halt if subscript < 0
153:    LDA  7,1(7) 	absolute jump if not
154:   HALT  0,0,0 	halt if subscript < 0
155:     LD  1,-8(5) 	load array base addr
156:    SUB  0,1,0 	base is at top of array
* <- subs
157:     ST  0,-8(5) 	op: push left
* -> subs
158:     LD  0,-2(5) 	load id value
159:     ST  0,-9(5) 	store array addr
* -> id
* looking up id: i
160:     LD  0,-5(5) 	load id value
* <- id
161:    JLT  0,1(7) 	halt if subscript < 0
162:    LDA  7,1(7) 	absolute jump if not
163:   HALT  0,0,0 	halt if subscript < 0
164:     LD  1,-9(5) 	load array base addr
165:    SUB  0,1,0 	base is at top of array
166:     LD  0,0(0) 	load value at array index
* <- subs
167:     LD  1,-8(5) 	op: load left
168:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> subs
169:     LD  0,-2(5) 	load id value
170:     ST  0,-8(5) 	store array addr
* -> id
* looking up id: i
171:     LD  0,-5(5) 	load id value
* <- id
172:    JLT  0,1(7) 	halt if subscript < 0
173:    LDA  7,1(7) 	absolute jump if not
174:   HALT  0,0,0 	halt if subscript < 0
175:     LD  1,-8(5) 	load array base addr
176:    SUB  0,1,0 	base is at top of array
* <- subs
177:     ST  0,-8(5) 	op: push left
* -> id
* looking up id: t
178:     LD  0,-7(5) 	load id value
* <- id
179:     LD  1,-8(5) 	op: load left
180:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
181:    LDA  0,-5(5) 	load id address
* <- id
182:     ST  0,-8(5) 	op: push left
* -> op
* -> id
* looking up id: i
183:     LD  0,-5(5) 	load id value
* <- id
184:     ST  0,-9(5) 	op: push left
* -> constant
185:    LDC  0,1(0) 	load const
* <- constant
186:     LD  1,-9(5) 	op: load left
187:    ADD  0,1,0 	op +
* <- op
188:     LD  1,-8(5) 	op: load left
189:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
190:    LDA  7,-84(7) 	while: absolute jmp to test
120:    JEQ  0,70(7) 	while: jmp to end
* <- while
* <- compound statement
191:     LD  7,-1(5) 	return to caller
100:    LDA  7,91(7) 	jump around fn body
* <- fundecl
* processing function: main
* jump around function body here
193:     ST  0,-1(5) 	store return
* -> compound statement
* processing local var: i
* -> op
* -> id
* looking up id: i
194:    LDA  0,-2(5) 	load id address
* <- id
195:     ST  0,-3(5) 	op: push left
* -> constant
196:    LDC  0,0(0) 	load const
* <- constant
197:     LD  1,-3(5) 	op: load left
198:     ST  0,0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
199:     LD  0,-2(5) 	load id value
* <- id
200:     ST  0,-3(5) 	op: push left
* -> constant
201:    LDC  0,10(0) 	load const
* <- constant
202:     LD  1,-3(5) 	op: load left
203:    SUB  0,1,0 	op <
204:    JLT  0,2(7) 	br if true
205:    LDC  0,0(0) 	false case
206:    LDA  7,1(7) 	unconditional jmp
207:    LDC  0,1(0) 	true case
* <- op
* while: jump to end belongs here
* -> compound statement
* -> op
* -> subs
209:    LDA  0,0(6) 	load id address
210:     ST  0,-3(5) 	store array addr
* -> id
* looking up id: i
211:     LD  0,-2(5) 	load id value
* <- id
212:    JLT  0,1(7) 	halt if subscript < 0
213:    LDA  7,1(7) 	absolute jump if not
214:   HALT  0,0,0 	halt if subscript < 0
215:     LD  1,-3(5) 	load array base addr
216:    SUB  0,1,0 	base is at top of array
* <- subs
217:     ST  0,-3(5) 	op: push left
* -> call of function: input
218:     ST  5,-4(5) 	push ofp
219:    LDA  5,-4(5) 	push frame
220:    LDA  0,1(7) 	load ac with ret ptr
221:    LDA  7,-218(7) 	jump to fun loc
222:     LD  5,0(5) 	pop frame
* <- call
223:     LD  1,-3(5) 	op: load left
224:     ST  0,0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
225:    LDA  0,-2(5) 	load id address
* <- id
226:     ST  0,-3(5) 	op: push left
* -> op
* -> id
* looking up id: i
227:     LD  0,-2(5) 	load id value
* <- id
228:     ST  0,-4(5) 	op: push left
* -> constant
229:    LDC  0,1(0) 	load const
* <- constant
230:     LD  1,-4(5) 	op: load left
231:    ADD  0,1,0 	op +
* <- op
232:     LD  1,-3(5) 	op: load left
233:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
234:    LDA  7,-36(7) 	while: absolute jmp to test
208:    JEQ  0,26(7) 	while: jmp to end
* <- while
* -> call of function: sort
* -> id
* looking up id: x
235:    LDA  0,0(6) 	load id address
* <- id
236:     ST  0,-5(5) 	store arg val
* -> constant
237:    LDC  0,0(0) 	load const
* <- constant
238:     ST  0,-6(5) 	store arg val
* -> constant
239:    LDC  0,10(0) 	load const
* <- constant
240:     ST  0,-7(5) 	store arg val
241:     ST  5,-3(5) 	push ofp
242:    LDA  5,-3(5) 	push frame
243:    LDA  0,1(7) 	load ac with ret ptr
244:    LDA  7,-144(7) 	jump to fun loc
245:     LD  5,0(5) 	pop frame
* <- call
* -> op
* -> id
* looking up id: i
246:    LDA  0,-2(5) 	load id address
* <- id
247:     ST  0,-3(5) 	op: push left
* -> constant
248:    LDC  0,0(0) 	load const
* <- constant
249:     LD  1,-3(5) 	op: load left
250:     ST  0,0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
251:     LD  0,-2(5) 	load id value
* <- id
252:     ST  0,-3(5) 	op: push left
* -> constant
253:    LDC  0,10(0) 	load const
* <- constant
254:     LD  1,-3(5) 	op: load left
255:    SUB  0,1,0 	op <
256:    JLT  0,2(7) 	br if true
257:    LDC  0,0(0) 	false case
258:    LDA  7,1(7) 	unconditional jmp
259:    LDC  0,1(0) 	true case
* <- op
* while: jump to end belongs here
* -> compound statement
* -> call of function: output
* -> subs
261:    LDA  0,0(6) 	load id address
262:     ST  0,-5(5) 	store array addr
* -> id
* looking up id: i
263:     LD  0,-2(5) 	load id value
* <- id
264:    JLT  0,1(7) 	halt if subscript < 0
265:    LDA  7,1(7) 	absolute jump if not
266:   HALT  0,0,0 	halt if subscript < 0
267:     LD  1,-5(5) 	load array base addr
268:    SUB  0,1,0 	base is at top of array
269:     LD  0,0(0) 	load value at array index
* <- subs
270:     ST  0,-5(5) 	store arg val
271:     ST  5,-3(5) 	push ofp
272:    LDA  5,-3(5) 	push frame
273:    LDA  0,1(7) 	load ac with ret ptr
274:    LDA  7,-268(7) 	jump to fun loc
275:     LD  5,0(5) 	pop frame
* <- call
* -> op
* -> id
* looking up id: i
276:    LDA  0,-2(5) 	load id address
* <- id
277:     ST  0,-3(5) 	op: push left
* -> op
* -> id
* looking up id: i
278:     LD  0,-2(5) 	load id value
* <- id
279:     ST  0,-4(5) 	op: push left
* -> constant
280:    LDC  0,1(0) 	load const
* <- constant
281:     LD  1,-4(5) 	op: load left
282:    ADD  0,1,0 	op +
* <- op
283:     LD  1,-3(5) 	op: load left
284:     ST  0,0(1) 	assign: store value
* <- op
* <- compound statement
285:    LDA  7,-35(7) 	while: absolute jmp to test
260:    JEQ  0,25(7) 	while: jmp to end
* <- while
* <- compound statement
286:     LD  7,-1(5) 	return to caller
192:    LDA  7,94(7) 	jump around fn body
* <- fundecl
287:     ST  5,-10(5) 	push ofp
288:    LDA  5,-10(5) 	push frame
289:    LDA  0,1(7) 	load ac with ret ptr
290:    LDA  7,-98(7) 	jump to main loc
291:     LD  5,0(5) 	pop frame
* End of execution.
292:   HALT  0,0,0 	
