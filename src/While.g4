grammar While;

program : stmtSeq EOF ;

arthExp
  : INT # intOpd
  | IDENT # varOpd
  | '(' arthExp ')' # arthParenOpd

  | arthExp op='*' arthExp # product
  | arthExp op=('+'|'-') arthExp # sum
  ;

boolExp
  : BOOL # boolOpd
  | '!' boolExp # not
  | '(' boolExp ')' # boolParenOpd

  | arthExp op=('='|'!='|'<'|'<='|'>'|'>=') arthExp # compare
  | boolExp 'and' boolExp # and
  | boolExp 'or' boolExp # or
  ;

stmt
  : 'skip' # skip
  | IDENT ':=' arthExp # assign
  | 'if' boolExp 'then' stmtSeq ('else' stmtSeq)? 'fi' # if
  | 'while' boolExp 'do' stmtSeq 'od' # while
  ;

stmtSeq
  : stmt ';' stmtSeq
  | stmt
  | // epsilon
  ;

ADD : '+' ;
SUB : '-' ;
MUL : '*' ;

EQ : '=' ;
NEQ : '!=' ;
LT : '<' ;
LTE : '<=' ;
GT : '>' ;
GTE : '>=' ;

INT : '-'?[0-9]+ ;
BOOL : 'true' | 'false' ;
IDENT : [a-zA-Z][a-zA-Z0-9]* ;
WS : [ \t\n\r]+ -> skip ;
