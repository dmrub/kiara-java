/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.ktd;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class PrimType extends Type {

    private PrimType(World world, int kind) {
        super(world, getNameOfPrimTypeKind(kind), kind, 0);
    }

    public static final String getNameOfPrimTypeKind(int kind) {
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
            case PrimTypeKind.PRIMTYPE_c_int8_t:
                return "c_int8_t";
            case PrimTypeKind.PRIMTYPE_c_uint8_t:
                return "c_uint8_t";
            case PrimTypeKind.PRIMTYPE_c_int16_t:
                return "c_int16_t";
            case PrimTypeKind.PRIMTYPE_c_uint16_t:
                return "c_uint16_t";
            case PrimTypeKind.PRIMTYPE_c_int32_t:
                return "c_int32_t";
            case PrimTypeKind.PRIMTYPE_c_uint32_t:
                return "c_uint32_t";
            case PrimTypeKind.PRIMTYPE_c_int64_t:
                return "c_int64_t";
            case PrimTypeKind.PRIMTYPE_c_uint64_t:
                return "c_uint64_t";
            case PrimTypeKind.PRIMTYPE_c_float:
                return "c_float";
            case PrimTypeKind.PRIMTYPE_c_double:
                return "c_double";
            case PrimTypeKind.PRIMTYPE_c_longdouble:
                return "c_longdouble";
            case PrimTypeKind.PRIMTYPE_c_bool:
                return "c_bool";
            case PrimTypeKind.PRIMTYPE_c_nullptr:
                return "c_nullptr";
            default:
                throw new IllegalArgumentException("Unknown primitive type ID");
        }
    }

    public static final int getByteSizeOfPrimTypeKind(int kind) {
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
            // For native types function should report size measured with sizeof
            case PrimTypeKind.PRIMTYPE_c_int8_t:
                return 1;
            case PrimTypeKind.PRIMTYPE_c_uint8_t:
                return 1;
            case PrimTypeKind.PRIMTYPE_c_int16_t:
                return 2;
            case PrimTypeKind.PRIMTYPE_c_uint16_t:
                return 2;
            case PrimTypeKind.PRIMTYPE_c_int32_t:
                return 4;
            case PrimTypeKind.PRIMTYPE_c_uint32_t:
                return 4;
            case PrimTypeKind.PRIMTYPE_c_int64_t:
                return 8;
            case PrimTypeKind.PRIMTYPE_c_uint64_t:
                return 8;
            case PrimTypeKind.PRIMTYPE_c_float:
                return 4;
            case PrimTypeKind.PRIMTYPE_c_double:
                return 8;
            case PrimTypeKind.PRIMTYPE_c_longdouble:
                return 0; //??? What to return here ?
            case PrimTypeKind.PRIMTYPE_c_bool:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown primitive type ID");
        }
    }

    public static final boolean isIntegerPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_i8
                || kind == PrimTypeKind.PRIMTYPE_u8
                || kind == PrimTypeKind.PRIMTYPE_i16
                || kind == PrimTypeKind.PRIMTYPE_u16
                || kind == PrimTypeKind.PRIMTYPE_i32
                || kind == PrimTypeKind.PRIMTYPE_u32
                || kind == PrimTypeKind.PRIMTYPE_i64
                || kind == PrimTypeKind.PRIMTYPE_u64
                || kind == PrimTypeKind.PRIMTYPE_c_int8_t
                || kind == PrimTypeKind.PRIMTYPE_c_uint8_t
                || kind == PrimTypeKind.PRIMTYPE_c_int16_t
                || kind == PrimTypeKind.PRIMTYPE_c_uint16_t
                || kind == PrimTypeKind.PRIMTYPE_c_int32_t
                || kind == PrimTypeKind.PRIMTYPE_c_uint32_t
                || kind == PrimTypeKind.PRIMTYPE_c_int64_t
                || kind == PrimTypeKind.PRIMTYPE_c_uint64_t);
    }

    public static final boolean isSignedIntegerPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_i8
                || kind == PrimTypeKind.PRIMTYPE_i16
                || kind == PrimTypeKind.PRIMTYPE_i32
                || kind == PrimTypeKind.PRIMTYPE_i64
                || kind == PrimTypeKind.PRIMTYPE_c_int8_t
                || kind == PrimTypeKind.PRIMTYPE_c_int16_t
                || kind == PrimTypeKind.PRIMTYPE_c_int32_t
                || kind == PrimTypeKind.PRIMTYPE_c_int64_t);
    }

    boolean isFloatingPointPrimTypeKind(int kind) {
        return (kind == PrimTypeKind.PRIMTYPE_float
                || kind == PrimTypeKind.PRIMTYPE_double
                || kind == PrimTypeKind.PRIMTYPE_c_float
                || kind == PrimTypeKind.PRIMTYPE_c_double
                || kind == PrimTypeKind.PRIMTYPE_c_longdouble);
    }

    public static final PrimType getBooleanType(World world) {
        return world.type_boolean();
    }

    public static final PrimType getStringType(World world) {
        return world.type_string();
    }

}
