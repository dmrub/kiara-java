/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class StructType extends CompositeType {

    /// Create unique struct type
    public static StructType create(World world, String name, int numElements) {
        return (StructType)world.getOrCreate(
                new StructType(world, name, numElements));
    }

    public static StructType create(World world, String name) {
        return (StructType)world.getOrCreate(new StructType(world, name));
    }

    public static StructType get(World world, String name,
            List<? extends KTDObject> elems) {
        return (StructType)world.getOrCreate(
                new StructType(world, name, elems));
    }

    public static StructType get(World world, String name,
            List<? extends KTDObject> elems, List<String> names) {
        return (StructType)world.getOrCreate(
                new StructType(world, name, elems, names));
    }

    protected StructType(World world, String name,
            List<? extends KTDObject> elems) {
        super(world, name, NodeKind.NODE_STRUCTTYPE, elems, /*unique =*/false);
    }

    protected StructType(World world, String name,
            List<? extends KTDObject> elems, List<String> names) {
        super(world, name, NodeKind.NODE_STRUCTTYPE, elems, names, /*unique =*/false);
    }

    protected StructType(World world, String name, int num) {
        super(world, name, NodeKind.NODE_STRUCTTYPE, num, /*unique=*/true);
    }

    protected StructType(World world, String name) {
        this(world, name, 0);
    }
}
