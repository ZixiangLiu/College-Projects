# Syntax error recovery on Calculator language
Partner: Yukun Chen
#### Calculator language created by Prof. Micheal Scott, University of Rochester
#### Code structure also provided by Prof. Micheal Scott

Print AST of input program with syntax error recovery.

P	→ 	SL $$
SL	→ 	S SL  |  ε
S	→ 	id := R  |  read id  |  write R  |  if R SL fi  |  do SL od  |  check R
R	→ 	E ET
E	→ 	T TT
T	→ 	F FT
F	→ 	( R )  |  id  |  lit
ET	→ 	ro E  |  ε
TT	→ 	ao T TT  |  ε
FT	→ 	mo F FT  |  ε
ro	→ 	==  |  <>  |  <  |  >  |  <=  |  >=
ao	→ 	+  |  -
mo	→ 	*  |  /
