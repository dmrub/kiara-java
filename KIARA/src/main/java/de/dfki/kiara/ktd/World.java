/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.ktd;


import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class World {

    private Map<String, StructType> uniqueStructTypes;
    private HashMap<KTDObject, KTDObject> objects;
    private final Namespace namespace;

    private TypeType type_;
    private VoidType void_;
    private UnresolvedSymbolType unresolved_symbol_;
    private AnyType any_;
    private PrimType i8_;
    private PrimType u8_;
    private PrimType i16_;
    private PrimType u16_;
    private PrimType i32_;
    private PrimType u32_;
    private PrimType i64_;
    private PrimType u64_;
    private PrimType float_;
    private PrimType double_;
    private PrimType boolean_;
    private PrimType string_;

    private PrimType c_int8_t_;
    private PrimType c_uint8_t_;
    private PrimType c_int16_t_;
    private PrimType c_uint16_t_;
    private PrimType c_int32_t_;
    private PrimType c_uint32_t_;
    private PrimType c_int64_t_;
    private PrimType c_uint64_t_;
    private PrimType c_float_;
    private PrimType c_double_;
    private PrimType c_longdouble_;
    private PrimType c_bool_;
    private PrimType c_nullptr_;

    private Type c_char_;
    private Type c_wchar_t_;
    private Type c_schar_;
    private Type c_uchar_;
    private Type c_short_;
    private Type c_ushort_;
    private Type c_int_;
    private Type c_uint_;
    private Type c_long_;
    private Type c_ulong_;
    private Type c_longlong_;
    private Type c_ulonglong_;
    private Type c_size_t_;
    private Type c_ssize_t_;

    private Type c_string_ptr_;

    //private PtrType c_void_ptr_;
    //private PtrType c_raw_char_ptr_;
    private Type c_char_ptr_;

    private StructType encryptedAnnotation_;

    public World() {
        namespace = Namespace.create(this, "kiara");

    }

    public Namespace getWorldNamespace() {
        return namespace;
    }

    // Abstract built-in types
    public TypeType type_type() {
        return type_;
    }

    public VoidType type_void() {
        return void_;
    }

    public UnresolvedSymbolType type_unresolved_symbol() {
        return unresolved_symbol_;
    }

    public AnyType type_any() {
        return any_;
    }

    public PrimType type_i8() {
        return i8_;
    }

    public PrimType type_u8() {
        return u8_;
    }

    public PrimType type_i16() {
        return i16_;
    }

    public PrimType type_u16() {
        return u16_;
    }

    public PrimType type_i32() {
        return i32_;
    }

    public PrimType type_u32() {
        return u32_;
    }

    public PrimType type_i64() {
        return i64_;
    }

    public PrimType type_u64() {
        return u64_;
    }

    public PrimType type_float() {
        return float_;
    }

    public PrimType type_double() {
        return double_;
    }

    public PrimType type_boolean() {
        return boolean_;
    }

    public PrimType type_string() {
        return string_;
    }

    // Native built-in types
    public PrimType type_c_int8_t() {
        return c_int8_t_;
    }

    public PrimType type_c_uint8_t() {
        return c_uint8_t_;
    }

    public PrimType type_c_int16_t() {
        return c_int16_t_;
    }

    public PrimType type_c_uint16_t() {
        return c_uint16_t_;
    }

    public PrimType type_c_int32_t() {
        return c_int32_t_;
    }

    public PrimType type_c_uint32_t() {
        return c_uint32_t_;
    }

    public PrimType type_c_int64_t() {
        return c_int64_t_;
    }

    public PrimType type_c_uint64_t() {
        return c_uint64_t_;
    }

    public Type type_c_char() {
        return c_char_;
    }

    public Type type_c_wchar_t() {
        return c_wchar_t_;
    }

    public Type type_c_schar() {
        return c_schar_;
    }

    public Type type_c_uchar() {
        return c_uchar_;
    }

    public Type type_c_short() {
        return c_short_;
    }

    public Type type_c_ushort() {
        return c_ushort_;
    }

    public Type type_c_int() {
        return c_int_;
    }

    public Type type_c_uint() {
        return c_uint_;
    }

    public Type type_c_long() {
        return c_long_;
    }

    public Type type_c_ulong() {
        return c_ulong_;
    }

    public Type type_c_longlong() {
        return c_longlong_;
    }

    public Type type_c_ulonglong() {
        return c_ulonglong_;
    }

    public Type type_c_size_t() {
        return c_size_t_;
    }

    public Type type_c_ssize_t() {
        return c_ssize_t_;
    }

    public PrimType type_c_float() {
        return c_float_;
    }

    public PrimType type_c_double() {
        return c_double_;
    }

    public PrimType type_c_longdouble() {
        return c_longdouble_;
    }

    public PrimType type_c_bool() {
        return c_bool_;
    }

    public PrimType type_c_nullptr() {
        return c_nullptr_;
    }

//    public PtrType type_c_ptr(Type elementType) {
//        return PtrType.get(elementType);
//    }

//    public RefType type_c_ref(Type elementType) {
//        return RefType::get(elementType);
//    }

    public Type type_c_char_ptr() {
        return c_char_ptr_;
    }

//    public PtrType type_c_raw_char_ptr() {
//        return c_raw_char_ptr_;
//    }
//
//    public PtrType type_c_void_ptr() {
//        return c_void_ptr_;
//    }

    public Type type_c_string_ptr() {
        return c_string_ptr_;
    }

    public StructType getEncryptedAnnotation() {
        return encryptedAnnotation_;
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

}
