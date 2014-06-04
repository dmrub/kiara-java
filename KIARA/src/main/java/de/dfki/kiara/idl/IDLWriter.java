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
import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class IDLWriter {

    private final Module module;

    private int indentLevel;

    public IDLWriter(Module module) {
        this.module = module;
        this.indentLevel = 0;
    }

    private void printIndent(PrintStream out) {
        for (int i = 0; i < indentLevel; ++i) {
            out.println(' ');
        }
    }

    private void println(PrintStream out, String s) {
        printIndent(out);
        out.println(s);
    }

    private void print(PrintStream out, String s) {
        printIndent(out);
        out.print(s);
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
        out.println("// Module Namespace "+ns.getName());
        List<Module.TypeDeclaration> decls = module.getTypeDeclarations();

        for (Module.TypeDeclaration decl : decls) {
            String ownTypeName = getTypeName(decl.type);
            String nsTypeName = ns.getTypeName(decl.type);
            if (decl.kind == Module.TypeDeclarationKind.TYPEDEF) {
                out.println("typedef " + ownTypeName + " " + decl.name);
            } else {
                if (decl.type instanceof StructType) {
                    writeStructType((StructType)decl.type, out);
                } else if (decl.type instanceof ServiceType) {
                    writeServiceType((ServiceType)decl.type, out);
                } else if (decl.type instanceof EnumType) {
                    writeEnumType((EnumType)decl.type, out);
                }
            }
        }
    }

    private void writeStructType(StructType type, PrintStream out) {
        if (type == null)
            return;

        writeAnnotationList(type, out);

        if (type.getAttributeValue(ExceptionTypeAttr.class) != null)
            out.print("exception ");
        else if (type.getAttributeValue(AnnotationTypeAttr.class) != null)
            out.print("annotation ");
        else
            out.print("struct ");
        out.print(getTypeName(type));
        out.print(" {\n");
        // indent
        writeTypeMembers(type, out);
        out.println("}");
    }

    private void writeServiceType(ServiceType type, PrintStream out) {
        if (type == null)
            return;

        writeAnnotationList(type, out);

        out.println("service "+getTypeName(type)+" {");
        // indent
        writeTypeMembers(type, out);
        out.println("}");
    }

    private void writeEnumType(EnumType type, PrintStream out) {
        if (type == null)
            return;

        writeAnnotationList(type, out);

        out.println("enum "+getTypeName(type)+" {");
        // indent
        final int n = type.getNumConstants();
        for (int i = 0; i < n; ++i) {
            if (i != 0)
                out.print(",\n");
            Expr expr = type.getConstantAt(i);
            String name = type.getConstantNameAt(i);
            out.print(name+" = ");
            out.print(expr);
        }
        if (n != 0)
            out.println();
        out.println("}");
    }

    private void writeTypeMembers(CompositeType type, PrintStream out) {
        final int n = type.getNumElements();
        for (int i = 0; i < n; ++i) {
            Type elemTy = type.getElementAt(i);
            final ElementData elemData = type.getElementDataAt(i);
            if (i != 0)
                out.println();
            if (elemTy instanceof FunctionType) {
                FunctionType fty = (FunctionType)elemTy;
                writeAnnotationList(fty, out);

                out.print(getTypeName(fty.getReturnType()));
                out.print(" ");

                writeAnnotationList(fty.getReturnElementData(), out, " ");

                out.print(elemData.getName());
                out.print("(");

                final int nargs = fty.getNumParams();
                for (int j = 0; j < nargs; ++j) {
                    if (j != 0)
                        out.print(", ");
                    ElementData paramElementData = fty.getParamElementDataAt(j);
                    writeAnnotationList(paramElementData, out, " ");
                    out.print(getTypeName(fty.getParamType(j)));
                    out.print(" ");
                    out.print(fty.getParamName(j));
                }
                out.print(")");
            } else {
                writeAnnotationList(elemData, out);
                out.print(getTypeName(elemTy));
                out.print(" ");
                out.print(elemData.getName());
                if (elemData.hasAttributeValue(DefaultFieldValueAttr.class)) {
                    Expr expr = elemData.getAttributeValue(DefaultFieldValueAttr.class).getValue();
                    out.print(" = ");
                    out.print(expr);
                }
            }
            out.print(";");
        }
        if (n != 0)
            out.println();
    }

    private void writeAnnotationList(AttributeHolder attributeHolder, PrintStream out) {
        writeAnnotationList(attributeHolder, out, "\n");
    }

    private void writeAnnotationList(AttributeHolder attributeHolder, PrintStream out, String sep) {
        if (attributeHolder.hasAttributeValue(AnnotationListAttr.class)) {
            List<Annotation> alist = attributeHolder.getAttributeValue(AnnotationListAttr.class).getValue();
            writeAnnotationList(alist, out, sep);
        }
    }

    private void writeAnnotationList(List<Annotation> alist, PrintStream out) {
        writeAnnotationList(alist, out, "\n");
    }

    private void writeAnnotationList(List<Annotation> alist, PrintStream out, String sep) {
        if (alist == null || alist.isEmpty())
            return;
        out.print("[");

        boolean first = true;

        for (Annotation annotation : alist) {
            if (annotation == null)
                continue;
            if (!first)
                out.print(", ");
            else
                first = false;
            out.print(getTypeName(annotation.getAnnotationType()));
            // FIXME Implement output of arguments
        }

        out.print("]");
        if (sep != null)
            out.print(sep);
    }
}
