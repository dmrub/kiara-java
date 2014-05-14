// Generated from /home/rubinste/proj_de3/kiara_java/KIARA_Java/KIARA/src/main/java/de/dfki/kiara/idl/Kiara.g4 by ANTLR 4.2.2
package de.dfki.kiara.idl;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KiaraParser}.
 */
public interface KiaraListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KiaraParser#simpleGenericType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleGenericType(@NotNull KiaraParser.SimpleGenericTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#simpleGenericType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleGenericType(@NotNull KiaraParser.SimpleGenericTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#xception}.
	 * @param ctx the parse tree
	 */
	void enterXception(@NotNull KiaraParser.XceptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#xception}.
	 * @param ctx the parse tree
	 */
	void exitXception(@NotNull KiaraParser.XceptionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#definition_list}.
	 * @param ctx the parse tree
	 */
	void enterDefinition_list(@NotNull KiaraParser.Definition_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#definition_list}.
	 * @param ctx the parse tree
	 */
	void exitDefinition_list(@NotNull KiaraParser.Definition_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#fieldRequiredness}.
	 * @param ctx the parse tree
	 */
	void enterFieldRequiredness(@NotNull KiaraParser.FieldRequirednessContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#fieldRequiredness}.
	 * @param ctx the parse tree
	 */
	void exitFieldRequiredness(@NotNull KiaraParser.FieldRequirednessContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#simpleBaseType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleBaseType(@NotNull KiaraParser.SimpleBaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#simpleBaseType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleBaseType(@NotNull KiaraParser.SimpleBaseTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#constList}.
	 * @param ctx the parse tree
	 */
	void enterConstList(@NotNull KiaraParser.ConstListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#constList}.
	 * @param ctx the parse tree
	 */
	void exitConstList(@NotNull KiaraParser.ConstListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#constValue}.
	 * @param ctx the parse tree
	 */
	void enterConstValue(@NotNull KiaraParser.ConstValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#constValue}.
	 * @param ctx the parse tree
	 */
	void exitConstValue(@NotNull KiaraParser.ConstValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#constMap}.
	 * @param ctx the parse tree
	 */
	void enterConstMap(@NotNull KiaraParser.ConstMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#constMap}.
	 * @param ctx the parse tree
	 */
	void exitConstMap(@NotNull KiaraParser.ConstMapContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotationList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationList(@NotNull KiaraParser.AnnotationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotationList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationList(@NotNull KiaraParser.AnnotationListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotatedTypeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedTypeDefinition(@NotNull KiaraParser.AnnotatedTypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotatedTypeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedTypeDefinition(@NotNull KiaraParser.AnnotatedTypeDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#nonAnnotatedTypeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterNonAnnotatedTypeDefinition(@NotNull KiaraParser.NonAnnotatedTypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#nonAnnotatedTypeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitNonAnnotatedTypeDefinition(@NotNull KiaraParser.NonAnnotatedTypeDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#fieldIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFieldIdentifier(@NotNull KiaraParser.FieldIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#fieldIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFieldIdentifier(@NotNull KiaraParser.FieldIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull KiaraParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull KiaraParser.FunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(@NotNull KiaraParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(@NotNull KiaraParser.AnnotationContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#enum_t}.
	 * @param ctx the parse tree
	 */
	void enterEnum_t(@NotNull KiaraParser.Enum_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#enum_t}.
	 * @param ctx the parse tree
	 */
	void exitEnum_t(@NotNull KiaraParser.Enum_tContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#functionType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionType(@NotNull KiaraParser.FunctionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#functionType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionType(@NotNull KiaraParser.FunctionTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#commaOrSemicolon}.
	 * @param ctx the parse tree
	 */
	void enterCommaOrSemicolon(@NotNull KiaraParser.CommaOrSemicolonContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#commaOrSemicolon}.
	 * @param ctx the parse tree
	 */
	void exitCommaOrSemicolon(@NotNull KiaraParser.CommaOrSemicolonContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(@NotNull KiaraParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(@NotNull KiaraParser.BaseTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#enumDef}.
	 * @param ctx the parse tree
	 */
	void enterEnumDef(@NotNull KiaraParser.EnumDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#enumDef}.
	 * @param ctx the parse tree
	 */
	void exitEnumDef(@NotNull KiaraParser.EnumDefContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#struct}.
	 * @param ctx the parse tree
	 */
	void enterStruct(@NotNull KiaraParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#struct}.
	 * @param ctx the parse tree
	 */
	void exitStruct(@NotNull KiaraParser.StructContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#union}.
	 * @param ctx the parse tree
	 */
	void enterUnion(@NotNull KiaraParser.UnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#union}.
	 * @param ctx the parse tree
	 */
	void exitUnion(@NotNull KiaraParser.UnionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#xsdNillable}.
	 * @param ctx the parse tree
	 */
	void enterXsdNillable(@NotNull KiaraParser.XsdNillableContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#xsdNillable}.
	 * @param ctx the parse tree
	 */
	void exitXsdNillable(@NotNull KiaraParser.XsdNillableContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#nonEmptyAnnotationArgList}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyAnnotationArgList(@NotNull KiaraParser.NonEmptyAnnotationArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#nonEmptyAnnotationArgList}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyAnnotationArgList(@NotNull KiaraParser.NonEmptyAnnotationArgListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstDefinition(@NotNull KiaraParser.ConstDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstDefinition(@NotNull KiaraParser.ConstDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#genericTypeArg}.
	 * @param ctx the parse tree
	 */
	void enterGenericTypeArg(@NotNull KiaraParser.GenericTypeArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#genericTypeArg}.
	 * @param ctx the parse tree
	 */
	void exitGenericTypeArg(@NotNull KiaraParser.GenericTypeArgContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#intConstant}.
	 * @param ctx the parse tree
	 */
	void enterIntConstant(@NotNull KiaraParser.IntConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#intConstant}.
	 * @param ctx the parse tree
	 */
	void exitIntConstant(@NotNull KiaraParser.IntConstantContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotationDef}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationDef(@NotNull KiaraParser.AnnotationDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotationDef}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationDef(@NotNull KiaraParser.AnnotationDefContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(@NotNull KiaraParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(@NotNull KiaraParser.DefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#xsdAttributes}.
	 * @param ctx the parse tree
	 */
	void enterXsdAttributes(@NotNull KiaraParser.XsdAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#xsdAttributes}.
	 * @param ctx the parse tree
	 */
	void exitXsdAttributes(@NotNull KiaraParser.XsdAttributesContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void enterFieldType(@NotNull KiaraParser.FieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void exitFieldType(@NotNull KiaraParser.FieldTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#senumDef}.
	 * @param ctx the parse tree
	 */
	void enterSenumDef(@NotNull KiaraParser.SenumDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#senumDef}.
	 * @param ctx the parse tree
	 */
	void exitSenumDef(@NotNull KiaraParser.SenumDefContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#xsdOptional}.
	 * @param ctx the parse tree
	 */
	void enterXsdOptional(@NotNull KiaraParser.XsdOptionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#xsdOptional}.
	 * @param ctx the parse tree
	 */
	void exitXsdOptional(@NotNull KiaraParser.XsdOptionalContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotationArg}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationArg(@NotNull KiaraParser.AnnotationArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotationArg}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationArg(@NotNull KiaraParser.AnnotationArgContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#xsdAll}.
	 * @param ctx the parse tree
	 */
	void enterXsdAll(@NotNull KiaraParser.XsdAllContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#xsdAll}.
	 * @param ctx the parse tree
	 */
	void exitXsdAll(@NotNull KiaraParser.XsdAllContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterTypedef(@NotNull KiaraParser.TypedefContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitTypedef(@NotNull KiaraParser.TypedefContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(@NotNull KiaraParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(@NotNull KiaraParser.HeaderContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(@NotNull KiaraParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(@NotNull KiaraParser.FieldContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#constValuePair}.
	 * @param ctx the parse tree
	 */
	void enterConstValuePair(@NotNull KiaraParser.ConstValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#constValuePair}.
	 * @param ctx the parse tree
	 */
	void exitConstValuePair(@NotNull KiaraParser.ConstValuePairContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#genericTypeArgList}.
	 * @param ctx the parse tree
	 */
	void enterGenericTypeArgList(@NotNull KiaraParser.GenericTypeArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#genericTypeArgList}.
	 * @param ctx the parse tree
	 */
	void exitGenericTypeArgList(@NotNull KiaraParser.GenericTypeArgListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#senum}.
	 * @param ctx the parse tree
	 */
	void enterSenum(@NotNull KiaraParser.SenumContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#senum}.
	 * @param ctx the parse tree
	 */
	void exitSenum(@NotNull KiaraParser.SenumContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#nonEmptyAnnotationList}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyAnnotationList(@NotNull KiaraParser.NonEmptyAnnotationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#nonEmptyAnnotationList}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyAnnotationList(@NotNull KiaraParser.NonEmptyAnnotationListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(@NotNull KiaraParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(@NotNull KiaraParser.FieldValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(@NotNull KiaraParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(@NotNull KiaraParser.ProgramContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#genericType}.
	 * @param ctx the parse tree
	 */
	void enterGenericType(@NotNull KiaraParser.GenericTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#genericType}.
	 * @param ctx the parse tree
	 */
	void exitGenericType(@NotNull KiaraParser.GenericTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#service}.
	 * @param ctx the parse tree
	 */
	void enterService(@NotNull KiaraParser.ServiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#service}.
	 * @param ctx the parse tree
	 */
	void exitService(@NotNull KiaraParser.ServiceContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#annotationArgList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationArgList(@NotNull KiaraParser.AnnotationArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#annotationArgList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationArgList(@NotNull KiaraParser.AnnotationArgListContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(@NotNull KiaraParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(@NotNull KiaraParser.TypeDefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#header_list}.
	 * @param ctx the parse tree
	 */
	void enterHeader_list(@NotNull KiaraParser.Header_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#header_list}.
	 * @param ctx the parse tree
	 */
	void exitHeader_list(@NotNull KiaraParser.Header_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link KiaraParser#throwz}.
	 * @param ctx the parse tree
	 */
	void enterThrowz(@NotNull KiaraParser.ThrowzContext ctx);
	/**
	 * Exit a parse tree produced by {@link KiaraParser#throwz}.
	 * @param ctx the parse tree
	 */
	void exitThrowz(@NotNull KiaraParser.ThrowzContext ctx);
}