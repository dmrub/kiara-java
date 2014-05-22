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

import java.util.HashMap;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class World {

    private final HashMap<KTDObject, KTDObject> objects;
    private final Namespace namespace;

    private final TypeType typeType;
    private final VoidType voidType;
    private final UnresolvedSymbolType unresolvedSymbolType;
    private final AnyType anyType;
    private final PrimType i8Type;
    private final PrimType u8Type;
    private final PrimType i16Type;
    private final PrimType u16Type;
    private final PrimType i32Type;
    private final PrimType u32Type;
    private final PrimType i64Type;
    private final PrimType u64Type;
    private final PrimType floatType;
    private final PrimType doubleType;
    private final PrimType booleanType;
    private final PrimType stringType;

    private final PrimType byteJavaType;
    private final PrimType shortJavaType;
    private final PrimType intJavaType;
    private final PrimType longJavaType;
    private final PrimType floatJavaType;
    private final PrimType doubleJavaType;
    private final PrimType charJavaType;
    private final PrimType booleanJavaType;
    private final PrimType nullptrJavaType;

    private final StructType encryptedAnnotation;

    public World() {
        objects = new HashMap<>();
        namespace = Namespace.create(this, "kiara");

        typeType = (TypeType)getOrCreate(new TypeType(this));
        voidType = (VoidType)getOrCreate(new VoidType(this));
        unresolvedSymbolType = (UnresolvedSymbolType)getOrCreate(new UnresolvedSymbolType(this));
        anyType = (AnyType)getOrCreate(new AnyType(this));
        i8Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_i8));
        u8Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_u8));
        i16Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_i16));
        u16Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_u16));
        i32Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_i32));
        u32Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_u32));
        i64Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_i64));
        u64Type = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_u64));
        floatType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_float));
        doubleType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_double));
        booleanType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_boolean));
        stringType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_string));

        byteJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_byte));
        shortJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_short));
        intJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_int));
        longJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_long));
        floatJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_float));
        doubleJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_double));
        charJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_char));
        booleanJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_boolean));

        nullptrJavaType = (PrimType)getOrCreate(new PrimType(this, PrimTypeKind.PRIMTYPE_java_nullptr));


        // builtin annotations

        {
            encryptedAnnotation = StructType.create(this, "Encrypted", 1);
            encryptedAnnotation.setElementNameAt(0, "keyName");
            encryptedAnnotation.setElements(type_string());
            encryptedAnnotation.setAttributeValue(new AnnotationTypeAttr(true));
        }

    }

    public Namespace getWorldNamespace() {
        return namespace;
    }

    // Abstract built-in types
    public TypeType type_type() {
        return typeType;
    }

    public VoidType type_void() {
        return voidType;
    }

    public UnresolvedSymbolType type_unresolved_symbol() {
        return unresolvedSymbolType;
    }

    public AnyType type_any() {
        return anyType;
    }

    public PrimType type_i8() {
        return i8Type;
    }

    public PrimType type_u8() {
        return u8Type;
    }

    public PrimType type_i16() {
        return i16Type;
    }

    public PrimType type_u16() {
        return u16Type;
    }

    public PrimType type_i32() {
        return i32Type;
    }

    public PrimType type_u32() {
        return u32Type;
    }

    public PrimType type_i64() {
        return i64Type;
    }

    public PrimType type_u64() {
        return u64Type;
    }

    public PrimType type_float() {
        return floatType;
    }

    public PrimType type_double() {
        return doubleType;
    }

    public PrimType type_boolean() {
        return booleanType;
    }

    public PrimType type_string() {
        return stringType;
    }

    // Native built-in types

    public PrimType type_java_byte() {
        return byteJavaType;
    }

    public PrimType type_java_short() {
        return shortJavaType;
    }

    public PrimType type_java_int() {
        return intJavaType;
    }

    public PrimType type_java_long() {
        return longJavaType;
    }

    public PrimType type_java_float() {
        return floatJavaType;
    }

    public PrimType type_java_double() {
        return doubleJavaType;
    }

    public PrimType type_java_char() {
        return charJavaType;
    }

    public PrimType type_java_boolean() {
        return booleanJavaType;
    }

    public PrimType type_java_nullptr() {
        return nullptrJavaType;
    }

    public StructType getEncryptedAnnotation() {
        return encryptedAnnotation;
    }

    public <T extends KTDObject> T getOrCreate(Class<T> cls, T val) {
        return cls.cast(findObject(val));
    }

    public KTDObject getOrCreate(KTDObject val) {
        return findObject(val);
    }

    protected final KTDObject findObject(KTDObject val) {
        KTDObject storedVal = objects.get(val);
        if (storedVal != null)
            return storedVal;
        objects.put(val, val);
        return val;
    }

    public void dump() {
        System.out.println("World "+System.identityHashCode(this)+" {");
        for (Object obj : objects.values()) {
            if (obj instanceof Type) {
                Type type = (Type)obj;
                System.out.println("  "+type.getTypeName()+" : "+type);
            } else {
                System.out.println("  "+obj);
            }
        }
        System.out.println("}");
    }

}
