grammar Mumbler;

file : form* ;

form : list
     | number
     | symbol
     | bool
     ;

list : '(' form* ')' ;

number : INT ;

symbol : SYMBOL ;

bool : BOOLEAN ;

INT : [0-9]+ ;
SYMBOL : ~('#'|[()]|[ \t\r\n]) ~([()]|[ \t\r\n])* ;
BOOLEAN : ('#f'|'#t') ;
WS : [ \t\r\n] -> skip ;
