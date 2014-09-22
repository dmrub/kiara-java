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
public class PrimTypeKind {
    public static final int PRIMTYPE_i8 = NodeKind.NODE_PRIMTYPE_i8;
    public static final int PRIMTYPE_u8 = NodeKind.NODE_PRIMTYPE_u8;
    public static final int PRIMTYPE_i16 = NodeKind.NODE_PRIMTYPE_i16;
    public static final int PRIMTYPE_u16 = NodeKind.NODE_PRIMTYPE_u16;
    public static final int PRIMTYPE_i32 = NodeKind.NODE_PRIMTYPE_i32;
    public static final int PRIMTYPE_u32 = NodeKind.NODE_PRIMTYPE_u32;
    public static final int PRIMTYPE_i64 = NodeKind.NODE_PRIMTYPE_i64;
    public static final int PRIMTYPE_u64 = NodeKind.NODE_PRIMTYPE_u64;
    public static final int PRIMTYPE_float = NodeKind.NODE_PRIMTYPE_float;
    public static final int PRIMTYPE_double = NodeKind.NODE_PRIMTYPE_double;
    public static final int PRIMTYPE_boolean = NodeKind.NODE_PRIMTYPE_boolean;
    public static final int PRIMTYPE_string = NodeKind.NODE_PRIMTYPE_string;
    public static final int PRIMTYPE_java_byte   = NodeKind.NODE_PRIMTYPE_java_byte;
    public static final int PRIMTYPE_java_short  = NodeKind.NODE_PRIMTYPE_java_short;
    public static final int PRIMTYPE_java_int  = NodeKind.NODE_PRIMTYPE_java_int;
    public static final int PRIMTYPE_java_long = NodeKind.NODE_PRIMTYPE_java_long;
    public static final int PRIMTYPE_java_float  = NodeKind.NODE_PRIMTYPE_java_float;
    public static final int PRIMTYPE_java_double = NodeKind.NODE_PRIMTYPE_java_double;
    public static final int PRIMTYPE_java_boolean  = NodeKind.NODE_PRIMTYPE_java_boolean;
    public static final int PRIMTYPE_java_char = NodeKind.NODE_PRIMTYPE_java_char;
    public static final int PRIMTYPE_java_nullptr = NodeKind.NODE_PRIMTYPE_java_nullptr;
    public static final int FIRST_JAVA_PRIMTYPE = NodeKind.FIRST_JAVA_PRIMTYPE_NODE;
}
