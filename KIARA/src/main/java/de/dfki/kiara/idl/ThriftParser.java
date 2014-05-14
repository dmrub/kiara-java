// Generated from /home/rubinste/proj_de3/kiara_java/KIARA_Java/KIARAParser/src/main/java/de/dfki/kiara/Thrift.g4 by ANTLR 4.2.2
package de.dfki.kiara.idl;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ThriftParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INCLUDE=1, CPP_INCLUDE=2, NAMESPACE=3, CPP_NAMESPACE=4, PHP_NAMESPACE=5,
		PY_MODULE=6, PERL_PACKAGE=7, RUBY_NAMESPACE=8, SMALLTALK_CATEGORY=9, SMALLTALK_PREFIX=10,
		JAVA_PACKAGE=11, COCOA_PACKAGE=12, XSD_NAMESPACE=13, CSHARP_NAMESPACE=14,
		TYPEDEF=15, ENUM=16, SENUM=17, CONST=18, STRUCT=19, UNION=20, XSD_ALL=21,
		XSD_OPTIONAL=22, XSD_NILLABLE=23, XSD_ATTRIBUTES=24, EXCEPTION=25, SERVICE=26,
		EXTENDS=27, ONEWAY=28, THROWS=29, REQUIRED=30, OPTIONAL=31, VOID=32, STRING=33,
		BINARY=34, SLIST=35, BOOL=36, BYTE=37, I16=38, I32=39, I64=40, DOUBLE=41,
		MAP=42, SET=43, LIST=44, CPP_TYPE=45, STAR=46, COMMA=47, COLON=48, SEMI=49,
		LBRACE=50, RBRACE=51, LBRACKET=52, RBRACKET=53, LPAREN=54, RPAREN=55,
		EQ=56, LT=57, GT=58, INTCONSTANT=59, HEXCONSTANT=60, DUBCONSTANT=61, IDENTIFIER=62,
		WHITESPACE=63, COMMENT=64, UNIXCOMMENT=65, ST_IDENTIFIER=66, LITERAL=67;
	public static final String[] tokenNames = {
		"<INVALID>", "'include'", "'cpp_include'", "'namespace'", "'cpp_namespace'",
		"'php_namespace'", "'py_module'", "'perl_package'", "'ruby_namespace'",
		"'smalltalk_category'", "'smalltalk_prefix'", "'java_package'", "'cocoa_package'",
		"'xsd_namespace'", "'csharp_namespace'", "'typedef'", "'enum'", "'senum'",
		"'const'", "'struct'", "'union'", "'xsd_all'", "'xsd_optional'", "'xsd_nillable'",
		"'xsd_attributes'", "'exception'", "'service'", "'extends'", "'oneway'",
		"'throws'", "'required'", "'optional'", "'void'", "'string'", "'binary'",
		"'slist'", "'bool'", "'byte'", "'i16'", "'i32'", "'i64'", "'double'",
		"'map'", "'set'", "'list'", "'cpp_type'", "'*'", "','", "':'", "';'",
		"'{'", "'}'", "'['", "']'", "'('", "')'", "'='", "'<'", "'>'", "INTCONSTANT",
		"HEXCONSTANT", "DUBCONSTANT", "IDENTIFIER", "WHITESPACE", "COMMENT", "UNIXCOMMENT",
		"ST_IDENTIFIER", "LITERAL"
	};
	public static final int
		RULE_program = 0, RULE_header = 1, RULE_definition = 2, RULE_typeDefinition = 3,
		RULE_typedef = 4, RULE_commaOrSemicolon = 5, RULE_enum_t = 6, RULE_enumDef = 7,
		RULE_senum = 8, RULE_senumDef = 9, RULE_constDefinition = 10, RULE_constValue = 11,
		RULE_constList = 12, RULE_constMap = 13, RULE_constValuePair = 14, RULE_struct = 15,
		RULE_union = 16, RULE_xsdAll = 17, RULE_xsdOptional = 18, RULE_xsdNillable = 19,
		RULE_xsdAttributes = 20, RULE_xception = 21, RULE_service = 22, RULE_function = 23,
		RULE_oneway = 24, RULE_throwz = 25, RULE_field = 26, RULE_fieldIdentifier = 27,
		RULE_fieldRequiredness = 28, RULE_fieldValue = 29, RULE_functionType = 30,
		RULE_fieldType = 31, RULE_baseType = 32, RULE_simpleBaseType = 33, RULE_containerType = 34,
		RULE_simpleContainerType = 35, RULE_mapType = 36, RULE_setType = 37, RULE_listType = 38,
		RULE_cppType = 39, RULE_typeAnnotations = 40, RULE_typeAnnotation = 41,
		RULE_intConstant = 42;
	public static final String[] ruleNames = {
		"program", "header", "definition", "typeDefinition", "typedef", "commaOrSemicolon",
		"enum_t", "enumDef", "senum", "senumDef", "constDefinition", "constValue",
		"constList", "constMap", "constValuePair", "struct", "union", "xsdAll",
		"xsdOptional", "xsdNillable", "xsdAttributes", "xception", "service",
		"function", "oneway", "throwz", "field", "fieldIdentifier", "fieldRequiredness",
		"fieldValue", "functionType", "fieldType", "baseType", "simpleBaseType",
		"containerType", "simpleContainerType", "mapType", "setType", "listType",
		"cppType", "typeAnnotations", "typeAnnotation", "intConstant"
	};

	@Override
	public String getGrammarFileName() { return "Thrift.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ThriftParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public HeaderContext header(int i) {
			return getRuleContext(HeaderContext.class,i);
		}
		public List<HeaderContext> header() {
			return getRuleContexts(HeaderContext.class);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INCLUDE) | (1L << CPP_INCLUDE) | (1L << NAMESPACE) | (1L << CPP_NAMESPACE) | (1L << PHP_NAMESPACE) | (1L << PY_MODULE) | (1L << PERL_PACKAGE) | (1L << RUBY_NAMESPACE) | (1L << SMALLTALK_CATEGORY) | (1L << SMALLTALK_PREFIX) | (1L << JAVA_PACKAGE) | (1L << COCOA_PACKAGE) | (1L << XSD_NAMESPACE) | (1L << CSHARP_NAMESPACE))) != 0)) {
				{
				{
				setState(86); header();
				}
				}
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPEDEF) | (1L << ENUM) | (1L << SENUM) | (1L << CONST) | (1L << STRUCT) | (1L << UNION) | (1L << EXCEPTION) | (1L << SERVICE))) != 0)) {
				{
				{
				setState(92); definition();
				}
				}
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public Token t;
		public Token n;
		public TerminalNode COCOA_PACKAGE() { return getToken(ThriftParser.COCOA_PACKAGE, 0); }
		public TerminalNode SMALLTALK_CATEGORY() { return getToken(ThriftParser.SMALLTALK_CATEGORY, 0); }
		public TerminalNode INCLUDE() { return getToken(ThriftParser.INCLUDE, 0); }
		public TerminalNode JAVA_PACKAGE() { return getToken(ThriftParser.JAVA_PACKAGE, 0); }
		public TerminalNode CPP_NAMESPACE() { return getToken(ThriftParser.CPP_NAMESPACE, 0); }
		public TerminalNode STAR() { return getToken(ThriftParser.STAR, 0); }
		public TerminalNode CPP_INCLUDE() { return getToken(ThriftParser.CPP_INCLUDE, 0); }
		public TerminalNode ST_IDENTIFIER() { return getToken(ThriftParser.ST_IDENTIFIER, 0); }
		public TerminalNode PY_MODULE() { return getToken(ThriftParser.PY_MODULE, 0); }
		public TerminalNode CSHARP_NAMESPACE() { return getToken(ThriftParser.CSHARP_NAMESPACE, 0); }
		public TerminalNode LITERAL() { return getToken(ThriftParser.LITERAL, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(ThriftParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(ThriftParser.IDENTIFIER, i);
		}
		public TerminalNode PERL_PACKAGE() { return getToken(ThriftParser.PERL_PACKAGE, 0); }
		public TerminalNode RUBY_NAMESPACE() { return getToken(ThriftParser.RUBY_NAMESPACE, 0); }
		public TerminalNode PHP_NAMESPACE() { return getToken(ThriftParser.PHP_NAMESPACE, 0); }
		public TerminalNode SMALLTALK_PREFIX() { return getToken(ThriftParser.SMALLTALK_PREFIX, 0); }
		public TerminalNode XSD_NAMESPACE() { return getToken(ThriftParser.XSD_NAMESPACE, 0); }
		public TerminalNode NAMESPACE() { return getToken(ThriftParser.NAMESPACE, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitHeader(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header);
		try {
			setState(130);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(98); match(INCLUDE);
				setState(99); match(LITERAL);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(100); match(CPP_INCLUDE);
				setState(101); match(LITERAL);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(102); match(NAMESPACE);
				setState(103); ((HeaderContext)_localctx).t = match(IDENTIFIER);
				setState(104); ((HeaderContext)_localctx).n = match(IDENTIFIER);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(105); match(NAMESPACE);
				setState(106); match(STAR);
				setState(107); match(IDENTIFIER);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(108); match(CPP_NAMESPACE);
				setState(109); match(IDENTIFIER);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(110); match(PHP_NAMESPACE);
				setState(111); match(IDENTIFIER);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(112); match(PY_MODULE);
				setState(113); match(IDENTIFIER);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(114); match(PERL_PACKAGE);
				setState(115); match(IDENTIFIER);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(116); match(RUBY_NAMESPACE);
				setState(117); match(IDENTIFIER);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(118); match(SMALLTALK_CATEGORY);
				setState(119); match(ST_IDENTIFIER);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(120); match(SMALLTALK_PREFIX);
				setState(121); match(IDENTIFIER);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(122); match(JAVA_PACKAGE);
				setState(123); match(IDENTIFIER);
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(124); match(COCOA_PACKAGE);
				setState(125); match(IDENTIFIER);
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(126); match(XSD_NAMESPACE);
				setState(127); match(LITERAL);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(128); match(CSHARP_NAMESPACE);
				setState(129); match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinitionContext extends ParserRuleContext {
		public TypeDefinitionContext typeDefinition() {
			return getRuleContext(TypeDefinitionContext.class,0);
		}
		public ServiceContext service() {
			return getRuleContext(ServiceContext.class,0);
		}
		public ConstDefinitionContext constDefinition() {
			return getRuleContext(ConstDefinitionContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitDefinition(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_definition);
		try {
			setState(135);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(132); constDefinition();
				}
				break;
			case TYPEDEF:
			case ENUM:
			case SENUM:
			case STRUCT:
			case UNION:
			case EXCEPTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(133); typeDefinition();
				}
				break;
			case SERVICE:
				enterOuterAlt(_localctx, 3);
				{
				setState(134); service();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDefinitionContext extends ParserRuleContext {
		public SenumContext senum() {
			return getRuleContext(SenumContext.class,0);
		}
		public StructContext struct() {
			return getRuleContext(StructContext.class,0);
		}
		public Enum_tContext enum_t() {
			return getRuleContext(Enum_tContext.class,0);
		}
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
		}
		public TypedefContext typedef() {
			return getRuleContext(TypedefContext.class,0);
		}
		public XceptionContext xception() {
			return getRuleContext(XceptionContext.class,0);
		}
		public TypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitTypeDefinition(this);
		}
	}

	public final TypeDefinitionContext typeDefinition() throws RecognitionException {
		TypeDefinitionContext _localctx = new TypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeDefinition);
		try {
			setState(143);
			switch (_input.LA(1)) {
			case TYPEDEF:
				enterOuterAlt(_localctx, 1);
				{
				setState(137); typedef();
				}
				break;
			case ENUM:
				enterOuterAlt(_localctx, 2);
				{
				setState(138); enum_t();
				}
				break;
			case SENUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(139); senum();
				}
				break;
			case STRUCT:
				enterOuterAlt(_localctx, 4);
				{
				setState(140); struct();
				}
				break;
			case UNION:
				enterOuterAlt(_localctx, 5);
				{
				setState(141); union();
				}
				break;
			case EXCEPTION:
				enterOuterAlt(_localctx, 6);
				{
				setState(142); xception();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypedefContext extends ParserRuleContext {
		public TerminalNode TYPEDEF() { return getToken(ThriftParser.TYPEDEF, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public TypedefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterTypedef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitTypedef(this);
		}
	}

	public final TypedefContext typedef() throws RecognitionException {
		TypedefContext _localctx = new TypedefContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typedef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145); match(TYPEDEF);
			setState(146); fieldType();
			setState(147); match(IDENTIFIER);
			setState(149);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(148); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommaOrSemicolonContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(ThriftParser.SEMI, 0); }
		public TerminalNode COMMA() { return getToken(ThriftParser.COMMA, 0); }
		public CommaOrSemicolonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commaOrSemicolon; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterCommaOrSemicolon(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitCommaOrSemicolon(this);
		}
	}

	public final CommaOrSemicolonContext commaOrSemicolon() throws RecognitionException {
		CommaOrSemicolonContext _localctx = new CommaOrSemicolonContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_commaOrSemicolon);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			_la = _input.LA(1);
			if ( !(_la==COMMA || _la==SEMI) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Enum_tContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public TerminalNode ENUM() { return getToken(ThriftParser.ENUM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public EnumDefContext enumDef(int i) {
			return getRuleContext(EnumDefContext.class,i);
		}
		public List<EnumDefContext> enumDef() {
			return getRuleContexts(EnumDefContext.class);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public Enum_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enum_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterEnum_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitEnum_t(this);
		}
	}

	public final Enum_tContext enum_t() throws RecognitionException {
		Enum_tContext _localctx = new Enum_tContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_enum_t);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153); match(ENUM);
			setState(154); match(IDENTIFIER);
			setState(155); match(LBRACE);
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(156); enumDef();
				}
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(162); match(RBRACE);
			setState(164);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(163); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumDefContext extends ParserRuleContext {
		public IntConstantContext intConstant() {
			return getRuleContext(IntConstantContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public TerminalNode EQ() { return getToken(ThriftParser.EQ, 0); }
		public EnumDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterEnumDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitEnumDef(this);
		}
	}

	public final EnumDefContext enumDef() throws RecognitionException {
		EnumDefContext _localctx = new EnumDefContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_enumDef);
		int _la;
		try {
			setState(182);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(166); match(IDENTIFIER);
				setState(167); match(EQ);
				setState(168); intConstant();
				setState(170);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(169); typeAnnotations();
					}
				}

				setState(173);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(172); commaOrSemicolon();
					}
				}

				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(175); match(IDENTIFIER);
				setState(177);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(176); typeAnnotations();
					}
				}

				setState(180);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(179); commaOrSemicolon();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SenumContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public SenumDefContext senumDef(int i) {
			return getRuleContext(SenumDefContext.class,i);
		}
		public List<SenumDefContext> senumDef() {
			return getRuleContexts(SenumDefContext.class);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public TerminalNode SENUM() { return getToken(ThriftParser.SENUM, 0); }
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public SenumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_senum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterSenum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitSenum(this);
		}
	}

	public final SenumContext senum() throws RecognitionException {
		SenumContext _localctx = new SenumContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_senum);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); match(SENUM);
			setState(185); match(IDENTIFIER);
			setState(186); match(LBRACE);
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LITERAL) {
				{
				{
				setState(187); senumDef();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193); match(RBRACE);
			setState(195);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(194); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SenumDefContext extends ParserRuleContext {
		public TerminalNode LITERAL() { return getToken(ThriftParser.LITERAL, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public SenumDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_senumDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterSenumDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitSenumDef(this);
		}
	}

	public final SenumDefContext senumDef() throws RecognitionException {
		SenumDefContext _localctx = new SenumDefContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_senumDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197); match(LITERAL);
			setState(199);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(198); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstDefinitionContext extends ParserRuleContext {
		public FieldTypeContext ft;
		public Token id;
		public ConstValueContext cv;
		public ConstValueContext constValue() {
			return getRuleContext(ConstValueContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public TerminalNode CONST() { return getToken(ThriftParser.CONST, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public TerminalNode EQ() { return getToken(ThriftParser.EQ, 0); }
		public ConstDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterConstDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitConstDefinition(this);
		}
	}

	public final ConstDefinitionContext constDefinition() throws RecognitionException {
		ConstDefinitionContext _localctx = new ConstDefinitionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_constDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201); match(CONST);
			setState(202); ((ConstDefinitionContext)_localctx).ft = fieldType();
			setState(203); ((ConstDefinitionContext)_localctx).id = match(IDENTIFIER);
			setState(204); match(EQ);
			setState(205); ((ConstDefinitionContext)_localctx).cv = constValue();
			setState(207);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(206); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstValueContext extends ParserRuleContext {
		public ConstMapContext constMap() {
			return getRuleContext(ConstMapContext.class,0);
		}
		public IntConstantContext intConstant() {
			return getRuleContext(IntConstantContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public TerminalNode LITERAL() { return getToken(ThriftParser.LITERAL, 0); }
		public ConstListContext constList() {
			return getRuleContext(ConstListContext.class,0);
		}
		public TerminalNode DUBCONSTANT() { return getToken(ThriftParser.DUBCONSTANT, 0); }
		public ConstValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterConstValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitConstValue(this);
		}
	}

	public final ConstValueContext constValue() throws RecognitionException {
		ConstValueContext _localctx = new ConstValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_constValue);
		int _la;
		try {
			setState(213);
			switch (_input.LA(1)) {
			case INTCONSTANT:
			case HEXCONSTANT:
				enterOuterAlt(_localctx, 1);
				{
				setState(209); intConstant();
				}
				break;
			case DUBCONSTANT:
			case IDENTIFIER:
			case LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(210);
				_la = _input.LA(1);
				if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (DUBCONSTANT - 61)) | (1L << (IDENTIFIER - 61)) | (1L << (LITERAL - 61)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(211); constList();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 4);
				{
				setState(212); constMap();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstListContext extends ParserRuleContext {
		public CommaOrSemicolonContext commaOrSemicolon(int i) {
			return getRuleContext(CommaOrSemicolonContext.class,i);
		}
		public List<ConstValueContext> constValue() {
			return getRuleContexts(ConstValueContext.class);
		}
		public ConstValueContext constValue(int i) {
			return getRuleContext(ConstValueContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(ThriftParser.LBRACKET, 0); }
		public List<CommaOrSemicolonContext> commaOrSemicolon() {
			return getRuleContexts(CommaOrSemicolonContext.class);
		}
		public TerminalNode RBRACKET() { return getToken(ThriftParser.RBRACKET, 0); }
		public ConstListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterConstList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitConstList(this);
		}
	}

	public final ConstListContext constList() throws RecognitionException {
		ConstListContext _localctx = new ConstListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215); match(LBRACKET);
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 50)) & ~0x3f) == 0 && ((1L << (_la - 50)) & ((1L << (LBRACE - 50)) | (1L << (LBRACKET - 50)) | (1L << (INTCONSTANT - 50)) | (1L << (HEXCONSTANT - 50)) | (1L << (DUBCONSTANT - 50)) | (1L << (IDENTIFIER - 50)) | (1L << (LITERAL - 50)))) != 0)) {
				{
				{
				setState(216); constValue();
				setState(218);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(217); commaOrSemicolon();
					}
				}

				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(225); match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstMapContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public ConstValuePairContext constValuePair(int i) {
			return getRuleContext(ConstValuePairContext.class,i);
		}
		public List<ConstValuePairContext> constValuePair() {
			return getRuleContexts(ConstValuePairContext.class);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public ConstMapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constMap; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterConstMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitConstMap(this);
		}
	}

	public final ConstMapContext constMap() throws RecognitionException {
		ConstMapContext _localctx = new ConstMapContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_constMap);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227); match(LBRACE);
			setState(231);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 50)) & ~0x3f) == 0 && ((1L << (_la - 50)) & ((1L << (LBRACE - 50)) | (1L << (LBRACKET - 50)) | (1L << (INTCONSTANT - 50)) | (1L << (HEXCONSTANT - 50)) | (1L << (DUBCONSTANT - 50)) | (1L << (IDENTIFIER - 50)) | (1L << (LITERAL - 50)))) != 0)) {
				{
				{
				setState(228); constValuePair();
				}
				}
				setState(233);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(234); match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstValuePairContext extends ParserRuleContext {
		public ConstValueContext k;
		public ConstValueContext v;
		public List<ConstValueContext> constValue() {
			return getRuleContexts(ConstValueContext.class);
		}
		public ConstValueContext constValue(int i) {
			return getRuleContext(ConstValueContext.class,i);
		}
		public TerminalNode COLON() { return getToken(ThriftParser.COLON, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public ConstValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterConstValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitConstValuePair(this);
		}
	}

	public final ConstValuePairContext constValuePair() throws RecognitionException {
		ConstValuePairContext _localctx = new ConstValuePairContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_constValuePair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236); ((ConstValuePairContext)_localctx).k = constValue();
			setState(237); match(COLON);
			setState(238); ((ConstValuePairContext)_localctx).v = constValue();
			setState(240);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(239); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public TerminalNode STRUCT() { return getToken(ThriftParser.STRUCT, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public XsdAllContext xsdAll() {
			return getRuleContext(XsdAllContext.class,0);
		}
		public StructContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterStruct(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitStruct(this);
		}
	}

	public final StructContext struct() throws RecognitionException {
		StructContext _localctx = new StructContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_struct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242); match(STRUCT);
			setState(243); match(IDENTIFIER);
			setState(245);
			_la = _input.LA(1);
			if (_la==XSD_ALL) {
				{
				setState(244); xsdAll();
				}
			}

			setState(247); match(LBRACE);
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(248); field();
				}
				}
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(254); match(RBRACE);
			setState(256);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(255); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnionContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public TerminalNode UNION() { return getToken(ThriftParser.UNION, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public XsdAllContext xsdAll() {
			return getRuleContext(XsdAllContext.class,0);
		}
		public UnionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_union; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitUnion(this);
		}
	}

	public final UnionContext union() throws RecognitionException {
		UnionContext _localctx = new UnionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_union);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258); match(UNION);
			setState(259); match(IDENTIFIER);
			setState(261);
			_la = _input.LA(1);
			if (_la==XSD_ALL) {
				{
				setState(260); xsdAll();
				}
			}

			setState(263); match(LBRACE);
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(264); field();
				}
				}
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(270); match(RBRACE);
			setState(272);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(271); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XsdAllContext extends ParserRuleContext {
		public TerminalNode XSD_ALL() { return getToken(ThriftParser.XSD_ALL, 0); }
		public XsdAllContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdAll; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterXsdAll(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitXsdAll(this);
		}
	}

	public final XsdAllContext xsdAll() throws RecognitionException {
		XsdAllContext _localctx = new XsdAllContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_xsdAll);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274); match(XSD_ALL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XsdOptionalContext extends ParserRuleContext {
		public TerminalNode XSD_OPTIONAL() { return getToken(ThriftParser.XSD_OPTIONAL, 0); }
		public XsdOptionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdOptional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterXsdOptional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitXsdOptional(this);
		}
	}

	public final XsdOptionalContext xsdOptional() throws RecognitionException {
		XsdOptionalContext _localctx = new XsdOptionalContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_xsdOptional);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276); match(XSD_OPTIONAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XsdNillableContext extends ParserRuleContext {
		public TerminalNode XSD_NILLABLE() { return getToken(ThriftParser.XSD_NILLABLE, 0); }
		public XsdNillableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdNillable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterXsdNillable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitXsdNillable(this);
		}
	}

	public final XsdNillableContext xsdNillable() throws RecognitionException {
		XsdNillableContext _localctx = new XsdNillableContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_xsdNillable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278); match(XSD_NILLABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XsdAttributesContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public TerminalNode XSD_ATTRIBUTES() { return getToken(ThriftParser.XSD_ATTRIBUTES, 0); }
		public XsdAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterXsdAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitXsdAttributes(this);
		}
	}

	public final XsdAttributesContext xsdAttributes() throws RecognitionException {
		XsdAttributesContext _localctx = new XsdAttributesContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_xsdAttributes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280); match(XSD_ATTRIBUTES);
			setState(281); match(LBRACE);
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(282); field();
				}
				}
				setState(287);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(288); match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XceptionContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public TerminalNode EXCEPTION() { return getToken(ThriftParser.EXCEPTION, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public XceptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xception; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterXception(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitXception(this);
		}
	}

	public final XceptionContext xception() throws RecognitionException {
		XceptionContext _localctx = new XceptionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_xception);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290); match(EXCEPTION);
			setState(291); match(IDENTIFIER);
			setState(292); match(LBRACE);
			setState(296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(293); field();
				}
				}
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(299); match(RBRACE);
			setState(301);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(300); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ServiceContext extends ParserRuleContext {
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public TerminalNode LBRACE() { return getToken(ThriftParser.LBRACE, 0); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(ThriftParser.IDENTIFIER, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(ThriftParser.IDENTIFIER); }
		public TerminalNode SERVICE() { return getToken(ThriftParser.SERVICE, 0); }
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ThriftParser.RBRACE, 0); }
		public TerminalNode EXTENDS() { return getToken(ThriftParser.EXTENDS, 0); }
		public ServiceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_service; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterService(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitService(this);
		}
	}

	public final ServiceContext service() throws RecognitionException {
		ServiceContext _localctx = new ServiceContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_service);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303); match(SERVICE);
			setState(304); match(IDENTIFIER);
			setState(307);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(305); match(EXTENDS);
				setState(306); match(IDENTIFIER);
				}
			}

			setState(309); match(LBRACE);
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ONEWAY) | (1L << VOID) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(310); function();
				}
				}
				setState(315);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(316); match(RBRACE);
			setState(318);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(317); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public ThrowzContext throwz() {
			return getRuleContext(ThrowzContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ThriftParser.RPAREN, 0); }
		public FunctionTypeContext functionType() {
			return getRuleContext(FunctionTypeContext.class,0);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public OnewayContext oneway() {
			return getRuleContext(OnewayContext.class,0);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ThriftParser.LPAREN, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			_la = _input.LA(1);
			if (_la==ONEWAY) {
				{
				setState(320); oneway();
				}
			}

			setState(323); functionType();
			setState(324); match(IDENTIFIER);
			setState(325); match(LPAREN);
			setState(329);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(326); field();
				}
				}
				setState(331);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(332); match(RPAREN);
			setState(334);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(333); throwz();
				}
			}

			setState(337);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(336); typeAnnotations();
				}
			}

			setState(340);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(339); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OnewayContext extends ParserRuleContext {
		public TerminalNode ONEWAY() { return getToken(ThriftParser.ONEWAY, 0); }
		public OnewayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneway; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterOneway(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitOneway(this);
		}
	}

	public final OnewayContext oneway() throws RecognitionException {
		OnewayContext _localctx = new OnewayContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_oneway);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342); match(ONEWAY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ThrowzContext extends ParserRuleContext {
		public TerminalNode THROWS() { return getToken(ThriftParser.THROWS, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ThriftParser.RPAREN, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode LPAREN() { return getToken(ThriftParser.LPAREN, 0); }
		public ThrowzContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwz; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterThrowz(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitThrowz(this);
		}
	}

	public final ThrowzContext throwz() throws RecognitionException {
		ThrowzContext _localctx = new ThrowzContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_throwz);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344); match(THROWS);
			setState(345); match(LPAREN);
			setState(349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(346); field();
				}
				}
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(352); match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public XsdAttributesContext xsdAttributes() {
			return getRuleContext(XsdAttributesContext.class,0);
		}
		public FieldRequirednessContext fieldRequiredness() {
			return getRuleContext(FieldRequirednessContext.class,0);
		}
		public XsdNillableContext xsdNillable() {
			return getRuleContext(XsdNillableContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public FieldValueContext fieldValue() {
			return getRuleContext(FieldValueContext.class,0);
		}
		public FieldIdentifierContext fieldIdentifier() {
			return getRuleContext(FieldIdentifierContext.class,0);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public XsdOptionalContext xsdOptional() {
			return getRuleContext(XsdOptionalContext.class,0);
		}
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitField(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			_la = _input.LA(1);
			if (_la==INTCONSTANT || _la==HEXCONSTANT) {
				{
				setState(354); fieldIdentifier();
				}
			}

			setState(358);
			_la = _input.LA(1);
			if (_la==REQUIRED || _la==OPTIONAL) {
				{
				setState(357); fieldRequiredness();
				}
			}

			setState(360); fieldType();
			setState(361); match(IDENTIFIER);
			setState(363);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(362); fieldValue();
				}
			}

			setState(366);
			_la = _input.LA(1);
			if (_la==XSD_OPTIONAL) {
				{
				setState(365); xsdOptional();
				}
			}

			setState(369);
			_la = _input.LA(1);
			if (_la==XSD_NILLABLE) {
				{
				setState(368); xsdNillable();
				}
			}

			setState(372);
			_la = _input.LA(1);
			if (_la==XSD_ATTRIBUTES) {
				{
				setState(371); xsdAttributes();
				}
			}

			setState(375);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(374); typeAnnotations();
				}
			}

			setState(378);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(377); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldIdentifierContext extends ParserRuleContext {
		public IntConstantContext intConstant() {
			return getRuleContext(IntConstantContext.class,0);
		}
		public TerminalNode COLON() { return getToken(ThriftParser.COLON, 0); }
		public FieldIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFieldIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFieldIdentifier(this);
		}
	}

	public final FieldIdentifierContext fieldIdentifier() throws RecognitionException {
		FieldIdentifierContext _localctx = new FieldIdentifierContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_fieldIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380); intConstant();
			setState(381); match(COLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldRequirednessContext extends ParserRuleContext {
		public TerminalNode OPTIONAL() { return getToken(ThriftParser.OPTIONAL, 0); }
		public TerminalNode REQUIRED() { return getToken(ThriftParser.REQUIRED, 0); }
		public FieldRequirednessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldRequiredness; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFieldRequiredness(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFieldRequiredness(this);
		}
	}

	public final FieldRequirednessContext fieldRequiredness() throws RecognitionException {
		FieldRequirednessContext _localctx = new FieldRequirednessContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_fieldRequiredness);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			_la = _input.LA(1);
			if ( !(_la==REQUIRED || _la==OPTIONAL) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldValueContext extends ParserRuleContext {
		public ConstValueContext constValue() {
			return getRuleContext(ConstValueContext.class,0);
		}
		public TerminalNode EQ() { return getToken(ThriftParser.EQ, 0); }
		public FieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFieldValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFieldValue(this);
		}
	}

	public final FieldValueContext fieldValue() throws RecognitionException {
		FieldValueContext _localctx = new FieldValueContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385); match(EQ);
			setState(386); constValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionTypeContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(ThriftParser.VOID, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public FunctionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFunctionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFunctionType(this);
		}
	}

	public final FunctionTypeContext functionType() throws RecognitionException {
		FunctionTypeContext _localctx = new FunctionTypeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_functionType);
		try {
			setState(390);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(388); match(VOID);
				}
				break;
			case STRING:
			case BINARY:
			case SLIST:
			case BOOL:
			case BYTE:
			case I16:
			case I32:
			case I64:
			case DOUBLE:
			case MAP:
			case SET:
			case LIST:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(389); fieldType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldTypeContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public ContainerTypeContext containerType() {
			return getRuleContext(ContainerTypeContext.class,0);
		}
		public BaseTypeContext baseType() {
			return getRuleContext(BaseTypeContext.class,0);
		}
		public FieldTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterFieldType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitFieldType(this);
		}
	}

	public final FieldTypeContext fieldType() throws RecognitionException {
		FieldTypeContext _localctx = new FieldTypeContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_fieldType);
		try {
			setState(395);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(392); match(IDENTIFIER);
				}
				break;
			case STRING:
			case BINARY:
			case SLIST:
			case BOOL:
			case BYTE:
			case I16:
			case I32:
			case I64:
			case DOUBLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(393); baseType();
				}
				break;
			case MAP:
			case SET:
			case LIST:
				enterOuterAlt(_localctx, 3);
				{
				setState(394); containerType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BaseTypeContext extends ParserRuleContext {
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public SimpleBaseTypeContext simpleBaseType() {
			return getRuleContext(SimpleBaseTypeContext.class,0);
		}
		public BaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitBaseType(this);
		}
	}

	public final BaseTypeContext baseType() throws RecognitionException {
		BaseTypeContext _localctx = new BaseTypeContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_baseType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397); simpleBaseType();
			setState(399);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(398); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleBaseTypeContext extends ParserRuleContext {
		public TerminalNode BYTE() { return getToken(ThriftParser.BYTE, 0); }
		public TerminalNode BOOL() { return getToken(ThriftParser.BOOL, 0); }
		public TerminalNode I16() { return getToken(ThriftParser.I16, 0); }
		public TerminalNode SLIST() { return getToken(ThriftParser.SLIST, 0); }
		public TerminalNode I64() { return getToken(ThriftParser.I64, 0); }
		public TerminalNode DOUBLE() { return getToken(ThriftParser.DOUBLE, 0); }
		public TerminalNode I32() { return getToken(ThriftParser.I32, 0); }
		public TerminalNode BINARY() { return getToken(ThriftParser.BINARY, 0); }
		public TerminalNode STRING() { return getToken(ThriftParser.STRING, 0); }
		public SimpleBaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleBaseType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterSimpleBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitSimpleBaseType(this);
		}
	}

	public final SimpleBaseTypeContext simpleBaseType() throws RecognitionException {
		SimpleBaseTypeContext _localctx = new SimpleBaseTypeContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_simpleBaseType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOL) | (1L << BYTE) | (1L << I16) | (1L << I32) | (1L << I64) | (1L << DOUBLE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContainerTypeContext extends ParserRuleContext {
		public SimpleContainerTypeContext simpleContainerType() {
			return getRuleContext(SimpleContainerTypeContext.class,0);
		}
		public TypeAnnotationsContext typeAnnotations() {
			return getRuleContext(TypeAnnotationsContext.class,0);
		}
		public ContainerTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_containerType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterContainerType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitContainerType(this);
		}
	}

	public final ContainerTypeContext containerType() throws RecognitionException {
		ContainerTypeContext _localctx = new ContainerTypeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_containerType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403); simpleContainerType();
			setState(405);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(404); typeAnnotations();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleContainerTypeContext extends ParserRuleContext {
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public SetTypeContext setType() {
			return getRuleContext(SetTypeContext.class,0);
		}
		public SimpleContainerTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleContainerType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterSimpleContainerType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitSimpleContainerType(this);
		}
	}

	public final SimpleContainerTypeContext simpleContainerType() throws RecognitionException {
		SimpleContainerTypeContext _localctx = new SimpleContainerTypeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_simpleContainerType);
		try {
			setState(410);
			switch (_input.LA(1)) {
			case MAP:
				enterOuterAlt(_localctx, 1);
				{
				setState(407); mapType();
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 2);
				{
				setState(408); setType();
				}
				break;
			case LIST:
				enterOuterAlt(_localctx, 3);
				{
				setState(409); listType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapTypeContext extends ParserRuleContext {
		public FieldTypeContext ft1;
		public FieldTypeContext ft2;
		public CppTypeContext cppType() {
			return getRuleContext(CppTypeContext.class,0);
		}
		public TerminalNode LT() { return getToken(ThriftParser.LT, 0); }
		public FieldTypeContext fieldType(int i) {
			return getRuleContext(FieldTypeContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(ThriftParser.COMMA, 0); }
		public TerminalNode MAP() { return getToken(ThriftParser.MAP, 0); }
		public TerminalNode GT() { return getToken(ThriftParser.GT, 0); }
		public List<FieldTypeContext> fieldType() {
			return getRuleContexts(FieldTypeContext.class);
		}
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterMapType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitMapType(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_mapType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412); match(MAP);
			setState(414);
			_la = _input.LA(1);
			if (_la==CPP_TYPE) {
				{
				setState(413); cppType();
				}
			}

			setState(416); match(LT);
			setState(417); ((MapTypeContext)_localctx).ft1 = fieldType();
			setState(418); match(COMMA);
			setState(419); ((MapTypeContext)_localctx).ft2 = fieldType();
			setState(420); match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetTypeContext extends ParserRuleContext {
		public FieldTypeContext ft;
		public TerminalNode SET() { return getToken(ThriftParser.SET, 0); }
		public CppTypeContext cppType() {
			return getRuleContext(CppTypeContext.class,0);
		}
		public TerminalNode LT() { return getToken(ThriftParser.LT, 0); }
		public TerminalNode GT() { return getToken(ThriftParser.GT, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public SetTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterSetType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitSetType(this);
		}
	}

	public final SetTypeContext setType() throws RecognitionException {
		SetTypeContext _localctx = new SetTypeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_setType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422); match(SET);
			setState(424);
			_la = _input.LA(1);
			if (_la==CPP_TYPE) {
				{
				setState(423); cppType();
				}
			}

			setState(426); match(LT);
			setState(427); ((SetTypeContext)_localctx).ft = fieldType();
			setState(428); match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListTypeContext extends ParserRuleContext {
		public FieldTypeContext ft;
		public CppTypeContext cppType() {
			return getRuleContext(CppTypeContext.class,0);
		}
		public TerminalNode LT() { return getToken(ThriftParser.LT, 0); }
		public TerminalNode GT() { return getToken(ThriftParser.GT, 0); }
		public TerminalNode LIST() { return getToken(ThriftParser.LIST, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public ListTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterListType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitListType(this);
		}
	}

	public final ListTypeContext listType() throws RecognitionException {
		ListTypeContext _localctx = new ListTypeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_listType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430); match(LIST);
			setState(431); match(LT);
			setState(432); ((ListTypeContext)_localctx).ft = fieldType();
			setState(433); match(GT);
			setState(435);
			_la = _input.LA(1);
			if (_la==CPP_TYPE) {
				{
				setState(434); cppType();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CppTypeContext extends ParserRuleContext {
		public Token i;
		public TerminalNode LITERAL() { return getToken(ThriftParser.LITERAL, 0); }
		public TerminalNode CPP_TYPE() { return getToken(ThriftParser.CPP_TYPE, 0); }
		public CppTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cppType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterCppType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitCppType(this);
		}
	}

	public final CppTypeContext cppType() throws RecognitionException {
		CppTypeContext _localctx = new CppTypeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_cppType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437); match(CPP_TYPE);
			setState(438); ((CppTypeContext)_localctx).i = match(LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAnnotationsContext extends ParserRuleContext {
		public List<TypeAnnotationContext> typeAnnotation() {
			return getRuleContexts(TypeAnnotationContext.class);
		}
		public TerminalNode RPAREN() { return getToken(ThriftParser.RPAREN, 0); }
		public TerminalNode LPAREN() { return getToken(ThriftParser.LPAREN, 0); }
		public TypeAnnotationContext typeAnnotation(int i) {
			return getRuleContext(TypeAnnotationContext.class,i);
		}
		public TypeAnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAnnotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterTypeAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitTypeAnnotations(this);
		}
	}

	public final TypeAnnotationsContext typeAnnotations() throws RecognitionException {
		TypeAnnotationsContext _localctx = new TypeAnnotationsContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_typeAnnotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440); match(LPAREN);
			setState(444);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(441); typeAnnotation();
				}
				}
				setState(446);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(447); match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAnnotationContext extends ParserRuleContext {
		public Token i;
		public Token l;
		public TerminalNode LITERAL() { return getToken(ThriftParser.LITERAL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ThriftParser.IDENTIFIER, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public TerminalNode EQ() { return getToken(ThriftParser.EQ, 0); }
		public TypeAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterTypeAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitTypeAnnotation(this);
		}
	}

	public final TypeAnnotationContext typeAnnotation() throws RecognitionException {
		TypeAnnotationContext _localctx = new TypeAnnotationContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_typeAnnotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(449); ((TypeAnnotationContext)_localctx).i = match(IDENTIFIER);
			setState(450); match(EQ);
			setState(451); ((TypeAnnotationContext)_localctx).l = match(LITERAL);
			setState(453);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(452); commaOrSemicolon();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntConstantContext extends ParserRuleContext {
		public TerminalNode INTCONSTANT() { return getToken(ThriftParser.INTCONSTANT, 0); }
		public TerminalNode HEXCONSTANT() { return getToken(ThriftParser.HEXCONSTANT, 0); }
		public IntConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intConstant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).enterIntConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ThriftListener ) ((ThriftListener)listener).exitIntConstant(this);
		}
	}

	public final IntConstantContext intConstant() throws RecognitionException {
		IntConstantContext _localctx = new IntConstantContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_intConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			_la = _input.LA(1);
			if ( !(_la==INTCONSTANT || _la==HEXCONSTANT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3E\u01cc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\3\2\7\2Z\n\2\f\2\16\2]\13\2\3\2\7\2`\n\2\f\2\16\2c\13\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0085\n\3\3\4\3\4"+
		"\3\4\5\4\u008a\n\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u0092\n\5\3\6\3\6\3\6\3"+
		"\6\5\6\u0098\n\6\3\7\3\7\3\b\3\b\3\b\3\b\7\b\u00a0\n\b\f\b\16\b\u00a3"+
		"\13\b\3\b\3\b\5\b\u00a7\n\b\3\t\3\t\3\t\3\t\5\t\u00ad\n\t\3\t\5\t\u00b0"+
		"\n\t\3\t\3\t\5\t\u00b4\n\t\3\t\5\t\u00b7\n\t\5\t\u00b9\n\t\3\n\3\n\3\n"+
		"\3\n\7\n\u00bf\n\n\f\n\16\n\u00c2\13\n\3\n\3\n\5\n\u00c6\n\n\3\13\3\13"+
		"\5\13\u00ca\n\13\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00d2\n\f\3\r\3\r\3\r\3\r"+
		"\5\r\u00d8\n\r\3\16\3\16\3\16\5\16\u00dd\n\16\7\16\u00df\n\16\f\16\16"+
		"\16\u00e2\13\16\3\16\3\16\3\17\3\17\7\17\u00e8\n\17\f\17\16\17\u00eb\13"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\20\5\20\u00f3\n\20\3\21\3\21\3\21\5\21"+
		"\u00f8\n\21\3\21\3\21\7\21\u00fc\n\21\f\21\16\21\u00ff\13\21\3\21\3\21"+
		"\5\21\u0103\n\21\3\22\3\22\3\22\5\22\u0108\n\22\3\22\3\22\7\22\u010c\n"+
		"\22\f\22\16\22\u010f\13\22\3\22\3\22\5\22\u0113\n\22\3\23\3\23\3\24\3"+
		"\24\3\25\3\25\3\26\3\26\3\26\7\26\u011e\n\26\f\26\16\26\u0121\13\26\3"+
		"\26\3\26\3\27\3\27\3\27\3\27\7\27\u0129\n\27\f\27\16\27\u012c\13\27\3"+
		"\27\3\27\5\27\u0130\n\27\3\30\3\30\3\30\3\30\5\30\u0136\n\30\3\30\3\30"+
		"\7\30\u013a\n\30\f\30\16\30\u013d\13\30\3\30\3\30\5\30\u0141\n\30\3\31"+
		"\5\31\u0144\n\31\3\31\3\31\3\31\3\31\7\31\u014a\n\31\f\31\16\31\u014d"+
		"\13\31\3\31\3\31\5\31\u0151\n\31\3\31\5\31\u0154\n\31\3\31\5\31\u0157"+
		"\n\31\3\32\3\32\3\33\3\33\3\33\7\33\u015e\n\33\f\33\16\33\u0161\13\33"+
		"\3\33\3\33\3\34\5\34\u0166\n\34\3\34\5\34\u0169\n\34\3\34\3\34\3\34\5"+
		"\34\u016e\n\34\3\34\5\34\u0171\n\34\3\34\5\34\u0174\n\34\3\34\5\34\u0177"+
		"\n\34\3\34\5\34\u017a\n\34\3\34\5\34\u017d\n\34\3\35\3\35\3\35\3\36\3"+
		"\36\3\37\3\37\3\37\3 \3 \5 \u0189\n \3!\3!\3!\5!\u018e\n!\3\"\3\"\5\""+
		"\u0192\n\"\3#\3#\3$\3$\5$\u0198\n$\3%\3%\3%\5%\u019d\n%\3&\3&\5&\u01a1"+
		"\n&\3&\3&\3&\3&\3&\3&\3\'\3\'\5\'\u01ab\n\'\3\'\3\'\3\'\3\'\3(\3(\3(\3"+
		"(\3(\5(\u01b6\n(\3)\3)\3)\3*\3*\7*\u01bd\n*\f*\16*\u01c0\13*\3*\3*\3+"+
		"\3+\3+\3+\5+\u01c8\n+\3,\3,\3,\2\2-\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTV\2\7\4\2\61\61\63\63\4\2?@EE\3"+
		"\2 !\3\2#+\3\2=>\u01f0\2[\3\2\2\2\4\u0084\3\2\2\2\6\u0089\3\2\2\2\b\u0091"+
		"\3\2\2\2\n\u0093\3\2\2\2\f\u0099\3\2\2\2\16\u009b\3\2\2\2\20\u00b8\3\2"+
		"\2\2\22\u00ba\3\2\2\2\24\u00c7\3\2\2\2\26\u00cb\3\2\2\2\30\u00d7\3\2\2"+
		"\2\32\u00d9\3\2\2\2\34\u00e5\3\2\2\2\36\u00ee\3\2\2\2 \u00f4\3\2\2\2\""+
		"\u0104\3\2\2\2$\u0114\3\2\2\2&\u0116\3\2\2\2(\u0118\3\2\2\2*\u011a\3\2"+
		"\2\2,\u0124\3\2\2\2.\u0131\3\2\2\2\60\u0143\3\2\2\2\62\u0158\3\2\2\2\64"+
		"\u015a\3\2\2\2\66\u0165\3\2\2\28\u017e\3\2\2\2:\u0181\3\2\2\2<\u0183\3"+
		"\2\2\2>\u0188\3\2\2\2@\u018d\3\2\2\2B\u018f\3\2\2\2D\u0193\3\2\2\2F\u0195"+
		"\3\2\2\2H\u019c\3\2\2\2J\u019e\3\2\2\2L\u01a8\3\2\2\2N\u01b0\3\2\2\2P"+
		"\u01b7\3\2\2\2R\u01ba\3\2\2\2T\u01c3\3\2\2\2V\u01c9\3\2\2\2XZ\5\4\3\2"+
		"YX\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\a\3\2\2\2][\3\2\2\2^`\5\6\4"+
		"\2_^\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2b\3\3\2\2\2ca\3\2\2\2de\7\3"+
		"\2\2e\u0085\7E\2\2fg\7\4\2\2g\u0085\7E\2\2hi\7\5\2\2ij\7@\2\2j\u0085\7"+
		"@\2\2kl\7\5\2\2lm\7\60\2\2m\u0085\7@\2\2no\7\6\2\2o\u0085\7@\2\2pq\7\7"+
		"\2\2q\u0085\7@\2\2rs\7\b\2\2s\u0085\7@\2\2tu\7\t\2\2u\u0085\7@\2\2vw\7"+
		"\n\2\2w\u0085\7@\2\2xy\7\13\2\2y\u0085\7D\2\2z{\7\f\2\2{\u0085\7@\2\2"+
		"|}\7\r\2\2}\u0085\7@\2\2~\177\7\16\2\2\177\u0085\7@\2\2\u0080\u0081\7"+
		"\17\2\2\u0081\u0085\7E\2\2\u0082\u0083\7\20\2\2\u0083\u0085\7@\2\2\u0084"+
		"d\3\2\2\2\u0084f\3\2\2\2\u0084h\3\2\2\2\u0084k\3\2\2\2\u0084n\3\2\2\2"+
		"\u0084p\3\2\2\2\u0084r\3\2\2\2\u0084t\3\2\2\2\u0084v\3\2\2\2\u0084x\3"+
		"\2\2\2\u0084z\3\2\2\2\u0084|\3\2\2\2\u0084~\3\2\2\2\u0084\u0080\3\2\2"+
		"\2\u0084\u0082\3\2\2\2\u0085\5\3\2\2\2\u0086\u008a\5\26\f\2\u0087\u008a"+
		"\5\b\5\2\u0088\u008a\5.\30\2\u0089\u0086\3\2\2\2\u0089\u0087\3\2\2\2\u0089"+
		"\u0088\3\2\2\2\u008a\7\3\2\2\2\u008b\u0092\5\n\6\2\u008c\u0092\5\16\b"+
		"\2\u008d\u0092\5\22\n\2\u008e\u0092\5 \21\2\u008f\u0092\5\"\22\2\u0090"+
		"\u0092\5,\27\2\u0091\u008b\3\2\2\2\u0091\u008c\3\2\2\2\u0091\u008d\3\2"+
		"\2\2\u0091\u008e\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0090\3\2\2\2\u0092"+
		"\t\3\2\2\2\u0093\u0094\7\21\2\2\u0094\u0095\5@!\2\u0095\u0097\7@\2\2\u0096"+
		"\u0098\5R*\2\u0097\u0096\3\2\2\2\u0097\u0098\3\2\2\2\u0098\13\3\2\2\2"+
		"\u0099\u009a\t\2\2\2\u009a\r\3\2\2\2\u009b\u009c\7\22\2\2\u009c\u009d"+
		"\7@\2\2\u009d\u00a1\7\64\2\2\u009e\u00a0\5\20\t\2\u009f\u009e\3\2\2\2"+
		"\u00a0\u00a3\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4"+
		"\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a4\u00a6\7\65\2\2\u00a5\u00a7\5R*\2\u00a6"+
		"\u00a5\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\17\3\2\2\2\u00a8\u00a9\7@\2\2"+
		"\u00a9\u00aa\7:\2\2\u00aa\u00ac\5V,\2\u00ab\u00ad\5R*\2\u00ac\u00ab\3"+
		"\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00af\3\2\2\2\u00ae\u00b0\5\f\7\2\u00af"+
		"\u00ae\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b9\3\2\2\2\u00b1\u00b3\7@"+
		"\2\2\u00b2\u00b4\5R*\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b6"+
		"\3\2\2\2\u00b5\u00b7\5\f\7\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7"+
		"\u00b9\3\2\2\2\u00b8\u00a8\3\2\2\2\u00b8\u00b1\3\2\2\2\u00b9\21\3\2\2"+
		"\2\u00ba\u00bb\7\23\2\2\u00bb\u00bc\7@\2\2\u00bc\u00c0\7\64\2\2\u00bd"+
		"\u00bf\5\24\13\2\u00be\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3"+
		"\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3"+
		"\u00c5\7\65\2\2\u00c4\u00c6\5R*\2\u00c5\u00c4\3\2\2\2\u00c5\u00c6\3\2"+
		"\2\2\u00c6\23\3\2\2\2\u00c7\u00c9\7E\2\2\u00c8\u00ca\5\f\7\2\u00c9\u00c8"+
		"\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\25\3\2\2\2\u00cb\u00cc\7\24\2\2\u00cc"+
		"\u00cd\5@!\2\u00cd\u00ce\7@\2\2\u00ce\u00cf\7:\2\2\u00cf\u00d1\5\30\r"+
		"\2\u00d0\u00d2\5\f\7\2\u00d1\u00d0\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\27"+
		"\3\2\2\2\u00d3\u00d8\5V,\2\u00d4\u00d8\t\3\2\2\u00d5\u00d8\5\32\16\2\u00d6"+
		"\u00d8\5\34\17\2\u00d7\u00d3\3\2\2\2\u00d7\u00d4\3\2\2\2\u00d7\u00d5\3"+
		"\2\2\2\u00d7\u00d6\3\2\2\2\u00d8\31\3\2\2\2\u00d9\u00e0\7\66\2\2\u00da"+
		"\u00dc\5\30\r\2\u00db\u00dd\5\f\7\2\u00dc\u00db\3\2\2\2\u00dc\u00dd\3"+
		"\2\2\2\u00dd\u00df\3\2\2\2\u00de\u00da\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0"+
		"\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00e0\3\2"+
		"\2\2\u00e3\u00e4\7\67\2\2\u00e4\33\3\2\2\2\u00e5\u00e9\7\64\2\2\u00e6"+
		"\u00e8\5\36\20\2\u00e7\u00e6\3\2\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00e7\3"+
		"\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00ec\3\2\2\2\u00eb\u00e9\3\2\2\2\u00ec"+
		"\u00ed\7\65\2\2\u00ed\35\3\2\2\2\u00ee\u00ef\5\30\r\2\u00ef\u00f0\7\62"+
		"\2\2\u00f0\u00f2\5\30\r\2\u00f1\u00f3\5\f\7\2\u00f2\u00f1\3\2\2\2\u00f2"+
		"\u00f3\3\2\2\2\u00f3\37\3\2\2\2\u00f4\u00f5\7\25\2\2\u00f5\u00f7\7@\2"+
		"\2\u00f6\u00f8\5$\23\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9"+
		"\3\2\2\2\u00f9\u00fd\7\64\2\2\u00fa\u00fc\5\66\34\2\u00fb\u00fa\3\2\2"+
		"\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0100"+
		"\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0102\7\65\2\2\u0101\u0103\5R*\2\u0102"+
		"\u0101\3\2\2\2\u0102\u0103\3\2\2\2\u0103!\3\2\2\2\u0104\u0105\7\26\2\2"+
		"\u0105\u0107\7@\2\2\u0106\u0108\5$\23\2\u0107\u0106\3\2\2\2\u0107\u0108"+
		"\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010d\7\64\2\2\u010a\u010c\5\66\34"+
		"\2\u010b\u010a\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e"+
		"\3\2\2\2\u010e\u0110\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0112\7\65\2\2"+
		"\u0111\u0113\5R*\2\u0112\u0111\3\2\2\2\u0112\u0113\3\2\2\2\u0113#\3\2"+
		"\2\2\u0114\u0115\7\27\2\2\u0115%\3\2\2\2\u0116\u0117\7\30\2\2\u0117\'"+
		"\3\2\2\2\u0118\u0119\7\31\2\2\u0119)\3\2\2\2\u011a\u011b\7\32\2\2\u011b"+
		"\u011f\7\64\2\2\u011c\u011e\5\66\34\2\u011d\u011c\3\2\2\2\u011e\u0121"+
		"\3\2\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u0122\3\2\2\2\u0121"+
		"\u011f\3\2\2\2\u0122\u0123\7\65\2\2\u0123+\3\2\2\2\u0124\u0125\7\33\2"+
		"\2\u0125\u0126\7@\2\2\u0126\u012a\7\64\2\2\u0127\u0129\5\66\34\2\u0128"+
		"\u0127\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2"+
		"\2\2\u012b\u012d\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012f\7\65\2\2\u012e"+
		"\u0130\5R*\2\u012f\u012e\3\2\2\2\u012f\u0130\3\2\2\2\u0130-\3\2\2\2\u0131"+
		"\u0132\7\34\2\2\u0132\u0135\7@\2\2\u0133\u0134\7\35\2\2\u0134\u0136\7"+
		"@\2\2\u0135\u0133\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0137\3\2\2\2\u0137"+
		"\u013b\7\64\2\2\u0138\u013a\5\60\31\2\u0139\u0138\3\2\2\2\u013a\u013d"+
		"\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c\u013e\3\2\2\2\u013d"+
		"\u013b\3\2\2\2\u013e\u0140\7\65\2\2\u013f\u0141\5R*\2\u0140\u013f\3\2"+
		"\2\2\u0140\u0141\3\2\2\2\u0141/\3\2\2\2\u0142\u0144\5\62\32\2\u0143\u0142"+
		"\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0146\5> \2\u0146"+
		"\u0147\7@\2\2\u0147\u014b\78\2\2\u0148\u014a\5\66\34\2\u0149\u0148\3\2"+
		"\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c"+
		"\u014e\3\2\2\2\u014d\u014b\3\2\2\2\u014e\u0150\79\2\2\u014f\u0151\5\64"+
		"\33\2\u0150\u014f\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0153\3\2\2\2\u0152"+
		"\u0154\5R*\2\u0153\u0152\3\2\2\2\u0153\u0154\3\2\2\2\u0154\u0156\3\2\2"+
		"\2\u0155\u0157\5\f\7\2\u0156\u0155\3\2\2\2\u0156\u0157\3\2\2\2\u0157\61"+
		"\3\2\2\2\u0158\u0159\7\36\2\2\u0159\63\3\2\2\2\u015a\u015b\7\37\2\2\u015b"+
		"\u015f\78\2\2\u015c\u015e\5\66\34\2\u015d\u015c\3\2\2\2\u015e\u0161\3"+
		"\2\2\2\u015f\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0162\3\2\2\2\u0161"+
		"\u015f\3\2\2\2\u0162\u0163\79\2\2\u0163\65\3\2\2\2\u0164\u0166\58\35\2"+
		"\u0165\u0164\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168\3\2\2\2\u0167\u0169"+
		"\5:\36\2\u0168\u0167\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016a\3\2\2\2\u016a"+
		"\u016b\5@!\2\u016b\u016d\7@\2\2\u016c\u016e\5<\37\2\u016d\u016c\3\2\2"+
		"\2\u016d\u016e\3\2\2\2\u016e\u0170\3\2\2\2\u016f\u0171\5&\24\2\u0170\u016f"+
		"\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0173\3\2\2\2\u0172\u0174\5(\25\2\u0173"+
		"\u0172\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0176\3\2\2\2\u0175\u0177\5*"+
		"\26\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0179\3\2\2\2\u0178"+
		"\u017a\5R*\2\u0179\u0178\3\2\2\2\u0179\u017a\3\2\2\2\u017a\u017c\3\2\2"+
		"\2\u017b\u017d\5\f\7\2\u017c\u017b\3\2\2\2\u017c\u017d\3\2\2\2\u017d\67"+
		"\3\2\2\2\u017e\u017f\5V,\2\u017f\u0180\7\62\2\2\u01809\3\2\2\2\u0181\u0182"+
		"\t\4\2\2\u0182;\3\2\2\2\u0183\u0184\7:\2\2\u0184\u0185\5\30\r\2\u0185"+
		"=\3\2\2\2\u0186\u0189\7\"\2\2\u0187\u0189\5@!\2\u0188\u0186\3\2\2\2\u0188"+
		"\u0187\3\2\2\2\u0189?\3\2\2\2\u018a\u018e\7@\2\2\u018b\u018e\5B\"\2\u018c"+
		"\u018e\5F$\2\u018d\u018a\3\2\2\2\u018d\u018b\3\2\2\2\u018d\u018c\3\2\2"+
		"\2\u018eA\3\2\2\2\u018f\u0191\5D#\2\u0190\u0192\5R*\2\u0191\u0190\3\2"+
		"\2\2\u0191\u0192\3\2\2\2\u0192C\3\2\2\2\u0193\u0194\t\5\2\2\u0194E\3\2"+
		"\2\2\u0195\u0197\5H%\2\u0196\u0198\5R*\2\u0197\u0196\3\2\2\2\u0197\u0198"+
		"\3\2\2\2\u0198G\3\2\2\2\u0199\u019d\5J&\2\u019a\u019d\5L\'\2\u019b\u019d"+
		"\5N(\2\u019c\u0199\3\2\2\2\u019c\u019a\3\2\2\2\u019c\u019b\3\2\2\2\u019d"+
		"I\3\2\2\2\u019e\u01a0\7,\2\2\u019f\u01a1\5P)\2\u01a0\u019f\3\2\2\2\u01a0"+
		"\u01a1\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\7;\2\2\u01a3\u01a4\5@!"+
		"\2\u01a4\u01a5\7\61\2\2\u01a5\u01a6\5@!\2\u01a6\u01a7\7<\2\2\u01a7K\3"+
		"\2\2\2\u01a8\u01aa\7-\2\2\u01a9\u01ab\5P)\2\u01aa\u01a9\3\2\2\2\u01aa"+
		"\u01ab\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\7;\2\2\u01ad\u01ae\5@!"+
		"\2\u01ae\u01af\7<\2\2\u01afM\3\2\2\2\u01b0\u01b1\7.\2\2\u01b1\u01b2\7"+
		";\2\2\u01b2\u01b3\5@!\2\u01b3\u01b5\7<\2\2\u01b4\u01b6\5P)\2\u01b5\u01b4"+
		"\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6O\3\2\2\2\u01b7\u01b8\7/\2\2\u01b8\u01b9"+
		"\7E\2\2\u01b9Q\3\2\2\2\u01ba\u01be\78\2\2\u01bb\u01bd\5T+\2\u01bc\u01bb"+
		"\3\2\2\2\u01bd\u01c0\3\2\2\2\u01be\u01bc\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf"+
		"\u01c1\3\2\2\2\u01c0\u01be\3\2\2\2\u01c1\u01c2\79\2\2\u01c2S\3\2\2\2\u01c3"+
		"\u01c4\7@\2\2\u01c4\u01c5\7:\2\2\u01c5\u01c7\7E\2\2\u01c6\u01c8\5\f\7"+
		"\2\u01c7\u01c6\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8U\3\2\2\2\u01c9\u01ca"+
		"\t\6\2\2\u01caW\3\2\2\2<[a\u0084\u0089\u0091\u0097\u00a1\u00a6\u00ac\u00af"+
		"\u00b3\u00b6\u00b8\u00c0\u00c5\u00c9\u00d1\u00d7\u00dc\u00e0\u00e9\u00f2"+
		"\u00f7\u00fd\u0102\u0107\u010d\u0112\u011f\u012a\u012f\u0135\u013b\u0140"+
		"\u0143\u014b\u0150\u0153\u0156\u015f\u0165\u0168\u016d\u0170\u0173\u0176"+
		"\u0179\u017c\u0188\u018d\u0191\u0197\u019c\u01a0\u01aa\u01b5\u01be\u01c7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}