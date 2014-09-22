/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.ktd;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class PrimType extends Type {

    PrimType(World world, int kind) {
        super(world, getNameOfPrimTypeKind(kind), kind, 0);
    }

    public static String getNameOfPrimTypeKind(int kind) {
        switch (kind) {
            case PrimTypeKind.PRIMTYPE_i8:
                return "i8";
            case PrimTypeKind.PRIMTYPE_u8:
                return "u8";
            case PrimTypeKind.PRIMTYPE_i16:
                return "i16";
            case PrimTypeKind.PRIMTYPE_u16:
                return "u16";
            case PrimTypeKind.PRIMTYPE_i32:
                return "i32";
            case PrimTypeKind.PRIMTYPE_u32:
                return "u32";
            case PrimTypeKind.PRIMTYPE_i64:
                return "i64";
            case PrimTypeKind.PRIMTYPE_u64:
                return "u64";
            case PrimTypeKind.PRIMTYPE_float:
                return "float";
            case PrimTypeKind.PRIMTYPE_double:
                return "double";
            case PrimTypeKind.PRIMTYPE_boolean:
                return "boolean";
            case PrimTypeKind.PRIMTYPE_string:
                return "string";
            case PrimTypeKind.PRIMTYPE_java_byte:
                return "java_byte";
            case PrimTypeKind.PRIMTYPE_java_short:
                return "java_short";
            case PrimTypeKind.PRIMTYPE_java_int:
                return "java_int";
            case PrimTypeKind.PRIMTYPE_java_long:
                return "java_long";
            case PrimTypeKind.PRIMTYPE_java_float:
                return "java_float";
            case PrimTypeKind.PRIMTYPE_java_double:
                return "java_double";
            case PrimTypeKind.PRIMTYPE_java_boolean:
                return "java_boolean";
            case PrimTypeKind.PRIMTYPE_java_char:
                return "java_char";
            case PrimTypeKind.PRIMTYPE_java_nullptr:
                return "java_nullptr";
            default:
                throw new IllegalArgumentException("Unknown primitive type ID");
        }
    }

    public static int getByteSizeOfPrimTypeKind(int kind) {
        switch (kind) {
            // For abstract types function should report size of the internal representation
            case PrimTypeKind.PRIMTYPE_i8:
                return 1;
            case PrimTypeKind.PRIMTYPE_u8:
                return 1;
            case PrimTypeKind.PRIMTYPE_i16:
                return 2;
            case PrimTypeKind.PRIMTYPE_u16:
                return 2;
            case PrimTypeKind.PRIMTYPE_i32:
                return 4;
            case PrimTypeKind.PRIMTYPE_u32:
                return 4;
            case PrimTypeKind.PRIMTYPE_i64:
                return 8;
            case PrimTypeKind.PRIMTYPE_u64:
                return 8;
            case PrimTypeKind.PRIMTYPE_float:
                return 4;
            case PrimTypeKind.PRIMTYPE_double:
                return 8;
            case PrimTypeKind.PRIMTYPE_boolean:
                return 1; //??? Is this correct
            case PrimTypeKind.PRIMTYPE_string:
                return 0; //??? What to return here ?
            // For native types function should report size used by DataOutputStream encoding
            case PrimTypeKind.PRIMTYPE_java_byte:
                return 1;
            case PrimTypeKind.PRIMTYPE_java_short:
                return 2;
            case PrimTypeKind.PRIMTYPE_java_int:
                return 4;
            case PrimTypeKind.PRIMTYPE_java_long:
                return 8;
            case PrimTypeKind.PRIMTYPE_java_float:
                return 4;
            case PrimTypeKind.PRIMTYPE_java_double:
                return 8;
            case PrimTypeKind.PRIMTYPE_java_boolean:
                return 1;
            case PrimTypeKind.PRIMTYPE_java_char:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown primitive type ID");
        }
    }

    public static boolean isIntegerPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_i8
                || kind == PrimTypeKind.PRIMTYPE_u8
                || kind == PrimTypeKind.PRIMTYPE_i16
                || kind == PrimTypeKind.PRIMTYPE_u16
                || kind == PrimTypeKind.PRIMTYPE_i32
                || kind == PrimTypeKind.PRIMTYPE_u32
                || kind == PrimTypeKind.PRIMTYPE_i64
                || kind == PrimTypeKind.PRIMTYPE_u64
                || kind == PrimTypeKind.PRIMTYPE_java_byte
                || kind == PrimTypeKind.PRIMTYPE_java_short
                || kind == PrimTypeKind.PRIMTYPE_java_int
                || kind == PrimTypeKind.PRIMTYPE_java_long);
    }

    public static boolean isSignedIntegerPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_i8
                || kind == PrimTypeKind.PRIMTYPE_i16
                || kind == PrimTypeKind.PRIMTYPE_i32
                || kind == PrimTypeKind.PRIMTYPE_i64
                || kind == PrimTypeKind.PRIMTYPE_java_byte
                || kind == PrimTypeKind.PRIMTYPE_java_short
                || kind == PrimTypeKind.PRIMTYPE_java_int
                || kind == PrimTypeKind.PRIMTYPE_java_long);
    }

    public final boolean isFloatingPointPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_float
                || kind == PrimTypeKind.PRIMTYPE_double
                || kind == PrimTypeKind.PRIMTYPE_java_float
                || kind == PrimTypeKind.PRIMTYPE_java_double);
    }

    public final int getByteSize() { return getByteSizeOfPrimTypeKind(getKind()); }

    public final boolean isInteger() {
        return isIntegerPrimTypeKind(getKind());
    }

    public final boolean isSignedInteger() {
        return isSignedIntegerPrimTypeKind(getKind());
    }

    public final boolean isFloatingPoint() {
        return isFloatingPointPrimTypeKind(getKind());
    }

    public final boolean isJavaType() {
        return getKind() >= PrimTypeKind.FIRST_JAVA_PRIMTYPE;
    }

    public static PrimType getBooleanType(World world) {
        return world.type_boolean();
    }

    public static PrimType getStringType(World world) {
        return world.type_string();
    }

}
