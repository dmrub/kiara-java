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
public class ArrayType extends CompositeType {

    public static ArrayType get(Type elementType) {
        World world = elementType.getWorld();
        return (ArrayType)world.getOrCreate(new ArrayType(world, elementType));
    }

    public Type getElementType() {
        return getElementAt(0);
    }

    private ArrayType(World world, Type elementType) {
        super(world, "array", NodeKind.NODE_ARRAYTYPE, 1, false);
        setElements(elementType);
    }
}
