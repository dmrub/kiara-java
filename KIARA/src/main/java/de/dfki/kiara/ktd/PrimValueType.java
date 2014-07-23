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
        return isUnsigned() == otherTy.isUnsigned();
    }

}
