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
import de.dfki.kiara.ktd.AnnotationTypeAttr;
import de.dfki.kiara.ktd.ArrayType;
import de.dfki.kiara.ktd.AttributeHolder;
import de.dfki.kiara.ktd.CompositeType;
import de.dfki.kiara.ktd.EnumType;
import de.dfki.kiara.ktd.ExceptionTypeAttr;
import de.dfki.kiara.ktd.FixedArrayType;
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

        out.println("service "+getTypeName(type)+" {\n");
        // indent
        writeTypeMembers(type, out);
        out.println("}");
    }

    private void writeEnumType(EnumType type, PrintStream out) {

    }

    private void writeTypeMembers(CompositeType type, PrintStream out) {

    }

    private void writeAnnotationList(AttributeHolder attributeHolder, PrintStream out) {
        writeAnnotationList(attributeHolder, out, "\n");
    }

    private void writeAnnotationList(AttributeHolder attributeHolder, PrintStream out, String sep) {

    }

    private void writeAnnotationList(List<Annotation> alist, PrintStream out) {
        writeAnnotationList(alist, out, "\n");
    }

    private void writeAnnotationList(List<Annotation> alist, PrintStream out, String sep) {

    }
}
