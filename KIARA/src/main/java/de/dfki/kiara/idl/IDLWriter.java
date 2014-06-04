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
import de.dfki.kiara.ktd.ArrayType;
import de.dfki.kiara.ktd.AttributeHolder;
import de.dfki.kiara.ktd.CompositeType;
import de.dfki.kiara.ktd.DefaultFieldValueAttr;
import de.dfki.kiara.ktd.ElementData;
import de.dfki.kiara.ktd.EnumType;
import de.dfki.kiara.ktd.ExceptionTypeAttr;
import de.dfki.kiara.ktd.Expr;
import de.dfki.kiara.ktd.FixedArrayType;
import de.dfki.kiara.ktd.FunctionType;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.Namespace;
import de.dfki.kiara.ktd.ServiceType;
import de.dfki.kiara.ktd.StructType;
import de.dfki.kiara.ktd.Type;
import de.dfki.kiara.ktd.TypedefType;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class IDLWriter {

    private final Module module;

    private int indentLevel;
    private boolean newlineOutput;

    public IDLWriter(Module module) {
        this.module = module;
        this.indentLevel = 0;
        this.newlineOutput = true;
    }

    private void printIndent(PrintStream out) {
        for (int i = 0; i < indentLevel; ++i) {
            out.print("  ");
        }
    }

    private void println(PrintStream out, String s) {
        if (newlineOutput)
            printIndent(out);
        out.println(s);
        newlineOutput = true;
    }

    private void println(PrintStream out) {
        if (newlineOutput)
            printIndent(out);
        out.println();
        newlineOutput = true;
    }

    private void print(PrintStream out, String s) {
        if (newlineOutput) {
            printIndent(out);
            newlineOutput = false;
        }
        out.print(s);
    }

    private void print(PrintStream out, Object o) {
        if (newlineOutput) {
            printIndent(out);
            newlineOutput = false;
        }
        out.print(o);
    }

    private void incrIndent() {
        ++indentLevel;
    }

    private void decrIndent() {
        --indentLevel;
    }

    private String getTypeName(Type type) {
        if (type instanceof ArrayType) {
            return "array<" + getTypeName(((ArrayType)type).getElementType()) + ">";
        } else if (type instanceof FixedArrayType) {
            FixedArrayType fty = (FixedArrayType)type;
            return "array<" + getTypeName(fty.getElementType()) + ", " + fty.getArraySize() + ">";
        }

        return type.getTypeName();
    }

    public void write(PrintStream out) {
        Namespace ns = module.getNamespace();
        println(out, "// Module Namespace "+ns.getName());
        List<Type> decls = module.getTypeDeclarations();

        for (Type decl : decls) {
            String ownTypeName = getTypeName(decl);
            String nsTypeName = ns.getTypeName(decl);
            if (decl instanceof TypedefType) {
                TypedefType typedefType = (TypedefType)decl;
                println(out, "typedef " + getTypeName(typedefType.getDeclType()) + " " + ownTypeName);
            } else if (decl instanceof StructType) {
                writeStructType((StructType)decl, out);
            } else if (decl instanceof ServiceType) {
                writeServiceType((ServiceType)decl, out);
            } else if (decl instanceof EnumType) {
                writeEnumType((EnumType)decl, out);
            }
        }
    }

    private void writeStructType(StructType type, PrintStream out) {
        if (type == null)
            return;

        writeAnnotationList(type, out);

        if (type.getAttributeValue(ExceptionTypeAttr.class) != null)
            print(out, "exception ");
        else if (type.getAttributeValue(AnnotationTypeAttr.class) != null)
            print(out, "annotation ");
        else
            print(out, "struct ");
        print(out, getTypeName(type));
        println(out, " {");
        incrIndent();
        writeTypeMembers(type, out);
        decrIndent();
        println(out, "}");
    }

    private void writeServiceType(ServiceType type, PrintStream out) {
        if (type == null)
            return;

        if (writeAnnotationList(type, out))
            println(out);

        println(out, "service "+getTypeName(type)+" {");
        incrIndent();
        writeTypeMembers(type, out);
        decrIndent();
        println(out, "}");
    }

    private void writeEnumType(EnumType type, PrintStream out) {
        if (type == null)
            return;

        writeAnnotationList(type, out);
        println(out);

        println(out, "enum "+getTypeName(type)+" {");
        incrIndent();
        final int n = type.getNumConstants();
        for (int i = 0; i < n; ++i) {
            if (i != 0)
                println(out, ",");
            Expr expr = type.getConstantAt(i);
            String name = type.getConstantNameAt(i);
            print(out, name+" = ");
            print(out, expr);
        }
        if (n != 0)
            println(out);
        decrIndent();
        println(out, "}");
    }

    private void writeTypeMembers(CompositeType type, PrintStream out) {
        final int n = type.getNumElements();
        for (int i = 0; i < n; ++i) {
            Type elemTy = type.getElementAt(i);
            final ElementData elemData = type.getElementDataAt(i);
            if (i != 0)
                println(out);
            if (elemTy instanceof FunctionType) {
                FunctionType fty = (FunctionType)elemTy;
                if (writeAnnotationList(fty, out))
                    println(out);

                print(out, getTypeName(fty.getReturnType()));
                print(out, " ");

                if (writeAnnotationList(fty.getReturnElementData(), out))
                    print(out, " ");

                print(out, elemData.getName());
                print(out, "(");

                final int nargs = fty.getNumParams();
                for (int j = 0; j < nargs; ++j) {
                    if (j != 0)
                        print(out, ", ");
                    ElementData paramElementData = fty.getParamElementDataAt(j);
                    if (writeAnnotationList(paramElementData, out))
                        print(out, " ");
                    print(out, getTypeName(fty.getParamType(j)));
                    print(out, " ");
                    print(out, fty.getParamName(j));
                }
                print(out, ")");
            } else {
                writeAnnotationList(elemData, out);
                print(out, getTypeName(elemTy));
                print(out, " ");
                print(out, elemData.getName());
                if (elemData.hasAttributeValue(DefaultFieldValueAttr.class)) {
                    Expr expr = elemData.getAttributeValue(DefaultFieldValueAttr.class).getValue();
                    print(out, " = ");
                    print(out, expr);
                }
            }
            print(out, ";");
        }
        if (n != 0)
            println(out);
    }

    private boolean writeAnnotationList(AttributeHolder attributeHolder, PrintStream out) {
        if (attributeHolder.hasAttributeValue(AnnotationListAttr.class)) {
            List<Annotation> alist = attributeHolder.getAttributeValue(AnnotationListAttr.class).getValue();
            return writeAnnotationList(alist, out);
        }
        return false;
    }

    private boolean writeAnnotationList(List<Annotation> alist, PrintStream out) {
        if (alist == null || alist.isEmpty())
            return false;
        print(out, "[");

        boolean first = true;

        for (Annotation annotation : alist) {
            if (annotation == null)
                continue;
            if (!first)
                print(out, ", ");
            else
                first = false;
            print(out, getTypeName(annotation.getAnnotationType()));
            // FIXME Implement output of arguments
        }

        print(out, "]");
        return true;
    }
}
