grammar While;

program
  : procSeq (';' stmtSeqPlus)? EOF
  | stmtSeq EOF
  ;

arthExp
  : INT # intOpd
  | IDENT # varOpd
  | '(' arthExp ')' # arthParenOpd

  | arthExp op=('*'|'/') arthExp # product
  | arthExp op=('+'|'-') arthExp # sum
  ;

boolExp
  : BOOL # boolOpd
  | 'not' boolExp # not
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
  | 'call' name=IDENT '(' argList ',' result=IDENT ')' # call
  ;

stmtSeqPlus : stmt (';' stmt)* ;

stmtSeq
  : stmtSeqPlus
  | // epsilon
  ;

paramList : IDENT (',' IDENT)* ;

argList : arthExp (',' arthExp)* ;

proc : 'proc' name=IDENT '(' 'val' paramList ',' 'res' result=IDENT ')' 'is'
  stmtSeq 'end' ;

procSeq : proc (';' proc)* ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;

EQ : '=' ;
NEQ : '!=' ;
LT : '<' ;
LTE : '<=' ;
GT : '>' ;
GTE : '>=' ;

INT : '-'?[0-9]+ ;
BOOL : 'true' | 'false' ;
IDENT : [a-zA-Z][a-zA-Z0-9]* ;
WS : [ \t\n\r]+ -> channel(HIDDEN) ;
COMMENT : '#' ~[\n\r]* -> skip ;
