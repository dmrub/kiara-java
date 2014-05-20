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
public class NodeKind {
    public static final int FIRST_NODE_KIND = 0;
    public static final int FIRST_PRIMTYPE_NODE = FIRST_NODE_KIND;
    public static final int NODE_PRIMTYPE_i8 = FIRST_PRIMTYPE_NODE + 0;
    public static final int NODE_PRIMTYPE_u8 = FIRST_PRIMTYPE_NODE + 1;
    public static final int NODE_PRIMTYPE_i16 = FIRST_PRIMTYPE_NODE + 2;
    public static final int NODE_PRIMTYPE_u16 = FIRST_PRIMTYPE_NODE + 3;
    public static final int NODE_PRIMTYPE_i32 = FIRST_PRIMTYPE_NODE + 4;
    public static final int NODE_PRIMTYPE_u32 = FIRST_PRIMTYPE_NODE + 5;
    public static final int NODE_PRIMTYPE_i64 = FIRST_PRIMTYPE_NODE + 6;
    public static final int NODE_PRIMTYPE_u64 = FIRST_PRIMTYPE_NODE + 7;
    public static final int NODE_PRIMTYPE_float = FIRST_PRIMTYPE_NODE + 8;
    public static final int NODE_PRIMTYPE_double = FIRST_PRIMTYPE_NODE + 9;
    public static final int NODE_PRIMTYPE_boolean = FIRST_PRIMTYPE_NODE + 10;
    public static final int NODE_PRIMTYPE_string = FIRST_PRIMTYPE_NODE + 11;
    public static final int NODE_PRIMTYPE_c_int8_t = FIRST_PRIMTYPE_NODE + 12;
    public static final int NODE_PRIMTYPE_c_uint8_t = FIRST_PRIMTYPE_NODE + 13;
    public static final int NODE_PRIMTYPE_c_int16_t = FIRST_PRIMTYPE_NODE + 14;
    public static final int NODE_PRIMTYPE_c_uint16_t = FIRST_PRIMTYPE_NODE + 15;
    public static final int NODE_PRIMTYPE_c_int32_t = FIRST_PRIMTYPE_NODE + 16;
    public static final int NODE_PRIMTYPE_c_uint32_t = FIRST_PRIMTYPE_NODE + 17;
    public static final int NODE_PRIMTYPE_c_int64_t = FIRST_PRIMTYPE_NODE + 18;
    public static final int NODE_PRIMTYPE_c_uint64_t = FIRST_PRIMTYPE_NODE + 19;
    public static final int NODE_PRIMTYPE_c_float = FIRST_PRIMTYPE_NODE + 20;
    public static final int NODE_PRIMTYPE_c_double = FIRST_PRIMTYPE_NODE + 21;
    public static final int NODE_PRIMTYPE_c_longdouble = FIRST_PRIMTYPE_NODE + 22;
    public static final int NODE_PRIMTYPE_c_bool = FIRST_PRIMTYPE_NODE + 23;
    public static final int NODE_PRIMTYPE_c_nullptr = FIRST_PRIMTYPE_NODE + 24;
    public static final int LAST_PRIMTYPE_NODE = NODE_PRIMTYPE_c_nullptr;
    public static final int FIRST_C_PRIMTYPE_NODE = NODE_PRIMTYPE_c_int8_t;
    public static final int NUM_PRIMTYPES = LAST_PRIMTYPE_NODE - FIRST_PRIMTYPE_NODE;
    public static final int NODE_VOIDTYPE = LAST_PRIMTYPE_NODE + 1;
    public static final int NODE_PRIMVALUETYPE = LAST_PRIMTYPE_NODE + 2;
    public static final int NODE_UNRESOLVEDSYMBOLTYPE = LAST_PRIMTYPE_NODE + 3;
    public static final int NODE_SYMBOLTYPE = LAST_PRIMTYPE_NODE + 4;
    public static final int NODE_ANYTYPE = LAST_PRIMTYPE_NODE + 5;
    public static final int NODE_TYPETYPE = LAST_PRIMTYPE_NODE + 6;
    public static final int NODE_TYPEDEFTYPE = LAST_PRIMTYPE_NODE + 7;
    public static final int NODE_ENUMTYPE = LAST_PRIMTYPE_NODE + 8;
    public static final int NODE_PTRTYPE = LAST_PRIMTYPE_NODE + 9;
    public static final int NODE_REFTYPE = LAST_PRIMTYPE_NODE + 10;
    public static final int NODE_ARRAYTYPE = LAST_PRIMTYPE_NODE + 11;
    public static final int NODE_FIXEDARRAYTYPE = LAST_PRIMTYPE_NODE + 12;
    public static final int NODE_STRUCTTYPE = LAST_PRIMTYPE_NODE + 13;
    public static final int NODE_FUNCTYPE = LAST_PRIMTYPE_NODE + 14;
    public static final int NODE_SERVICETYPE = LAST_PRIMTYPE_NODE + 15;
    public static final int LAST_NODE_KIND = NODE_SERVICETYPE;
}
