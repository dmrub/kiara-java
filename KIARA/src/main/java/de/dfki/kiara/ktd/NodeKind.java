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
    public static final int NODE_PRIMTYPE_java_byte = FIRST_PRIMTYPE_NODE + 12;
    public static final int NODE_PRIMTYPE_java_short = FIRST_PRIMTYPE_NODE + 13;
    public static final int NODE_PRIMTYPE_java_int = FIRST_PRIMTYPE_NODE + 14;
    public static final int NODE_PRIMTYPE_java_long = FIRST_PRIMTYPE_NODE + 15;
    public static final int NODE_PRIMTYPE_java_float = FIRST_PRIMTYPE_NODE + 16;
    public static final int NODE_PRIMTYPE_java_double = FIRST_PRIMTYPE_NODE + 17;
    public static final int NODE_PRIMTYPE_java_boolean = FIRST_PRIMTYPE_NODE + 18;
    public static final int NODE_PRIMTYPE_java_char = FIRST_PRIMTYPE_NODE + 19;
    public static final int NODE_PRIMTYPE_java_nullptr = FIRST_PRIMTYPE_NODE + 20;
    public static final int LAST_PRIMTYPE_NODE = NODE_PRIMTYPE_java_nullptr;
    public static final int FIRST_JAVA_PRIMTYPE_NODE = NODE_PRIMTYPE_java_byte;
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
    public static final int NODE_OBJECTTYPE = LAST_PRIMTYPE_NODE + 11;
    public static final int NODE_ARRAYTYPE = LAST_PRIMTYPE_NODE + 12;
    public static final int NODE_FIXEDARRAYTYPE = LAST_PRIMTYPE_NODE + 13;
    public static final int NODE_STRUCTTYPE = LAST_PRIMTYPE_NODE + 14;
    public static final int NODE_FUNCTYPE = LAST_PRIMTYPE_NODE + 15;
    public static final int NODE_SERVICETYPE = LAST_PRIMTYPE_NODE + 16;
    public static final int LAST_NODE_KIND = NODE_SERVICETYPE;
}
