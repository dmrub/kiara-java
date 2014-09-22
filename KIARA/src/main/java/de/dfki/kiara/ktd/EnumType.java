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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class EnumType extends Type {

    public static final int npos = -1;

    private final BiMap<String, Integer> nameToIndexBimap;

    private EnumType(World world, String name) {
        super(world, name, NodeKind.NODE_ENUMTYPE, 0);
        this.nameToIndexBimap = HashBiMap.create();
    }

    public static EnumType create(World world, String name) {
        return (EnumType)world.getOrCreate(new EnumType(world, name));
    }

    public void addConstant(String name, Expr expr) {
        int index = getNumElements();
        resizeElements(index+1);
        this.elements.set(index, expr);
        nameToIndexBimap.put(name, index);
    }

    public final int getNumConstants() {
        return getNumElements();
    }

    public final Expr getConstantAt(int index) {
        return getElementAs(Expr.class, index);
    }

    public final String getConstantNameAt(int index) {
        return nameToIndexBimap.inverse().get(index);
    }

    public final int getConstantIndexByName(String name) {
        Integer index = nameToIndexBimap.get(name);
        return index != null ? index : npos;
    }

    @Override public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }

}
