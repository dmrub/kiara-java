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
