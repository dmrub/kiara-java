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
