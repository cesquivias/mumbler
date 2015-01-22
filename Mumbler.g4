grammar Mumbler;

file : forms ;

forms : form* ;

form : '(' forms ')'                        # list
    | INT                                   # number
    | SYMBOL                                # symbol
    | BOOLEAN                               # bool
    ;

INT : [0-9]+ ;
SYMBOL : ~('#'|[()]|[ \t\r\n]) ~([()]|[ \t\r\n])* ;
BOOLEAN : ('#f'|'#t') ;

COMMENT : ';' .*? '\n' -> skip ;
WS : [ \t\r\n] -> skip ;
