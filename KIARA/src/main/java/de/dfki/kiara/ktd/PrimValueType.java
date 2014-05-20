/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.ktd;

import java.util.Objects;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class PrimValueType extends Type {

    private final Object value;
    private final boolean unsigned;

    private PrimValueType(World world, byte v) {
        super(world, "value_type_i8(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, short v) {
        super(world, "value_type_i16(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, int v) {
        super(world, "value_type_i32(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, long v) {
        super(world, "value_type_i64(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, float v) {
        super(world, "value_type_float(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, double v) {
        super(world, "value_type_double(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, boolean v) {
        super(world, "value_type_boolean(" + v + ")", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    private PrimValueType(World world, String v) {
        super(world, "value_type_string('" + v + "')", NodeKind.NODE_PRIMVALUETYPE, 0);
        this.value = v;
        this.unsigned = false;
    }

    public static PrimValueType get(World world, byte v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, short v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, int v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, long v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, float v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, double v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, boolean v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public static PrimValueType get(World world, String v) {
        return (PrimValueType) world.getOrCreate(new PrimValueType(world, v));
    }

    public Object getValue() {
        return value;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unsigned);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof PrimValueType))
            return false;
        PrimValueType otherTy = (PrimValueType)other;
        if (getKind() != otherTy.getKind())
            return false;
        if (!getValue().equals(otherTy.getValue()))
            return false;
        if (isUnsigned() != otherTy.isUnsigned())
            return false;
        return true;
    }

}
