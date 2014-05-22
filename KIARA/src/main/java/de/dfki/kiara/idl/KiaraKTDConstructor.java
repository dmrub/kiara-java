/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.idl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class KiaraKTDConstructor implements KiaraListener {

    ParseTreeProperty<Object> values = new ParseTreeProperty<>();

    public void setValue(ParseTree node, Object value) {
        values.put(node, value);
    }

    public Object getValue(ParseTree node) {
        return values.get(node);
    }

    @Override
    public void enterXception(KiaraParser.XceptionContext ctx) {
    }

    @Override
    public void exitXception(KiaraParser.XceptionContext ctx) {
    }

    @Override
    public void enterSimpleGenericType(KiaraParser.SimpleGenericTypeContext ctx) {
    }

    @Override
    public void exitSimpleGenericType(KiaraParser.SimpleGenericTypeContext ctx) {
    }

    @Override
    public void enterFieldRequiredness(KiaraParser.FieldRequirednessContext ctx) {
    }

    @Override
    public void exitFieldRequiredness(KiaraParser.FieldRequirednessContext ctx) {
    }

    @Override
    public void enterConstMap(KiaraParser.ConstMapContext ctx) {
    }

    @Override
    public void exitConstMap(KiaraParser.ConstMapContext ctx) {
    }

    @Override
    public void enterAnnotationList(KiaraParser.AnnotationListContext ctx) {
    }

    @Override
    public void exitAnnotationList(KiaraParser.AnnotationListContext ctx) {
    }

    @Override
    public void enterAnnotatedTypeDefinition(KiaraParser.AnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void exitAnnotatedTypeDefinition(KiaraParser.AnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void enterFunction(KiaraParser.FunctionContext ctx) {
    }

    @Override
    public void exitFunction(KiaraParser.FunctionContext ctx) {
    }

    @Override
    public void enterAnnotation(KiaraParser.AnnotationContext ctx) {
    }

    @Override
    public void exitAnnotation(KiaraParser.AnnotationContext ctx) {
    }

    @Override
    public void enterEnum_t(KiaraParser.Enum_tContext ctx) {
    }

    @Override
    public void exitEnum_t(KiaraParser.Enum_tContext ctx) {
    }

    @Override
    public void enterEnumDef(KiaraParser.EnumDefContext ctx) {
    }

    @Override
    public void exitEnumDef(KiaraParser.EnumDefContext ctx) {
    }

    @Override
    public void enterNonEmptyAnnotationArgList(KiaraParser.NonEmptyAnnotationArgListContext ctx) {
    }

    @Override
    public void exitNonEmptyAnnotationArgList(KiaraParser.NonEmptyAnnotationArgListContext ctx) {
    }

    @Override
    public void enterIntConstant(KiaraParser.IntConstantContext ctx) {
    }

    @Override
    public void exitIntConstant(KiaraParser.IntConstantContext ctx) {
    }

    @Override
    public void enterSenumDef(KiaraParser.SenumDefContext ctx) {
    }

    @Override
    public void exitSenumDef(KiaraParser.SenumDefContext ctx) {
    }

    @Override
    public void enterAnnotationArg(KiaraParser.AnnotationArgContext ctx) {
    }

    @Override
    public void exitAnnotationArg(KiaraParser.AnnotationArgContext ctx) {
    }

    @Override
    public void enterTypedef(KiaraParser.TypedefContext ctx) {
    }

    @Override
    public void exitTypedef(KiaraParser.TypedefContext ctx) {
    }

    @Override
    public void enterProgram(KiaraParser.ProgramContext ctx) {
    }

    @Override
    public void exitProgram(KiaraParser.ProgramContext ctx) {
    }

    @Override
    public void enterFieldValue(KiaraParser.FieldValueContext ctx) {
    }

    @Override
    public void exitFieldValue(KiaraParser.FieldValueContext ctx) {
    }

    @Override
    public void enterHeader_list(KiaraParser.Header_listContext ctx) {
    }

    @Override
    public void exitHeader_list(KiaraParser.Header_listContext ctx) {
    }

    @Override
    public void enterThrowz(KiaraParser.ThrowzContext ctx) {
    }

    @Override
    public void exitThrowz(KiaraParser.ThrowzContext ctx) {
    }

    @Override
    public void enterDefinition_list(KiaraParser.Definition_listContext ctx) {
    }

    @Override
    public void exitDefinition_list(KiaraParser.Definition_listContext ctx) {
    }

    @Override
    public void enterConstList(KiaraParser.ConstListContext ctx) {
    }

    @Override
    public void exitConstList(KiaraParser.ConstListContext ctx) {
    }

    @Override
    public void enterSimpleBaseType(KiaraParser.SimpleBaseTypeContext ctx) {
    }

    @Override
    public void exitSimpleBaseType(KiaraParser.SimpleBaseTypeContext ctx) {
    }

    @Override
    public void enterConstValue(KiaraParser.ConstValueContext ctx) {
    }

    @Override
    public void exitConstValue(KiaraParser.ConstValueContext ctx) {
    }

    @Override
    public void enterHeaderNamedNamespace(KiaraParser.HeaderNamedNamespaceContext ctx) {
        System.out.println("IDENTIFIER = "+ctx.IDENTIFIER().get(0));
        setValue(ctx, "Test");
    }

    @Override
    public void exitHeaderNamedNamespace(KiaraParser.HeaderNamedNamespaceContext ctx) {

    }

    @Override
    public void enterNonAnnotatedTypeDefinition(KiaraParser.NonAnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void exitNonAnnotatedTypeDefinition(KiaraParser.NonAnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void enterHeaderInclude(KiaraParser.HeaderIncludeContext ctx) {
        System.out.println("INCLUDE "+ctx.LITERAL().getText()); //???DEBUG
    }

    @Override
    public void exitHeaderInclude(KiaraParser.HeaderIncludeContext ctx) {
    }

    @Override
    public void enterFieldIdentifier(KiaraParser.FieldIdentifierContext ctx) {
    }

    @Override
    public void exitFieldIdentifier(KiaraParser.FieldIdentifierContext ctx) {
    }

    @Override
    public void enterCommaOrSemicolon(KiaraParser.CommaOrSemicolonContext ctx) {
    }

    @Override
    public void exitCommaOrSemicolon(KiaraParser.CommaOrSemicolonContext ctx) {
    }

    @Override
    public void enterFunctionType(KiaraParser.FunctionTypeContext ctx) {
    }

    @Override
    public void exitFunctionType(KiaraParser.FunctionTypeContext ctx) {
    }

    @Override
    public void enterBaseType(KiaraParser.BaseTypeContext ctx) {
    }

    @Override
    public void exitBaseType(KiaraParser.BaseTypeContext ctx) {
    }

    @Override
    public void enterStruct(KiaraParser.StructContext ctx) {
    }

    @Override
    public void exitStruct(KiaraParser.StructContext ctx) {
    }

    @Override
    public void enterUnion(KiaraParser.UnionContext ctx) {
    }

    @Override
    public void exitUnion(KiaraParser.UnionContext ctx) {
    }

    @Override
    public void enterXsdNillable(KiaraParser.XsdNillableContext ctx) {
    }

    @Override
    public void exitXsdNillable(KiaraParser.XsdNillableContext ctx) {
    }

    @Override
    public void enterConstDefinition(KiaraParser.ConstDefinitionContext ctx) {
    }

    @Override
    public void exitConstDefinition(KiaraParser.ConstDefinitionContext ctx) {
    }

    @Override
    public void enterGenericTypeArg(KiaraParser.GenericTypeArgContext ctx) {
    }

    @Override
    public void exitGenericTypeArg(KiaraParser.GenericTypeArgContext ctx) {
    }

    @Override
    public void enterAnnotationDef(KiaraParser.AnnotationDefContext ctx) {
    }

    @Override
    public void exitAnnotationDef(KiaraParser.AnnotationDefContext ctx) {
    }

    @Override
    public void enterDefinition(KiaraParser.DefinitionContext ctx) {
    }

    @Override
    public void exitDefinition(KiaraParser.DefinitionContext ctx) {
    }

    @Override
    public void enterXsdAttributes(KiaraParser.XsdAttributesContext ctx) {
    }

    @Override
    public void exitXsdAttributes(KiaraParser.XsdAttributesContext ctx) {
    }

    @Override
    public void enterFieldType(KiaraParser.FieldTypeContext ctx) {
    }

    @Override
    public void exitFieldType(KiaraParser.FieldTypeContext ctx) {
    }

    @Override
    public void enterXsdOptional(KiaraParser.XsdOptionalContext ctx) {
    }

    @Override
    public void exitXsdOptional(KiaraParser.XsdOptionalContext ctx) {
    }

    @Override
    public void enterHeaderStarNamespace(KiaraParser.HeaderStarNamespaceContext ctx) {
    }

    @Override
    public void exitHeaderStarNamespace(KiaraParser.HeaderStarNamespaceContext ctx) {
    }

    @Override
    public void enterXsdAll(KiaraParser.XsdAllContext ctx) {
    }

    @Override
    public void exitXsdAll(KiaraParser.XsdAllContext ctx) {
    }

    @Override
    public void enterConstValuePair(KiaraParser.ConstValuePairContext ctx) {
    }

    @Override
    public void exitConstValuePair(KiaraParser.ConstValuePairContext ctx) {
    }

    @Override
    public void enterField(KiaraParser.FieldContext ctx) {
    }

    @Override
    public void exitField(KiaraParser.FieldContext ctx) {
    }

    @Override
    public void enterGenericTypeArgList(KiaraParser.GenericTypeArgListContext ctx) {
    }

    @Override
    public void exitGenericTypeArgList(KiaraParser.GenericTypeArgListContext ctx) {
    }

    @Override
    public void enterSenum(KiaraParser.SenumContext ctx) {
    }

    @Override
    public void exitSenum(KiaraParser.SenumContext ctx) {
    }

    @Override
    public void enterNonEmptyAnnotationList(KiaraParser.NonEmptyAnnotationListContext ctx) {
    }

    @Override
    public void exitNonEmptyAnnotationList(KiaraParser.NonEmptyAnnotationListContext ctx) {
    }

    @Override
    public void enterGenericType(KiaraParser.GenericTypeContext ctx) {
    }

    @Override
    public void exitGenericType(KiaraParser.GenericTypeContext ctx) {
    }

    @Override
    public void enterService(KiaraParser.ServiceContext ctx) {
    }

    @Override
    public void exitService(KiaraParser.ServiceContext ctx) {
    }

    @Override
    public void enterAnnotationArgList(KiaraParser.AnnotationArgListContext ctx) {
    }

    @Override
    public void exitAnnotationArgList(KiaraParser.AnnotationArgListContext ctx) {
    }

    @Override
    public void enterTypeDefinition(KiaraParser.TypeDefinitionContext ctx) {
    }

    @Override
    public void exitTypeDefinition(KiaraParser.TypeDefinitionContext ctx) {
    }

    @Override
    public void visitTerminal(TerminalNode tn) {
    }

    @Override
    public void visitErrorNode(ErrorNode en) {
    }

    @Override
    public void enterEveryRule(ParserRuleContext prc) {
    }

    @Override
    public void exitEveryRule(ParserRuleContext prc) {
    }

}
