// KIARA IDL Grammar
// Author: Dmitri Rubinstein
// Based on Thrift Grammar, original copyright:
//
// =================================================================================================
// Copyright 2011 Twitter, Inc.
// -------------------------------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this work except in compliance with the License.
// You may obtain a copy of the License in the LICENSE file, or at:
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// =================================================================================================
//
// Author: David Helder

grammar Kiara;

/* ******************** */

/* Program */
program:
  header_list definition_list;

/* Header list */
header_list: header*;

/* Header */
header
    : INCLUDE                LITERAL        # HeaderInclude
    | NAMESPACE t=IDENTIFIER n=IDENTIFIER   # HeaderNamedNamespace
    | NAMESPACE STAR          IDENTIFIER    # HeaderStarNamespace
    ;

definition_list: definition*;

definition:
  constDefinition |
  typeDefinition |
  service;

typeDefinition
    : nonAnnotatedTypeDefinition
    | annotationList annotatedTypeDefinition
    ;

nonAnnotatedTypeDefinition:
  typedef;

annotatedTypeDefinition
    : enum_t
    | senum
    | struct
    | union
    | xception
    | annotationDef
    ;

typedef:
  TYPEDEF fieldType IDENTIFIER;

commaOrSemicolon:
  (COMMA | SEMI);

enum_t:
  ENUM IDENTIFIER LBRACE enumDef* RBRACE;

enumDef:
  IDENTIFIER EQ intConstant  commaOrSemicolon? |
  IDENTIFIER  commaOrSemicolon?;

senum:
  SENUM IDENTIFIER LBRACE senumDef* RBRACE ;

senumDef:
  LITERAL commaOrSemicolon?;

constDefinition:
  CONST ft=fieldType id=IDENTIFIER EQ cv=constValue commaOrSemicolon?;

constValue
    : intConstant
    | (DUBCONSTANT
    | LITERAL
    | IDENTIFIER)
    | constList
    | constMap
    ;

constList:
  LBRACKET (constValue commaOrSemicolon?)* RBRACKET;

constMap:
  LBRACE (constValuePair)* RBRACE;

constValuePair:
  k=constValue COLON v=constValue commaOrSemicolon?;

struct:
  STRUCT IDENTIFIER xsdAll? LBRACE field* RBRACE;

union:
  UNION IDENTIFIER xsdAll? LBRACE field* RBRACE;

xsdAll:
  XSD_ALL;

xsdOptional:
  XSD_OPTIONAL;

xsdNillable:
  XSD_NILLABLE;

xsdAttributes:
  XSD_ATTRIBUTES LBRACE field* RBRACE;

xception:
  EXCEPTION IDENTIFIER LBRACE field* RBRACE;

annotationDef:
  ANNOTATION IDENTIFIER LBRACE field* RBRACE;

service:
  annotationList SERVICE IDENTIFIER (EXTENDS IDENTIFIER)? LBRACE function* RBRACE;

function:
  annotationList functionType annotationList IDENTIFIER LPAREN field* RPAREN throwz? commaOrSemicolon?;


throwz:
  THROWS LPAREN field* RPAREN;

field:
  fieldIdentifier? annotationList fieldRequiredness? fieldType IDENTIFIER fieldValue?
    xsdOptional? xsdNillable? xsdAttributes?
    commaOrSemicolon?
   ;

fieldIdentifier:
  intConstant COLON;

fieldRequiredness:
  REQUIRED |
  OPTIONAL;

fieldValue:
  EQ constValue;

functionType:
  VOID |
  fieldType;

fieldType
    : IDENTIFIER
    | baseType
    | genericType
    ;

baseType
    : simpleBaseType ;

simpleBaseType
    : STRING
    | BINARY
    | SLIST
    | BOOLEAN
    | I8
    | U8
    | I16
    | U16
    | I32
    | U32
    | I64
    | U64
    | FLOAT
    | DOUBLE
    | ANY
    ;

genericType:
    simpleGenericType;

simpleGenericType:
    IDENTIFIER genericTypeArgList;

genericTypeArg
    : (IDENTIFIER
    | VOID)
    | baseType
    | genericType
    | (INTCONSTANT
    | DUBCONSTANT
    | LITERAL)
    | constList
    | constMap
    ;

genericTypeArgList
    : LT genericTypeArg (COMMA genericTypeArg)* GT
    | LT GT
    ;

annotationList
    : LBRACKET annotation (COMMA annotation)* RBRACKET
    |
    ;

annotation
    : IDENTIFIER
    | IDENTIFIER LPAREN annotationArgList RPAREN
    ;

annotationArg
    : IDENTIFIER EQ constValue
    | constValue
    ;

annotationArgList
    : annotationArg (COMMA annotationArg)*
    |
    ;

intConstant: INTCONSTANT | HEXCONSTANT;

/* ******************** */

INCLUDE: 'include';
//CPP_INCLUDE: 'cpp_include';
NAMESPACE: 'namespace';
//CPP_NAMESPACE: 'cpp_namespace';
//PHP_NAMESPACE: 'php_namespace';
//PY_MODULE: 'py_module';
//PERL_PACKAGE: 'perl_package';
//RUBY_NAMESPACE: 'ruby_namespace';
//SMALLTALK_CATEGORY: 'smalltalk_category';
//SMALLTALK_PREFIX: 'smalltalk_prefix';
//JAVA_PACKAGE: 'java_package';
//COCOA_PACKAGE: 'cocoa_package';
//XSD_NAMESPACE: 'xsd_namespace';
//CSHARP_NAMESPACE: 'csharp_namespace';
TYPEDEF: 'typedef';
ENUM: 'enum';
SENUM: 'senum';
CONST: 'const';
STRUCT: 'struct';
UNION: 'union';
XSD_ALL: 'xsd_all';
XSD_OPTIONAL: 'xsd_optional';
XSD_NILLABLE: 'xsd_nillable';
XSD_ATTRIBUTES: 'xsd_attributes';
EXCEPTION: 'exception';
SERVICE: 'service';
EXTENDS: 'extends';
ONEWAY: 'oneway';
THROWS: 'throws';
REQUIRED: 'required';
OPTIONAL: 'optional';
ANNOTATION: 'annotation';
VOID: 'void';
STRING: 'string';
BINARY: 'binary';
SLIST: 'slist';
BOOLEAN: 'boolean';
I8: 'i8';
U8: 'u8';
I16: 'i16';
U16: 'u16';
I32: 'i32';
U32: 'u32';
I64: 'i64';
U64: 'u64';
FLOAT: 'float';
DOUBLE: 'double';
ANY: 'any';
STAR: '*';
COMMA: ',';
COLON: ':';
SEMI: ';';
LBRACE: '{';
RBRACE: '}';
LBRACKET: '[';
RBRACKET: ']';
LPAREN: '(';
RPAREN: ')';
EQ: '=';
LT: '<';
GT: '>';

INTCONSTANT   : ('+' | '-')? '0'..'9'+;
HEXCONSTANT   : '0x' ('0'..'9' | 'a'..'f' | 'A'..'F')+;


DUBCONSTANT   : ('+' | '-')? '0'..'9'+ ('.' '0'..'9'+)? EXPONENT?
               | ('+' | '-')? '0'..'9'* ('.' '0'..'9'+)  EXPONENT?;

fragment
EXPONENT : ('e' | 'E') ('+'|'-')? ('0'..'9')+ ;

IDENTIFIER    : ('a'..'z' | 'A'..'Z' | '_')
                ('.' | 'a'..'z' | 'A'..'Z' | '_' | '0'..'9')*;

WHITESPACE    : (' ' | '\t' | '\r' | '\n')+ -> channel(HIDDEN);


COMMENT
	: ('//'~[\n\r]*|'/*'(.|'\n')*?'*/')+ -> channel(HIDDEN);
UNIXCOMMENT   : ('#' (~'\n')*) -> channel(HIDDEN);
ST_IDENTIFIER : ('a'..'z' | 'A'..'Z' | '-')
                ('.' | 'a'..'z' | 'A'..'Z' | '_' | '0'..'9' | '-')*;
LITERAL       : (('\'' (~'\'')* '\'') | ('"' (~'"')* '"'));
