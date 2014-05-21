/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Objects;

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
}
