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
public class FixedArrayType extends CompositeType {

    public static FixedArrayType get(Type elementType, int arraySize) {
        World world = elementType.getWorld();
        FixedArrayType ty = new FixedArrayType(world, elementType, arraySize);
        ty.init();
        return (FixedArrayType)world.getOrCreate(ty);
    }

    public Type getElementType() {
        return getElementAt(0);
    }

    public final int getArraySize() {
        return (Integer)getElementAs(PrimValueType.class, 1).getValue();
    }

    private FixedArrayType(World world, Type elementType, int arraySize) {
        super(world, "array", NodeKind.NODE_FIXEDARRAYTYPE, 2, false);
        setElements(elementType, PrimValueType.get(world, arraySize));
    }

    private void init() {
        if (!getElementType().isCanonicalType())
            setCanonicalTypeUnsafe(
                    FixedArrayType.get(getElementType().getCanonicalType(), getArraySize()));
    }
}
