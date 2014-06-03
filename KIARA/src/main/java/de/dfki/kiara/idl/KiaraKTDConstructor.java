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

import de.dfki.kiara.ktd.Annotation;
import de.dfki.kiara.ktd.AnnotationListAttr;
import de.dfki.kiara.ktd.AnnotationTypeAttr;
import de.dfki.kiara.ktd.AnyType;
import de.dfki.kiara.ktd.ArrayType;
import de.dfki.kiara.ktd.AttributeHolder;
import de.dfki.kiara.ktd.DefaultFieldValueAttr;
import de.dfki.kiara.ktd.ElementData;
import de.dfki.kiara.ktd.EnumType;
import de.dfki.kiara.ktd.ExceptionTypeAttr;
import de.dfki.kiara.ktd.Expr;
import de.dfki.kiara.ktd.FixedArrayType;
import de.dfki.kiara.ktd.FunctionType;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.ParameterInfo;
import de.dfki.kiara.ktd.PrimLiteral;
import de.dfki.kiara.ktd.PrimValueType;
import de.dfki.kiara.ktd.StructType;
import de.dfki.kiara.ktd.Type;
import de.dfki.kiara.ktd.VoidType;
import de.dfki.kiara.ktd.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

    private final ParseTreeProperty<Object> values;
    private final Module module;

    private static final class EnumDef {
        public final String name;
        public final PrimLiteral constant;

        public EnumDef() {
            this(null, null);
        }

        public EnumDef(String name, PrimLiteral constant) {
            this.name = name;
            this.constant = constant;
        }
    }

    private static final class EnumDefList implements Iterable<EnumDef> {
        public final List<EnumDef> constants;
        public long nextConst;

        public EnumDefList() {
            this.constants = new ArrayList<>();
            this.nextConst = 0;
        }

        @Override
        public Iterator<EnumDef> iterator() {
            return constants.iterator();
        }
    }

    private static final class Field {
        public Integer id;
        public Type type;
        public String name;
        public Expr value;
        public List<Annotation> annotationList;

        public Field() {
        }

        public String getName() {
            return name;
        }
    }

    private static final class FieldList implements Iterable<Field> {
        public List<Field> fields;

        public final List<Type> getTypes() {
            List<Type> result = new ArrayList<>();
            for (Field f : fields) {
                result.add(f.type);
            }
            return result;
        }

        public final List<String> getNames() {
            List<String> result = new ArrayList<>();
            for (Field f : fields) {
                result.add(f.name);
            }
            return result;
        }

        public final void initAnnotations(StructType sty) {
            final int n = fields.size();
            for (int i = 0; i < n; ++i) {
                ElementData elemData = sty.getElementDataAt(i);
                if (fields.get(i).value != null)
                    elemData.setAttributeValue(
                            new DefaultFieldValueAttr(fields.get(i).value));
                if (fields.get(i).annotationList != null &&
                        !fields.get(i).annotationList.isEmpty())
                    elemData.setAttributeValue(
                            new AnnotationListAttr(fields.get(i).annotationList));
            }
        }

        public final FunctionType createFunction(String name, Type returnType,
                List<Annotation> funcAnnotList,
                List<Annotation> retAnnotList) {
                    //KIARA::World &world = returnType->getWorld();
            List<ParameterInfo> paramTypes = new ArrayList<>(fields.size());

            for (Field f : fields) {
                paramTypes.add(new FunctionType.Parameter(f.name, f.type));
            }

            FunctionType fty = FunctionType.create(name, returnType, paramTypes);

            // set parameter annotations
            for (int i = 0; i < fty.getNumParams(); ++i) {
                if (fields.get(i).annotationList != null && !fields.get(i).annotationList.isEmpty())
                    fty.getParamElementDataAt(i).setAttributeValue(
                            new AnnotationListAttr(fields.get(i).annotationList));
            }

            // set return type annotations
            if (retAnnotList != null && !retAnnotList.isEmpty())
                fty.getReturnElementData().setAttributeValue(new AnnotationListAttr(retAnnotList));

            // set function annotations
            if (funcAnnotList != null && !funcAnnotList.isEmpty())
                fty.setAttributeValue(new AnnotationListAttr(funcAnnotList));

            return fty;
        }

        @Override
        public Iterator<Field> iterator() {
            return fields.iterator();
        }
    }


    private List<Field> unwrapFieldList(List<KiaraParser.FieldContext> fieldCtxList) {
        List<Field> fields = new ArrayList<>(fieldCtxList.size());
        for (KiaraParser.FieldContext fctx : fieldCtxList) {
            fields.add((Field)getValue(fctx));
        }
        return fields;
    }

    private List<Annotation> unwrapAnnotationList(KiaraParser.AnnotationListContext ctx) {
        return (List<Annotation>)getValue(ctx);
    }

    private Expr unwrapFieldValue(KiaraParser.FieldValueContext ctx) {
        return (Expr)getValue(ctx);
    }

    private Type unwrapFieldType(KiaraParser.FieldTypeContext ctx) {
        return (Type)getValue(ctx);
    }

    private Integer unwrapFieldIdentifier(KiaraParser.FieldIdentifierContext ctx) {
        return (Integer)getValue(ctx);
    }

    private List<Type> unwrapGenericTypeArgList(KiaraParser.GenericTypeArgListContext ctx) {
        return (List<Type>)getValue(ctx);
    }

    private FunctionType createFunction(String name, Type returnType,
            List<Field> fields,
            List<Annotation> funcAnnotList,
            List<Annotation> retAnnotList) {
                //KIARA::World &world = returnType->getWorld();
        List<ParameterInfo> paramTypes = new ArrayList<>(fields.size());

        for (Field field : fields) {
            paramTypes.add(new FunctionType.Parameter(field.name, field.type));
        }

        FunctionType fty = FunctionType.create(name, returnType, paramTypes);

        // set parameter annotations
        for (int i = 0; i < fty.getNumParams(); ++i) {

            if (fields.get(i).annotationList != null && !fields.get(i).annotationList.isEmpty())
                fty.getParamElementDataAt(i).setAttributeValue(
                        new AnnotationListAttr(fields.get(i).annotationList));
        }

        // set return type annotations
        if (retAnnotList != null && !retAnnotList.isEmpty())
            fty.getReturnElementData().setAttributeValue(new AnnotationListAttr(retAnnotList));

        // set function annotations
        if (funcAnnotList != null && !funcAnnotList.isEmpty())
            fty.setAttributeValue(new AnnotationListAttr(funcAnnotList));

        return fty;
    }

    private void initStructTypeFromFields(StructType sty,
            List<KiaraParser.FieldContext> fieldCtxList) {
        final int n = fieldCtxList.size();
        for (int i = 0; i < n; ++i) {
            KiaraParser.FieldContext fieldCtx = fieldCtxList.get(i);
            Field field = (Field)getValue(fieldCtx);

            sty.setElementAt(i, field.type);
            sty.setElementNameAt(i, field.name);

            ElementData elemData = sty.getElementDataAt(i);
            if (field.value != null)
                elemData.setAttributeValue(
                        new DefaultFieldValueAttr(field.value));
            if (field.annotationList != null &&
                    !field.annotationList.isEmpty())
                elemData.setAttributeValue(
                        new AnnotationListAttr(field.annotationList));
        }
    }

    private void initStructTypeFromFunctions(StructType sty,
            List<KiaraParser.FunctionContext> functionCtxList) {
        final int n = functionCtxList.size();
        for (int i = 0; i < n; ++i) {
            KiaraParser.FunctionContext fieldCtx = functionCtxList.get(i);
            Function func = (Function)getValue(fieldCtx);

            sty.setElementAt(i, func.type);
            sty.setElementNameAt(i, func.getName());
        }
    }

    private static final class Function {
        public FunctionType type;

        public Function() {
            this(null);
        }

        public Function(FunctionType type) {
            this.type = type;
        }

        public final Type getType() {
            return type;
        }

        public final String getName() {
            return type != null ? type.getTypeName() : "";
        }
    }

    private static final class FunctionList implements Iterable<Function> {
        public final List<Function> functions;

        public FunctionList() {
            this.functions = new ArrayList<>();
        }

        public final List<Type> getTypes() {
            List<Type> result = new ArrayList<>();
            for (Function f : functions) {
                result.add(f.type);
            }
            return result;
        }

        public final List<String> getNames() {
            List<String> result = new ArrayList<>();
            for (Function f : functions) {
                result.add(f.getName());
            }
            return result;
        }

        @Override
        public Iterator<Function> iterator() {
            return functions.iterator();
        }
    }

    public KiaraKTDConstructor(Module module) {
        this.values = new ParseTreeProperty<>();
        this.module = module;
    }

    public final Module getModule() {
        return this.module;
    }

    public final World getWorld() {
        return this.module.getWorld();
    }

    public final void setValue(ParseTree node, Object value) {
        values.put(node, value);
    }

    public final Object getValue(ParseTree node) {
        return values.get(node);
    }

    public final void copyValue(ParseTree destNode, ParseTree srcNode) {
        setValue(destNode, getValue(srcNode));
    }

    private final boolean DEBUG_MODE = true;

    public void pdebug(String msg) {
        if (DEBUG_MODE) {
            System.err.print("DEBUG: ");
            System.err.println(msg);
        }
    }

    public void perror(String msg) {
        System.err.print("ERROR: ");
        System.err.println(msg);
    }

    @Override
    public void enterProgram(KiaraParser.ProgramContext ctx) {
    }

    @Override
    public void exitProgram(KiaraParser.ProgramContext ctx) {
        pdebug("Program -> Headers DefinitionList");
    }

    @Override
    public void enterHeader_list(KiaraParser.Header_listContext ctx) {
    }

    @Override
    public void exitHeader_list(KiaraParser.Header_listContext ctx) {
        pdebug("Header_list -> header*");
    }

    @Override
    public void enterHeaderInclude(KiaraParser.HeaderIncludeContext ctx) {
        System.out.println("INCLUDE "+ctx.LITERAL().getText()); //???DEBUG
    }

    @Override
    public void exitHeaderInclude(KiaraParser.HeaderIncludeContext ctx) {
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
    public void enterHeaderStarNamespace(KiaraParser.HeaderStarNamespaceContext ctx) {
    }

    @Override
    public void exitHeaderStarNamespace(KiaraParser.HeaderStarNamespaceContext ctx) {
    }

    @Override
    public void enterDefinition_list(KiaraParser.Definition_listContext ctx) {
    }

    @Override
    public void exitDefinition_list(KiaraParser.Definition_listContext ctx) {
    }

    @Override
    public void enterDefinition(KiaraParser.DefinitionContext ctx) {
    }

    @Override
    public void exitDefinition(KiaraParser.DefinitionContext ctx) {
        copyValue(ctx, ctx.getChild(0));
    }

    @Override
    public void enterTypeDefinition(KiaraParser.TypeDefinitionContext ctx) {
    }

    @Override
    public void exitTypeDefinition(KiaraParser.TypeDefinitionContext ctx) {
        Object value = getValue(ctx.nonAnnotatedTypeDefinition());
        if (value == null) {
            value = getValue(ctx.annotatedTypeDefinition());
            if (value != null) {
                List<Annotation> al = (List<Annotation>)getValue(ctx.annotationList());
                ((AttributeHolder)value).setAttributeValue(new AnnotationListAttr(al));
            }
        }
        setValue(ctx, value);
    }

    @Override
    public void enterNonAnnotatedTypeDefinition(KiaraParser.NonAnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void exitNonAnnotatedTypeDefinition(KiaraParser.NonAnnotatedTypeDefinitionContext ctx) {
        copyValue(ctx, ctx.typedef());
    }

    @Override
    public void enterAnnotatedTypeDefinition(KiaraParser.AnnotatedTypeDefinitionContext ctx) {
    }

    @Override
    public void exitAnnotatedTypeDefinition(KiaraParser.AnnotatedTypeDefinitionContext ctx) {
        copyValue(ctx, ctx.getChild(0));
    }

    @Override
    public void enterTypedef(KiaraParser.TypedefContext ctx) {
    }

    @Override
    public void exitTypedef(KiaraParser.TypedefContext ctx) {
        pdebug("TypeDef -> TYPEDEF FieldType IDENTIFIER:"+ctx.IDENTIFIER().getText());
        KiaraParser.FieldTypeContext fieldTypeCtx = ctx.fieldType();
        Type fieldType = null;
        if (fieldTypeCtx != null) {
            fieldType = (Type)getValue(fieldTypeCtx);
            pdebug("BIND "+getModule().getTypeName(fieldType)+" TO "+ctx.IDENTIFIER().getText());
            getModule().bindType(ctx.IDENTIFIER().getText(), fieldType);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.TYPEDEF, fieldType);
        }
        setValue(ctx, fieldType);
    }

    @Override
    public void enterCommaOrSemicolon(KiaraParser.CommaOrSemicolonContext ctx) {
    }

    @Override
    public void exitCommaOrSemicolon(KiaraParser.CommaOrSemicolonContext ctx) {
    }

    @Override
    public void enterEnum_t(KiaraParser.Enum_tContext ctx) {
    }

    @Override
    public void exitEnum_t(KiaraParser.Enum_tContext ctx) {
        pdebug("Enum -> ENUM IDENTIFIER { enumDef* }");

        String ename = ctx.IDENTIFIER().getText();

        EnumType ety = EnumType.create(getWorld(), ename);

        for (KiaraParser.EnumDefContext edctx : ctx.enumDef()) {
            EnumDef ed = (EnumDef)getValue(edctx);
            ety.addConstant(ed.name, ed.constant);
        }
        try {
            getModule().bindType(ename, ety);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.NEWTYPE, ety);
        } catch (Exception e) {
            perror(e.toString());
        }

        setValue(ctx, ety);
    }

    @Override
    public void enterEnumExplicitConstDef(KiaraParser.EnumExplicitConstDefContext ctx) {
    }

    @Override
    public void exitEnumExplicitConstDef(KiaraParser.EnumExplicitConstDefContext ctx) {
        EnumDef enumDef = new EnumDef(ctx.IDENTIFIER().getText(),
                new PrimLiteral((Integer)getValue(ctx.intConstant()), getWorld()));
        setValue(ctx, enumDef);
    }

    @Override
    public void enterEnumAutoConstDef(KiaraParser.EnumAutoConstDefContext ctx) {
    }

    @Override
    public void exitEnumAutoConstDef(KiaraParser.EnumAutoConstDefContext ctx) {
        EnumDef enumDef = new EnumDef(ctx.IDENTIFIER().getText(), null);
        setValue(ctx, enumDef);
    }

    @Override
    public void enterSenum(KiaraParser.SenumContext ctx) {
    }

    @Override
    public void exitSenum(KiaraParser.SenumContext ctx) {
    }

    @Override
    public void enterSenumDef(KiaraParser.SenumDefContext ctx) {
    }

    @Override
    public void exitSenumDef(KiaraParser.SenumDefContext ctx) {
    }

    @Override
    public void enterConstDefinition(KiaraParser.ConstDefinitionContext ctx) {
    }

    @Override
    public void exitConstDefinition(KiaraParser.ConstDefinitionContext ctx) {
        pdebug("CONST ft=fieldType id=IDENTIFIER EQ cv=constValue commaOrSemicolon?");
    }

    @Override
    public void enterConstValue(KiaraParser.ConstValueContext ctx) {
    }

    @Override
    public void exitConstValue(KiaraParser.ConstValueContext ctx) {
        KiaraParser.IntConstantContext intConstantCtx = ctx.intConstant();
        if (intConstantCtx != null) {
            setValue(ctx,
                    new PrimLiteral((Integer)getValue(intConstantCtx),
                            getWorld()));
            return;
        }

        TerminalNode tn = ctx.DUBCONSTANT();
        if (tn != null) {
            setValue(ctx,
                    new PrimLiteral(Double.valueOf(tn.getText()),
                            getWorld()));
            return;
        }

        tn = ctx.LITERAL();
        if (tn != null) {
            setValue(ctx, new PrimLiteral(tn.getText(), getWorld()));
            return;
        }

        tn = ctx.IDENTIFIER();
        if (tn != null) {
            setValue(ctx, new PrimLiteral(tn.getText(), getWorld()));
            return;
        }

        setValue(ctx, getValue(ctx.getChild(0)));
    }

    @Override
    public void enterConstList(KiaraParser.ConstListContext ctx) {

    }

    @Override
    public void exitConstList(KiaraParser.ConstListContext ctx) {
        List<Expr> constList = new ArrayList<>();
        for (KiaraParser.ConstValueContext constValueCtx : ctx.constValue()) {
            constList.add((Expr)getValue(constValueCtx));
        }
        setValue(ctx, constList);
    }

    @Override
    public void enterConstMap(KiaraParser.ConstMapContext ctx) {
    }

    @Override
    public void exitConstMap(KiaraParser.ConstMapContext ctx) {
    }

    @Override
    public void enterConstValuePair(KiaraParser.ConstValuePairContext ctx) {
    }

    @Override
    public void exitConstValuePair(KiaraParser.ConstValuePairContext ctx) {
    }


    @Override
    public void enterStruct(KiaraParser.StructContext ctx) {
    }

    @Override
    public void exitStruct(KiaraParser.StructContext ctx) {
        pdebug("Struct -> tok_struct tok_identifier { FieldList }");

        String structName = ctx.IDENTIFIER().getText();
        List<KiaraParser.FieldContext> fieldCtxList = ctx.field();
        StructType s = StructType.create(getWorld(), structName,
                fieldCtxList.size());

        initStructTypeFromFields(s, fieldCtxList);
        try {
            getModule().bindType(structName, s);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.NEWTYPE, s);
        } catch (Exception e) {
            perror(e.toString());
            s = null;
        }
        setValue(ctx, s);
    }

    @Override
    public void enterUnion(KiaraParser.UnionContext ctx) {
    }

    @Override
    public void exitUnion(KiaraParser.UnionContext ctx) {
    }

    @Override
    public void enterXsdAll(KiaraParser.XsdAllContext ctx) {
    }

    @Override
    public void exitXsdAll(KiaraParser.XsdAllContext ctx) {
    }

    @Override
    public void enterXsdOptional(KiaraParser.XsdOptionalContext ctx) {
    }

    @Override
    public void exitXsdOptional(KiaraParser.XsdOptionalContext ctx) {
    }

    @Override
    public void enterXsdNillable(KiaraParser.XsdNillableContext ctx) {
    }

    @Override
    public void exitXsdNillable(KiaraParser.XsdNillableContext ctx) {
    }

    @Override
    public void enterXsdAttributes(KiaraParser.XsdAttributesContext ctx) {
    }

    @Override
    public void exitXsdAttributes(KiaraParser.XsdAttributesContext ctx) {
    }

    @Override
    public void enterXception(KiaraParser.XceptionContext ctx) {
    }

    @Override
    public void exitXception(KiaraParser.XceptionContext ctx) {
        pdebug("Xception -> XCEPTION IDENTIFIER { FieldList }");

        String exceptionName = ctx.IDENTIFIER().getText();
        List<KiaraParser.FieldContext> fieldCtxList = ctx.field();
        StructType s = StructType.create(getWorld(), exceptionName,
                fieldCtxList.size());

        initStructTypeFromFields(s, fieldCtxList);

        s.setAttributeValue(new ExceptionTypeAttr());

        try {
            getModule().bindType(exceptionName, s);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.NEWTYPE, s);
        } catch (Exception e) {
            perror(e.toString());
            s = null;
        }
        setValue(ctx, s);
    }

    @Override
    public void enterAnnotationDef(KiaraParser.AnnotationDefContext ctx) {
    }

    @Override
    public void exitAnnotationDef(KiaraParser.AnnotationDefContext ctx) {
        pdebug("AnnotationDef -> ANNOTATION IDENTIFIER { FieldList }");

        String annotationName = ctx.IDENTIFIER().getText();
        List<KiaraParser.FieldContext> fieldCtxList = ctx.field();
        StructType s = StructType.create(getWorld(), annotationName,
                fieldCtxList.size());

        initStructTypeFromFields(s, fieldCtxList);

        s.setAttributeValue(new AnnotationTypeAttr(true));

        try {
            getModule().bindType(annotationName, s);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.NEWTYPE, s);
        } catch (Exception e) {
            perror(e.toString());
            s = null;
        }
        setValue(ctx, s);
    }

    @Override
    public void enterService(KiaraParser.ServiceContext ctx) {
    }

    @Override
    public void exitService(KiaraParser.ServiceContext ctx) {
        pdebug("Service -> SERVICE IDENTIFIER { FunctionList }");
        String serviceName = ctx.sname.getText();
        List<KiaraParser.FunctionContext> functionCtxList = ctx.function();
        StructType s = StructType.create(getWorld(), serviceName, functionCtxList.size());

        initStructTypeFromFunctions(s, functionCtxList);

        List<Annotation> al = (List<Annotation>)getValue(ctx.annotationList());
        if (al != null && !al.isEmpty()) {
            s.setAttributeValue(new AnnotationListAttr(al));
        }

        try {
            getModule().bindType(serviceName, s);
            getModule().addTypeDeclaration(Module.TypeDeclarationKind.NEWTYPE, s);
        } catch (Exception e) {
            perror(e.toString());
            s = null;
        }
        setValue(ctx, s);
    }

    @Override
    public void enterFunction(KiaraParser.FunctionContext ctx) {
    }

    @Override
    public void exitFunction(KiaraParser.FunctionContext ctx) {
        pdebug("Function");
        String funcName = ctx.IDENTIFIER().getText();
        Type retType = (Type)getValue(ctx.retType);
        Function f = null;
        if (retType != null) {
            List<Annotation> funcAnnotationList = unwrapAnnotationList(ctx.funcAnnotationList);
            List<Annotation> retAnnotationList = unwrapAnnotationList(ctx.retAnnotationList);
            List<Field> fieldList = unwrapFieldList(ctx.field());
            f = new Function(createFunction(funcName, retType, fieldList,
                    funcAnnotationList, retAnnotationList));
        } else {
            perror("No return type for function "+funcName+" specified");
        }
        setValue(ctx, f);
    }

    @Override
    public void enterThrowz(KiaraParser.ThrowzContext ctx) {
    }

    @Override
    public void exitThrowz(KiaraParser.ThrowzContext ctx) {
    }

    @Override
    public void enterField(KiaraParser.FieldContext ctx) {
    }

    @Override
    public void exitField(KiaraParser.FieldContext ctx) {
        Field f = new Field();
        f.id = unwrapFieldIdentifier(ctx.fieldIdentifier());
        f.type = unwrapFieldType(ctx.fieldType());
        f.name = ctx.IDENTIFIER().getText();
        f.value = unwrapFieldValue(ctx.fieldValue());
        f.annotationList = unwrapAnnotationList(ctx.annotationList());

        setValue(ctx, f);
    }

    @Override
    public void enterFieldIdentifier(KiaraParser.FieldIdentifierContext ctx) {
    }

    @Override
    public void exitFieldIdentifier(KiaraParser.FieldIdentifierContext ctx) {
        copyValue(ctx, ctx.intConstant());
    }

    @Override
    public void enterFieldRequiredness(KiaraParser.FieldRequirednessContext ctx) {
    }

    @Override
    public void exitFieldRequiredness(KiaraParser.FieldRequirednessContext ctx) {
    }

    @Override
    public void enterFieldValue(KiaraParser.FieldValueContext ctx) {
    }

    @Override
    public void exitFieldValue(KiaraParser.FieldValueContext ctx) {
        copyValue(ctx, ctx.constValue());
    }

    @Override
    public void enterFunctionType(KiaraParser.FunctionTypeContext ctx) {
    }

    @Override
    public void exitFunctionType(KiaraParser.FunctionTypeContext ctx) {
        if (ctx.VOID() != null)
            setValue(ctx, VoidType.get(getWorld()));
        else
            setValue(ctx, getValue(ctx.getChild(0)));
    }

    @Override
    public void enterFieldType(KiaraParser.FieldTypeContext ctx) {
    }

    @Override
    public void exitFieldType(KiaraParser.FieldTypeContext ctx) {
        Type ty;
        TerminalNode tn = ctx.IDENTIFIER();
        if (tn != null) {
            ty = getModule().lookupType(tn.getText());
            if (ty == null) {
                perror("Unknown type identifier : "+tn.getText());
            }
        } else {
            ty = (Type)getValue(ctx.getChild(0));
        }
        setValue(ctx, ty);
    }

    @Override
    public void enterBaseType(KiaraParser.BaseTypeContext ctx) {
    }

    @Override
    public void exitBaseType(KiaraParser.BaseTypeContext ctx) {
        setValue(ctx, getValue(ctx.simpleBaseType()));
    }

    @Override
    public void enterSimpleBaseType(KiaraParser.SimpleBaseTypeContext ctx) {
    }

    @Override
    public void exitSimpleBaseType(KiaraParser.SimpleBaseTypeContext ctx) {
        Type ty = null;
        String text = ctx.getText();
        if (text.equals("string"))
            ty = getWorld().type_string();
        else if (text.equals("binary"))
            ty = null; // FIXME not supported yet
        else if (text.equals("slist"))
            ty = null; // FIXME not supported yet
        else if (text.equals("boolean"))
            ty = getWorld().type_boolean();
        else if (text.equals("i8"))
            ty = getWorld().type_i8();
        else if (text.equals("u8"))
            ty = getWorld().type_u8();
        else if (text.equals("i16"))
            ty = getWorld().type_i16();
        else if (text.equals("u16"))
            ty = getWorld().type_u16();
        else if (text.equals("i32"))
            ty = getWorld().type_i32();
        else if (text.equals("u32"))
            ty = getWorld().type_u32();
        else if (text.equals("i64"))
            ty = getWorld().type_i64();
        else if (text.equals("u64"))
            ty = getWorld().type_u64();
        else if (text.equals("float"))
            ty = getWorld().type_float();
        else if (text.equals("double"))
            ty = getWorld().type_double();
        else if (text.equals("any"))
            ty = AnyType.get(getWorld());
        setValue(ctx, ty);
    }

    @Override
    public void enterGenericType(KiaraParser.GenericTypeContext ctx) {
    }

    @Override
    public void exitGenericType(KiaraParser.GenericTypeContext ctx) {
        pdebug("GenericType -> SimpleGenericType");
        copyValue(ctx, ctx.simpleGenericType());
    }

    @Override
    public void enterSimpleGenericType(KiaraParser.SimpleGenericTypeContext ctx) {
    }

    @Override
    public void exitSimpleGenericType(KiaraParser.SimpleGenericTypeContext ctx) {
        String tname = ctx.IDENTIFIER().getText();
        if (tname.equals("array")) {
            List<Type> typeList = unwrapGenericTypeArgList(ctx.genericTypeArgList());
            if (typeList == null || (typeList.size() != 1 && typeList.size() != 2)) {
                perror("Array argument list must have single type and optional size");
            } else {
                Type elementType = typeList.get(0);
                if (typeList.size() == 2)
                {
                    PrimValueType sizeType = (PrimValueType)typeList.get(1);
                    if (sizeType != null && sizeType.getValue() instanceof Integer)
                    {
                        setValue(ctx, FixedArrayType.get(elementType, (Integer)sizeType.getValue()));
                    }
                    else
                    {
                        perror("Second argument of array type must be integer constant");
                    }
                }
                else
                {
                    setValue(ctx, ArrayType.get(elementType));
                }
            }
        } else {
            perror("Unsupported generic type: "+tname);
        }
    }

    @Override
    public void enterGenericTypeArg(KiaraParser.GenericTypeArgContext ctx) {
    }

    @Override
    public void exitGenericTypeArg(KiaraParser.GenericTypeArgContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            setValue(ctx, getModule().lookupType(ctx.IDENTIFIER().getText()));
        } else if (ctx.VOID() != null) {
            setValue(ctx, getWorld().type_void());
        } else if (ctx.baseType() != null) {
            copyValue(ctx, ctx.baseType());
        } else if (ctx.genericType() != null) {
            copyValue(ctx, ctx.genericType());
        } else if (ctx.INTCONSTANT() != null) {
            setValue(ctx, PrimValueType.get(getWorld(), Integer.valueOf(ctx.INTCONSTANT().getText(), 10)));
        } else if (ctx.DUBCONSTANT() != null) {
            setValue(ctx, PrimValueType.get(getWorld(), Double.valueOf(ctx.DUBCONSTANT().getText())));
        } else if (ctx.LITERAL() != null) {
            setValue(ctx, PrimValueType.get(getWorld(), ctx.LITERAL().getText()));
        }
    }

    @Override
    public void enterGenericTypeArgList(KiaraParser.GenericTypeArgListContext ctx) {
    }

    @Override
    public void exitGenericTypeArgList(KiaraParser.GenericTypeArgListContext ctx) {
        List<KiaraParser.GenericTypeArgContext> tyArgCtxList = ctx.genericTypeArg();
        List<Type> typeList = new ArrayList<>();
        for (KiaraParser.GenericTypeArgContext tactx : tyArgCtxList) {
            typeList.add((Type)getValue(tactx));
        }
        setValue(ctx, typeList);
    }

    @Override
    public void enterAnnotationList(KiaraParser.AnnotationListContext ctx) {
    }

    @Override
    public void exitAnnotationList(KiaraParser.AnnotationListContext ctx) {
        List<Annotation> al = null;
        if (ctx.getChildCount() > 0) {
            pdebug("AnnotationList: number of children "+ctx.getChildCount());
            al = new ArrayList<>();
            for (KiaraParser.AnnotationContext annotation : ctx.annotation()) {
                al.add((Annotation)getValue(annotation));
            }
        }
        setValue(ctx, al);
    }


    @Override
    public void enterAnnotation(KiaraParser.AnnotationContext ctx) {
    }

    @Override
    public void exitAnnotation(KiaraParser.AnnotationContext ctx) {
        final String typeId = ctx.IDENTIFIER().getText();
        pdebug("Annotation -> IDENTIFIER:"+typeId);

        final Type ty = this.module.lookupType(typeId);
        if (ty == null) {
            perror("Unknown type identifier : "+typeId);
        } else {
            if (!(ty instanceof StructType)) {
                perror("Type '"+typeId+"' is not an annotation type");
                return;
            }

            StructType sty = (StructType)ty;
            AnnotationTypeAttr atr = sty.getAttributeValue(AnnotationTypeAttr.class);
            if (atr == null || atr.getValue() == false) {
                perror("Type '"+typeId+"' is not an annotation type");
                return;
            }

            setValue(ctx, new Annotation(sty));
        }
    }

    @Override
    public void enterAnnotationArg(KiaraParser.AnnotationArgContext ctx) {
    }

    @Override
    public void exitAnnotationArg(KiaraParser.AnnotationArgContext ctx) {
    }

    @Override
    public void enterAnnotationArgList(KiaraParser.AnnotationArgListContext ctx) {
    }

    @Override
    public void exitAnnotationArgList(KiaraParser.AnnotationArgListContext ctx) {
    }

    @Override
    public void enterIntConstant(KiaraParser.IntConstantContext ctx) {
    }

    @Override
    public void exitIntConstant(KiaraParser.IntConstantContext ctx) {
        TerminalNode tn = ctx.INTCONSTANT();
        if (tn != null) {
            setValue(ctx, Integer.valueOf(tn.getText(), 10));
        } else {
            setValue(ctx, Integer.valueOf(ctx.HEXCONSTANT().getText().substring(2), 16));
        }
        pdebug("intConstant : "+getValue(ctx));
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
