// Generated from /home/rubinste/proj_de3/kiara_java/KIARA_Java/KIARA/src/main/java/de/dfki/kiara/idl/Kiara.g4 by ANTLR 4.2.2
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
public class KiaraParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INCLUDE=1, NAMESPACE=2, TYPEDEF=3, ENUM=4, SENUM=5, CONST=6, STRUCT=7,
		UNION=8, XSD_ALL=9, XSD_OPTIONAL=10, XSD_NILLABLE=11, XSD_ATTRIBUTES=12,
		EXCEPTION=13, SERVICE=14, EXTENDS=15, ONEWAY=16, THROWS=17, REQUIRED=18,
		OPTIONAL=19, ANNOTATION=20, VOID=21, STRING=22, BINARY=23, SLIST=24, BOOLEAN=25,
		I8=26, U8=27, I16=28, U16=29, I32=30, U32=31, I64=32, U64=33, FLOAT=34,
		DOUBLE=35, ANY=36, STAR=37, COMMA=38, COLON=39, SEMI=40, LBRACE=41, RBRACE=42,
		LBRACKET=43, RBRACKET=44, LPAREN=45, RPAREN=46, EQ=47, LT=48, GT=49, INTCONSTANT=50,
		HEXCONSTANT=51, DUBCONSTANT=52, IDENTIFIER=53, WHITESPACE=54, COMMENT=55,
		UNIXCOMMENT=56, ST_IDENTIFIER=57, LITERAL=58;
	public static final String[] tokenNames = {
		"<INVALID>", "'include'", "'namespace'", "'typedef'", "'enum'", "'senum'",
		"'const'", "'struct'", "'union'", "'xsd_all'", "'xsd_optional'", "'xsd_nillable'",
		"'xsd_attributes'", "'exception'", "'service'", "'extends'", "'oneway'",
		"'throws'", "'required'", "'optional'", "'annotation'", "'void'", "'string'",
		"'binary'", "'slist'", "'boolean'", "'i8'", "'u8'", "'i16'", "'u16'",
		"'i32'", "'u32'", "'i64'", "'u64'", "'float'", "'double'", "'any'", "'*'",
		"','", "':'", "';'", "'{'", "'}'", "'['", "']'", "'('", "')'", "'='",
		"'<'", "'>'", "INTCONSTANT", "HEXCONSTANT", "DUBCONSTANT", "IDENTIFIER",
		"WHITESPACE", "COMMENT", "UNIXCOMMENT", "ST_IDENTIFIER", "LITERAL"
	};
	public static final int
		RULE_program = 0, RULE_header_list = 1, RULE_header = 2, RULE_definition_list = 3,
		RULE_definition = 4, RULE_typeDefinition = 5, RULE_nonAnnotatedTypeDefinition = 6,
		RULE_annotatedTypeDefinition = 7, RULE_typedef = 8, RULE_commaOrSemicolon = 9,
		RULE_enum_t = 10, RULE_enumDef = 11, RULE_senum = 12, RULE_senumDef = 13,
		RULE_constDefinition = 14, RULE_constValue = 15, RULE_constList = 16,
		RULE_constMap = 17, RULE_constValuePair = 18, RULE_struct = 19, RULE_union = 20,
		RULE_xsdAll = 21, RULE_xsdOptional = 22, RULE_xsdNillable = 23, RULE_xsdAttributes = 24,
		RULE_xception = 25, RULE_annotationDef = 26, RULE_service = 27, RULE_function = 28,
		RULE_throwz = 29, RULE_field = 30, RULE_fieldIdentifier = 31, RULE_fieldRequiredness = 32,
		RULE_fieldValue = 33, RULE_functionType = 34, RULE_fieldType = 35, RULE_baseType = 36,
		RULE_simpleBaseType = 37, RULE_genericType = 38, RULE_simpleGenericType = 39,
		RULE_genericTypeArg = 40, RULE_genericTypeArgList = 41, RULE_annotationList = 42,
		RULE_nonEmptyAnnotationList = 43, RULE_annotation = 44, RULE_annotationArg = 45,
		RULE_annotationArgList = 46, RULE_nonEmptyAnnotationArgList = 47, RULE_intConstant = 48;
	public static final String[] ruleNames = {
		"program", "header_list", "header", "definition_list", "definition", "typeDefinition",
		"nonAnnotatedTypeDefinition", "annotatedTypeDefinition", "typedef", "commaOrSemicolon",
		"enum_t", "enumDef", "senum", "senumDef", "constDefinition", "constValue",
		"constList", "constMap", "constValuePair", "struct", "union", "xsdAll",
		"xsdOptional", "xsdNillable", "xsdAttributes", "xception", "annotationDef",
		"service", "function", "throwz", "field", "fieldIdentifier", "fieldRequiredness",
		"fieldValue", "functionType", "fieldType", "baseType", "simpleBaseType",
		"genericType", "simpleGenericType", "genericTypeArg", "genericTypeArgList",
		"annotationList", "nonEmptyAnnotationList", "annotation", "annotationArg",
		"annotationArgList", "nonEmptyAnnotationArgList", "intConstant"
	};

	@Override
	public String getGrammarFileName() { return "Kiara.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KiaraParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public Header_listContext header_list() {
			return getRuleContext(Header_listContext.class,0);
		}
		public Definition_listContext definition_list() {
			return getRuleContext(Definition_listContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98); header_list();
			setState(99); definition_list();
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

	public static class Header_listContext extends ParserRuleContext {
		public HeaderContext header(int i) {
			return getRuleContext(HeaderContext.class,i);
		}
		public List<HeaderContext> header() {
			return getRuleContexts(HeaderContext.class);
		}
		public Header_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterHeader_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitHeader_list(this);
		}
	}

	public final Header_listContext header_list() throws RecognitionException {
		Header_listContext _localctx = new Header_listContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INCLUDE || _la==NAMESPACE) {
				{
				{
				setState(101); header();
				}
				}
				setState(106);
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
		public TerminalNode INCLUDE() { return getToken(KiaraParser.INCLUDE, 0); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(KiaraParser.IDENTIFIER, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(KiaraParser.IDENTIFIER); }
		public TerminalNode LITERAL() { return getToken(KiaraParser.LITERAL, 0); }
		public TerminalNode STAR() { return getToken(KiaraParser.STAR, 0); }
		public TerminalNode NAMESPACE() { return getToken(KiaraParser.NAMESPACE, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitHeader(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_header);
		try {
			setState(115);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(107); match(INCLUDE);
				setState(108); match(LITERAL);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(109); match(NAMESPACE);
				setState(110); ((HeaderContext)_localctx).t = match(IDENTIFIER);
				setState(111); ((HeaderContext)_localctx).n = match(IDENTIFIER);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(112); match(NAMESPACE);
				setState(113); match(STAR);
				setState(114); match(IDENTIFIER);
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

	public static class Definition_listContext extends ParserRuleContext {
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public Definition_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterDefinition_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitDefinition_list(this);
		}
	}

	public final Definition_listContext definition_list() throws RecognitionException {
		Definition_listContext _localctx = new Definition_listContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_definition_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPEDEF) | (1L << ENUM) | (1L << SENUM) | (1L << CONST) | (1L << STRUCT) | (1L << UNION) | (1L << EXCEPTION) | (1L << SERVICE) | (1L << ANNOTATION) | (1L << LBRACKET))) != 0)) {
				{
				{
				setState(117); definition();
				}
				}
				setState(122);
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
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitDefinition(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_definition);
		try {
			setState(126);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(123); constDefinition();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(124); typeDefinition();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(125); service();
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

	public static class TypeDefinitionContext extends ParserRuleContext {
		public AnnotationListContext annotationList() {
			return getRuleContext(AnnotationListContext.class,0);
		}
		public AnnotatedTypeDefinitionContext annotatedTypeDefinition() {
			return getRuleContext(AnnotatedTypeDefinitionContext.class,0);
		}
		public NonAnnotatedTypeDefinitionContext nonAnnotatedTypeDefinition() {
			return getRuleContext(NonAnnotatedTypeDefinitionContext.class,0);
		}
		public TypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitTypeDefinition(this);
		}
	}

	public final TypeDefinitionContext typeDefinition() throws RecognitionException {
		TypeDefinitionContext _localctx = new TypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_typeDefinition);
		try {
			setState(132);
			switch (_input.LA(1)) {
			case TYPEDEF:
				enterOuterAlt(_localctx, 1);
				{
				setState(128); nonAnnotatedTypeDefinition();
				}
				break;
			case ENUM:
			case SENUM:
			case STRUCT:
			case UNION:
			case EXCEPTION:
			case ANNOTATION:
			case LBRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(129); annotationList();
				setState(130); annotatedTypeDefinition();
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

	public static class NonAnnotatedTypeDefinitionContext extends ParserRuleContext {
		public TypedefContext typedef() {
			return getRuleContext(TypedefContext.class,0);
		}
		public NonAnnotatedTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonAnnotatedTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterNonAnnotatedTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitNonAnnotatedTypeDefinition(this);
		}
	}

	public final NonAnnotatedTypeDefinitionContext nonAnnotatedTypeDefinition() throws RecognitionException {
		NonAnnotatedTypeDefinitionContext _localctx = new NonAnnotatedTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_nonAnnotatedTypeDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134); typedef();
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

	public static class AnnotatedTypeDefinitionContext extends ParserRuleContext {
		public SenumContext senum() {
			return getRuleContext(SenumContext.class,0);
		}
		public StructContext struct() {
			return getRuleContext(StructContext.class,0);
		}
		public AnnotationDefContext annotationDef() {
			return getRuleContext(AnnotationDefContext.class,0);
		}
		public Enum_tContext enum_t() {
			return getRuleContext(Enum_tContext.class,0);
		}
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
		}
		public XceptionContext xception() {
			return getRuleContext(XceptionContext.class,0);
		}
		public AnnotatedTypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatedTypeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotatedTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotatedTypeDefinition(this);
		}
	}

	public final AnnotatedTypeDefinitionContext annotatedTypeDefinition() throws RecognitionException {
		AnnotatedTypeDefinitionContext _localctx = new AnnotatedTypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_annotatedTypeDefinition);
		try {
			setState(142);
			switch (_input.LA(1)) {
			case ENUM:
				enterOuterAlt(_localctx, 1);
				{
				setState(136); enum_t();
				}
				break;
			case SENUM:
				enterOuterAlt(_localctx, 2);
				{
				setState(137); senum();
				}
				break;
			case STRUCT:
				enterOuterAlt(_localctx, 3);
				{
				setState(138); struct();
				}
				break;
			case UNION:
				enterOuterAlt(_localctx, 4);
				{
				setState(139); union();
				}
				break;
			case EXCEPTION:
				enterOuterAlt(_localctx, 5);
				{
				setState(140); xception();
				}
				break;
			case ANNOTATION:
				enterOuterAlt(_localctx, 6);
				{
				setState(141); annotationDef();
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
		public TerminalNode TYPEDEF() { return getToken(KiaraParser.TYPEDEF, 0); }
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public TypedefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterTypedef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitTypedef(this);
		}
	}

	public final TypedefContext typedef() throws RecognitionException {
		TypedefContext _localctx = new TypedefContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_typedef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144); match(TYPEDEF);
			setState(145); fieldType();
			setState(146); match(IDENTIFIER);
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
		public TerminalNode SEMI() { return getToken(KiaraParser.SEMI, 0); }
		public TerminalNode COMMA() { return getToken(KiaraParser.COMMA, 0); }
		public CommaOrSemicolonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commaOrSemicolon; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterCommaOrSemicolon(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitCommaOrSemicolon(this);
		}
	}

	public final CommaOrSemicolonContext commaOrSemicolon() throws RecognitionException {
		CommaOrSemicolonContext _localctx = new CommaOrSemicolonContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_commaOrSemicolon);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public TerminalNode ENUM() { return getToken(KiaraParser.ENUM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public EnumDefContext enumDef(int i) {
			return getRuleContext(EnumDefContext.class,i);
		}
		public List<EnumDefContext> enumDef() {
			return getRuleContexts(EnumDefContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public Enum_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enum_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterEnum_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitEnum_t(this);
		}
	}

	public final Enum_tContext enum_t() throws RecognitionException {
		Enum_tContext _localctx = new Enum_tContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_enum_t);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150); match(ENUM);
			setState(151); match(IDENTIFIER);
			setState(152); match(LBRACE);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(153); enumDef();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(159); match(RBRACE);
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
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public TerminalNode EQ() { return getToken(KiaraParser.EQ, 0); }
		public EnumDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterEnumDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitEnumDef(this);
		}
	}

	public final EnumDefContext enumDef() throws RecognitionException {
		EnumDefContext _localctx = new EnumDefContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_enumDef);
		int _la;
		try {
			setState(171);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(161); match(IDENTIFIER);
				setState(162); match(EQ);
				setState(163); intConstant();
				setState(165);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(164); commaOrSemicolon();
					}
				}

				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(167); match(IDENTIFIER);
				setState(169);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(168); commaOrSemicolon();
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public SenumDefContext senumDef(int i) {
			return getRuleContext(SenumDefContext.class,i);
		}
		public List<SenumDefContext> senumDef() {
			return getRuleContexts(SenumDefContext.class);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public TerminalNode SENUM() { return getToken(KiaraParser.SENUM, 0); }
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public SenumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_senum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterSenum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitSenum(this);
		}
	}

	public final SenumContext senum() throws RecognitionException {
		SenumContext _localctx = new SenumContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_senum);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173); match(SENUM);
			setState(174); match(IDENTIFIER);
			setState(175); match(LBRACE);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LITERAL) {
				{
				{
				setState(176); senumDef();
				}
				}
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(182); match(RBRACE);
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
		public TerminalNode LITERAL() { return getToken(KiaraParser.LITERAL, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public SenumDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_senumDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterSenumDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitSenumDef(this);
		}
	}

	public final SenumDefContext senumDef() throws RecognitionException {
		SenumDefContext _localctx = new SenumDefContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_senumDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); match(LITERAL);
			setState(186);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(185); commaOrSemicolon();
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
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public TerminalNode CONST() { return getToken(KiaraParser.CONST, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public TerminalNode EQ() { return getToken(KiaraParser.EQ, 0); }
		public ConstDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterConstDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitConstDefinition(this);
		}
	}

	public final ConstDefinitionContext constDefinition() throws RecognitionException {
		ConstDefinitionContext _localctx = new ConstDefinitionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_constDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188); match(CONST);
			setState(189); ((ConstDefinitionContext)_localctx).ft = fieldType();
			setState(190); ((ConstDefinitionContext)_localctx).id = match(IDENTIFIER);
			setState(191); match(EQ);
			setState(192); ((ConstDefinitionContext)_localctx).cv = constValue();
			setState(194);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(193); commaOrSemicolon();
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
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public TerminalNode LITERAL() { return getToken(KiaraParser.LITERAL, 0); }
		public ConstListContext constList() {
			return getRuleContext(ConstListContext.class,0);
		}
		public TerminalNode DUBCONSTANT() { return getToken(KiaraParser.DUBCONSTANT, 0); }
		public ConstValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterConstValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitConstValue(this);
		}
	}

	public final ConstValueContext constValue() throws RecognitionException {
		ConstValueContext _localctx = new ConstValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_constValue);
		int _la;
		try {
			setState(200);
			switch (_input.LA(1)) {
			case INTCONSTANT:
			case HEXCONSTANT:
				enterOuterAlt(_localctx, 1);
				{
				setState(196); intConstant();
				}
				break;
			case DUBCONSTANT:
			case IDENTIFIER:
			case LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(197);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DUBCONSTANT) | (1L << IDENTIFIER) | (1L << LITERAL))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(198); constList();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 4);
				{
				setState(199); constMap();
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
		public TerminalNode LBRACKET() { return getToken(KiaraParser.LBRACKET, 0); }
		public List<CommaOrSemicolonContext> commaOrSemicolon() {
			return getRuleContexts(CommaOrSemicolonContext.class);
		}
		public TerminalNode RBRACKET() { return getToken(KiaraParser.RBRACKET, 0); }
		public ConstListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterConstList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitConstList(this);
		}
	}

	public final ConstListContext constList() throws RecognitionException {
		ConstListContext _localctx = new ConstListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_constList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202); match(LBRACKET);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << DUBCONSTANT) | (1L << IDENTIFIER) | (1L << LITERAL))) != 0)) {
				{
				{
				setState(203); constValue();
				setState(205);
				_la = _input.LA(1);
				if (_la==COMMA || _la==SEMI) {
					{
					setState(204); commaOrSemicolon();
					}
				}

				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(212); match(RBRACKET);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public ConstValuePairContext constValuePair(int i) {
			return getRuleContext(ConstValuePairContext.class,i);
		}
		public List<ConstValuePairContext> constValuePair() {
			return getRuleContexts(ConstValuePairContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public ConstMapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constMap; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterConstMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitConstMap(this);
		}
	}

	public final ConstMapContext constMap() throws RecognitionException {
		ConstMapContext _localctx = new ConstMapContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constMap);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214); match(LBRACE);
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << DUBCONSTANT) | (1L << IDENTIFIER) | (1L << LITERAL))) != 0)) {
				{
				{
				setState(215); constValuePair();
				}
				}
				setState(220);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(221); match(RBRACE);
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
		public TerminalNode COLON() { return getToken(KiaraParser.COLON, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public ConstValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterConstValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitConstValuePair(this);
		}
	}

	public final ConstValuePairContext constValuePair() throws RecognitionException {
		ConstValuePairContext _localctx = new ConstValuePairContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_constValuePair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223); ((ConstValuePairContext)_localctx).k = constValue();
			setState(224); match(COLON);
			setState(225); ((ConstValuePairContext)_localctx).v = constValue();
			setState(227);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(226); commaOrSemicolon();
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public TerminalNode STRUCT() { return getToken(KiaraParser.STRUCT, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public XsdAllContext xsdAll() {
			return getRuleContext(XsdAllContext.class,0);
		}
		public StructContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterStruct(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitStruct(this);
		}
	}

	public final StructContext struct() throws RecognitionException {
		StructContext _localctx = new StructContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_struct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229); match(STRUCT);
			setState(230); match(IDENTIFIER);
			setState(232);
			_la = _input.LA(1);
			if (_la==XSD_ALL) {
				{
				setState(231); xsdAll();
				}
			}

			setState(234); match(LBRACE);
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(235); field();
				}
				}
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(241); match(RBRACE);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public TerminalNode UNION() { return getToken(KiaraParser.UNION, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public XsdAllContext xsdAll() {
			return getRuleContext(XsdAllContext.class,0);
		}
		public UnionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_union; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitUnion(this);
		}
	}

	public final UnionContext union() throws RecognitionException {
		UnionContext _localctx = new UnionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_union);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243); match(UNION);
			setState(244); match(IDENTIFIER);
			setState(246);
			_la = _input.LA(1);
			if (_la==XSD_ALL) {
				{
				setState(245); xsdAll();
				}
			}

			setState(248); match(LBRACE);
			setState(252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(249); field();
				}
				}
				setState(254);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(255); match(RBRACE);
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
		public TerminalNode XSD_ALL() { return getToken(KiaraParser.XSD_ALL, 0); }
		public XsdAllContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdAll; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterXsdAll(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitXsdAll(this);
		}
	}

	public final XsdAllContext xsdAll() throws RecognitionException {
		XsdAllContext _localctx = new XsdAllContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_xsdAll);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257); match(XSD_ALL);
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
		public TerminalNode XSD_OPTIONAL() { return getToken(KiaraParser.XSD_OPTIONAL, 0); }
		public XsdOptionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdOptional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterXsdOptional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitXsdOptional(this);
		}
	}

	public final XsdOptionalContext xsdOptional() throws RecognitionException {
		XsdOptionalContext _localctx = new XsdOptionalContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_xsdOptional);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259); match(XSD_OPTIONAL);
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
		public TerminalNode XSD_NILLABLE() { return getToken(KiaraParser.XSD_NILLABLE, 0); }
		public XsdNillableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdNillable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterXsdNillable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitXsdNillable(this);
		}
	}

	public final XsdNillableContext xsdNillable() throws RecognitionException {
		XsdNillableContext _localctx = new XsdNillableContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_xsdNillable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261); match(XSD_NILLABLE);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public TerminalNode XSD_ATTRIBUTES() { return getToken(KiaraParser.XSD_ATTRIBUTES, 0); }
		public XsdAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xsdAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterXsdAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitXsdAttributes(this);
		}
	}

	public final XsdAttributesContext xsdAttributes() throws RecognitionException {
		XsdAttributesContext _localctx = new XsdAttributesContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_xsdAttributes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263); match(XSD_ATTRIBUTES);
			setState(264); match(LBRACE);
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(265); field();
				}
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(271); match(RBRACE);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public TerminalNode EXCEPTION() { return getToken(KiaraParser.EXCEPTION, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public XceptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xception; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterXception(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitXception(this);
		}
	}

	public final XceptionContext xception() throws RecognitionException {
		XceptionContext _localctx = new XceptionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_xception);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273); match(EXCEPTION);
			setState(274); match(IDENTIFIER);
			setState(275); match(LBRACE);
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(276); field();
				}
				}
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(282); match(RBRACE);
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

	public static class AnnotationDefContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode ANNOTATION() { return getToken(KiaraParser.ANNOTATION, 0); }
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public AnnotationDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotationDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotationDef(this);
		}
	}

	public final AnnotationDefContext annotationDef() throws RecognitionException {
		AnnotationDefContext _localctx = new AnnotationDefContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_annotationDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284); match(ANNOTATION);
			setState(285); match(IDENTIFIER);
			setState(286); match(LBRACE);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(287); field();
				}
				}
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(293); match(RBRACE);
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
		public TerminalNode LBRACE() { return getToken(KiaraParser.LBRACE, 0); }
		public AnnotationListContext annotationList() {
			return getRuleContext(AnnotationListContext.class,0);
		}
		public TerminalNode IDENTIFIER(int i) {
			return getToken(KiaraParser.IDENTIFIER, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(KiaraParser.IDENTIFIER); }
		public TerminalNode SERVICE() { return getToken(KiaraParser.SERVICE, 0); }
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public TerminalNode RBRACE() { return getToken(KiaraParser.RBRACE, 0); }
		public TerminalNode EXTENDS() { return getToken(KiaraParser.EXTENDS, 0); }
		public ServiceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_service; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterService(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitService(this);
		}
	}

	public final ServiceContext service() throws RecognitionException {
		ServiceContext _localctx = new ServiceContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_service);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295); annotationList();
			setState(296); match(SERVICE);
			setState(297); match(IDENTIFIER);
			setState(300);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(298); match(EXTENDS);
				setState(299); match(IDENTIFIER);
				}
			}

			setState(302); match(LBRACE);
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << VOID) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(303); function();
				}
				}
				setState(308);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(309); match(RBRACE);
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
		public List<AnnotationListContext> annotationList() {
			return getRuleContexts(AnnotationListContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public ThrowzContext throwz() {
			return getRuleContext(ThrowzContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(KiaraParser.RPAREN, 0); }
		public FunctionTypeContext functionType() {
			return getRuleContext(FunctionTypeContext.class,0);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public AnnotationListContext annotationList(int i) {
			return getRuleContext(AnnotationListContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(KiaraParser.LPAREN, 0); }
		public CommaOrSemicolonContext commaOrSemicolon() {
			return getRuleContext(CommaOrSemicolonContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311); annotationList();
			setState(312); functionType();
			setState(313); annotationList();
			setState(314); match(IDENTIFIER);
			setState(315); match(LPAREN);
			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(316); field();
				}
				}
				setState(321);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(322); match(RPAREN);
			setState(324);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(323); throwz();
				}
			}

			setState(327);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(326); commaOrSemicolon();
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

	public static class ThrowzContext extends ParserRuleContext {
		public TerminalNode THROWS() { return getToken(KiaraParser.THROWS, 0); }
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(KiaraParser.RPAREN, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public TerminalNode LPAREN() { return getToken(KiaraParser.LPAREN, 0); }
		public ThrowzContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwz; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterThrowz(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitThrowz(this);
		}
	}

	public final ThrowzContext throwz() throws RecognitionException {
		ThrowzContext _localctx = new ThrowzContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_throwz);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329); match(THROWS);
			setState(330); match(LPAREN);
			setState(334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REQUIRED) | (1L << OPTIONAL) | (1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY) | (1L << LBRACKET) | (1L << INTCONSTANT) | (1L << HEXCONSTANT) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(331); field();
				}
				}
				setState(336);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(337); match(RPAREN);
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
		public AnnotationListContext annotationList() {
			return getRuleContext(AnnotationListContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public FieldValueContext fieldValue() {
			return getRuleContext(FieldValueContext.class,0);
		}
		public FieldIdentifierContext fieldIdentifier() {
			return getRuleContext(FieldIdentifierContext.class,0);
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
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitField(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			_la = _input.LA(1);
			if (_la==INTCONSTANT || _la==HEXCONSTANT) {
				{
				setState(339); fieldIdentifier();
				}
			}

			setState(342); annotationList();
			setState(344);
			_la = _input.LA(1);
			if (_la==REQUIRED || _la==OPTIONAL) {
				{
				setState(343); fieldRequiredness();
				}
			}

			setState(346); fieldType();
			setState(347); match(IDENTIFIER);
			setState(349);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(348); fieldValue();
				}
			}

			setState(352);
			_la = _input.LA(1);
			if (_la==XSD_OPTIONAL) {
				{
				setState(351); xsdOptional();
				}
			}

			setState(355);
			_la = _input.LA(1);
			if (_la==XSD_NILLABLE) {
				{
				setState(354); xsdNillable();
				}
			}

			setState(358);
			_la = _input.LA(1);
			if (_la==XSD_ATTRIBUTES) {
				{
				setState(357); xsdAttributes();
				}
			}

			setState(361);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(360); commaOrSemicolon();
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
		public TerminalNode COLON() { return getToken(KiaraParser.COLON, 0); }
		public FieldIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFieldIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFieldIdentifier(this);
		}
	}

	public final FieldIdentifierContext fieldIdentifier() throws RecognitionException {
		FieldIdentifierContext _localctx = new FieldIdentifierContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_fieldIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363); intConstant();
			setState(364); match(COLON);
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
		public TerminalNode OPTIONAL() { return getToken(KiaraParser.OPTIONAL, 0); }
		public TerminalNode REQUIRED() { return getToken(KiaraParser.REQUIRED, 0); }
		public FieldRequirednessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldRequiredness; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFieldRequiredness(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFieldRequiredness(this);
		}
	}

	public final FieldRequirednessContext fieldRequiredness() throws RecognitionException {
		FieldRequirednessContext _localctx = new FieldRequirednessContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_fieldRequiredness);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
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
		public TerminalNode EQ() { return getToken(KiaraParser.EQ, 0); }
		public FieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFieldValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFieldValue(this);
		}
	}

	public final FieldValueContext fieldValue() throws RecognitionException {
		FieldValueContext _localctx = new FieldValueContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368); match(EQ);
			setState(369); constValue();
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
		public TerminalNode VOID() { return getToken(KiaraParser.VOID, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public FunctionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFunctionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFunctionType(this);
		}
	}

	public final FunctionTypeContext functionType() throws RecognitionException {
		FunctionTypeContext _localctx = new FunctionTypeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_functionType);
		try {
			setState(373);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(371); match(VOID);
				}
				break;
			case STRING:
			case BINARY:
			case SLIST:
			case BOOLEAN:
			case I8:
			case U8:
			case I16:
			case U16:
			case I32:
			case U32:
			case I64:
			case U64:
			case FLOAT:
			case DOUBLE:
			case ANY:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(372); fieldType();
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
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public GenericTypeContext genericType() {
			return getRuleContext(GenericTypeContext.class,0);
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
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterFieldType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitFieldType(this);
		}
	}

	public final FieldTypeContext fieldType() throws RecognitionException {
		FieldTypeContext _localctx = new FieldTypeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_fieldType);
		try {
			setState(378);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(375); match(IDENTIFIER);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(376); baseType();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(377); genericType();
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

	public static class BaseTypeContext extends ParserRuleContext {
		public SimpleBaseTypeContext simpleBaseType() {
			return getRuleContext(SimpleBaseTypeContext.class,0);
		}
		public BaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitBaseType(this);
		}
	}

	public final BaseTypeContext baseType() throws RecognitionException {
		BaseTypeContext _localctx = new BaseTypeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_baseType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380); simpleBaseType();
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
		public TerminalNode I8() { return getToken(KiaraParser.I8, 0); }
		public TerminalNode FLOAT() { return getToken(KiaraParser.FLOAT, 0); }
		public TerminalNode SLIST() { return getToken(KiaraParser.SLIST, 0); }
		public TerminalNode ANY() { return getToken(KiaraParser.ANY, 0); }
		public TerminalNode I64() { return getToken(KiaraParser.I64, 0); }
		public TerminalNode DOUBLE() { return getToken(KiaraParser.DOUBLE, 0); }
		public TerminalNode U8() { return getToken(KiaraParser.U8, 0); }
		public TerminalNode U32() { return getToken(KiaraParser.U32, 0); }
		public TerminalNode BOOLEAN() { return getToken(KiaraParser.BOOLEAN, 0); }
		public TerminalNode I16() { return getToken(KiaraParser.I16, 0); }
		public TerminalNode U16() { return getToken(KiaraParser.U16, 0); }
		public TerminalNode I32() { return getToken(KiaraParser.I32, 0); }
		public TerminalNode STRING() { return getToken(KiaraParser.STRING, 0); }
		public TerminalNode BINARY() { return getToken(KiaraParser.BINARY, 0); }
		public TerminalNode U64() { return getToken(KiaraParser.U64, 0); }
		public SimpleBaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleBaseType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterSimpleBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitSimpleBaseType(this);
		}
	}

	public final SimpleBaseTypeContext simpleBaseType() throws RecognitionException {
		SimpleBaseTypeContext _localctx = new SimpleBaseTypeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_simpleBaseType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << BINARY) | (1L << SLIST) | (1L << BOOLEAN) | (1L << I8) | (1L << U8) | (1L << I16) | (1L << U16) | (1L << I32) | (1L << U32) | (1L << I64) | (1L << U64) | (1L << FLOAT) | (1L << DOUBLE) | (1L << ANY))) != 0)) ) {
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

	public static class GenericTypeContext extends ParserRuleContext {
		public SimpleGenericTypeContext simpleGenericType() {
			return getRuleContext(SimpleGenericTypeContext.class,0);
		}
		public GenericTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterGenericType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitGenericType(this);
		}
	}

	public final GenericTypeContext genericType() throws RecognitionException {
		GenericTypeContext _localctx = new GenericTypeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_genericType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384); simpleGenericType();
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

	public static class SimpleGenericTypeContext extends ParserRuleContext {
		public GenericTypeArgListContext genericTypeArgList() {
			return getRuleContext(GenericTypeArgListContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public SimpleGenericTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleGenericType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterSimpleGenericType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitSimpleGenericType(this);
		}
	}

	public final SimpleGenericTypeContext simpleGenericType() throws RecognitionException {
		SimpleGenericTypeContext _localctx = new SimpleGenericTypeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_simpleGenericType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386); match(IDENTIFIER);
			setState(387); genericTypeArgList();
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

	public static class GenericTypeArgContext extends ParserRuleContext {
		public ConstMapContext constMap() {
			return getRuleContext(ConstMapContext.class,0);
		}
		public TerminalNode INTCONSTANT() { return getToken(KiaraParser.INTCONSTANT, 0); }
		public TerminalNode LITERAL() { return getToken(KiaraParser.LITERAL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public ConstListContext constList() {
			return getRuleContext(ConstListContext.class,0);
		}
		public GenericTypeContext genericType() {
			return getRuleContext(GenericTypeContext.class,0);
		}
		public TerminalNode VOID() { return getToken(KiaraParser.VOID, 0); }
		public TerminalNode DUBCONSTANT() { return getToken(KiaraParser.DUBCONSTANT, 0); }
		public BaseTypeContext baseType() {
			return getRuleContext(BaseTypeContext.class,0);
		}
		public GenericTypeArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericTypeArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterGenericTypeArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitGenericTypeArg(this);
		}
	}

	public final GenericTypeArgContext genericTypeArg() throws RecognitionException {
		GenericTypeArgContext _localctx = new GenericTypeArgContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_genericTypeArg);
		int _la;
		try {
			setState(395);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(389);
				_la = _input.LA(1);
				if ( !(_la==VOID || _la==IDENTIFIER) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(390); baseType();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(391); genericType();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(392);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTCONSTANT) | (1L << DUBCONSTANT) | (1L << LITERAL))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(393); constList();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(394); constMap();
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

	public static class GenericTypeArgListContext extends ParserRuleContext {
		public List<GenericTypeArgContext> genericTypeArg() {
			return getRuleContexts(GenericTypeArgContext.class);
		}
		public TerminalNode LT() { return getToken(KiaraParser.LT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(KiaraParser.COMMA); }
		public TerminalNode GT() { return getToken(KiaraParser.GT, 0); }
		public GenericTypeArgContext genericTypeArg(int i) {
			return getRuleContext(GenericTypeArgContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(KiaraParser.COMMA, i);
		}
		public GenericTypeArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericTypeArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterGenericTypeArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitGenericTypeArgList(this);
		}
	}

	public final GenericTypeArgListContext genericTypeArgList() throws RecognitionException {
		GenericTypeArgListContext _localctx = new GenericTypeArgListContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_genericTypeArgList);
		int _la;
		try {
			setState(410);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(397); match(LT);
				setState(398); genericTypeArg();
				setState(403);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(399); match(COMMA);
					setState(400); genericTypeArg();
					}
					}
					setState(405);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(406); match(GT);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(408); match(LT);
				setState(409); match(GT);
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

	public static class AnnotationListContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(KiaraParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(KiaraParser.RBRACKET, 0); }
		public NonEmptyAnnotationListContext nonEmptyAnnotationList() {
			return getRuleContext(NonEmptyAnnotationListContext.class,0);
		}
		public AnnotationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotationList(this);
		}
	}

	public final AnnotationListContext annotationList() throws RecognitionException {
		AnnotationListContext _localctx = new AnnotationListContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_annotationList);
		try {
			setState(417);
			switch (_input.LA(1)) {
			case LBRACKET:
				enterOuterAlt(_localctx, 1);
				{
				setState(412); match(LBRACKET);
				setState(413); nonEmptyAnnotationList();
				setState(414); match(RBRACKET);
				}
				break;
			case ENUM:
			case SENUM:
			case STRUCT:
			case UNION:
			case EXCEPTION:
			case SERVICE:
			case REQUIRED:
			case OPTIONAL:
			case ANNOTATION:
			case VOID:
			case STRING:
			case BINARY:
			case SLIST:
			case BOOLEAN:
			case I8:
			case U8:
			case I16:
			case U16:
			case I32:
			case U32:
			case I64:
			case U64:
			case FLOAT:
			case DOUBLE:
			case ANY:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
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

	public static class NonEmptyAnnotationListContext extends ParserRuleContext {
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KiaraParser.COMMA); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public TerminalNode COMMA(int i) {
			return getToken(KiaraParser.COMMA, i);
		}
		public NonEmptyAnnotationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonEmptyAnnotationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterNonEmptyAnnotationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitNonEmptyAnnotationList(this);
		}
	}

	public final NonEmptyAnnotationListContext nonEmptyAnnotationList() throws RecognitionException {
		NonEmptyAnnotationListContext _localctx = new NonEmptyAnnotationListContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_nonEmptyAnnotationList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419); annotation();
			setState(424);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(420); match(COMMA);
				setState(421); annotation();
				}
				}
				setState(426);
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

	public static class AnnotationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public TerminalNode RPAREN() { return getToken(KiaraParser.RPAREN, 0); }
		public TerminalNode LPAREN() { return getToken(KiaraParser.LPAREN, 0); }
		public AnnotationArgListContext annotationArgList() {
			return getRuleContext(AnnotationArgListContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotation(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_annotation);
		try {
			setState(433);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(427); match(IDENTIFIER);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(428); match(IDENTIFIER);
				setState(429); match(LPAREN);
				setState(430); annotationArgList();
				setState(431); match(RPAREN);
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

	public static class AnnotationArgContext extends ParserRuleContext {
		public ConstValueContext constValue() {
			return getRuleContext(ConstValueContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(KiaraParser.IDENTIFIER, 0); }
		public TerminalNode EQ() { return getToken(KiaraParser.EQ, 0); }
		public AnnotationArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotationArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotationArg(this);
		}
	}

	public final AnnotationArgContext annotationArg() throws RecognitionException {
		AnnotationArgContext _localctx = new AnnotationArgContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_annotationArg);
		try {
			setState(439);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(435); match(IDENTIFIER);
				setState(436); match(EQ);
				setState(437); constValue();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(438); constValue();
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

	public static class AnnotationArgListContext extends ParserRuleContext {
		public NonEmptyAnnotationArgListContext nonEmptyAnnotationArgList() {
			return getRuleContext(NonEmptyAnnotationArgListContext.class,0);
		}
		public AnnotationArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterAnnotationArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitAnnotationArgList(this);
		}
	}

	public final AnnotationArgListContext annotationArgList() throws RecognitionException {
		AnnotationArgListContext _localctx = new AnnotationArgListContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_annotationArgList);
		try {
			setState(443);
			switch (_input.LA(1)) {
			case LBRACE:
			case LBRACKET:
			case INTCONSTANT:
			case HEXCONSTANT:
			case DUBCONSTANT:
			case IDENTIFIER:
			case LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(441); nonEmptyAnnotationArgList();
				}
				break;
			case RPAREN:
				enterOuterAlt(_localctx, 2);
				{
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

	public static class NonEmptyAnnotationArgListContext extends ParserRuleContext {
		public AnnotationArgContext annotationArg(int i) {
			return getRuleContext(AnnotationArgContext.class,i);
		}
		public List<AnnotationArgContext> annotationArg() {
			return getRuleContexts(AnnotationArgContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(KiaraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KiaraParser.COMMA, i);
		}
		public NonEmptyAnnotationArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonEmptyAnnotationArgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterNonEmptyAnnotationArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitNonEmptyAnnotationArgList(this);
		}
	}

	public final NonEmptyAnnotationArgListContext nonEmptyAnnotationArgList() throws RecognitionException {
		NonEmptyAnnotationArgListContext _localctx = new NonEmptyAnnotationArgListContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_nonEmptyAnnotationArgList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445); annotationArg();
			setState(450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(446); match(COMMA);
				setState(447); annotationArg();
				}
				}
				setState(452);
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

	public static class IntConstantContext extends ParserRuleContext {
		public TerminalNode INTCONSTANT() { return getToken(KiaraParser.INTCONSTANT, 0); }
		public TerminalNode HEXCONSTANT() { return getToken(KiaraParser.HEXCONSTANT, 0); }
		public IntConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intConstant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).enterIntConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KiaraListener ) ((KiaraListener)listener).exitIntConstant(this);
		}
	}

	public final IntConstantContext intConstant() throws RecognitionException {
		IntConstantContext _localctx = new IntConstantContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_intConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3<\u01ca\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\3\2\3\2\3\2\3\3\7"+
		"\3i\n\3\f\3\16\3l\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4v\n\4\3\5\7"+
		"\5y\n\5\f\5\16\5|\13\5\3\6\3\6\3\6\5\6\u0081\n\6\3\7\3\7\3\7\3\7\5\7\u0087"+
		"\n\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u0091\n\t\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\7\f\u009d\n\f\f\f\16\f\u00a0\13\f\3\f\3\f\3\r\3"+
		"\r\3\r\3\r\5\r\u00a8\n\r\3\r\3\r\5\r\u00ac\n\r\5\r\u00ae\n\r\3\16\3\16"+
		"\3\16\3\16\7\16\u00b4\n\16\f\16\16\16\u00b7\13\16\3\16\3\16\3\17\3\17"+
		"\5\17\u00bd\n\17\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00c5\n\20\3\21\3"+
		"\21\3\21\3\21\5\21\u00cb\n\21\3\22\3\22\3\22\5\22\u00d0\n\22\7\22\u00d2"+
		"\n\22\f\22\16\22\u00d5\13\22\3\22\3\22\3\23\3\23\7\23\u00db\n\23\f\23"+
		"\16\23\u00de\13\23\3\23\3\23\3\24\3\24\3\24\3\24\5\24\u00e6\n\24\3\25"+
		"\3\25\3\25\5\25\u00eb\n\25\3\25\3\25\7\25\u00ef\n\25\f\25\16\25\u00f2"+
		"\13\25\3\25\3\25\3\26\3\26\3\26\5\26\u00f9\n\26\3\26\3\26\7\26\u00fd\n"+
		"\26\f\26\16\26\u0100\13\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\32\7\32\u010d\n\32\f\32\16\32\u0110\13\32\3\32\3\32\3\33\3\33"+
		"\3\33\3\33\7\33\u0118\n\33\f\33\16\33\u011b\13\33\3\33\3\33\3\34\3\34"+
		"\3\34\3\34\7\34\u0123\n\34\f\34\16\34\u0126\13\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\35\5\35\u012f\n\35\3\35\3\35\7\35\u0133\n\35\f\35\16\35\u0136"+
		"\13\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\7\36\u0140\n\36\f\36\16"+
		"\36\u0143\13\36\3\36\3\36\5\36\u0147\n\36\3\36\5\36\u014a\n\36\3\37\3"+
		"\37\3\37\7\37\u014f\n\37\f\37\16\37\u0152\13\37\3\37\3\37\3 \5 \u0157"+
		"\n \3 \3 \5 \u015b\n \3 \3 \3 \5 \u0160\n \3 \5 \u0163\n \3 \5 \u0166"+
		"\n \3 \5 \u0169\n \3 \5 \u016c\n \3!\3!\3!\3\"\3\"\3#\3#\3#\3$\3$\5$\u0178"+
		"\n$\3%\3%\3%\5%\u017d\n%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3*\3*\3*\3*\3*\3"+
		"*\5*\u018e\n*\3+\3+\3+\3+\7+\u0194\n+\f+\16+\u0197\13+\3+\3+\3+\3+\5+"+
		"\u019d\n+\3,\3,\3,\3,\3,\5,\u01a4\n,\3-\3-\3-\7-\u01a9\n-\f-\16-\u01ac"+
		"\13-\3.\3.\3.\3.\3.\3.\5.\u01b4\n.\3/\3/\3/\3/\5/\u01ba\n/\3\60\3\60\5"+
		"\60\u01be\n\60\3\61\3\61\3\61\7\61\u01c3\n\61\f\61\16\61\u01c6\13\61\3"+
		"\62\3\62\3\62\2\2\63\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJLNPRTVXZ\\^`b\2\t\4\2((**\4\2\66\67<<\3\2\24\25\3\2"+
		"\30&\4\2\27\27\67\67\5\2\64\64\66\66<<\3\2\64\65\u01d6\2d\3\2\2\2\4j\3"+
		"\2\2\2\6u\3\2\2\2\bz\3\2\2\2\n\u0080\3\2\2\2\f\u0086\3\2\2\2\16\u0088"+
		"\3\2\2\2\20\u0090\3\2\2\2\22\u0092\3\2\2\2\24\u0096\3\2\2\2\26\u0098\3"+
		"\2\2\2\30\u00ad\3\2\2\2\32\u00af\3\2\2\2\34\u00ba\3\2\2\2\36\u00be\3\2"+
		"\2\2 \u00ca\3\2\2\2\"\u00cc\3\2\2\2$\u00d8\3\2\2\2&\u00e1\3\2\2\2(\u00e7"+
		"\3\2\2\2*\u00f5\3\2\2\2,\u0103\3\2\2\2.\u0105\3\2\2\2\60\u0107\3\2\2\2"+
		"\62\u0109\3\2\2\2\64\u0113\3\2\2\2\66\u011e\3\2\2\28\u0129\3\2\2\2:\u0139"+
		"\3\2\2\2<\u014b\3\2\2\2>\u0156\3\2\2\2@\u016d\3\2\2\2B\u0170\3\2\2\2D"+
		"\u0172\3\2\2\2F\u0177\3\2\2\2H\u017c\3\2\2\2J\u017e\3\2\2\2L\u0180\3\2"+
		"\2\2N\u0182\3\2\2\2P\u0184\3\2\2\2R\u018d\3\2\2\2T\u019c\3\2\2\2V\u01a3"+
		"\3\2\2\2X\u01a5\3\2\2\2Z\u01b3\3\2\2\2\\\u01b9\3\2\2\2^\u01bd\3\2\2\2"+
		"`\u01bf\3\2\2\2b\u01c7\3\2\2\2de\5\4\3\2ef\5\b\5\2f\3\3\2\2\2gi\5\6\4"+
		"\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\5\3\2\2\2lj\3\2\2\2mn\7\3"+
		"\2\2nv\7<\2\2op\7\4\2\2pq\7\67\2\2qv\7\67\2\2rs\7\4\2\2st\7\'\2\2tv\7"+
		"\67\2\2um\3\2\2\2uo\3\2\2\2ur\3\2\2\2v\7\3\2\2\2wy\5\n\6\2xw\3\2\2\2y"+
		"|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\t\3\2\2\2|z\3\2\2\2}\u0081\5\36\20\2~\u0081"+
		"\5\f\7\2\177\u0081\58\35\2\u0080}\3\2\2\2\u0080~\3\2\2\2\u0080\177\3\2"+
		"\2\2\u0081\13\3\2\2\2\u0082\u0087\5\16\b\2\u0083\u0084\5V,\2\u0084\u0085"+
		"\5\20\t\2\u0085\u0087\3\2\2\2\u0086\u0082\3\2\2\2\u0086\u0083\3\2\2\2"+
		"\u0087\r\3\2\2\2\u0088\u0089\5\22\n\2\u0089\17\3\2\2\2\u008a\u0091\5\26"+
		"\f\2\u008b\u0091\5\32\16\2\u008c\u0091\5(\25\2\u008d\u0091\5*\26\2\u008e"+
		"\u0091\5\64\33\2\u008f\u0091\5\66\34\2\u0090\u008a\3\2\2\2\u0090\u008b"+
		"\3\2\2\2\u0090\u008c\3\2\2\2\u0090\u008d\3\2\2\2\u0090\u008e\3\2\2\2\u0090"+
		"\u008f\3\2\2\2\u0091\21\3\2\2\2\u0092\u0093\7\5\2\2\u0093\u0094\5H%\2"+
		"\u0094\u0095\7\67\2\2\u0095\23\3\2\2\2\u0096\u0097\t\2\2\2\u0097\25\3"+
		"\2\2\2\u0098\u0099\7\6\2\2\u0099\u009a\7\67\2\2\u009a\u009e\7+\2\2\u009b"+
		"\u009d\5\30\r\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3"+
		"\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1"+
		"\u00a2\7,\2\2\u00a2\27\3\2\2\2\u00a3\u00a4\7\67\2\2\u00a4\u00a5\7\61\2"+
		"\2\u00a5\u00a7\5b\62\2\u00a6\u00a8\5\24\13\2\u00a7\u00a6\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\u00ae\3\2\2\2\u00a9\u00ab\7\67\2\2\u00aa\u00ac\5"+
		"\24\13\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ae\3\2\2\2\u00ad"+
		"\u00a3\3\2\2\2\u00ad\u00a9\3\2\2\2\u00ae\31\3\2\2\2\u00af\u00b0\7\7\2"+
		"\2\u00b0\u00b1\7\67\2\2\u00b1\u00b5\7+\2\2\u00b2\u00b4\5\34\17\2\u00b3"+
		"\u00b2\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2"+
		"\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7,\2\2\u00b9"+
		"\33\3\2\2\2\u00ba\u00bc\7<\2\2\u00bb\u00bd\5\24\13\2\u00bc\u00bb\3\2\2"+
		"\2\u00bc\u00bd\3\2\2\2\u00bd\35\3\2\2\2\u00be\u00bf\7\b\2\2\u00bf\u00c0"+
		"\5H%\2\u00c0\u00c1\7\67\2\2\u00c1\u00c2\7\61\2\2\u00c2\u00c4\5 \21\2\u00c3"+
		"\u00c5\5\24\13\2\u00c4\u00c3\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\37\3\2"+
		"\2\2\u00c6\u00cb\5b\62\2\u00c7\u00cb\t\3\2\2\u00c8\u00cb\5\"\22\2\u00c9"+
		"\u00cb\5$\23\2\u00ca\u00c6\3\2\2\2\u00ca\u00c7\3\2\2\2\u00ca\u00c8\3\2"+
		"\2\2\u00ca\u00c9\3\2\2\2\u00cb!\3\2\2\2\u00cc\u00d3\7-\2\2\u00cd\u00cf"+
		"\5 \21\2\u00ce\u00d0\5\24\13\2\u00cf\u00ce\3\2\2\2\u00cf\u00d0\3\2\2\2"+
		"\u00d0\u00d2\3\2\2\2\u00d1\u00cd\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1"+
		"\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6"+
		"\u00d7\7.\2\2\u00d7#\3\2\2\2\u00d8\u00dc\7+\2\2\u00d9\u00db\5&\24\2\u00da"+
		"\u00d9\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2"+
		"\2\2\u00dd\u00df\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e0\7,\2\2\u00e0"+
		"%\3\2\2\2\u00e1\u00e2\5 \21\2\u00e2\u00e3\7)\2\2\u00e3\u00e5\5 \21\2\u00e4"+
		"\u00e6\5\24\13\2\u00e5\u00e4\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\'\3\2\2"+
		"\2\u00e7\u00e8\7\t\2\2\u00e8\u00ea\7\67\2\2\u00e9\u00eb\5,\27\2\u00ea"+
		"\u00e9\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00f0\7+"+
		"\2\2\u00ed\u00ef\5> \2\u00ee\u00ed\3\2\2\2\u00ef\u00f2\3\2\2\2\u00f0\u00ee"+
		"\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f3\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f3"+
		"\u00f4\7,\2\2\u00f4)\3\2\2\2\u00f5\u00f6\7\n\2\2\u00f6\u00f8\7\67\2\2"+
		"\u00f7\u00f9\5,\27\2\u00f8\u00f7\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fa"+
		"\3\2\2\2\u00fa\u00fe\7+\2\2\u00fb\u00fd\5> \2\u00fc\u00fb\3\2\2\2\u00fd"+
		"\u0100\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0101\3\2"+
		"\2\2\u0100\u00fe\3\2\2\2\u0101\u0102\7,\2\2\u0102+\3\2\2\2\u0103\u0104"+
		"\7\13\2\2\u0104-\3\2\2\2\u0105\u0106\7\f\2\2\u0106/\3\2\2\2\u0107\u0108"+
		"\7\r\2\2\u0108\61\3\2\2\2\u0109\u010a\7\16\2\2\u010a\u010e\7+\2\2\u010b"+
		"\u010d\5> \2\u010c\u010b\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u010c\3\2\2"+
		"\2\u010e\u010f\3\2\2\2\u010f\u0111\3\2\2\2\u0110\u010e\3\2\2\2\u0111\u0112"+
		"\7,\2\2\u0112\63\3\2\2\2\u0113\u0114\7\17\2\2\u0114\u0115\7\67\2\2\u0115"+
		"\u0119\7+\2\2\u0116\u0118\5> \2\u0117\u0116\3\2\2\2\u0118\u011b\3\2\2"+
		"\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011c\3\2\2\2\u011b\u0119"+
		"\3\2\2\2\u011c\u011d\7,\2\2\u011d\65\3\2\2\2\u011e\u011f\7\26\2\2\u011f"+
		"\u0120\7\67\2\2\u0120\u0124\7+\2\2\u0121\u0123\5> \2\u0122\u0121\3\2\2"+
		"\2\u0123\u0126\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0127"+
		"\3\2\2\2\u0126\u0124\3\2\2\2\u0127\u0128\7,\2\2\u0128\67\3\2\2\2\u0129"+
		"\u012a\5V,\2\u012a\u012b\7\20\2\2\u012b\u012e\7\67\2\2\u012c\u012d\7\21"+
		"\2\2\u012d\u012f\7\67\2\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f"+
		"\u0130\3\2\2\2\u0130\u0134\7+\2\2\u0131\u0133\5:\36\2\u0132\u0131\3\2"+
		"\2\2\u0133\u0136\3\2\2\2\u0134\u0132\3\2\2\2\u0134\u0135\3\2\2\2\u0135"+
		"\u0137\3\2\2\2\u0136\u0134\3\2\2\2\u0137\u0138\7,\2\2\u01389\3\2\2\2\u0139"+
		"\u013a\5V,\2\u013a\u013b\5F$\2\u013b\u013c\5V,\2\u013c\u013d\7\67\2\2"+
		"\u013d\u0141\7/\2\2\u013e\u0140\5> \2\u013f\u013e\3\2\2\2\u0140\u0143"+
		"\3\2\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0144\3\2\2\2\u0143"+
		"\u0141\3\2\2\2\u0144\u0146\7\60\2\2\u0145\u0147\5<\37\2\u0146\u0145\3"+
		"\2\2\2\u0146\u0147\3\2\2\2\u0147\u0149\3\2\2\2\u0148\u014a\5\24\13\2\u0149"+
		"\u0148\3\2\2\2\u0149\u014a\3\2\2\2\u014a;\3\2\2\2\u014b\u014c\7\23\2\2"+
		"\u014c\u0150\7/\2\2\u014d\u014f\5> \2\u014e\u014d\3\2\2\2\u014f\u0152"+
		"\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0153\3\2\2\2\u0152"+
		"\u0150\3\2\2\2\u0153\u0154\7\60\2\2\u0154=\3\2\2\2\u0155\u0157\5@!\2\u0156"+
		"\u0155\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015a\5V"+
		",\2\u0159\u015b\5B\"\2\u015a\u0159\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c"+
		"\3\2\2\2\u015c\u015d\5H%\2\u015d\u015f\7\67\2\2\u015e\u0160\5D#\2\u015f"+
		"\u015e\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0162\3\2\2\2\u0161\u0163\5."+
		"\30\2\u0162\u0161\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0165\3\2\2\2\u0164"+
		"\u0166\5\60\31\2\u0165\u0164\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168\3"+
		"\2\2\2\u0167\u0169\5\62\32\2\u0168\u0167\3\2\2\2\u0168\u0169\3\2\2\2\u0169"+
		"\u016b\3\2\2\2\u016a\u016c\5\24\13\2\u016b\u016a\3\2\2\2\u016b\u016c\3"+
		"\2\2\2\u016c?\3\2\2\2\u016d\u016e\5b\62\2\u016e\u016f\7)\2\2\u016fA\3"+
		"\2\2\2\u0170\u0171\t\4\2\2\u0171C\3\2\2\2\u0172\u0173\7\61\2\2\u0173\u0174"+
		"\5 \21\2\u0174E\3\2\2\2\u0175\u0178\7\27\2\2\u0176\u0178\5H%\2\u0177\u0175"+
		"\3\2\2\2\u0177\u0176\3\2\2\2\u0178G\3\2\2\2\u0179\u017d\7\67\2\2\u017a"+
		"\u017d\5J&\2\u017b\u017d\5N(\2\u017c\u0179\3\2\2\2\u017c\u017a\3\2\2\2"+
		"\u017c\u017b\3\2\2\2\u017dI\3\2\2\2\u017e\u017f\5L\'\2\u017fK\3\2\2\2"+
		"\u0180\u0181\t\5\2\2\u0181M\3\2\2\2\u0182\u0183\5P)\2\u0183O\3\2\2\2\u0184"+
		"\u0185\7\67\2\2\u0185\u0186\5T+\2\u0186Q\3\2\2\2\u0187\u018e\t\6\2\2\u0188"+
		"\u018e\5J&\2\u0189\u018e\5N(\2\u018a\u018e\t\7\2\2\u018b\u018e\5\"\22"+
		"\2\u018c\u018e\5$\23\2\u018d\u0187\3\2\2\2\u018d\u0188\3\2\2\2\u018d\u0189"+
		"\3\2\2\2\u018d\u018a\3\2\2\2\u018d\u018b\3\2\2\2\u018d\u018c\3\2\2\2\u018e"+
		"S\3\2\2\2\u018f\u0190\7\62\2\2\u0190\u0195\5R*\2\u0191\u0192\7(\2\2\u0192"+
		"\u0194\5R*\2\u0193\u0191\3\2\2\2\u0194\u0197\3\2\2\2\u0195\u0193\3\2\2"+
		"\2\u0195\u0196\3\2\2\2\u0196\u0198\3\2\2\2\u0197\u0195\3\2\2\2\u0198\u0199"+
		"\7\63\2\2\u0199\u019d\3\2\2\2\u019a\u019b\7\62\2\2\u019b\u019d\7\63\2"+
		"\2\u019c\u018f\3\2\2\2\u019c\u019a\3\2\2\2\u019dU\3\2\2\2\u019e\u019f"+
		"\7-\2\2\u019f\u01a0\5X-\2\u01a0\u01a1\7.\2\2\u01a1\u01a4\3\2\2\2\u01a2"+
		"\u01a4\3\2\2\2\u01a3\u019e\3\2\2\2\u01a3\u01a2\3\2\2\2\u01a4W\3\2\2\2"+
		"\u01a5\u01aa\5Z.\2\u01a6\u01a7\7(\2\2\u01a7\u01a9\5Z.\2\u01a8\u01a6\3"+
		"\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab"+
		"Y\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01b4\7\67\2\2\u01ae\u01af\7\67\2"+
		"\2\u01af\u01b0\7/\2\2\u01b0\u01b1\5^\60\2\u01b1\u01b2\7\60\2\2\u01b2\u01b4"+
		"\3\2\2\2\u01b3\u01ad\3\2\2\2\u01b3\u01ae\3\2\2\2\u01b4[\3\2\2\2\u01b5"+
		"\u01b6\7\67\2\2\u01b6\u01b7\7\61\2\2\u01b7\u01ba\5 \21\2\u01b8\u01ba\5"+
		" \21\2\u01b9\u01b5\3\2\2\2\u01b9\u01b8\3\2\2\2\u01ba]\3\2\2\2\u01bb\u01be"+
		"\5`\61\2\u01bc\u01be\3\2\2\2\u01bd\u01bb\3\2\2\2\u01bd\u01bc\3\2\2\2\u01be"+
		"_\3\2\2\2\u01bf\u01c4\5\\/\2\u01c0\u01c1\7(\2\2\u01c1\u01c3\5\\/\2\u01c2"+
		"\u01c0\3\2\2\2\u01c3\u01c6\3\2\2\2\u01c4\u01c2\3\2\2\2\u01c4\u01c5\3\2"+
		"\2\2\u01c5a\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7\u01c8\t\b\2\2\u01c8c\3\2"+
		"\2\2\63juz\u0080\u0086\u0090\u009e\u00a7\u00ab\u00ad\u00b5\u00bc\u00c4"+
		"\u00ca\u00cf\u00d3\u00dc\u00e5\u00ea\u00f0\u00f8\u00fe\u010e\u0119\u0124"+
		"\u012e\u0134\u0141\u0146\u0149\u0150\u0156\u015a\u015f\u0162\u0165\u0168"+
		"\u016b\u0177\u017c\u018d\u0195\u019c\u01a3\u01aa\u01b3\u01b9\u01bd\u01c4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}