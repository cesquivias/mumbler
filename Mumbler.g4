grammar Mumbler;

file : form* ;

form : '(' form* ')'            # list
    | '\'' form                 # quote
    | INT                       # number
    | BOOLEAN                   # bool
    | SYMBOL                    # symbol
    ;

INT : [0-9]+ ;
SYMBOL : ~('#'|'\''|[()]|[ \t\r\n]) ~('\''|[()]|[ \t\r\n])* ;
BOOLEAN : ('#f'|'#t') ;

COMMENT : ';' .*? '\n' -> skip ;
WS : [ \t\r\n] -> skip ;
