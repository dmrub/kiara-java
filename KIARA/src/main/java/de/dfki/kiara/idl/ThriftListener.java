// Generated from /home/rubinste/proj_de3/kiara_java/KIARA_Java/KIARAParser/src/main/java/de/dfki/kiara/Thrift.g4 by ANTLR 4.2.2
package de.dfki.kiara.idl;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ThriftParser}.
 */
public interface ThriftListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ThriftParser#containerType}.
	 * @param ctx the parse tree
	 */
	void enterContainerType(@NotNull ThriftParser.ContainerTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#containerType}.
	 * @param ctx the parse tree
	 */
	void exitContainerType(@NotNull ThriftParser.ContainerTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#xception}.
	 * @param ctx the parse tree
	 */
	void enterXception(@NotNull ThriftParser.XceptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#xception}.
	 * @param ctx the parse tree
	 */
	void exitXception(@NotNull ThriftParser.XceptionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#fieldRequiredness}.
	 * @param ctx the parse tree
	 */
	void enterFieldRequiredness(@NotNull ThriftParser.FieldRequirednessContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#fieldRequiredness}.
	 * @param ctx the parse tree
	 */
	void exitFieldRequiredness(@NotNull ThriftParser.FieldRequirednessContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#simpleBaseType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleBaseType(@NotNull ThriftParser.SimpleBaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#simpleBaseType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleBaseType(@NotNull ThriftParser.SimpleBaseTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#constList}.
	 * @param ctx the parse tree
	 */
	void enterConstList(@NotNull ThriftParser.ConstListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#constList}.
	 * @param ctx the parse tree
	 */
	void exitConstList(@NotNull ThriftParser.ConstListContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#constValue}.
	 * @param ctx the parse tree
	 */
	void enterConstValue(@NotNull ThriftParser.ConstValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#constValue}.
	 * @param ctx the parse tree
	 */
	void exitConstValue(@NotNull ThriftParser.ConstValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#constMap}.
	 * @param ctx the parse tree
	 */
	void enterConstMap(@NotNull ThriftParser.ConstMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#constMap}.
	 * @param ctx the parse tree
	 */
	void exitConstMap(@NotNull ThriftParser.ConstMapContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#fieldIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFieldIdentifier(@NotNull ThriftParser.FieldIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#fieldIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFieldIdentifier(@NotNull ThriftParser.FieldIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#typeAnnotations}.
	 * @param ctx the parse tree
	 */
	void enterTypeAnnotations(@NotNull ThriftParser.TypeAnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#typeAnnotations}.
	 * @param ctx the parse tree
	 */
	void exitTypeAnnotations(@NotNull ThriftParser.TypeAnnotationsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#cppType}.
	 * @param ctx the parse tree
	 */
	void enterCppType(@NotNull ThriftParser.CppTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#cppType}.
	 * @param ctx the parse tree
	 */
	void exitCppType(@NotNull ThriftParser.CppTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull ThriftParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull ThriftParser.FunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#enum_t}.
	 * @param ctx the parse tree
	 */
	void enterEnum_t(@NotNull ThriftParser.Enum_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#enum_t}.
	 * @param ctx the parse tree
	 */
	void exitEnum_t(@NotNull ThriftParser.Enum_tContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#functionType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionType(@NotNull ThriftParser.FunctionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#functionType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionType(@NotNull ThriftParser.FunctionTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#commaOrSemicolon}.
	 * @param ctx the parse tree
	 */
	void enterCommaOrSemicolon(@NotNull ThriftParser.CommaOrSemicolonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#commaOrSemicolon}.
	 * @param ctx the parse tree
	 */
	void exitCommaOrSemicolon(@NotNull ThriftParser.CommaOrSemicolonContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(@NotNull ThriftParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(@NotNull ThriftParser.BaseTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#enumDef}.
	 * @param ctx the parse tree
	 */
	void enterEnumDef(@NotNull ThriftParser.EnumDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#enumDef}.
	 * @param ctx the parse tree
	 */
	void exitEnumDef(@NotNull ThriftParser.EnumDefContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#struct}.
	 * @param ctx the parse tree
	 */
	void enterStruct(@NotNull ThriftParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#struct}.
	 * @param ctx the parse tree
	 */
	void exitStruct(@NotNull ThriftParser.StructContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#union}.
	 * @param ctx the parse tree
	 */
	void enterUnion(@NotNull ThriftParser.UnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#union}.
	 * @param ctx the parse tree
	 */
	void exitUnion(@NotNull ThriftParser.UnionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#xsdNillable}.
	 * @param ctx the parse tree
	 */
	void enterXsdNillable(@NotNull ThriftParser.XsdNillableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#xsdNillable}.
	 * @param ctx the parse tree
	 */
	void exitXsdNillable(@NotNull ThriftParser.XsdNillableContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#oneway}.
	 * @param ctx the parse tree
	 */
	void enterOneway(@NotNull ThriftParser.OnewayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#oneway}.
	 * @param ctx the parse tree
	 */
	void exitOneway(@NotNull ThriftParser.OnewayContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#typeAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterTypeAnnotation(@NotNull ThriftParser.TypeAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#typeAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitTypeAnnotation(@NotNull ThriftParser.TypeAnnotationContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#mapType}.
	 * @param ctx the parse tree
	 */
	void enterMapType(@NotNull ThriftParser.MapTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#mapType}.
	 * @param ctx the parse tree
	 */
	void exitMapType(@NotNull ThriftParser.MapTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstDefinition(@NotNull ThriftParser.ConstDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstDefinition(@NotNull ThriftParser.ConstDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#intConstant}.
	 * @param ctx the parse tree
	 */
	void enterIntConstant(@NotNull ThriftParser.IntConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#intConstant}.
	 * @param ctx the parse tree
	 */
	void exitIntConstant(@NotNull ThriftParser.IntConstantContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(@NotNull ThriftParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(@NotNull ThriftParser.DefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#xsdAttributes}.
	 * @param ctx the parse tree
	 */
	void enterXsdAttributes(@NotNull ThriftParser.XsdAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#xsdAttributes}.
	 * @param ctx the parse tree
	 */
	void exitXsdAttributes(@NotNull ThriftParser.XsdAttributesContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#setType}.
	 * @param ctx the parse tree
	 */
	void enterSetType(@NotNull ThriftParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#setType}.
	 * @param ctx the parse tree
	 */
	void exitSetType(@NotNull ThriftParser.SetTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void enterFieldType(@NotNull ThriftParser.FieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void exitFieldType(@NotNull ThriftParser.FieldTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#senumDef}.
	 * @param ctx the parse tree
	 */
	void enterSenumDef(@NotNull ThriftParser.SenumDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#senumDef}.
	 * @param ctx the parse tree
	 */
	void exitSenumDef(@NotNull ThriftParser.SenumDefContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#listType}.
	 * @param ctx the parse tree
	 */
	void enterListType(@NotNull ThriftParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#listType}.
	 * @param ctx the parse tree
	 */
	void exitListType(@NotNull ThriftParser.ListTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#xsdOptional}.
	 * @param ctx the parse tree
	 */
	void enterXsdOptional(@NotNull ThriftParser.XsdOptionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#xsdOptional}.
	 * @param ctx the parse tree
	 */
	void exitXsdOptional(@NotNull ThriftParser.XsdOptionalContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#xsdAll}.
	 * @param ctx the parse tree
	 */
	void enterXsdAll(@NotNull ThriftParser.XsdAllContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#xsdAll}.
	 * @param ctx the parse tree
	 */
	void exitXsdAll(@NotNull ThriftParser.XsdAllContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterTypedef(@NotNull ThriftParser.TypedefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitTypedef(@NotNull ThriftParser.TypedefContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(@NotNull ThriftParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(@NotNull ThriftParser.HeaderContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(@NotNull ThriftParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(@NotNull ThriftParser.FieldContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#constValuePair}.
	 * @param ctx the parse tree
	 */
	void enterConstValuePair(@NotNull ThriftParser.ConstValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#constValuePair}.
	 * @param ctx the parse tree
	 */
	void exitConstValuePair(@NotNull ThriftParser.ConstValuePairContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#senum}.
	 * @param ctx the parse tree
	 */
	void enterSenum(@NotNull ThriftParser.SenumContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#senum}.
	 * @param ctx the parse tree
	 */
	void exitSenum(@NotNull ThriftParser.SenumContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(@NotNull ThriftParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(@NotNull ThriftParser.FieldValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(@NotNull ThriftParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(@NotNull ThriftParser.ProgramContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#service}.
	 * @param ctx the parse tree
	 */
	void enterService(@NotNull ThriftParser.ServiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#service}.
	 * @param ctx the parse tree
	 */
	void exitService(@NotNull ThriftParser.ServiceContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(@NotNull ThriftParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(@NotNull ThriftParser.TypeDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#simpleContainerType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleContainerType(@NotNull ThriftParser.SimpleContainerTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#simpleContainerType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleContainerType(@NotNull ThriftParser.SimpleContainerTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ThriftParser#throwz}.
	 * @param ctx the parse tree
	 */
	void enterThrowz(@NotNull ThriftParser.ThrowzContext ctx);
	/**
	 * Exit a parse tree produced by {@link ThriftParser#throwz}.
	 * @param ctx the parse tree
	 */
	void exitThrowz(@NotNull ThriftParser.ThrowzContext ctx);
}