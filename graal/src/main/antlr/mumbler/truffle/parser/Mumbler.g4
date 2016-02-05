grammar Mumbler;

@header {
    package mumbler.truffle.parser;
}

file : form* ;

form : '(' form* ')'            # list
    | '\'' form                 # quote
    | INT                       # number
    | BOOLEAN                   # bool
    | STRING                    # string
    | SYMBOL                    # symbol
    ;

INT : [0-9]+ ;
BOOLEAN : ('#f'|'#t') ;
STRING : '"' ( ~'"' | '\\' '"')* '"' ;
SYMBOL : ~('#'|'"'|'\''|[()]|[ \t\r\n]) ~('"'|'\''|[()]|[ \t\r\n])* ;

COMMENT : ';' .*? '\n' -> skip ;
WS : [ \t\r\n] -> skip ;
