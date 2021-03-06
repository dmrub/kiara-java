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

// Generates a parser that parses thrift IDL files and emits a simplified Abstract Syntax Tree.
// ThriftTreeWalker.g then parses that AST and generates descriptors.
//
// For completeness, this grammar accepts the entire thrift IDL language, even the rarely used
// parts that we don't support. The tree walker acts only on the subset it supports and ignores
// the rest.

// Author: David Helder
// Modified for ANTLR4 by Dmitri Rubinstein

grammar Thrift;

/* ******************** */

program:
  header* definition*;

header:
  INCLUDE                LITERAL |
  CPP_INCLUDE            LITERAL  |
  NAMESPACE t=IDENTIFIER n=IDENTIFIER |
  NAMESPACE STAR          IDENTIFIER |
  CPP_NAMESPACE          IDENTIFIER |
  PHP_NAMESPACE          IDENTIFIER |
  PY_MODULE              IDENTIFIER |
  PERL_PACKAGE           IDENTIFIER |
  RUBY_NAMESPACE         IDENTIFIER |
  SMALLTALK_CATEGORY     ST_IDENTIFIER |
  SMALLTALK_PREFIX       IDENTIFIER |
  JAVA_PACKAGE           IDENTIFIER |
  COCOA_PACKAGE          IDENTIFIER |
  XSD_NAMESPACE          LITERAL |
  CSHARP_NAMESPACE       IDENTIFIER;

definition:
  constDefinition |
  typeDefinition |
  service;

typeDefinition:
  typedef |
  enum_t |
  senum |
  struct |
  union |
  xception;

typedef:
  TYPEDEF fieldType IDENTIFIER typeAnnotations?;

commaOrSemicolon:
  (COMMA | SEMI);

enum_t:
  ENUM IDENTIFIER LBRACE enumDef* RBRACE typeAnnotations?;

enumDef:
  IDENTIFIER EQ intConstant typeAnnotations? commaOrSemicolon? |
  IDENTIFIER typeAnnotations? commaOrSemicolon?;

senum:
  SENUM IDENTIFIER LBRACE senumDef* RBRACE typeAnnotations?;

senumDef:
  LITERAL commaOrSemicolon?;

constDefinition:
  CONST ft=fieldType id=IDENTIFIER EQ cv=constValue commaOrSemicolon?;

constValue:
  intConstant |
  (DUBCONSTANT |
  LITERAL |
  IDENTIFIER) |
  constList |
  constMap;

constList:
  LBRACKET (constValue commaOrSemicolon?)* RBRACKET;

constMap:
  LBRACE (constValuePair)* RBRACE;

constValuePair:
  k=constValue COLON v=constValue commaOrSemicolon?;

struct:
  STRUCT IDENTIFIER xsdAll? LBRACE field* RBRACE typeAnnotations?;

union:
  UNION IDENTIFIER xsdAll? LBRACE field* RBRACE typeAnnotations?;

xsdAll:
  XSD_ALL;

xsdOptional:
  XSD_OPTIONAL;

xsdNillable:
  XSD_NILLABLE;

xsdAttributes:
  XSD_ATTRIBUTES LBRACE field* RBRACE;

xception:
  EXCEPTION IDENTIFIER LBRACE field* RBRACE typeAnnotations?;

service:
  SERVICE IDENTIFIER (EXTENDS IDENTIFIER)? LBRACE function* RBRACE typeAnnotations?;

function:
  oneway? functionType IDENTIFIER LPAREN field* RPAREN throwz? typeAnnotations? commaOrSemicolon?;

oneway:
  ONEWAY;

throwz:
  THROWS LPAREN field* RPAREN;

field:
  fieldIdentifier? fieldRequiredness? fieldType IDENTIFIER fieldValue?
    xsdOptional? xsdNillable? xsdAttributes? typeAnnotations?
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

fieldType:
  IDENTIFIER |
  baseType |
  containerType;

baseType:
  simpleBaseType typeAnnotations?;

simpleBaseType:
  STRING |
  BINARY |
  SLIST  |
  BOOL   |
  BYTE   |
  I16    |
  I32    |
  I64    |
  DOUBLE;

containerType:
  simpleContainerType typeAnnotations?;

simpleContainerType:
  mapType | setType | listType;

mapType:
  MAP cppType? LT ft1=fieldType COMMA ft2=fieldType GT;

setType:
  SET cppType? LT ft=fieldType GT;

// It's weird, and probably an error, but the original thrift yacc
// grammar puts cppType after the angle brackets for lists, but before
// for sets and maps. The cpp_type isn't actually used anyway, so this
// probably doesn't actually matter.
listType:
  LIST LT ft=fieldType GT cppType?;

cppType:
  CPP_TYPE i=LITERAL;

typeAnnotations:
  LPAREN typeAnnotation* RPAREN;

typeAnnotation:
  i=IDENTIFIER EQ l=LITERAL commaOrSemicolon?;

intConstant: INTCONSTANT | HEXCONSTANT;

/* ******************** */

INCLUDE: 'include';
CPP_INCLUDE: 'cpp_include';
NAMESPACE: 'namespace';
CPP_NAMESPACE: 'cpp_namespace';
PHP_NAMESPACE: 'php_namespace';
PY_MODULE: 'py_module';
PERL_PACKAGE: 'perl_package';
RUBY_NAMESPACE: 'ruby_namespace';
SMALLTALK_CATEGORY: 'smalltalk_category';
SMALLTALK_PREFIX: 'smalltalk_prefix';
JAVA_PACKAGE: 'java_package';
COCOA_PACKAGE: 'cocoa_package';
XSD_NAMESPACE: 'xsd_namespace';
CSHARP_NAMESPACE: 'csharp_namespace';
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
VOID: 'void';
STRING: 'string';
BINARY: 'binary';
SLIST: 'slist';
BOOL: 'bool';
BYTE: 'byte';
I16: 'i16';
I32: 'i32';
I64: 'i64';
DOUBLE: 'double';
MAP: 'map';
SET: 'set';
LIST: 'list';
CPP_TYPE: 'cpp_type';
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
