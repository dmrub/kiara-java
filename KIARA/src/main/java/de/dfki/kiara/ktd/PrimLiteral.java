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

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class PrimLiteral extends Expr {

    private final Object value;
    private final boolean unsigned;

    public PrimLiteral(World world) {
        super(world.type_java_nullptr());
        this.value = null;
        this.unsigned = false;
    }

    public PrimLiteral(byte v, World world) {
        super(world.type_java_byte());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(short v, World world) {
        super(world.type_java_short());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(int v, World world) {
        super(world.type_java_int());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(long v, World world) {
        super(world.type_java_long());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(float v, World world) {
        super(world.type_java_float());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(double v, World world) {
        super(world.type_java_double());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(boolean v, World world) {
        super(world.type_java_boolean());
        this.value = v;
        this.unsigned = false;
    }

    public PrimLiteral(String v, World world) {
        super(world.type_string());
        this.value = v;
        this.unsigned = false;
    }

    public final Object getValue() {
        return value;
    }

    public final boolean isUnsigned() {
        return unsigned;
    }

    public static PrimLiteral getNullPtr(World world) {
        return new PrimLiteral(world);
    }

}
