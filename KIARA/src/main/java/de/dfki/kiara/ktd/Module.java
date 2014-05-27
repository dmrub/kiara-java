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
package de.dfki.kiara.ktd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Module extends KTDObject {

    public static enum TypeDeclarationKind {

        TYPEDEF, NEWTYPE
    };

    public static class TypeDeclaration {

        public final TypeDeclarationKind kind;
        public final Type type;

        public TypeDeclaration(TypeDeclarationKind kind, Type type) {
            this.kind = kind;
            this.type = type;
        }
    }

    private final Namespace namespace;
    private final List<TypeDeclaration> typeDeclarations;

    public Module(World world, String namespaceName) {
        super(world);
        this.typeDeclarations = new ArrayList<>();
        this.namespace = Namespace.create(world, namespaceName);

        // Abstract built-in types
        this.namespace.bindType("type", world.type_type());

        this.namespace.bindType("void", world.type_void());

        this.namespace.bindType("unresolved_symbol",
                world.type_unresolved_symbol());

        this.namespace.bindType("any", world.type_any());

        this.namespace.bindType("i8", world.type_i8());

        this.namespace.bindType("u8", world.type_u8());

        this.namespace.bindType("i16", world.type_i16());

        this.namespace.bindType("u16", world.type_u16());

        this.namespace.bindType("i32", world.type_i32());

        this.namespace.bindType("u32", world.type_u32());

        this.namespace.bindType("i64", world.type_i64());

        this.namespace.bindType("u64", world.type_u64());

        this.namespace.bindType("float", world.type_float());

        this.namespace.bindType("double", world.type_double());

        this.namespace.bindType("boolean", world.type_boolean());

        this.namespace.bindType("string", world.type_string());

        // Native built-in types
        this.namespace.bindType("java_byte", world.type_java_byte());

        this.namespace.bindType("java_short", world.type_java_short());

        this.namespace.bindType("java_int", world.type_java_int());

        this.namespace.bindType("java_long", world.type_java_long());

        this.namespace.bindType("java_float", world.type_java_float());

        this.namespace.bindType("java_double", world.type_java_double());

        this.namespace.bindType("java_char", world.type_java_char());

        this.namespace.bindType("java_boolean", world.type_java_boolean());

        this.namespace.bindType("java_nullptr", world.type_java_nullptr());

        this.namespace.bindType("Encrypted", world.getEncryptedAnnotation());

        this.namespace.bindType("Oneway", world.getOnewayAnnotation());
    }

    public final Namespace getNamespace() {
        return namespace;
    }

    public final void bindType(String name, Type type) {
        namespace.bindType(name, type);
    }

    public final Type lookupType(String name) {
        return namespace.lookupType(name);
    }

    public final String getTypeName(Type type) {
        return namespace.getTypeName(type);
    }

    public final void addTypeDeclaration(TypeDeclarationKind kind, Type type) {
        typeDeclarations.add(new TypeDeclaration(kind, type));
    }
}
